package com.jeesite.modules.sys.web.nwmh;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计Controller
 *
 * @author suijinchi
 * @version 2020-03-27
 */
@RestController
@RequestMapping(value = "${adminPath}/sys/nwmh/statistics")
public class StatisticsController extends BaseController {

    /**
     * 雨情信息统计
     */
    @GetMapping(value = "yqxx")
    public Object yqxx(HttpServletRequest request) {
//        Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMapPost(request), "");
        String jsonStr = "{\"status\":true,\"code\":20000,\"message\":\"获取成功\",\"data\":{\"rainfallInfos\":[{\"name\":\"小雨\",\"value\":0},{\"name\":\"中雨\",\"value\":0},{\"name\":\"大雨\",\"value\":0},{\"name\":\"暴雨\",\"value\":0},{\"name\":\"特大暴雨\",\"value\":0}],\"startTime\":\"2020-03-17 09:00\",\"endTime\":\"2020-03-28 09:00\"}}";
        return JSONObject.parse(jsonStr);
    }

}