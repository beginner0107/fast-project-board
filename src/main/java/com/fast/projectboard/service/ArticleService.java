package com.fast.projectboard.service;

import com.fast.projectboard.domain.type.SearchType;
import com.fast.projectboard.dto.ArticleDto;
import com.fast.projectboard.dto.ArticleUpdateDto;
import com.fast.projectboard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType title, String search_keyword) {
        return Page.empty();
    }

    @Transactional
    public ArticleDto searchArticle(long l) {
        return null;
    }

    public void saveArticle(ArticleDto dto) {
    }

    public void deleteArticle(long articleId) {
    }

    public void updateArticle(long articleId, ArticleUpdateDto dto) {
    }
}
