package com.chinags.auth.dao;

import com.chinags.auth.entity.ArticleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleCategoryDao extends JpaRepository<ArticleCategory, String>, JpaSpecificationExecutor<ArticleCategory> {

    ArticleCategory getArticleCategoryByCategoryCode(String categoryCode);

    ArticleCategory getArticleCategoryByCategoryName(String articleCategoryName);

    @Query(value = "select count(*) from t_pub_sys_article_category where parent_codes LIKE :categoryCode", nativeQuery = true)
    Integer getCountByParentCodes(@Param("categoryCode") String categoryCode);

    @Query(value = "select * from t_pub_sys_article_category where parent_codes LIKE %:parentCodes%", nativeQuery = true)
    List<ArticleCategory> getParentCodesActicleCategory(@Param("parentCodes") String categoryCode);

    List<ArticleCategory> getAreaByParentCodesLike(String parentCodes);

    @Query(value = "select * from t_pub_sys_article_category where tree_level = :treeLevel and tree_names LIKE %:parentName% order by tree_sort", nativeQuery = true)
    List<ArticleCategory> selectByLevelAndParentName(@Param("treeLevel") String treeLevel, @Param("parentName") String parentName);

}
