package com.chinags.device.check.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.device.check.entity.CheckNote;
import com.chinags.device.check.service.CheckNoteService;
import com.chinags.device.client.UserClient;
import com.chinags.device.schedule.client.FileUploadClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Api("设备管理controller")
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")

@RequestMapping("/checkNote")
public class CheckNoteControler extends BaseController {
    @Autowired
    private UserClient userClient;
    @Autowired
    private CheckNoteService checkNoteService;
    @Autowired
    private FileUploadClient fileUploadClient;
    /**
     * 分页获取设备信息
     *
     * @return
     */
    @ApiOperation("分页获取巡检信息")
    @RequestMapping(value = "/appSelect", method = RequestMethod.GET)
    @ResponseBody
    public Result appSelect(CheckNote checkNote) {
        PageResult<CheckNote> page;
        try {
            //类似查询全部

            page = checkNoteService.find(checkNote);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", page);
    }
    @ApiOperation("分页获取巡检信息")
    @RequestMapping(value = "/select", method = RequestMethod.POST)
    @ResponseBody
    public Result officeListPage(@RequestBody  CheckNote checkNote) {
        PageResult<CheckNote> page;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            //类似查询全部
            if(null != checkNote.getKb() && !"".equals(checkNote.getKb()) ){
                    checkNote.setCreateDate(sdf.parse(checkNote.getKb()));
            }
            page = checkNoteService.selectCheckNote(checkNote);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", page);
    }
    @ApiOperation("获取机构信息")
    @RequestMapping(value = "/selectOffice", method = RequestMethod.GET)
    @ResponseBody
    public Result selectOffice(String officeLevel) {
        Result result=null;
        try {
            //类似查询全部
            result = userClient.officeList(officeLevel);
            List<Map<String,Object>> maps = (List<Map<String, Object>>) result.getData();
            for(Map<String,Object> ti:maps){
                for (Map.Entry<String, Object> m :ti.entrySet())  {

                    if (m.getValue().equals("分中心")) {
                        maps.remove(ti);

                    }}
            }
            result.setData(maps);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return result;
    }
    @ApiOperation("获取巡检记录信息")
    @RequestMapping(value = "/selectCheckNoteByOffice", method = RequestMethod.GET)
    @ResponseBody
    public Result selectCheckNoteByOffice(String checkCenter,String manageStation,String manageOffice) {
        List<Map<String,Object>> list=null;
        try {
            //类似查询全部
            list = checkNoteService.selectCheckNoteByOffice(checkCenter,manageStation,manageOffice);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", list);
    }
    @ApiOperation("获取分中心三级联动信息")
    @RequestMapping(value = "/listCheckTo", method = RequestMethod.GET)
    @ResponseBody
    public Result listCheckTo(String officeName) {
        List<String> list=null;
        try {
            //类似查询全部
            Result result = userClient.officeListFenzhongxin(officeName);
            list = (List<String>)result.getData();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", list);
    }
    @ApiOperation("获取巡检信息")
    @RequestMapping(value = "/selectCheckByOffice", method = RequestMethod.GET)
    @ResponseBody
    public Result selectCheckByOffice(String checkCenter,String manageStation,String manageOffice) {
        List<Map<String,Object>> list=null;
        try {
            //类似查询全部
            list = checkNoteService.selectCheckByOffice(checkCenter,manageStation,manageOffice);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", list);
    }


    @ApiOperation("获取规范信息")
    @RequestMapping(value = "/selectCheck", method = RequestMethod.GET)
    @ResponseBody
    public Result selectCheck(String id) {
        CheckNote checkNote = null;
        try {
            //类似查询全部
            checkNote = checkNoteService.selectCheck(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", checkNote);
    }
    @ApiOperation("获取巡检名称")
    @RequestMapping(value = "/selectCheckLongitudeById", method = RequestMethod.GET)
    @ResponseBody
    public Result selectCheckLongitudeById(String id) {
        List list = null;
        try {
            //类似查询全部
            list = checkNoteService.selectCheckLongitudeById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", list);
    }
    @ApiOperation("获取巡检名称")
    @RequestMapping(value = "/selectCheckName", method = RequestMethod.GET)
    @ResponseBody
    public Result selectCheckName() {
        List list = null;
        try {
            //类似查询全部
            list = checkNoteService.selectCheckName();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", list);
    }
    @ApiOperation("获取巡检名称")
    @RequestMapping(value = "/selectCheckNote", method = RequestMethod.GET)
    @ResponseBody
    public Result selectCheckNote(String checkPerson,String createDate) {
        List list = null;
        try {
            //类似查询全部
            list = checkNoteService.selectCheckNote(checkPerson,createDate);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", list);
    }


    @ApiOperation("保存设备")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestBody CheckNote checkNote) {
        Boolean isNew=false;
        if(checkNote.getId()==null){
            isNew=true;
        }
        checkNoteService.save(checkNote);

        if(isNew){
            fileUploadClient.updatePid(checkNote.getId(),"checkNote");
        }
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("删除设备")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Result delete(CheckNote checkNote) {
        checkNoteService.delete(checkNote);
        return new Result(true, StatusCode.OK, "删除成功");

    }
    }
