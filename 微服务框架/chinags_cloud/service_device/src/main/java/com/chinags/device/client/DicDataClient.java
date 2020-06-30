package com.chinags.device.client;

import com.chinags.common.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient("service-auth")
public interface DicDataClient {
    @RequestMapping(value = "/dicData/findAll", method = RequestMethod.GET)
    public Result findAll(@RequestParam(value = "dicType")String dicType, @RequestParam(value = "dicValue")String dicValue);
    @RequestMapping(value = "/office/getUserOffice", method = RequestMethod.GET)
    public Result getUserOffice(@RequestParam(value = "userCode")String userCode);
}
