package com.bjpowernode.p2p.service.user;

import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.model.vo.ResultObject;

/**
 * ClassName：UserService
 * Package:com.bjpowernode.p2p.service.user
 * Description:
 *
 * @date:2019/2/23 12:29
 * @author:guoxin@bjpowernode.com
 */
public interface UserService {


    Long queryAllUserCount();
    /**
     *
     * 根据手机号码查询用户信息
     *
     *
     *
     * **/

    User queryUserByPhone(String phone);

    ResultObject register(String phone, String loginPassword);
    /**
     *
     *
     * 根据用户标识更新用户信息
     *
     *
     *
     * **/
    int modifyUserById(User user);

    User login(String phone, String loginPassword);
}
