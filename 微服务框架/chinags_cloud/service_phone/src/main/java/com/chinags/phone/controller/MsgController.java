package com.chinags.phone.controller;

import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.LoginUser;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.phone.client.AuthClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("消息管理controller")
@RestController
@CrossOrigin
@RequestMapping("/msg")
public class MsgController extends BaseController {

    @Autowired
    private AuthClient authClient;

    @ApiOperation("查询消息列表")
    @GetMapping("/selectMsgList")
    public Result selectMsgList(String msgType, Integer pageNo, Integer pageSize) {
        Result result = null;
        try {
            if (pageNo == null || pageSize == null) {
                return new Result(false, StatusCode.ERROR, "页码错误");
            }
            LoginUser loginUser = getLoginUser();
            result = authClient.selectMsgList(loginUser.getUsercode(), msgType, pageNo, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return result;
    }

    @ApiOperation("更新消息为已读")
    @GetMapping("/updateMsgReadStatus")
    public Result updateMsgReadStatus(String msgId) {
        Result result = null;
        try {
            LoginUser loginUser = getLoginUser();
            result = authClient.updateMsgReadStatus(loginUser.getUsercode(), msgId);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "更新失败");
        }
        return result;
    }

    @ApiOperation("查询是否有未读消息")
    @GetMapping("/selectIsUnReadMsg")
    public Result selectIsUnReadMsg() {
        Result result = null;
        try {
            LoginUser loginUser = getLoginUser();
            result = authClient.selectIsUnReadMsg(loginUser.getUsercode());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "查询失败");
        }
        return result;
    }

}
