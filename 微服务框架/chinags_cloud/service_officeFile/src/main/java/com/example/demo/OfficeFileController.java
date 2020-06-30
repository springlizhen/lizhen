package com.example.demo;

import com.alibaba.fastjson.JSONObject;
//import com.zhuozhengsoft.pageoffice.FileSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/office")
public class OfficeFileController {
    @Autowired
    private OAFileService oaFileService;

    /**
     * 获取数据
     */
    @RequestMapping(value = "list")
    @ResponseBody
    @CrossOrigin
    public String get(String id, HttpServletRequest request) {
        String jsonpCallback = request.getParameter("jsonpCallback");
        OAFile oaFile = oaFileService.get(id);
        Map map = new HashMap();
        map.put("path", oaFile.getSavePath());
        map.put("name", oaFile.getSaveName());
        map.put("type", oaFile.getExtention());
        map.put("id", id);
        if(jsonpCallback!=null) {
            return jsonpCallback + "(" + JSONObject.toJSONString(map) + ")";
        }else{
            String s = JSONObject.toJSONString(map)+"";
            return s;
        }
    }
}
