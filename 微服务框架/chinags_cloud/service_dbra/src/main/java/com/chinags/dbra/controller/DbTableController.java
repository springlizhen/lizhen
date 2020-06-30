package com.chinags.dbra.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.BaseEntity;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.dbra.entity.DbColumn;
import com.chinags.dbra.entity.DbTable;
import com.chinags.dbra.entity.Resource;
import com.chinags.dbra.service.DbColumnService;
import com.chinags.dbra.service.DbTableService;
import com.chinags.dbra.service.ResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 数据表操作接口
 * @author Mark_Wang
 * @date 2019/6/28
 * @time 9:46
 **/
@Api("数据表管理controller")
@RestController
@CrossOrigin
@RequestMapping("/tb")
public class DbTableController extends BaseController {

    @Autowired
    private DbTableService dbTableService;

    @Autowired
    private DbColumnService dbColumnService;

    @Autowired
    private ResourceService resourceService;

    @ApiOperation("分页查询所有数据表配置")
    @RequestMapping(value = "/findAllByPage", method = RequestMethod.POST)
    public Result findAllByPage(BaseEntity baseEntity) {
        PageResult<DbTable> dbDatabases = dbTableService.findAllByPage(baseEntity);
        if (CollectionUtils.isEmpty(dbDatabases.getList())) {
            return new Result(false, StatusCode.ERROR, "没有配置的数据表，请前往配置");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", dbDatabases);
        }
    }

    @ApiOperation("查询所有数据表配置")
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", dbTableService.findAll());
    }


    @ApiOperation("保存数据表配置")
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody DbTable dbTable) {
        String userCode = getLoginUser().getUsercode();
        if (StringUtils.isEmpty(dbTable.getCreateBy())) {
            dbTable.setCreateBy(userCode);
            dbTable.setCreateDate(new Date());
            List<DbTable> dbTables = dbTableService.getDbTableByDbIdAndName(dbTable.getDbId(), dbTable.getName());
            if (!CollectionUtils.isEmpty(dbTables)) {
                return new Result(false, StatusCode.ERROR, "数据表配置已存在");
            }
        }
        dbTable.setUpdateBy(userCode);
        dbTable.setUpdateDate(new Date());
        DbTable table = dbTableService.save(dbTable);
        if(table == null) {
            return new Result(false, StatusCode.ERROR, "数据表保存失败");
        } else {
            return new Result(true, StatusCode.OK, "数据表保存成功", table);
        }
    }

    @ApiOperation("根据id查询数据表配置")
    @RequestMapping(value = "/find/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable("id") String id) {
        List<DbTable> dbTables = new ArrayList<>();
        dbTables.add(dbTableService.findById(id));
        return new Result(true, StatusCode.OK, "查询成功", PageResult.getPageResult(dbTables));
    }

    @ApiOperation("根据id查询数据表配置")
    @RequestMapping(value = "/selectById/{id}", method = RequestMethod.GET)
    public Result selectById(@PathVariable("id") String id) {
        return new Result(true, StatusCode.OK, "查询成功", dbTableService.findById(id));
    }

    @ApiOperation("根据数据库id查询数据表配置")
    @RequestMapping(value = "/findByDbId/{dbId}/{pageNo}/{pageSize}", method = RequestMethod.GET)
    public Result findByDbId(@PathVariable("dbId") String dbId, @PathVariable("pageNo") int pageNo, @PathVariable("pageSize") int pageSize) {
        int num = 0;
        int size = pageSize;
        if (pageNo != 1) {
            num = pageSize * (pageNo - 1);
            size = pageSize * pageNo;
        }
        long count = dbTableService.getCountByDbId(dbId);
        List<DbTable> dbTables = dbTableService.findByDbIdAndPage(dbId, num, size);
        PageResult<DbTable> page = PageResult.getPageResult(dbTables, count, pageNo);
        if (CollectionUtils.isEmpty(dbTables)) {
            return new Result(false, StatusCode.ERROR, "数据库下没有配置表,请前往配置");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", page);
        }
    }

    @ApiOperation("根据数据库id查询数据表配置")
    @RequestMapping(value = "/selectByDbId/{dbId}", method = RequestMethod.GET)
    public Result selectByDbId(@PathVariable("dbId") String dbId) {
        List<DbTable> dbTables = dbTableService.findByDbId(dbId);
        if (CollectionUtils.isEmpty(dbTables)) {
            return new Result(false, StatusCode.ERROR, "数据库下没有配置表,请前往配置");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", dbTables);
        }
    }

    @ApiOperation("根据数据库id和数据表name查询表数据字段")
    @RequestMapping(value = "/findColumn/{dbId}/{tableName}", method = RequestMethod.GET)
    public Result findColumnsByDbIdAndTableName(@PathVariable("dbId") String dbId, @PathVariable("tableName") String tableName) {
        List<String> columns = dbTableService.findColumnsNameByDbIdAndTableName(dbId, tableName);
        if (CollectionUtils.isEmpty(columns)) {
            return new Result(false, StatusCode.ERROR, "查询失败，没有找到字段");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", columns);
        }
    }

    @ApiOperation("根据id删除数据表配置")
    @RequestMapping(value = "/del/{id}", method = RequestMethod.GET)
    public Result deleteById(@PathVariable("id") String id) {
        //查询数据表下面是否有数据字段信息，如果有则不允许删除
        List<DbColumn> dbColumns = dbColumnService.findDbColumnsByTbId(id);
        if (dbColumns.size() != 0) {
            return new Result(false, StatusCode.ERROR, "数据表下有字段配置，请先删除字段配置");
        }
        //如果表下面的数据共享出去，禁止删除表配置信息
        List<Resource> resources = resourceService.findByTbId(id);
        if (!CollectionUtils.isEmpty(resources)) {
            return new Result(false, StatusCode.ERROR, "数据表已发布API接口，禁止删除");
        }
        //删除数据表配置信息
        dbTableService.deleteById(id);
        return new Result(true, StatusCode.OK, "数据表配置信息删除成功");
    }

    @ApiOperation("查询配置了字段的数据表信息")
    @RequestMapping(value = "/tbIncl", method = RequestMethod.GET)
    public Result getDbTablesInColumn() {
        return new Result(true, StatusCode.OK, "查询成功", dbTableService.getDbTablesInColumn());
    }

    @ApiOperation("根据数据库id删除数据表配置")
    @RequestMapping(value = "/delByDbId/{dbId}", method = RequestMethod.GET)
    public Result deleteByDbId(@PathVariable("dbId") String dbId) {
        dbTableService.deleteByDbId(dbId);
        return new Result(true, StatusCode.OK, "数据表配置信息删除成功");
    }

}
