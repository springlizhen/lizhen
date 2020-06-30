package com.chinags.auth.controller;

import com.chinags.auth.entity.ArticleCategory;
import com.chinags.auth.entity.Menu;
import com.chinags.auth.service.ArticleCategoryService;
import com.chinags.common.collect.ListUtils;
import com.chinags.common.collect.MapUtils;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Api("文章分类管理controller")
@RestController
@CrossOrigin
@RequestMapping("/articleCategory")
public class ArticleCategoryController extends BaseController {

    @Autowired
    private ArticleCategoryService articleCategoryService;

    @ApiOperation("分页获取文章分类信息")
    @RequestMapping(value = "/pageList", method = RequestMethod.GET)
    @ResponseBody
    public Result articleCategoryListPage(ArticleCategory articleCategory) {
        PageResult<ArticleCategory> page;
        try {
            String parentCode = articleCategory.getParentCode() == null ? "0" : articleCategory.getParentCode();
            articleCategory.setParentCode(parentCode);
            //类似查询全部
            articleCategory.setPageSize(100000000);
            page = articleCategoryService.find(articleCategory);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", page);
    }

    @ApiOperation("获取分类信息")
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public Result articleCategoryForm(String categoryCode, String parentCode) {
        ArticleCategory articleCategory = new ArticleCategory();
        try {
            if (StringUtils.isEmpty(parentCode)) {
                articleCategory = articleCategoryService.getArticleCategoryByCategoryCode(categoryCode);
            } else {
                ArticleCategory ac = articleCategoryService.getArticleCategoryByCategoryCode(parentCode);
                articleCategory.setParentCode(ac.getCategoryCode());
                articleCategory.setTreeNames(ac.getCategoryName() + "/" + ac.getCategoryName() + "/" + ac.getCategoryName());
            }
        } catch (Exception e) {
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", articleCategory);
    }

    @ApiOperation("保存分类")
    @RequestMapping(value = "/saveAndUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Result saveAndUpdate(@RequestBody ArticleCategory articleCategory) {
        articleCategory.setCreateBy(getLoginUser().getUsercode());
        articleCategory.setUpdateBy(getLoginUser().getUsercode());
        articleCategory.setCreateDate(new Date());
        articleCategory.setUpdateDate(new Date());
        articleCategoryService.save(articleCategory);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("分类树结构")
    @RequestMapping(value = "/treeData", method = RequestMethod.GET)
    public Result treeData(String id) {
        List list = ListUtils.newArrayList();
        List<ArticleCategory> articleCategorys;
        try {
            ArticleCategory articleCategory = new ArticleCategory();
            articleCategorys = articleCategoryService.findAll(articleCategory);
        } catch (Exception e) {
            return new Result(false, StatusCode.ERROR, "获取失败");
        }

        for (ArticleCategory ac : articleCategorys) {
            if (StringUtils.isNotEmpty(id)) {
                if (ac.getId().equals(id) || ac.getParentCodes().contains(id)) {
                    continue;
                }
            }
            HashMap map = MapUtils.newHashMap();
            map.put("id", ac.getId());
            map.put("pId", ac.getParentCode());
            map.put("name", ac.getCategoryName());
            list.add(map);
        }
        return new Result(true, StatusCode.OK, "获取成功", list);
    }

    @ApiOperation("删除文章分类")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Result delete(ArticleCategory articleCategory) {
        if (articleCategoryService.delete(articleCategory)) {
            return new Result(true, StatusCode.OK, "删除成功");
        } else {
            return new Result(true, StatusCode.ERROR, "请先删除子分类");
        }
    }

    @ApiOperation("根据级别和父级名称查询")
    @RequestMapping(value = "/selectByLevelAndParentName", method = RequestMethod.GET)
    @ResponseBody
    public Result selectByLevelAndParentName(String level, String parentName) {
        List<ArticleCategory> articleCategories;
        try {
            articleCategories = articleCategoryService.selectByLevelAndParentName(level, parentName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", articleCategories);
    }

}
