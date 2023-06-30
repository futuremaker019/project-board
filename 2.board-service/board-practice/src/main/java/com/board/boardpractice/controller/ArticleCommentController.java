package com.board.boardpractice.controller;

import com.board.boardpractice.dto.UserAccountDto;
import com.board.boardpractice.dto.request.ArticleCommentRequest;
import com.board.boardpractice.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class ArticleCommentController {

    /**
     *  게시글의 댓글은 등록, 삭제만 개발 (수정은 개발하지 않는다고 함)
     */

    private final ArticleCommentService articleCommentService;

    @PostMapping("/new")
    public String postNewArticleComment(ArticleCommentRequest articleCommentRequest) {
        // TODO : 인증 정보를 넣어줘야 한다.
        articleCommentService.saveArticleComment(articleCommentRequest.toDto(
                UserAccountDto.of("uno", "pw", "uno@mail.com", null, null)
        ));
        return "redirect:/articles/" + articleCommentRequest.articleId() ;
    }

    @PostMapping ("/{commentId}/delete")
    public String deleteArticleComment(@PathVariable Long commentId, Long articleId) {
        articleCommentService.deleteArticleComment(commentId);
        return "redirect:/articles/" + articleId;
    }
}
