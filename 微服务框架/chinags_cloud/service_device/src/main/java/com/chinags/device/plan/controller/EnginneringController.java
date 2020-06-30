package com.chinags.device.plan.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.StringUtils;
import com.chinags.device.basic.entity.DerviceOffice;
import com.chinags.device.basic.entity.Device;
import com.chinags.device.basic.service.DeivceService;
import com.chinags.device.basic.service.DerviceOfficeService;
import com.chinags.device.maintain.entity.MaintainInfo;
import com.chinags.device.maintain.service.MaintainInfoService;
import com.chinags.device.plan.entity.Enginnering;
import com.chinags.device.plan.entity.Plan;
import com.chinags.device.plan.entity.Project;
import com.chinags.device.plan.service.EnginneringService;
import com.chinags.device.plan.service.PlanService;
import com.chinags.device.plan.service.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.*;

@Api("工程管理controller")
@RestController
@CrossOrigin
@RequestMapping("/enginnering")
public class EnginneringController extends BaseController {

    @Autowired
    private EnginneringService enginneringService;
    @Autowired
    private PlanService planService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private MaintainInfoService maintainInfoService;
    @Autowired
    private DeivceService deivceService;
    @Autowired
    private DerviceOfficeService derviceOfficeService;
    /**
     * 通过计划id获取工程
     * @return
     */
    @ApiOperation("通过计划id获取工程")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ResponseBody
    public Result findAll(String planid){
        List<Enginnering> lists;
        try {
            //类似查询全部
            lists = enginneringService.findAll(planid);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",lists);
    }

    @ApiOperation("保存工程")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestBody Enginnering enginnering) {
        enginnering.setCreateBy(getLoginUser().getUsercode());
        enginnering.setUpdateBy(getLoginUser().getUsercode());
        enginnering.setCreateDate(new Date());
        enginnering.setUpdateDate(new Date());
        enginnering.setStatus("0");
        this.enginneringService.save(enginnering);
        return new Result(true, StatusCode.OK, enginnering.getId());
    }
    @ApiOperation("获取养护次数")
    @RequestMapping(value = "/listDateTo", method = RequestMethod.POST)
    @ResponseBody
    public Result listDateTo(@RequestBody Enginnering enginnering) {
        String status = "1";
        //获取所有审核通过的计划
        List<Plan> list = planService.selectByPlan(status);
        List<Enginnering> projectList = new ArrayList<>();
        for(Plan to:list){
            //获取到该计划下的所有工程
            List<Enginnering> list1 = new ArrayList<>();
            list1 = enginneringService.selectEnginnering(to.getId());
            if(list1.size()>0){
                for(Enginnering ti:list1){
                    //获取到工程下的项目
                    List<Project> list2 = projectService.selectByProject(ti.getId());
                    Integer sum=0;
                    if(list2.size()>0){
                       for(Project tu:list2){
                           if(null!=tu.getProjectPriceall() && !"".equals(tu.getProjectPriceall())) {
                               sum += Integer.valueOf(tu.getProjectPriceall());
                           }
                       }

                    }
                    ti.setCount(sum);
                    projectList.add(ti);

                }
            }
        }
        return new Result(true, StatusCode.OK,"获取成功", projectList);
    }

    @ApiOperation("没有时间选择的时候获取相应养护")
    @RequestMapping(value = "/listDateToKTo", method = RequestMethod.POST)
    @ResponseBody
    public Result listDateToKTo() {
        List<Object> objectList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        int gugan = 0;
        int guanli = 0;
        int fushu = 0;
        int gongheng = 0;
        List<MaintainInfo> list = maintainInfoService.selectBymaintainDateTo();

            if (list.size() > 0) {
                List<String> list1 = new ArrayList<>();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
                for (MaintainInfo toL : list) {
                    Device device = deivceService.getbyId(toL.getDeceiveId());
                    Date date = toL.getEndTime();
                    String dateTo = formatter.format(date);
                    list1.add(dateTo);
                    DerviceOffice derviceOffice = derviceOfficeService.selectByoffice(device.getDeviceClass());
                    String codes = derviceOffice.getParentCodes();
                    String[] code = codes.split(",");
                    //获取最高的设施设备名称
                    DerviceOffice derviceOffice1 = derviceOfficeService.selectByoffice(code[1]);
                    if ("骨干工程".equals(derviceOffice1.getOfficeName())) {
                        gugan += 1;
                    } else if ("管理设施".equals(derviceOffice1.getOfficeName())) {
                        guanli += 1;
                    } else if ("附属设施".equals(derviceOffice1.getOfficeName())) {
                        fushu += 1;
                    } else if ("工程设施".equals(derviceOffice1.getOfficeName())) {
                        gongheng += 1;
                    }

                }
                map.put("gugan", gugan);
                map.put("guanli", guanli);
                map.put("fushu", fushu);
                map.put("gongcheng", gongheng);
                objectList.add(map);
                objectList.addAll(list1);

            }
            return new Result(true, StatusCode.OK, "获取成功", objectList);

    }


    @ApiOperation("删除工程")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Result delete(String id) {
        if(StringUtils.isNotEmpty(id)){
            enginneringService.delete(id);
            return new Result(true, StatusCode.OK, "删除工程成功");
        }else {
            return new Result(true, StatusCode.ERROR, "删除工程失败");
        }
    }

}
