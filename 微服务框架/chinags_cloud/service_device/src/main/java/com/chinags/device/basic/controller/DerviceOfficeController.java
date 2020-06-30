package com.chinags.device.basic.controller;

import com.chinags.common.collect.ListUtils;
import com.chinags.common.collect.MapUtils;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.StringUtils;
import com.chinags.device.basic.entity.DerviceOffice;
import com.chinags.device.basic.entity.Device;
import com.chinags.device.basic.service.DeivceService;
import com.chinags.device.basic.service.DerviceOfficeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 分类
 *
 * @author Mr.Zhang
 * @version V1.0
 * @date
 * @since 1.8
 */

@Api("分类管理controller")
@RestController
@CrossOrigin
@RequestMapping("/deriviceOffice")
public class DerviceOfficeController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(DerviceOfficeController.class);

    @Autowired
    private DerviceOfficeService derviceOfficeService;

    @Autowired
    private DeivceService deivceService;

    /**
     * 分页获取分类信息
     * @return
     */
    @ApiOperation("分页获取分类信息")
    @RequestMapping(value = "/pageList", method = RequestMethod.GET)
    @ResponseBody
    public Result officeListPage(DerviceOffice office){
        PageResult<DerviceOffice> page;
        try {
            String parentCode = office.getOfficeName()==null&&office.getViewCode()==null&&office.getParentCode()==null?"0":office.getParentCode();
            office.setParentCode(parentCode);
            //类似查询全部
            office.setPageSize(100000000);
            page = derviceOfficeService.find(office);
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
    @ApiOperation("获取分类信息")
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public Result officeForm(String officeCode,String parentCode){
        DerviceOffice office = new DerviceOffice();
        try {
            if(StringUtils.isEmpty(parentCode)) {
                office = derviceOfficeService.getAreaByOfficeCode(officeCode);
            } else {
                DerviceOffice o = derviceOfficeService.getAreaByOfficeCode(parentCode);
                office.setParentCode(o.getOfficeCode());
                office.setTreeNames(o.getOfficeName()+"/"+o.getOfficeName()+"/"+o.getOfficeName());
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",office);
    }

    /**
     * 菜单树结构
     * @return
     */
    @ApiOperation("分类树结构")
    @RequestMapping(value = "/treeData", method = RequestMethod.GET)
    public Result treeData(String excludeCode){
        List list = ListUtils.newArrayList();
        List<DerviceOffice> offices;
        try {
            offices = derviceOfficeService.treeData(excludeCode);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }

        for (DerviceOffice a : offices) {
            HashMap map = MapUtils.newHashMap();
            map.put("id", a.getId());
            map.put("pId", a.getParentCode());
            map.put("name", a.getOfficeName());
            map.put("title", a.getOfficeName());
            list.add(map);
        }
        return new Result(true, StatusCode.OK, "获取成功",list);
    }

    @ApiOperation("保存分类")
    @RequestMapping(value = "/saveAndUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Result saveAndUpdate(@RequestBody DerviceOffice office) {
        if(office.getIsNewRecord()) {
            office.setOfficeCode(office.getViewCode());
            DerviceOffice officeParent;
            officeParent = derviceOfficeService.getAreaByOfficeCode(office.getOfficeCode());
            if(officeParent!=null){
                return new Result(true, StatusCode.ERROR, "分类编码已存在");
            }
        }
        office.setCreateBy(getLoginUser().getUsercode());
        office.setUpdateBy(getLoginUser().getUsercode());
        office.setCreateDate(new Date());
        office.setUpdateDate(new Date());
        this.derviceOfficeService.save(office);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("删除分类")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Result delete(DerviceOffice office) {
        Device device = new Device();
        device.setDeviceClass(office.getId());
        List<Device> all = deivceService.findAll(device);
        if(all.size()>0){
            return new Result(true, StatusCode.ERROR, "分类下有设备");
        }
        if(derviceOfficeService.delete(office)) {
            return new Result(true, StatusCode.OK, "删除成功");
        }
        else {
            return new Result(true, StatusCode.ERROR, "请先删除子分类");
        }
    }

    @ApiOperation("停用分类")
    @RequestMapping(value = "/disable", method = RequestMethod.GET)
    @ResponseBody
    public Result disable(String id) {
        if(StringUtils.isNotEmpty(id)) {
            return derviceOfficeService.disable(id);
        }
        else {
            return new Result(true, StatusCode.ERROR, "未选择分类");
        }
    }

    @ApiOperation("启用分类")
    @RequestMapping(value = "/enable", method = RequestMethod.GET)
    @ResponseBody
    public Result enable(String id) {
        if(StringUtils.isNotEmpty(id)) {
            String result = derviceOfficeService.enable(id);
            return new Result(true, StatusCode.OK, result);
        }
        else {
            return new Result(true, StatusCode.ERROR, "未选择分类");
        }
    }
}
