package com.chinags.auth.controller;

import com.chinags.auth.entity.Post;
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
@RequestMapping("/post")
public class PostController extends BaseController {

    @Autowired
    private PostService postService;

    /**
     * 获取左侧栏菜单
     * @return
     */
    @ApiOperation("获取岗位")
    @RequestMapping(value = "/allList", method = RequestMethod.GET)
    public Result postList(){
        List<Post> posts;
        try {
            Post post = new Post();
            posts = postService.treeData(post);
        }catch (Exception e){
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        if(posts==null) {
            posts = new ArrayList<>();
        }
        return new Result(true, StatusCode.OK, "获取成功",posts);
    }

    /**
     * 分页获取岗位信息
     * @return
     */
    @ApiOperation("分页获取岗位信息")
    @RequestMapping(value = "/pageList", method = RequestMethod.GET)
    @ResponseBody
    public Result postListPage(Post post){
        PageResult<Post> page;
        try {
            //类似查询全部
            page = postService.find(post);
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
    public Result postForm(String postCode){
        Post post = new Post();
        try {
            if(StringUtils.isNotEmpty(postCode)) {
                post = postService.getAreaByPostCode(postCode);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",post);
    }

    /**
     * 菜单树结构
     * @return
     */
    @ApiOperation("岗位树结构")
    @RequestMapping(value = "/treeData", method = RequestMethod.GET)
    public Result treeData(Post post){
        List list = ListUtils.newArrayList();
        List<Post> posts;
        try {
            posts = postService.treeData(post);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }

        for (Post p : posts) {
            HashMap map = MapUtils.newHashMap();
            map.put("id", p.getId());
            map.put("pId", p.getPostCode());
            map.put("name", p.getPostName());
            list.add(map);
        }
        return new Result(true, StatusCode.OK, "获取成功",list);
    }

    @ApiOperation("保存岗位")
    @RequestMapping(value = "/saveAndUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Result saveAndUpdate(@RequestBody Post post) {
        Post postParent;
        postParent = postService.getAreaByPostCode(post.getPostCode());
        if(post.getIsNewRecord()) {
            post.setStatus("0");
            if(postParent!=null){
                return new Result(true, StatusCode.ERROR, "岗位编码已存在");
            }
        }else{
            post.setStatus(postParent.getStatus());
        }
        post.setCreateBy(getLoginUser().getUsercode());
        post.setUpdateBy(getLoginUser().getUsercode());
        post.setCreateDate(new Date());
        post.setUpdateDate(new Date());
        this.postService.save(post);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("删除岗位")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Result delete(Post post) {
        if(postService.delete(post)) {
            return new Result(true, StatusCode.OK, "删除成功");
        }
        else {
            return new Result(true, StatusCode.ERROR, "请先删除子菜单");
        }
    }

    @ApiOperation("停用岗位")
    @RequestMapping(value = "/disable", method = RequestMethod.GET)
    @ResponseBody
    public Result disable(String id) {
        if(StringUtils.isNotEmpty(id)) {
            return postService.disable(id);
        }
        else {
            return new Result(true, StatusCode.ERROR, "未选择机构");
        }
    }

    @ApiOperation("启用岗位")
    @RequestMapping(value = "/enable", method = RequestMethod.GET)
    @ResponseBody
    public Result enable(String id) {
        if(StringUtils.isNotEmpty(id)) {
            String result = postService.enable(id);
            return new Result(true, StatusCode.OK, result);
        }
        else {
            return new Result(true, StatusCode.ERROR, "未选择机构");
        }
    }

    @ApiOperation("验证岗位名是否有效")
    @RequestMapping(value = "/checkPostName", method = RequestMethod.GET)
    @ResponseBody
    public Result checkPostName(String postName) {
        if(StringUtils.isNotEmpty(postName)) {
            String result = postService.checkPostName(postName);
            return new Result(true, StatusCode.OK, result);
        }
        return new Result(true, StatusCode.OK, "true");
    }
}
