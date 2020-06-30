package com.chinags.dbra.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.BaseEntity;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.dbra.entity.*;
import com.chinags.dbra.service.*;
import com.chinags.dbra.util.JdbcUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 数据库操作接口
 * @author Mark_Wang
 * @date 2019/6/26
 * @time 16:40
 **/
@Api("数据库管理controller")
@RestController
@CrossOrigin
@RequestMapping("/db")
public class DbDatabaseController extends BaseController {

    @Autowired
    private DbDatabaseService dbDatabaseService;

    @Autowired
    private DbTableService dbTableService;

    @Autowired
    private ResourceService resourceService;

    @ApiOperation("分页查询数据库配置")
    @RequestMapping(value = "/selectAll", method = RequestMethod.POST)
    public Result selectAll(BaseEntity baseEntity) {
        PageResult<DbDatabase> dbDatabases = dbDatabaseService.selectAll(baseEntity);
        if(!CollectionUtils.isEmpty(dbDatabases.getList())) {
            return new Result(true, StatusCode.OK, "获取数据库信息成功", dbDatabases);
        }
        else {
            return new Result(false, StatusCode.ERROR, "没有配置好的数据库，请选择数据库配置增加");
        }
    }

    @ApiOperation("测试数据库连接")
    @RequestMapping(value = "/checkConn", method = RequestMethod.POST)
    public Result checkConn(@RequestBody DbDatabase dbDatabase) {
        Connection conn = JdbcUtil.getConn(dbDatabase);
        if (conn != null) {
            JdbcUtil.close(conn);
            return new Result(true, StatusCode.OK, "数据库连接成功");
        } else {
            return new Result(false, StatusCode.ERROR, "数据库连接失败");
        }
    }

    @ApiOperation("保存数据库配置")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result saveDatabase(@RequestBody DbDatabase dbDatabase) {
        String userCode = getLoginUser().getUsercode();
        if (StringUtils.isEmpty(dbDatabase.getCreateBy())) {
            dbDatabase.setCreateBy(userCode);
            dbDatabase.setCreateDate(new Date());
            List<DbDatabase> dbs = dbDatabaseService.getDbDatabaseByAddressAndDbUserAndDbPwd(dbDatabase.getAddress(), dbDatabase.getDbUser(), dbDatabase.getDbPwd());
            if (!CollectionUtils.isEmpty(dbs)) {
                return new Result(false, StatusCode.ERROR, "数据库已存在");
            }
            dbs = dbDatabaseService.getDbDataBaseByName(dbDatabase.getName());
            if (!CollectionUtils.isEmpty(dbs)) {
                return new Result(false, StatusCode.ERROR, "数据库名已存在");
            }
        }
        dbDatabase.setUpdateBy(userCode);
        dbDatabase.setUpdateDate(new Date());
        boolean result = dbDatabaseService.saveDbDatabase(dbDatabase);
        if(result) {
            return new Result(true, StatusCode.OK, "保存数据库成功");
        } else {
            return new Result(false, StatusCode.ERROR, "数据库连接失败，请检查数据库地址，帐号，密码");
        }
    }

    @ApiOperation("查询所有数据库配置")
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", dbDatabaseService.findAll());
    }

    @ApiOperation("根据id查询数据库配置")
    @RequestMapping(value = "/find/{id}", method = RequestMethod.GET)
    public Result find(@PathVariable("id") String id) {
        return new Result(true, StatusCode.OK, "获取数据库信息成功", dbDatabaseService.getDbDatabaseById(id));
    }

    @ApiOperation("根据id查询数据库配置")
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable("id") String id) {
        List<DbDatabase> dbDatabases = new ArrayList<>();
        dbDatabases.add(dbDatabaseService.getDbDatabaseById(id));
        return new Result(true, StatusCode.OK, "获取数据库信息成功", PageResult.getPageResult(dbDatabases));
    }

    @ApiOperation("根据数据库配置查询数据库表名")
    @RequestMapping(value = "/findDbTableName/{id}", method = RequestMethod.GET)
    public Result findAllTableNameById(@PathVariable("id") String id) {
        List<String> tableNames = dbDatabaseService.findAllTableNameById(id);
        if (CollectionUtils.isEmpty(tableNames)) {
            return new Result(false, StatusCode.ERROR, "查询的数据库下没有表");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", tableNames);
        }
    }

    @ApiOperation("根据数据库id查询数据表的名称和注释")
    @RequestMapping(value = "/findTable/{id}", method = RequestMethod.GET)
    public Result findAllTableNameAndCommentsById(@PathVariable("id") String id) {
        List<TableComments> tableMap = dbDatabaseService.findAllTableNameAndCommentsById(id);
        if (tableMap.size() != 0) {
            return new Result(true, StatusCode.OK, "查询成功", tableMap);
        } else {
            return new Result(false, StatusCode.ERROR, "没有找到数据库下的表");
        }
    }


    @ApiOperation("根据id删除数据库配置")
    @RequestMapping(value = "/del/{id}", method = RequestMethod.GET)
    public Result deleteById(@PathVariable("id") String id) {
        // 如果数据库下有表的信息被共享出去，禁止删除数据库配置信息
        List<Resource> resources = resourceService.findByDbId(id);
        if (!CollectionUtils.isEmpty(resources)) {
            return new Result(false, StatusCode.ERROR, "数据库下存在表共享信息,禁止删除");
        }
        // 判断数据库id下是否有数据表配置信息，如果存在把数据表配置信息删除
        List<DbTable> dbTables = dbTableService.findByDbId(id);
        if (dbTables.size() != 0) {
            return new Result(false, StatusCode.ERROR, "数据库下存在表配置,请先删除表配置");
        }
        dbDatabaseService.deleteById(id);
        return new Result(true, StatusCode.OK, "数据库配置信息删除成功");
    }

    @ApiOperation("查看配置数据表的数据库信息")
    @RequestMapping(value = "/dbIntb", method = RequestMethod.GET)
    public Result getDbDatabasesInTable() {
        return new Result(true, StatusCode.OK, "查询成功", dbDatabaseService.getDbDatabasesInTable());
    }

    @ApiOperation("查看配置字段表的数据库信息")
    @RequestMapping(value = "/dbIncl", method = RequestMethod.GET)
    public Result getDbDatabasesInColumn() {
        return new Result(true, StatusCode.OK, "查询成功", dbDatabaseService.getDbDatabasesInColumn());
    }

}
