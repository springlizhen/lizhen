package com.chinags.dbra.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.BaseEntity;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.dbra.entity.DbColumn;
import com.chinags.dbra.service.DbColumnService;
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
 * @author Mark_Wang
 * @date 2019/6/28
 * @time 11:32
 **/
@Api("数据字段管理controller")
@RestController
@CrossOrigin
@RequestMapping("/cl")
public class DbColumnController extends BaseController {

    @Autowired
    private DbColumnService dbColumnService;

    @ApiOperation("分页查询所有数据字段配置")
    @RequestMapping(value = "/findAll", method = RequestMethod.POST)
    public Result findAllByPage(BaseEntity entity) {
        PageResult<DbColumn> dbColumns = dbColumnService.findAllByPage(entity);
        if (CollectionUtils.isEmpty(dbColumns.getList())) {
            return new Result(false, StatusCode.ERROR, "没有数据字段配置，请前往配置");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", dbColumns);
        }
    }

    @ApiOperation("查询所有数据字段配置")
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(BaseEntity entity) {
        List<DbColumn> dbColumns = dbColumnService.findAll();
        if (CollectionUtils.isEmpty(dbColumns)) {
            return new Result(false, StatusCode.ERROR, "没有数据字段配置，请前往配置");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", dbColumns);
        }
    }

    @ApiOperation("根据id查询数据字段配置")
    @RequestMapping(value = "/selectById/{id}", method = RequestMethod.GET)
    public Result selectById(@PathVariable("id") String id) {
        return new Result(true, StatusCode.OK, "查询成功", dbColumnService.findDbColumnById(id));
    }

    @ApiOperation("根据id查询数据字段配置")
    @RequestMapping(value = "/find/{id}", method = RequestMethod.GET)
    public Result findDbColumnById(@PathVariable("id") String id) {
        List<DbColumn> dbColumns = new ArrayList<>();
        dbColumns.add(dbColumnService.findDbColumnById(id));
        PageResult<DbColumn> pageResult = PageResult.getPageResult(dbColumns);
        return new Result(true, StatusCode.OK, "查询成功", pageResult);
    }

    @ApiOperation("根据数据表id查询数据字段配置")
    @RequestMapping(value = "/findByTbId/{tbId}/{pageNo}/{pageSize}", method = RequestMethod.GET)
    public Result findDbColumnByTbId(@PathVariable("tbId") String tbId, @PathVariable("pageNo") int pageNo, @PathVariable("pageSize") int pageSize) {
        int num = 0;
        int size = pageSize;
        if (pageNo != 1) {
            num = pageSize * (pageNo - 1);
            size = pageSize * pageNo;
        }
        long count = dbColumnService.findCountByTbId(tbId);
        List<DbColumn> dbColumns = dbColumnService.findDbColumnsByTbIdAndPage(tbId, num, size);
        PageResult<DbColumn> pageResult = PageResult.getPageResult(dbColumns, count, pageNo);
        if (CollectionUtils.isEmpty(pageResult.getList())) {
            return new Result(false, StatusCode.ERROR, "表下没有字段信息，请前往配置");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", pageResult);
        }
    }

    @ApiOperation("根据数据库id查询数据字段配置")
    @RequestMapping(value = "/findByDbId/{dbId}/{pageNo}/{pageSize}", method = RequestMethod.GET)
    public Result findDbColumnsByDbId(@PathVariable("dbId") String dbId, @PathVariable("pageNo") int pageNo, @PathVariable("pageSize") int pageSize) {
        int num = 0;
        int size = pageSize;
        if (pageNo != 1) {
            num = pageSize * (pageNo - 1);
            size = pageSize * pageNo;
        }
        long count = dbColumnService.findCountByDbId(dbId);
        List<DbColumn> dbColumns = dbColumnService.findDbColumnsByDbIdAndPage(dbId, num, size);
        PageResult<DbColumn> pageResult = PageResult.getPageResult(dbColumns);
        if (CollectionUtils.isEmpty(pageResult.getList())) {
            return new Result(false, StatusCode.ERROR, "表下没有字段信息，请前往配置");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", pageResult);
        }
    }

    @ApiOperation("保存数据字段信息")
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody DbColumn dbColumn) {
        String userCode = getLoginUser().getUsercode();
        if (StringUtils.isEmpty(dbColumn.getCreateBy())) {
            dbColumn.setCreateBy(userCode);
            dbColumn.setCreateDate(new Date());
            List<DbColumn> cols = dbColumnService.getDbColumnByTbIdAndDbIdAndName(dbColumn.getTbId(), dbColumn.getDbId(), dbColumn.getName());
            if (!CollectionUtils.isEmpty(cols)) {
                return new Result(false, StatusCode.ERROR, "数据字段配置已存在");
            }
        }
        dbColumn.setUpdateBy(userCode);
        dbColumn.setUpdateDate(new Date());
        DbColumn column = dbColumnService.save(dbColumn);
        if (column == null) {
            return new Result(false, StatusCode.ERROR, "数据字段保存失败");
        } else {
            return new Result(true, StatusCode.OK, "数据字段保存成功", column);
        }
    }

    @ApiOperation("根据id删除数据字段信息")
    @RequestMapping(value = "/del/{id}", method = RequestMethod.GET)
    public Result deleteById(@PathVariable("id") String id) {
        dbColumnService.deleteById(id);
        return new Result(true, StatusCode.OK, "数据字段删除成功");
    }


}
