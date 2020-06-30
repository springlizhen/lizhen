package com.chinags.auth.dao;

import com.chinags.auth.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ArticleDao extends JpaRepository<Article, String>, JpaSpecificationExecutor<Article> {

    Article getArticleByArticleCode(String articleCode);
}
