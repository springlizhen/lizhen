package com.bjpowernode.p2p.service.user;

import com.bjpowernode.p2p.common.contants.Constants;
import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.mapper.user.UserMapper;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.model.vo.ResultObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * ClassName：UserServiceImpl
 * Package:com.bjpowernode.p2p.service.user
 * Description:
 *
 * @date:2019/2/23 12:31
 * @author:guoxin@bjpowernode.com
 */
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private FinanceAccountMapper financeAccountMapper;
    @Override
    public Long queryAllUserCount() {
        /*
         * 首先从redis缓存中获取缓存值
         *获取到操作key中指定的value值
         *
         * */
        BoundValueOperations<String, Object> boundValueOperations = redisTemplate.boundValueOps(Constants.ALL_USER_COUNT);
        //从该对象中取出value值
        Long allUserCount = (Long) boundValueOperations.get();
        if (allUserCount == null) {
            //没有值从数据库中查询
            allUserCount = userMapper.selectAllUserCount();
            //将值存放到redis的缓存中
            boundValueOperations.set(allUserCount, 15, TimeUnit.MINUTES);
        }

        return allUserCount;
    }

    @Override
    public User queryUserByPhone(String phone) {
        return userMapper.selectUserByPhone(phone);
    }

    @Override
    public ResultObject register(String phone, String loginPassword) {
       ResultObject resultObject = new ResultObject();
       resultObject.setErrorCode(Constants.SUCCESS);
       //新增用户
       User user = new User();
       user.setLoginPassword(loginPassword);
       user.setPhone(phone);
       user.setAddTime(new Date());
       //最近登陆时间
       user.setLastLoginTime(new Date());
       int insertUserCount = userMapper.insertSelective(user);
       if(insertUserCount>0){
           //新增账户
           User userDetail = userMapper.selectUserByPhone(phone);
            FinanceAccount financeAccount = new FinanceAccount();
            financeAccount.setUid(userDetail.getId());
            financeAccount.setAvailableMoney(888.0);
           int insertSelective = financeAccountMapper.insertSelective(financeAccount);

           if (insertSelective <= 0) {
               //fail失败
               resultObject.setErrorCode(Constants.FAIL);
           }

       } else {
           resultObject.setErrorCode(Constants.FAIL);
       }
        return resultObject;
       }

    @Override
    public int modifyUserById(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public User login(String phone, String loginPassword) {
        //根据手机号码与密码查询用户
        User user = userMapper.selectUserByPhoneAndLoginPassword(phone,loginPassword);
        //更新系统时间
        if(user !=null){
            User updateUser = new User();
            updateUser.setId(user.getId());
            updateUser.setAddTime(new Date());
            userMapper.updateByPrimaryKeySelective(updateUser);

        }
        return user;
    }

}



