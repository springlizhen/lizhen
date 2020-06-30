package com.chinags.auth.service;

import com.chinags.auth.dao.ArticleCategoryDao;
import com.chinags.auth.dao.ArticleDao;
import com.chinags.auth.entity.Article;
import com.chinags.auth.entity.ArticleCategory;
import com.chinags.common.entity.PageResult;
import com.chinags.common.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private ArticleCategoryDao articleCategoryDao;

    /**
     * 查询全部列表
     *
     * @return 分页数据
     */
    public PageResult find(Article article, String articleCategoryCode, String articleCategoryName) {
        PageRequest pageable;
        if (article.getOrderBy() == null || "".equals(article.getOrderBy())) {
            pageable = PageRequest.of(article.getPageNo(), article.getPageSize(), Sort.Direction.DESC, "articleDate");
        } else {
            pageable = PageRequest.of(article.getPageNo(), article.getPageSize(), Sort.Direction.DESC, article.getOrderBy());
        }
        //数据简单转换，转换为前台框架所需要分页json
        return PageResult.getPageResult(articleDao.findAll(
                (Specification<Article>) (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (article.getArticleTitle() != null && !"".equals(article.getArticleTitle())) {
                        predicates.add(cb.like(root.get("articleTitle").as(String.class), "%" + article.getArticleTitle() + "%"));
                    }
                    if (article.getIsShow() != null && !article.getIsShow().equals("")) {
                        predicates.add(cb.equal(root.get("isShow").as(String.class), article.getIsShow()));
                    }
                    if (article.getIsIndexCarousel() != null && !article.getIsIndexCarousel().equals("")) {
                        predicates.add(cb.equal(root.get("isIndexCarousel").as(String.class), article.getIsIndexCarousel()));
                    }
                    if (article.getAuditProgress() != null && !article.getAuditProgress().equals("")) {
                        predicates.add(cb.equal(root.get("auditProgress").as(String.class), article.getAuditProgress()));
                    }
                    predicates.add(cb.notEqual(root.get("status").as(String.class), Article.STATUS_DELETE));
                    Join<Article, ArticleCategory> join = root.join("articleCategory", JoinType.LEFT);
                    if (article.getArticleCategory() != null) {
                        if (StringUtils.isNotEmpty(article.getArticleCategory().getCategoryCode())) {
                            CriteriaBuilder.In<String> in = cb.in(join.get("categoryCode"));
                            in.value(article.getArticleCategory().getCategoryCode());
                            for (ArticleCategory ac : articleCategoryDao.getAreaByParentCodesLike("%" + article.getArticleCategory().getCategoryCode() + "%")) {
                                in.value(ac.getCategoryCode());
                            }
                            predicates.add(in);
                        }
                    }
                    if (articleCategoryCode != null && !"".equals(articleCategoryCode)) {
                        CriteriaBuilder.In<String> in = cb.in(join.get("categoryCode"));
                        in.value(articleCategoryCode);
                        predicates.add(in);
                    }
                    if (articleCategoryName != null && !"".equals(articleCategoryName)) {
                        CriteriaBuilder.In<String> in = cb.in(join.get("categoryCode"));
                        in.value(articleCategoryDao.getArticleCategoryByCategoryName(articleCategoryName).getCategoryCode());
                        predicates.add(in);
                    }
                    Predicate[] pre = new Predicate[predicates.size()];
                    query.where(predicates.toArray(pre));
                    return cb.and(predicates.toArray(pre));
                }, pageable));
    }

    /**
     * 删除文章
     *
     * @param article
     */
    public void delete(Article article) {
        articleDao.delete(article);
    }

    /**
     * 根据编码查询
     *
     * @param articleCode
     * @return
     */
    public Article getArticleByArticleCode(String articleCode) {
        return articleDao.getArticleByArticleCode(articleCode);
    }

    /**
     * 保存文章
     *
     * @param article
     */
    @Transactional(rollbackOn = Exception.class)
    public void save(Article article) {
        articleDao.save(article);
    }
}
