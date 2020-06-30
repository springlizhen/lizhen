package com.chinags.device.basic.controller;

import com.chinags.common.collect.ListUtils;
import com.chinags.common.collect.MapUtils;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.StringUtils;
import com.chinags.device.basic.entity.DerviceOfficePb;
import com.chinags.device.basic.entity.Field;
import com.chinags.device.basic.service.DerviceOfficePbService;
import com.chinags.device.basic.service.FieldService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Api("公共分类管理controller")
@RestController
@CrossOrigin
@RequestMapping("/derviceOfficePb")
public class DerviceOfficePbController extends BaseController {

    @Autowired
    private DerviceOfficePbService derviceOfficePbService;

    /**
     * 获取指定代码字段信息
     * @return
     */
    @ApiOperation("获取公共分类信息")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ResponseBody
    public Result findAll(DerviceOfficePb derviceOfficePb){
        List<DerviceOfficePb> derviceOfficePbs;
        try {
            //类似查询全部
            derviceOfficePbs = derviceOfficePbService.findAll(derviceOfficePb);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",derviceOfficePbs);
    }

    /**
     * 分页获取代码字段信息
     * @return
     */
    @ApiOperation("分页获取公共分类信息")
    @RequestMapping(value = "/pageList", method = RequestMethod.GET)
    @ResponseBody
    public Result areaListPage(DerviceOfficePb derviceOfficePb){
        PageResult<DerviceOfficePb> page;
        try {
            //类似查询全部
            page = derviceOfficePbService.find(derviceOfficePb);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",page);
    }

    /**
     * 获取公共分类信息
     * @return
     */
    @ApiOperation("获取公共分类信息")
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public Result areaForm(String id){
        DerviceOfficePb derviceOfficePb = new DerviceOfficePb();
        try {
            if(StringUtils.isNotEmpty(id)) {
                derviceOfficePb = derviceOfficePbService.getDerviceOfficePbById(id);
            }
        }catch (Exception e){
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",derviceOfficePb);
    }

    @ApiOperation("保存公共分类")
    @RequestMapping(value = "/saveAndUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Result saveAndUpdate(@RequestBody DerviceOfficePb derviceOfficePb) {
        if(derviceOfficePb.getIsNewRecord()) {
            derviceOfficePb.setStatus("0");
        }else{
            DerviceOfficePb derviceOfficePbParent = derviceOfficePbService.getDerviceOfficePbById(derviceOfficePb.getId());
            derviceOfficePb.setStatus(derviceOfficePbParent.getStatus());
        }
        derviceOfficePb.setCreateBy(getLoginUser().getUsercode());
        derviceOfficePb.setUpdateBy(getLoginUser().getUsercode());
        derviceOfficePb.setCreateDate(new Date());
        derviceOfficePb.setUpdateDate(new Date());
        this.derviceOfficePbService.save(derviceOfficePb);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("删除公共分类")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Result delete(String id) {
        if(StringUtils.isNotEmpty(id)){
            DerviceOfficePb derviceOfficePb = derviceOfficePbService.delete(id);
            return new Result(true, StatusCode.OK, "删除公共分类“"+derviceOfficePb.getName()+"”成功");
        }else {
            return new Result(true, StatusCode.ERROR, "删除公共分类失败");
        }

    }

}
