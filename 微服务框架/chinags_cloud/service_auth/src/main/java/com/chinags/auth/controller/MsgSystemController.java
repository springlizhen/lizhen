package com.chinags.auth.controller;

import com.chinags.auth.dao.MsgInnerDao;
import com.chinags.auth.entity.Comm;
import com.chinags.auth.entity.MsgInner;
import com.chinags.auth.entity.MsgInnerRecord;
import com.chinags.auth.entity.system.Message;
import com.chinags.auth.service.*;
import com.chinags.common.controller.SystemBaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.JwtUtil;
import com.chinags.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("消息管理controller")
@RestController
@CrossOrigin
@RequestMapping("/message")
public class MsgSystemController extends SystemBaseController {

    @Autowired
    private MsgInnerService msgInnerService;

    @Autowired
    private MsgInnerRecordService msgInnerRecordService;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 发送消息
     * @return
     */
    @ApiOperation("发送消息")
    @RequestMapping(value = "/sendmsg", method = RequestMethod.POST)
    public Result sendmsg(Message message){
        try {
            msgInnerService.saveMsg(message,getCommCode(),getCommName());
            return new Result(true, StatusCode.OK, "消息发送成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获消息发送失败");
        }
    }

    /**
     * 查询消息列表
     * @return
     */
    @ApiOperation("查询消息列表")
    @RequestMapping(value = "/selectMsgList", method = RequestMethod.POST)
    public Result selectMsgList(String usercode, String msgType, Integer pageNo, Integer pageSize){
        Page<Map<String, Object>> page = msgInnerRecordService.selectMsgListByCondition(usercode, msgType, pageNo, pageSize);
        if (CollectionUtils.isEmpty(page.getContent())) {
            return new Result(false, StatusCode.ERROR, "没有数据");
        } else {
            return new Result(true, StatusCode.OK, "查询成功", page);
        }
    }

    /**
     * 更新消息为已读
     * @return
     */
    @ApiOperation("更新消息为已读")
    @RequestMapping(value = "/updateMsgReadStatus", method = RequestMethod.POST)
    public Result updateMsgReadStatus(String usercode, String msgId){
        msgInnerRecordService.updateMsgReadStatus(usercode, msgId);
        return new Result(true, StatusCode.OK, "更新成功");
    }

    /**
     * 查询是否有未读消息
     * @return
     */
    @ApiOperation("查询是否有未读消息")
    @RequestMapping(value = "/selectIsUnReadMsg", method = RequestMethod.GET)
    public Result selectIsUnReadMsg(String usercode){
        Map<String, Object> data = new HashMap<>(1);
        int isUnReadMsg = msgInnerRecordService.selectIsUnReadMsg(usercode);
        data.put("isUnReadMsg", isUnReadMsg);
        return new Result(true, StatusCode.OK, "查询成功", data);
    }

}
