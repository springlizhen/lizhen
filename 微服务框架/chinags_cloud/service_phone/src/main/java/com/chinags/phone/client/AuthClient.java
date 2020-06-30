package com.chinags.phone.client;

import com.chinags.common.entity.LoginUser;
import com.chinags.common.entity.Result;
import com.chinags.phone.entity.Base64;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@FeignClient("service-auth")
public interface AuthClient {

    // APP端登录
    @PostMapping("/sso/phonelogin")
    Result phoneLogin(@RequestBody LoginUser loginUser);

    // 部门列表
    @GetMapping("/office/officeList")
    Result officeList(@RequestParam("officeLevel") String officeLevel);

    // 菜单列表
    @GetMapping("/menuMobile/getMenuList")
    Result getMenuList(@RequestParam("usercode") String usercode);

    // 消息列表
    @PostMapping("/message/selectMsgList")
    Result selectMsgList(@RequestParam("usercode") String usercode, @RequestParam("msgType") String msgType,
                         @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize);

    // 更新消息已读状态
    @PostMapping("/message/updateMsgReadStatus")
    Result updateMsgReadStatus(@RequestParam("usercode") String usercode, @RequestParam("msgId") String msgId);

    // 查询未读消息
    @GetMapping("/message/selectIsUnReadMsg")
    Result selectIsUnReadMsg(@RequestParam("usercode") String usercode);

    // 修改密码
    @PostMapping("/sso/editPassword")
    Result editPassword(@RequestParam("usercode") String usercode, @RequestParam("oldPassword") String oldPassword, @RequestParam("password") String password,
                             @RequestParam("confirmPassword") String confirmPassword);

    // 获取用户信息
    @GetMapping("/empUser/form")
    Result userForm(@RequestParam("userCode") String userCode);

    // 修改用户信息
    @GetMapping("/empUser/editUserInfo")
    Result editUserInfo(@RequestParam("userCode") String userCode, @RequestParam("email") String email, @RequestParam("mobile")  String mobile,
                        @RequestParam("phone") String phone, @RequestParam("sign") String sign, @RequestParam("userName") String userName, @RequestParam("sex") String sex);

    // 获取用户头像
    @GetMapping("/empUser/getUserAvatar")
    String getUserAvatar(@RequestParam("userCode") String userCode);

    // 更新用户头像
    @PostMapping("/empUser/uploadUserAvatar")
    Result uploadUserAvatar(@RequestBody Base64 base64);
}
