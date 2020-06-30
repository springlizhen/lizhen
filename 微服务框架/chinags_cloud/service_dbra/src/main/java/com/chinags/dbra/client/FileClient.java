package com.chinags.dbra.client;

import com.chinags.common.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @Author : Mark_wang
 * @Date : 2019-9-2
 **/
@Component
@FeignClient("service-file")
public interface FileClient {

    @RequestMapping(value = "/file/down",method = RequestMethod.POST)
    ResponseEntity<byte[]> downloadFile(@RequestParam(value = "id") String id);

    @RequestMapping(value = "/file/pid", method = RequestMethod.GET)
    String downByPid(@RequestParam(value = "pid") String pid);

    @RequestMapping(value = "/file/updatePid/{pid}/{opid}", method = RequestMethod.GET)
    Result updatePid(@PathVariable("pid") String pid, @PathVariable("opid") String opid);

}
