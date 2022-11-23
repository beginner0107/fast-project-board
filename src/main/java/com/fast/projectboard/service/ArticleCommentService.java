package com.fast.projectboard.service;

import com.fast.projectboard.dto.ArticleCommentDto;
import com.fast.projectboard.repository.ArticleCommentRepository;
import com.fast.projectboard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleCommentService {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    public List<ArticleCommentDto> searchArticleComment(long articleId) {
        return List.of();
    }
}
