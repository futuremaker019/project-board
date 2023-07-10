package com.board.boardpractice.config;

import com.board.boardpractice.domain.UserAccount;
import com.board.boardpractice.repository.UserAccountRepository;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

    @MockBean
    private UserAccountRepository userAccountRepository;

    /**
     * 각 테스트를 실행하기 전 인증과 관련된 부분을 실행시켜준다.
     */
    @BeforeTestMethod
    public void securitySetup() {
        given(userAccountRepository.findById(anyString())).willReturn(
                Optional.of(
                        UserAccount.of(
                                "noah00o",
                                "ppww",
                                "jung@gmail.com",
                                "test",
                                "test memo")
                ));
    }
}
