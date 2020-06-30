package com.chinags.auth.controller;

import com.chinags.auth.entity.Area;
import com.chinags.auth.entity.Menu;
import com.chinags.auth.service.AreaService;
import com.chinags.auth.service.MenuService;
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
 * 行政区划类
 *
 * @author Mr.Zhang
 * @version V1.0
 * @date
 * @since 1.8
 */

@Api("行政区划管理controller")
@RestController
@CrossOrigin
@RequestMapping("/area")
public class AreaController extends BaseController {


    @Autowired
    private AreaService areaService;


    /**
     * 分页获取区域信息
     * @return
     */
    @ApiOperation("分页获取区域信息")
    @RequestMapping(value = "/pageList", method = RequestMethod.GET)
    @ResponseBody
    public Result areaListPage(Area area){

        String ceshi = null;
        if (ceshi.equals("123")) {
            System.currentTimeMillis();
        }

        PageResult<Area> page;
        try {
            String parentCode = area.getParentCode()==null?"0":area.getParentCode();
            area.setParentCode(parentCode);
            //类似查询全部
            area.setPageSize(100000000);
            page = areaService.find(area);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",page);
    }

    /**
     * 获取区域信息
     * @return
     */
    @ApiOperation("获取区域信息")
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public Result areaForm(String areaCode,String parentCode){
        Area area = new Area();
        try {
            if(StringUtils.isEmpty(parentCode)) {
                area = areaService.getAreaByAreaCode(areaCode);
            } else {
                Area a = areaService.getAreaByAreaCode(parentCode);
                area.setParentCode(a.getAreaCode());
                area.setTreeNames(a.getAreaName()+"/"+a.getAreaName()+"/"+a.getAreaName());
            }
        }catch (Exception e){
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",area);
    }

    /**
     * 菜单树结构
     * @return
     */
    @ApiOperation("区域树结构")
    @RequestMapping(value = "/treeData", method = RequestMethod.GET)
    public Result treeData(String excludeCode, String systemname){
        List list = ListUtils.newArrayList();
        List<Area> areas;
        try {
            Area area = new Area();
            areas = areaService.treeData(excludeCode);
        }catch (Exception e){
            return new Result(false, StatusCode.ERROR, "获取失败");
        }

        for (Area a : areas) {
            HashMap map = MapUtils.newHashMap();
            map.put("id", a.getId());
            map.put("pId", a.getParentCode());
            map.put("name", a.getAreaName());
            list.add(map);
        }
        return new Result(true, StatusCode.OK, "获取成功",list);
    }

    @ApiOperation("保存区域")
    @RequestMapping(value = "/saveAndUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Result saveAndUpdate(@RequestBody Area area) {
        if(area.getIsNewRecord()) {
            Area areaParent = areaService.getAreaByAreaCode(area.getAreaCode());
            if(areaParent!=null){
                return new Result(true, StatusCode.ERROR, "区域代码已存在");
            }
        }
        area.setCreateBy(getLoginUser().getUsercode());
        area.setUpdateBy(getLoginUser().getUsercode());
        area.setCreateDate(new Date());
        area.setUpdateDate(new Date());
        this.areaService.save(area);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("删除区域")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Result delete(Area area) {
        if(areaService.delete(area)) {
            return new Result(true, StatusCode.OK, "删除成功");
        }
        else {
            return new Result(true, StatusCode.ERROR, "请先删除子菜单");
        }
    }

    @ApiOperation("停用区域")
    @RequestMapping(value = "/disable", method = RequestMethod.GET)
    @ResponseBody
    public Result disable(String id) {
        if(StringUtils.isNotEmpty(id)) {
            return areaService.disable(id);
        }
        else {
            return new Result(true, StatusCode.ERROR, "未选择区域");
        }
    }

    @ApiOperation("启用区域")
    @RequestMapping(value = "/enable", method = RequestMethod.GET)
    @ResponseBody
    public Result enable(String id) {
        if(StringUtils.isNotEmpty(id)) {
            String result = areaService.enable(id);
            return new Result(true, StatusCode.OK, result);
        }
        else {
            return new Result(true, StatusCode.ERROR, "未选择区域");
        }
    }
}
