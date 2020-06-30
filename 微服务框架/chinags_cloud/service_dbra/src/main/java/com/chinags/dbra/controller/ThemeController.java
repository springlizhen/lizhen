package com.chinags.dbra.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.BaseEntity;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.dbra.entity.Theme;
import com.chinags.dbra.service.ThemeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @Author : Mark_wang
 * @Date : 2019-9-19
 **/
@Api("主题分类controller")
@RestController
@CrossOrigin
@RequestMapping("/theme")
public class ThemeController extends BaseController {
    @Autowired
    private ThemeService themeService;

    @ApiOperation("分页查询所有主题分类")
    @RequestMapping(value = "/findAllByPage", method = RequestMethod.POST)
    public Result findAllByPage(Theme theme) {
        PageResult<Theme> dbDatabases = themeService.findAllByPage(theme);
        if (CollectionUtils.isEmpty(dbDatabases.getList())) {
            return new Result(false, StatusCode.ERROR, "没有配置主题分类，请前往配置");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", dbDatabases);
        }
    }

    @ApiOperation("查询所有主题分类")
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", themeService.findAll());
    }

    @ApiOperation("根据id查询主题分类")
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable("id") String id) {
        return new Result(true, StatusCode.OK, "查询成功", themeService.findThemeById(id));
    }

    @ApiOperation("保存主题分类")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(@RequestBody Theme theme) {
        String userCode = getLoginUser().getUsercode();
        if (StringUtils.isEmpty(theme.getCreateBy())) {
            theme.setCreateBy(userCode);
            theme.setCreateDate(new Date());
        }
        theme.setUpdateBy(userCode);
        theme.setUpdateDate(new Date());
        themeService.save(theme);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("根据id删除主题分类")
    @RequestMapping(value = "/del/{id}", method = RequestMethod.GET)
    public Result del(@PathVariable("id") String id) {
        themeService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }
}
