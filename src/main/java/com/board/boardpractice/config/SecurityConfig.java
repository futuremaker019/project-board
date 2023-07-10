package com.board.boardpractice.config;

import com.board.boardpractice.dto.UserAccountDto;
import com.board.boardpractice.dto.security.BoardPrincipal;
import com.board.boardpractice.repository.UserAccountRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .mvcMatchers(
                                HttpMethod.GET,
                                "/",
                                "/articles",
                                "/articles/search-hashtag"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin()
                        .and()
                .logout()
                        .logoutSuccessUrl("/")
                        .and()
                .build();
    }

    // security 설정하는 곳에서 같이 설정해야 csrf 와 같은 취약점을 보완할 수 있다.
    //@Bean
    //public WebSecurityCustomizer webSecurityCustomizer() {
    //    // static resource(css, js) 와 관련된 파일을 ignore 시켜줄때 사용한다.
    //    return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    //}

    @Bean
    public UserDetailsService userDetailsService(UserAccountRepository userAccountRepository) {
        /**
         * UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
         * loadUserByUsername 을 람다식으로 구현한것이다.
          */
        return username -> userAccountRepository.findById(username)
                .map(UserAccountDto::from)
                .map(BoardPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을수 없습니다. username: " + username));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 패스워드 인코딩 설정을 패스워드 인코더 팩토리에서 위임하여 가져오겠다는 설정인데 무슨말이지?
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
