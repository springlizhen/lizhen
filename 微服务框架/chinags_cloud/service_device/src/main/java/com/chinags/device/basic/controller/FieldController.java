package com.chinags.device.basic.controller;

import com.chinags.common.collect.ListUtils;
import com.chinags.common.collect.MapUtils;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.StringUtils;
import com.chinags.device.basic.entity.Field;
import com.chinags.device.basic.service.FieldService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Api("代码字段管理controller")
@RestController
@CrossOrigin
@RequestMapping("/field")
public class FieldController extends BaseController {

    @Autowired
    private FieldService fieldService;

    /**
     * 获取指定代码字段信息
     * @return
     */
    @ApiOperation("获取指定代码字段信息")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ResponseBody
    public Result findAll(Field field){
        List<Field> fields;
        try {
            //类似查询全部
            fields = fieldService.getfindAll(field);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",fields);
    }

    /**
     * 分页获取代码字段信息
     * @return
     */
    @ApiOperation("分页获取代码字段信息")
    @RequestMapping(value = "/pageList", method = RequestMethod.GET)
    @ResponseBody
    public Result areaListPage(Field field){
        PageResult<Field> page;
        try {
            //类似查询全部
            page = fieldService.find(field);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",page);
    }

    /**
     * 获取代码字段信息
     * @return
     */
    @ApiOperation("获取代码字段信息")
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public Result areaForm(String id){
        Field field = new Field();
        try {
            if(StringUtils.isNotEmpty(id)) {
                field = fieldService.getFieldById(id);
            }
        }catch (Exception e){
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",field);
    }

    @ApiOperation("保存代码字段")
    @RequestMapping(value = "/saveAndUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Result saveAndUpdate(@RequestBody Field field) {
        if(field.getIsNewRecord()) {
            field.setStatus("0");
        }else{
            Field fieldParent = fieldService.getFieldById(field.getId());
            field.setStatus(fieldParent.getStatus());
        }
        field.setCreateBy(getLoginUser().getUsercode());
        field.setUpdateBy(getLoginUser().getUsercode());
        field.setCreateDate(new Date());
        field.setUpdateDate(new Date());
        this.fieldService.save(field);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("删除代码字段")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Result delete(String id) {
        if(StringUtils.isNotEmpty(id)){
            Field field = fieldService.delete(id);
            return new Result(true, StatusCode.OK, "删除代码字段“"+field.getFieldName()+"”成功");
        }else {
            return new Result(true, StatusCode.ERROR, "删除代码失败");
        }

    }

    /**
     * 菜单树结构
     * @return
     */
    @ApiOperation("代码树结构")
    @RequestMapping(value = "/treeData", method = RequestMethod.GET)
    public Result treeData(String deviceId){
        List list = ListUtils.newArrayList();
        List<Field> fields;
        try {
            fields = fieldService.treeData(deviceId);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }

        for (Field f : fields) {
            HashMap map = MapUtils.newHashMap();
            map.put("id", f.getId());
            map.put("name", f.getFieldName());
            list.add(map);
        }
        return new Result(true, StatusCode.OK, "获取成功",list);
    }

}
