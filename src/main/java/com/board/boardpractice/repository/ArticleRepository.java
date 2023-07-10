package com.board.boardpractice.repository;

import com.board.boardpractice.domain.Article;
import com.board.boardpractice.domain.QArticle;
import com.board.boardpractice.repository.querydsl.ArticleRepositoryCustom;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        ArticleRepositoryCustom,
        QuerydslPredicateExecutor<Article>,
        QuerydslBinderCustomizer<QArticle> {

    Page<Article> findByTitleContaining(String title, Pageable pageable);
    Page<Article> findByContentContaining(String title, Pageable pageable);
    Page<Article> findByUserAccount_UserIdContaining(String title, Pageable pageable);
    Page<Article> findByUserAccount_NicknameContaining(String title, Pageable pageable);
    Page<Article> findByHashtag(String title, Pageable pageable);

    void deleteByIdAndUserAccount_UserId(Long articleId, String userid);

    // 검색을 위해 querydslBinderCustomizer 를 추가한다.
    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        // QueryPredicateExecutor 를 열어두면 모든 필드에 대해서 검색되기 때문에 모든 필드를 exclude 시킨다.
        bindings.excludeUnlistedProperties(true);
        // 검색조건에 포함될 필드를 추가시켜 줄수 있다.
        bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createdBy);
//        bindings.bind(root.title).first(StringExpression::likeIgnoreCase);      // like '${v}'
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase);    // like '%${v}%'
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }

}
