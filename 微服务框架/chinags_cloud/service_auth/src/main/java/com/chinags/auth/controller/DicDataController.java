package com.chinags.auth.controller;

import com.chinags.auth.entity.DicData;
import com.chinags.auth.entity.DicType;
import com.chinags.auth.service.DicDataService;
import com.chinags.auth.service.DicTypeService;
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
import java.util.List;
import java.util.UUID;

@Api("字典数据管理controller")
@RestController
@CrossOrigin
@RequestMapping("/dicData")
public class DicDataController extends BaseController {

    @Autowired
    private DicDataService dicDataService;

    @Autowired
    private DicTypeService dicTypeService;


    /**
     * 获取字典信息名称
     * @return
     */
    @ApiOperation("获取字典信息名称")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ResponseBody
    public Result findAll(String dicType, String dicValue){
        DicData dicData = new DicData();
        dicData.setDictType(dicType);
        dicData.setDictValue(dicValue);
        List<DicData> list;
        try {
            //类似查询全部
            list = dicDataService.findAll(dicData);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",list.get(0).getDictLabel());
    }

    /**
     * 分页获取字典信息
     * @return
     */
    @ApiOperation("分页获取字典信息")
    @RequestMapping(value = "/pageList", method = RequestMethod.GET)
    @ResponseBody
    public Result areaListPage(DicData dicData){
        PageResult<DicData> page;
        try {
            //类似查询全部
            page = dicDataService.find(dicData);
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
    public Result areaForm(String dictCode){
        DicData dicType = new DicData();
        try {
            if(StringUtils.isNotEmpty(dictCode)){
                dicType = dicDataService.getByDictCode(dictCode);
            }
        }catch (Exception e){
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",dicType);
    }

    @ApiOperation("保存字典")
    @RequestMapping(value = "/saveAndUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Result saveAndUpdate(@RequestBody DicData dicData) {
        DicData dicDataParent = dicDataService.getByDictCode(dicData.getDictCode());
        if(dicDataParent!=null) {
            dicData.setStatus(dicDataParent.getStatus());
        }else{
            dicData.setStatus("0");
        }
        dicData.setCreateBy(getLoginUser().getUsercode());
        dicData.setUpdateBy(getLoginUser().getUsercode());
        dicData.setCreateDate(new Date());
        dicData.setUpdateDate(new Date());
        if(dicData.getId()==null) {
            dicData.setId(UUID.randomUUID().toString());
        }
        this.dicDataService.save(dicData);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("删除字典")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Result delete(DicData dicData) {
        if(dicDataService.delete(dicData)) {
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
            return dicDataService.disable(id);
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
            String result = dicDataService.enable(id);
            return new Result(true, StatusCode.OK, result);
        }
        else {
            return new Result(true, StatusCode.ERROR, "未选择字典");
        }
    }

    /**
     * 获取字典信息
     * @return
     */
    @ApiOperation("获取字典信息")
    @RequestMapping(value = "/dicType", method = RequestMethod.GET)
    public Result dicType(String dicType){
        List<DicData> dicData = null;
        try {
            if(StringUtils.isNotEmpty(dicType)){
                dicData = dicDataService.getAllByDictType(dicType);
            }
        }catch (Exception e){
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",dicData);
    }

    /**
     * 获取字典信息
     * @return
     */
    @ApiOperation("获取字典信息")
    @RequestMapping(value = "/dicTypeId", method = RequestMethod.GET)
    public Result dicTypeId(String dicTypeId){
        DicType byId = dicTypeService.getById(dicTypeId);
        List<DicData> dicData = null;
        try {
            if(StringUtils.isNotEmpty(byId.getDictType())){
                dicData = dicDataService.getAllByDictType(byId.getDictType());
            }
        }catch (Exception e){
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",dicData);
    }
}
