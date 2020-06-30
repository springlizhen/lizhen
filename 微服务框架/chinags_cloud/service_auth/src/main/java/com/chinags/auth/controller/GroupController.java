package com.chinags.auth.controller;

import com.chinags.auth.entity.Group;
import com.chinags.auth.entity.Post;
import com.chinags.auth.service.GroupService;
import com.chinags.auth.service.PostService;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 菜单类
 *
 * @author Mr.Zhang
 * @version V1.0
 * @date
 * @since 1.8
 */

@Api("岗位管理controller")
@RestController
@CrossOrigin
@RequestMapping("/group")
public class GroupController extends BaseController {

    @Autowired
    private GroupService groupService;

    /**
     * 分页获取岗位信息
     * @return
     */
    @ApiOperation("分页获取岗位信息")
    @RequestMapping(value = "/pageList", method = RequestMethod.GET)
    @ResponseBody
    public Result postListPage(Group group){
        PageResult<Group> page;
        try {
            //类似查询全部
            page = groupService.find(group);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",page);
    }

    /**
     * 获取岗位信息
     * @return
     */
    @ApiOperation("获取岗位信息")
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public Result postForm(String id){
        Group group = new Group();
        try {
            if(StringUtils.isNotEmpty(id)) {
                group = groupService.getId(id);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",group);
    }

    @ApiOperation("保存岗位")
    @RequestMapping(value = "/saveAndUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Result saveAndUpdate(@RequestBody Group group) {
        Group groupParent;
        groupParent = groupService.getId(group.getId());
        if(group.getIsNewRecord()) {
            group.setStatus("0");
        }else{
            group.setStatus(groupParent.getStatus());
        }
        group.setCreateBy(getLoginUser().getUsercode());
        group.setUpdateBy(getLoginUser().getUsercode());
        group.setCreateDate(new Date());
        group.setUpdateDate(new Date());
        this.groupService.save(group);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("删除岗位")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Result delete(Group group) {
        if(groupService.delete(group)) {
            return new Result(true, StatusCode.OK, "删除成功");
        }
        else {
            return new Result(true, StatusCode.ERROR, "删除失败");
        }
    }

}
