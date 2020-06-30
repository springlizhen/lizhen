package com.chinags.device.schedule.client;

import com.chinags.common.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * @Author : Mark_wang
 * @Date : 2019-9-2
 **/
@Component
@FeignClient("service-file")
public interface FileUploadClient {
    /**
     * 为默认添加的上传文件的pid修改pid
     * @param pid 正式的pid
     * @param opid 默认添加的pid
     * @return
     */
    @RequestMapping(value = "/file/updatePid/{pid}/{opid}", method = RequestMethod.GET)
    Result updatePid(@PathVariable("pid") String pid, @PathVariable("opid") String opid);

}
