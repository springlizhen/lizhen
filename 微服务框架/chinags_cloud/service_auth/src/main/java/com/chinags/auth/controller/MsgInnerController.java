package com.chinags.auth.controller;

import com.chinags.auth.entity.MsgInner;
import com.chinags.auth.service.MsgInnerService;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("系统消息管理controller")
@RestController
@CrossOrigin
@RequestMapping("/messageInner")
public class MsgInnerController extends BaseController {

    @Autowired
    private MsgInnerService msgInnerService;

    /**
     * 获取消息
     * @return
     */
    @ApiOperation("消息信息")
    @RequestMapping(value = "/pageList", method = RequestMethod.GET)
    @ResponseBody
    public Result commListPage(MsgInner msgInner){
        String usercode = getLoginUser().getUsercode();
        PageResult<MsgInner> page;
        try {
            page = msgInnerService.find(msgInner, usercode);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功",page);
    }
}
