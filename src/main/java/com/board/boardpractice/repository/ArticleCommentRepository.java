package com.board.boardpractice.repository;

import com.board.boardpractice.domain.ArticleComment;
import com.board.boardpractice.domain.QArticleComment;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ArticleCommentRepository extends
        JpaRepository<ArticleComment, Long>,
        QuerydslPredicateExecutor<ArticleComment>,
        QuerydslBinderCustomizer<QArticleComment> {

    List<ArticleComment> findByArticle_Id(Long articleId);

    void deleteByIdAndUserAccount_UserId(Long articleCommentId, String userId);

    // 검색을 위해 querydslBinderCustomizer 를 추가한다.
    @Override
    default void customize(QuerydslBindings bindings, QArticleComment root) {
        // QueryPredicateExecutor 를 열어두면 모든 필드에 대해서 검색되기 때문에 모든 필드를 exclude 시킨다.
        bindings.excludeUnlistedProperties(true);
        // 검색조건에 포함될 필드를 추가시켜 줄수 있다.
        bindings.including(root.content, root.createdAt, root.createdBy);
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }
}
