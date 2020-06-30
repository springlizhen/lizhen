package com.chinags.archives.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author XieWenqing
 * @date 2019/11/22 上午 12:44
 */
@Component
@FeignClient("service-auth")
public interface EmployeeClient {

    @RequestMapping(value = "/employee/findEmpMap", method = RequestMethod.GET)
    List<Map<String, Object>> findEmpMap();

    @RequestMapping(value = "/employee/findEmpByOfficeCode", method = RequestMethod.GET)
    List<Map<String, Object>> findEmpByOfficeCode(@RequestParam(value = "officeCode") String officeCode);
}
