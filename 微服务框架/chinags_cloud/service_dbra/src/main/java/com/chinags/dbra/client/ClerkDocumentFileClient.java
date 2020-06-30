package com.chinags.dbra.client;

import com.chinags.archives.entity.ClerkDocumentFile;
import com.chinags.common.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author : Mark_wang
 * @Date : 2019-9-29
 **/
@Component
@FeignClient("service-archives")
public interface ClerkDocumentFileClient {
    @RequestMapping(value = "/documentfile/public", method = RequestMethod.POST)
    Result findClerkDocumentFilesByIsPublic(ClerkDocumentFile clerkDocumentFile);

    @RequestMapping(value = "/documentfile/findById/{id}", method = RequestMethod.GET)
    Result findById(@PathVariable("id") String id);
}
