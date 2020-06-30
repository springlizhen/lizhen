package com.chinags.user.controller;

import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Api("用户管理controller")
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {


    @ApiOperation("hello")
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public Result hello(){
        return new Result(true, StatusCode.OK, "hello world user");
    }
}
