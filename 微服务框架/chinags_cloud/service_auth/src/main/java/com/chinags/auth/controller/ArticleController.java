package com.chinags.auth.controller;

import com.chinags.auth.client.FileClient;
import com.chinags.auth.entity.*;
import com.chinags.auth.service.*;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.Global;
import com.chinags.common.utils.PasswordUtils;
import com.chinags.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Api("文章管理controller")
@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private FileClient fileClient;

    @Autowired
    private ArticleService articleService;

    /**
     * 分页获取文章信息
     *
     * @return
     */
    @ApiOperation("分页获取文章信息")
    @RequestMapping(value = "/pageList", method = RequestMethod.GET)
    @ResponseBody
    public Result articleListPage(Article article, String articleCategoryCode, String articleCategoryName) {
        PageResult<Article> page;
        try {
            //类似查询全部
            page = articleService.find(article, articleCategoryCode, articleCategoryName);
            if (article.getIsIndexCarousel() != null && article.getIsIndexCarousel().equals("1")) {
                List<Article> articles = new ArrayList<>();
                for (Article a : page.getList()) {
                    a.setImg(fileClient.downByPid(a.getId()));
                    articles.add(a);
                }
                page.setList(articles);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败"   );
        }
        return new Result(true, StatusCode.OK, "获取成功", page);
    }

    @ApiOperation("获取文章信息")
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public Result articleForm(String articleCode) {
        Article article = new Article();
        try {
            if (StringUtils.isNotEmpty(articleCode)) {
                article = articleService.getArticleByArticleCode(articleCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", article);
    }

    @ApiOperation("保存文章")
    @RequestMapping(value = "/saveAndUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Result saveAndUpdate(@RequestBody Article article) {
        article.setCreateBy(getLoginUser().getUsercode());
        article.setUpdateBy(getLoginUser().getUsercode());
        article.setCreateDate(new Date());
        article.setUpdateDate(new Date());
        article.setStatus("0");
        article.setAuditProgress("1");
        articleService.save(article);
        return new Result(true, StatusCode.OK, "保存成功", article.getId());
    }

    @ApiOperation("删除文章")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Result delete(Article article) {
        try {
            articleService.delete(article);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "删除失败");
        }
        return new Result(true, StatusCode.OK, "删除成功");
    }

}
