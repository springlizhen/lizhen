package com.chinags.auth.service;

import com.chinags.auth.dao.ArticleCategoryDao;
import com.chinags.auth.entity.ArticleCategory;
import com.chinags.auth.entity.Menu;
import com.chinags.common.entity.PageResult;
import com.chinags.common.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.awt.SystemColor.menu;

/**
 * @author suijinchi
 * @version V1.0
 * @date
 * @since 1.8
 */
@Service
public class ArticleCategoryService {

    public Sort sort = new Sort(Sort.Direction.ASC, "treeLevel", "treeSort");

    @Autowired
    private ArticleCategoryDao articleCategoryDao;

    /**
     * 查询全部列表
     *
     * @return 分类分页
     */
    public PageResult find(ArticleCategory articleCategory) {
        PageRequest pageable;
        if (articleCategory.getOrderBy() == null || "".equals(articleCategory.getOrderBy())) {
            pageable = PageRequest.of(articleCategory.getPageNo(), articleCategory.getPageSize(), Sort.Direction.ASC, "treeSort");
        } else {
            pageable = PageRequest.of(articleCategory.getPageNo(), articleCategory.getPageSize(), articleCategory.getDesc(), articleCategory.getOrderBy());
        }
        //数据简单转换，转换为前台框架所需要分页json
        PageResult pageResult;
        pageResult = PageResult.getPageResult(articleCategoryDao.findAll(
                (Specification<ArticleCategory>) (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (articleCategory.getCategoryName() != null && !"".equals(articleCategory.getCategoryName())) {
                        predicates.add(cb.like(root.get("categoryName").as(String.class), "%" + articleCategory.getCategoryName() + "%"));
                    }
                    if (articleCategory.getParentCode() != null && !"".equals(articleCategory.getParentCode())) {
                        predicates.add(cb.equal(root.get("parentCode").as(String.class), articleCategory.getParentCode()));
                    }
                    predicates.add(cb.equal(root.get("status").as(String.class), 0));
                    Predicate[] pre = new Predicate[predicates.size()];
                    query.where(predicates.toArray(pre));
                    return cb.and(predicates.toArray(pre));
                }, pageable));
        return pageResult;
    }

    /**
     * 删除分类
     *
     * @param articleCategory
     * @return
     */
    public boolean delete(ArticleCategory articleCategory) {
        int count = parentCodesCount("%" + articleCategory.getCategoryCode() + ",%");
        if (count > 0) {
            return false;
        }
        String parentCode = getByCategoryCode(articleCategory.getCategoryCode()).getParentCode();
        articleCategoryDao.delete(articleCategory);
        if (!parentCode.equals("0")) {
            count = parentCodesCount("%" + parentCode + ",%");
            if (count == 0) {
                ArticleCategory parentArticleCategory = getByCategoryCode(parentCode);
                parentArticleCategory.setTreeLeaf("1");
                articleCategoryDao.save(parentArticleCategory);
            }
        }
        return true;
    }

    /**
     * 子级分类数量
     *
     * @param categoryCode
     * @return
     */
    public int parentCodesCount(String categoryCode) {
        return articleCategoryDao.getCountByParentCodes(categoryCode);
    }

    /**
     * 根据编码查询
     *
     * @param categoryCode
     * @return
     */
    public ArticleCategory getByCategoryCode(String categoryCode) {
        return articleCategoryDao.getArticleCategoryByCategoryCode(categoryCode);
    }

    /**
     * 获取全部分类
     * @param articleCategory
     * @return
     */
    public List<ArticleCategory> findAll(ArticleCategory articleCategory) {
        articleCategory.setIsShow("1");
        articleCategory.setStatus("0");
        Example<ArticleCategory> ex = Example.of(articleCategory);
        return articleCategoryDao.findAll(ex, sort);
    }

    /**
     * 根据编码查询分类
     * @param categoryCode
     * @return
     */
    public ArticleCategory getArticleCategoryByCategoryCode(String categoryCode) {
        return articleCategoryDao.getArticleCategoryByCategoryCode(categoryCode);
    }

    /**
     * 保存分类
     * @param articleCategory
     */
    @Transactional(rollbackOn = Exception.class)
    public void save(ArticleCategory articleCategory) {
        ArticleCategory articleCategoryParent = getArticleCategoryByCategoryCode(articleCategory.getParentCode());
        ArticleCategory ac = getArticleCategoryByCategoryCode(articleCategory.getCategoryCode());
        if(articleCategoryParent!=null) {
            articleCategory.setParentCodes(articleCategoryParent.getParentCodes() + articleCategoryParent.getCategoryCode() + ",");
            articleCategory.setTreeSorts(articleCategoryParent.getTreeSorts() + String.format("%10d", articleCategory.getTreeSort()).replace(" ", "0") + ",");
            articleCategory.setTreeNames(articleCategoryParent.getTreeNames() + "/" + articleCategory.getCategoryName());
            if (StringUtils.isEmpty(articleCategory.getCategoryCode())) {
                articleCategory.setTreeLeaf("1");
                articleCategory.setTreeLevel(articleCategoryParent.getTreeLevel() + 1);
                articleCategory.setStatus("0");
                articleCategoryParent.setTreeLeaf("0");
                articleCategoryDao.save(articleCategoryParent);
            }
        }else{
            articleCategory.setParentCode("0");
            articleCategory.setParentCodes("0,");
            articleCategory.setTreeSorts(String.format("%10d", articleCategory.getTreeSort()).replace(" ", "0") + ",");
            articleCategory.setTreeNames(articleCategory.getCategoryName());
            if (StringUtils.isEmpty(articleCategory.getCategoryCode())) {
                articleCategory.setTreeLeaf("1");
                articleCategory.setTreeLevel(0);
                articleCategory.setStatus("0");
            }
        }
        if(articleCategory.getCategoryCode()!=null){
            articleCategory.setTreeLeaf(ac.getTreeLeaf());
            articleCategory.setTreeLevel(ac.getTreeLevel());
            articleCategory.setStatus(ac.getStatus());
        }
        //父级菜单是否变动
        if(ac!=null&&!articleCategory.getParentCode().equals(ac.getParentCode())){
            articleCategoryDao.save(articleCategory);
            articleTreelevel(ac, articleCategoryParent);
        }else{
            articleCategoryDao.save(articleCategory);
            articleTreelevel(articleCategory, articleCategoryParent);
        }
    }

    /**
     * 重置分类以及子分类层级
     * @param
     */
    public void articleTreelevel(ArticleCategory ac, ArticleCategory ArticleCategoryParent){
        String parentCode = ac.getParentCode();
        if(StringUtils.isEmpty(parentCode)||ArticleCategoryParent==null){
            ac.setTreeLevel(0);
        }else{
            ac.setTreeLevel(ArticleCategoryParent.getTreeLevel() + 1);
        }
        articleCategoryDao.save(ac);
        List<ArticleCategory> acList = articleCategoryDao.getParentCodesActicleCategory(ac.getCategoryCode());
        for (ArticleCategory articleCategory : acList) {
            articleTreelevel(articleCategory, ac);
        }
    }

    /**
     * 根据级别和父级名称查询
     * @param treeLevel
     * @param parentName
     * @return
     */
    public List<ArticleCategory> selectByLevelAndParentName(String treeLevel, String parentName) {
        return articleCategoryDao.selectByLevelAndParentName(treeLevel, parentName);
    }
}
