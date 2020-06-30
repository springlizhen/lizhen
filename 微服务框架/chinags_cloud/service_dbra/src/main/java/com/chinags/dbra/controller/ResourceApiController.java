package com.chinags.dbra.controller;

import com.alibaba.fastjson.JSON;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.dbra.entity.BasicData;
import com.chinags.dbra.entity.DbDatabase;
import com.chinags.dbra.entity.ResourceApi;
import com.chinags.dbra.entity.DbTable;
import com.chinags.dbra.service.*;
import com.chinags.dbra.util.FunctionUtil;
import com.chinags.dbra.util.JdbcUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

/**
 * @author Mark_Wang
 * @date 2019/7/1
 **/
@Api("资源共享管理controller")
@RestController
@CrossOrigin
@RequestMapping("/reapi")
public class ResourceApiController extends BaseController {

    @Autowired
    private ResourceApiService resourceApiService;

    @Autowired
    private DbTableService dbTableService;

    @Autowired
    private BasicDataService basicDataService;

    @Autowired
    private ApiParamService apiParamService;

    @Autowired
    private DbDatabaseService dbDatabaseService;

    @ApiOperation("根据资源id查询api信息后台展示")
    @RequestMapping(value = "/reId/{reId}", method = RequestMethod.GET)
    public Result findByReId(@PathVariable("reId") String reId) {
        ResourceApi resourceApi = resourceApiService.findByReId(reId);
        if (resourceApi != null) {
            String columns = resourceApi.getColumns();
            List<BasicData> basicDataList = JSON.parseArray(columns, BasicData.class);
            String col = "";
            for (int i=0; i<basicDataList.size(); i++) {
                col+=basicDataList.get(i).getName();
                if (i < basicDataList.size() -1) {
                    col+=",";
                }
            }
            resourceApi.setColumns(col);
            return new Result(true, StatusCode.OK, "共享信息查询成功", resourceApi);
        } else {
            return new Result(false, StatusCode.ERROR, "该表没有共享信息");
        }
    }

    @ApiOperation("查询共享字段信息")
    @RequestMapping(value = "/col/{reId}", method = RequestMethod.GET)
    public Result findColByReId(@PathVariable("reId") String reId) {
        ResourceApi resourceApi = resourceApiService.findByReId(reId);
        List<BasicData> basicDatas = JSON.parseArray(resourceApi.getColumns(), BasicData.class);
        return new Result(true, StatusCode.OK, "查询成功", basicDatas);
    }

    @ApiOperation("根据资源id查询api信息前端展示")
    @RequestMapping(value = "/find/{reId}", method = RequestMethod.GET)
    public Result findApiByReId(@PathVariable("reId") String reId) {
        ResourceApi resourceApi = resourceApiService.findByReId(reId);
        return new Result(true, StatusCode.OK, "查询成功", resourceApi);
    }

    @ApiOperation("保存资源共享信息")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result saveShare(@RequestBody ResourceApi resourceApi) {
        boolean isNew = false;
        if (StringUtils.isEmpty(resourceApi.getCreateDate())) {
            resourceApi.setCreateBy(getLoginUser().getUsercode());
            resourceApi.setCreateDate(new Date());
            isNew = true;
        }
        resourceApi.setUpdateBy(getLoginUser().getUsercode());
        resourceApi.setUpdateDate(new Date());
        DbTable dbTable = dbTableService.findById(resourceApi.getTbId());
        if (StringUtils.isEmpty(resourceApi.getColumns()) || StringUtils.isEmpty(dbTable.getName())) {
            return new Result(false, StatusCode.ERROR, "传入的属性或表错误");
        }
        List<BasicData> cols = new ArrayList<>();
        List<BasicData> basicDataList = basicDataService.findColumnsCommentsByDbIdAndTableName(resourceApi.getDbId(), dbTable.getName());
        String[] columns = resourceApi.getColumns().split("\\|", -1);
        for (BasicData basicData : basicDataList) {
            for (String col : columns) {
                if (basicData.getName().equalsIgnoreCase(col)) {
                    cols.add(basicData);
                }
            }
        }
        resourceApi.setColumns(JSON.toJSONString(cols));
        resourceApiService.save(resourceApi, dbTable.getName());
        if (isNew) {
            // 更新api host信息
            String apiHost = "/service/this/" + resourceApi.getId() + "/data";
            String apiCountHost = "/service/this/" + resourceApi.getId() + "/data_count";
            resourceApiService.updateHostById(apiHost, apiCountHost, resourceApi.getId());
            apiParamService.defaultAddByApiId(resourceApi.getId());
        }
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("根据选择字段生成查询sql")
    @RequestMapping(value = "/sql/{tbId}/{cls}", method = RequestMethod.GET)
    public Result getSelectSqlByColsAndTbId(@PathVariable("tbId") String tbId, @PathVariable("cls") String cls) {
        if (StringUtils.isEmpty(cls)) {
            return null;
        }
        DbTable dbTable = dbTableService.findById(tbId);
        List<BasicData> cols = new ArrayList<>();
        List<BasicData> basicDataList = basicDataService.findColumnsCommentsByDbIdAndTableName(dbTable.getDbId(), dbTable.getName());
        String[] columns = cls.split("\\*", -1);
        for (BasicData basicData : basicDataList) {
            for (String col : columns) {
                if (basicData.getName().equalsIgnoreCase(col)) {
                    cols.add(basicData);
                }
            }
        }
        String sql = FunctionUtil.generatSelectSql(cols, dbTable.getName());
        return new Result(true, StatusCode.OK, "获取成功", sql);
    }

    @ApiOperation("统计接口测试")
    @RequestMapping(value = "/test/{tbId}", method = RequestMethod.GET)
    public Result testCountApi(@PathVariable("tbId") String tbId) {
        DbTable dbTable = dbTableService.findById(tbId);
        if (dbTable != null) {
            Long nums = basicDataService.findCount(dbTable.getDbId(), dbTable.getName());
            return new Result(true, StatusCode.OK, "查询成功", nums);
        } else {
            return new Result(false, StatusCode.ERROR, "数据库或数据表没找到");
        }
    }

    @ApiOperation("接口测试")
    @RequestMapping(value = "/testApi", method = RequestMethod.POST)
    public Result testApi(@RequestBody ResourceApi resourceApi) {
        DbDatabase dbDatabase = dbDatabaseService.getDbDatabaseById(resourceApi.getDbId());
        PreparedStatement pstmt;
        try {
            Connection conn = JdbcUtil.getConn(dbDatabase);
            String sql = resourceApi.getSql();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, 20);
            pstmt.setInt(2, 0);
            ResultSet rs = pstmt.executeQuery();
            List list =new ArrayList();
            List<BasicData> basicDataList = new ArrayList<>();
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                if (!rs.getMetaData().getColumnLabel(i+1).equalsIgnoreCase("rn")) {
                    BasicData basicData = new BasicData();
                    basicData.setName(rs.getMetaData().getColumnLabel(i+1));
                    basicDataList.add(basicData);
                }
            }
            List<Object> resultList = FunctionUtil.generateResultToJson(rs, basicDataList);
            rs.close();
            conn.close();
            return new Result(true, StatusCode.OK, "查询成功", resultList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, e.getMessage());
        }
    }

}
