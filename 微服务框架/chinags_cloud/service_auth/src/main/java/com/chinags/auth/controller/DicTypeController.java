package com.chinags.auth.controller;

import com.chinags.auth.entity.DicType;
import com.chinags.auth.service.DicTypeService;
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
import java.util.Map;

@Api("字典类型管理controller")
@RestController
@CrossOrigin
@RequestMapping("/dicType")
public class DicTypeController extends BaseController {

    @Autowired
    private DicTypeService dicTypeService;

    /**
     * 分页获取字典信息
     * @return
     */
    @ApiOperation("分页获取字典信息")
    @RequestMapping(value = "/pageList", method = RequestMethod.GET)
    @ResponseBody
    public Result areaListPage(DicType dicType){
        PageResult<DicType> page;
        try {
            //类似查询全部
            page = dicTypeService.find(dicType);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",page);
    }

    /**
     * 获取字典信息
     * @return
     */
    @ApiOperation("获取字典信息")
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public Result areaForm(String id){
        DicType dicType = new DicType();
        try {
            if(StringUtils.isNotEmpty(id)) {
                dicType = dicTypeService.getById(id);
            }
        }catch (Exception e){
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",dicType);
    }

    @ApiOperation("保存字典")
    @RequestMapping(value = "/saveAndUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Result saveAndUpdate(@RequestBody DicType dicType) {
        if(dicType.getIsNewRecord()) {
            DicType areaParent = dicTypeService.getByDictType(dicType.getDictType());
            if(areaParent!=null){
                return new Result(true, StatusCode.ERROR, "字典编码已存在");
            }
            dicType.setStatus("0");
        }else{
            DicType areaParent = dicTypeService.getById(dicType.getId());
            dicType.setStatus(areaParent.getStatus());
        }
        dicType.setCreateBy(getLoginUser().getUsercode());
        dicType.setUpdateBy(getLoginUser().getUsercode());
        dicType.setCreateDate(new Date());
        dicType.setUpdateDate(new Date());
        this.dicTypeService.save(dicType);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("删除字典")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Result delete(DicType dicType) {
        if(dicTypeService.delete(dicType)) {
            return new Result(true, StatusCode.OK, "删除成功");
        }
        else {
            return new Result(true, StatusCode.ERROR, "请先删除子菜单");
        }
    }

    @ApiOperation("停用字典")
    @RequestMapping(value = "/disable", method = RequestMethod.GET)
    @ResponseBody
    public Result disable(String id) {
        if(StringUtils.isNotEmpty(id)) {
            return dicTypeService.disable(id);
        }
        else {
            return new Result(true, StatusCode.ERROR, "未选择字典");
        }
    }

    @ApiOperation("启用字典")
    @RequestMapping(value = "/enable", method = RequestMethod.GET)
    @ResponseBody
    public Result enable(String id) {
        if(StringUtils.isNotEmpty(id)) {
            String result = dicTypeService.enable(id);
            return new Result(true, StatusCode.OK, result);
        }
        else {
            return new Result(true, StatusCode.ERROR, "未选择字典");
        }
    }

    @ApiOperation("验证字典")
    @RequestMapping(value = "/checkDictType", method = RequestMethod.GET)
    @ResponseBody
    public Result checkDictType(String dictType) {
        if(StringUtils.isNotEmpty(dictType)) {
            DicType result = dicTypeService.getByDictType(dictType);
            return new Result(true, StatusCode.OK,"获取成功", (result!=null?false:true));
        }
        else {
            return new Result(true, StatusCode.ERROR, "未选择字典");
        }
    }

    @ApiOperation("字典树")
    @RequestMapping(value = "/selectDicType", method = RequestMethod.GET)
    public Result selectDicType(){
        List<Map<String,Object>> map= ListUtils.newArrayList();
        List<DicType> list=dicTypeService.findAll(new DicType());
        for(int i=0;i<list.size();i++){
            DicType contract=list.get(i);
            HashMap  hashMap=new HashMap();
            hashMap.put("id",contract.getId());
            hashMap.put("name",contract.getDictName());
            map.add(hashMap);
        }

        if(!StringUtils.isEmpty(list)) {
            return new Result(true, StatusCode.OK, "查询成功", map);
        }else{
            return new Result(true, StatusCode.OK, "查询失败，未查询到数据", map);
        }
    }
}
