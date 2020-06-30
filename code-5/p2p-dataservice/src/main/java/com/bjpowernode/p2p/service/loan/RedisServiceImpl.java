package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.p2p.common.contants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.concurrent.TimeUnit;

/**
 * ClassName：RedisServiceImpl
 * Package:com.bjpowernode.p2p.service.loan
 * Description:
 *
 * @date:2019/2/26 22:28
 * @author:guoxin@bjpowernode.com
 */
@Service
public class RedisServiceImpl implements RedisServer {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Override
    public void put(String key, String value) {
         redisTemplate.opsForValue().set(key,value,60, TimeUnit.SECONDS);
    }

    @Override
    public String get(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    @Override
    public Long getOnlyNumber() {
        return redisTemplate.opsForValue().increment(Constants.ONLY_NUMBER,1L);
    }
}
