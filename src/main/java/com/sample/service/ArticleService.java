package com.sample.service;

import com.sample.model.Article;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ArticleService {

    private List<Article> articles = new ArrayList<>();

    public ArticleService() {
        articles.add(new Article(1L, "Free Article", "This is a free article.", false));
        articles.add(new Article(2L, "Premium Article", "This is a premium article, for premium members only.", true));
        articles.add(new Article(3L, "Premium Article2", "This is a premium article2, for premium members only.", true));
    }

    public List<Article> getArticles() {
        return articles;
    }

    public Article getArticle(Long id) {
        Optional<Article> article = articles.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
        return article.orElse(null);
    }
}
