package com.chinags.auth.client;

import com.chinags.common.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
@FeignClient("service-user")
public interface UserClient {
    @RequestMapping(value = "/user/hello", method = RequestMethod.GET)
    public Result hello();
}
