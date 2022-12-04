package com.fast.projectboard.repository.querydsl;

import com.fast.projectboard.domain.Article;
import com.fast.projectboard.domain.QArticle;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ArticleRepositoryCustomImpl extends QuerydslRepositorySupport implements ArticleRepositoryCustom{

    public ArticleRepositoryCustomImpl() {
        super(Article.class);
    }

    @Override
    public List<String> findAllDistinctHashtags() {
        QArticle article = QArticle.article;
        return from(article)
                .select(article.hashtag)
                .distinct()
                .where(article.hashtag.isNotNull())
                .fetch();
    }
}
