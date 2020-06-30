package com.chinags.dbra.controller;

import com.alibaba.fastjson.JSON;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.dbra.entity.BasicData;
import com.chinags.dbra.entity.DbTable;
import com.chinags.dbra.service.BasicDataService;
import com.chinags.dbra.service.DbTableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Mark_Wang
 * @date 2019/7/1
 **/
@Api("数据表管理controller")
@RestController
@CrossOrigin
@RequestMapping("/bd")
public class BasicDataController extends BaseController {

    @Autowired
    private BasicDataService basicDataService;

    @Autowired
    private DbTableService dbTableService;

    @ApiOperation("根据数据库id和表名称查询字段属性和备注信息")
    @RequestMapping(value = "/findColComments/{dbId}/{tableName}", method = RequestMethod.GET)
    public Result findColumnsCommentsByDbIdAndTableName(@PathVariable("dbId") String dbId, @PathVariable("tableName") String tableName) {
        List<BasicData> comments = basicDataService.findColumnsCommentsByDbIdAndTableName(dbId, tableName);
        if (CollectionUtils.isEmpty(comments)) {
            return new Result(false, StatusCode.ERROR, "查询失败");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", comments);
        }
    }

    @ApiOperation("根据数据表id查询字段属性和备注信息")
    @RequestMapping(value = "/findColCommentsByTbId/{tbId}", method = RequestMethod.GET)
    public Result findColumnsCommentsByDbIdAndTableName(@PathVariable("tbId") String tbId) {
        DbTable dbTable = dbTableService.findById(tbId);
        List<BasicData> comments = basicDataService.findColumnsCommentsByDbIdAndTableName(dbTable.getDbId(), dbTable.getName());
        if (CollectionUtils.isEmpty(comments)) {
            return new Result(false, StatusCode.ERROR, "查询失败");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", comments);
        }
    }

    @ApiOperation("根据数据库id，表名和属性信息生成sql语句插入数据")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result saveDataToTable(@RequestBody Map<String, String> map) {
        String dbId = map.get("dbId");
        String tbName = map.get("tbName");
        String id = map.get("ID");
        System.out.println(JSON.toJSONString(map));
        if (StringUtils.isEmpty(dbId) || StringUtils.isEmpty(tbName) || StringUtils.isEmpty(id)) {
            return new Result(false, StatusCode.ERROR, "保存失败");
        }
        List<BasicData> basicDataList = basicDataService.findById(dbId, tbName, id);
        List<BasicData> saveData = new ArrayList<>();
        //根据查询数据判断是新增还是更新
        boolean update = false;
        for (BasicData basicData : basicDataList) {
            if (!StringUtils.isEmpty(basicData.getValue())) {
                update = true;
                break;
            }
        }
        basicDataList = basicDataService.findColumnsCommentsByDbIdAndTableName(dbId, tbName);
        for (BasicData basicData : basicDataList) {
            // 传入的数据存入属性集合中
            basicData.setValue(map.get(basicData.getName()));
            saveData.add(basicData);
        }
        boolean result = false;
        if (update) {
            // 更新
            result = basicDataService.updateDataToTable(dbId, tbName, id, saveData);
        } else {
            // 插入
            result = basicDataService.saveDataToTable(dbId, tbName, saveData);
        }
        if (result) {
            return new Result(true, StatusCode.OK, "保存成功");
        } else {
            return new Result(false, StatusCode.ERROR, "保存失败");
        }
    }

    @ApiOperation("根据数据库id，表名和属性信息生成sql语句删除数据")
    @RequestMapping(value = "/del/{dbId}/{tableName}/{id}", method = RequestMethod.GET)
    public Result deleteDataToTable(@PathVariable("dbId") String dbId, @PathVariable("tableName") String tableName,
                                    @PathVariable("id") String id) {
        boolean result = basicDataService.deleteDataToTable(dbId, tableName, id);
        if (result) {
            return new Result(true, StatusCode.OK, "删除成功");
        } else {
            return new Result(false, StatusCode.ERROR, "删除失败");
        }
    }

    @ApiOperation("查询分页")
    @RequestMapping(value = "/find/{dbId}/{tableName}/{pageNo}/{pageSize}", method = RequestMethod.POST)
    public Result findPage(@PathVariable("dbId") String dbId, @PathVariable("tableName") String tableName,
                           @PathVariable("pageNo") int pageNo, @PathVariable("pageSize") int pageSize) {
        int num = 0;
        int size = pageSize;
        if (pageNo != 1) {
            num = pageSize * (pageNo - 1);
            size = pageSize * pageNo;
        }
        Long nums = basicDataService.findCount(dbId, tableName);
        List<Object> resultList = basicDataService.find(dbId, tableName, num, size);
        PageResult<Object> page = PageResult.getPageResult(resultList, nums, pageNo);
        if (CollectionUtils.isEmpty(resultList)) {
            return new Result(false, StatusCode.ERROR, "没有数据");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", page);
        }
    }

    @ApiOperation("根据数据库id，数据表名和id查询数据")
    @RequestMapping(value = "/findById/{dbId}/{tableName}/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable("dbId") String dbId, @PathVariable("tableName") String tableName,
                           @PathVariable("id") String id) {
        List<BasicData> basicDataList = basicDataService.findById(dbId, tableName, id);
        if (CollectionUtils.isEmpty(basicDataList)) {
            return new Result(false, StatusCode.ERROR, "查询失败");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", basicDataList);
        }
    }
}
