package com.board.boardpractice.service;

import com.board.boardpractice.domain.Article;
import com.board.boardpractice.domain.UserAccount;
import com.board.boardpractice.dto.ArticleCommentDto;
import com.board.boardpractice.repository.ArticleCommentRepository;
import com.board.boardpractice.repository.ArticleRepository;
import com.board.boardpractice.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ArticleCommentService {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;
    private final UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true)
    public List<ArticleCommentDto> searchArticleComments(Long articleId) {
        return articleCommentRepository.findByArticle_Id(articleId)
                .stream().map(ArticleCommentDto::from).toList();
    }

    public void saveArticleComment(ArticleCommentDto dto) {
        try {
            // 댓글의 게시글 정보
            Article article = articleRepository.getReferenceById(dto.articleId());
            // 댓글의 작성자
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());
            articleCommentRepository.save(dto.toEntity(article, userAccount));
        } catch (EntityNotFoundException e) {
            log.warn("댓글저장 실패. 댓글 작성에 필요한 정보를 찾을 수 없습니다. - dto {}", e.getLocalizedMessage());
        }
    }

    public void updateArticleComment(ArticleCommentDto dto) {

    }

    public void deleteArticleComment(Long articleCommentId, String userId) {
        articleCommentRepository.deleteByIdAndUserAccount_UserId(articleCommentId, userId);
    }
}
