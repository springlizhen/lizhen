package com.jeesite.modules.sys.web.nwmh;

import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.utils.DbraLoginUtiles;
import com.jeesite.modules.sys.utils.OAConfigUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.jeesite.common.web.BaseController.text;
import static com.jeesite.common.web.http.ServletUtils.renderResult;

/**
 * 文件上传接口
 * @Author : Mark_wang
 * @Date : 2019/8/1
 **/
@Controller
@RequestMapping(value = "${adminPath}/nwmh/upload")
public class UploadController {
    /**
     * 查询列表
     * 需要输入的信息：
     * userCode 用户登录编号
     * pid 保存的订单的编号
     * readOnly 本次是否不允许上传 0：是  非0：否
     */
    @RequestMapping(value = {"list", ""})
    public String list(HttpServletRequest request, Model model, String pid, String readOnly) {
        model.addAttribute("pid", pid);
        model.addAttribute("reonly", readOnly);
        model.addAttribute("fileService", OAConfigUtil.uploadHtp());
        return "modules/sys/nwmh/WebUpload";
    }

    /**
     * Json数据参数例子
     * {
     *  count:20,
     *  pageNo:1,
     *  pageSize:20,
     *  list:[]
     * }
     * 查询列表数据
     */
    @RequestMapping(value = "listData")
    @ResponseBody
    public String listData(HttpServletRequest request, HttpServletResponse response, Model model) {
        Map<String,String[]> dataParam = request.getParameterMap();
        Map<String,String> dataMap = MapUtils.newHashMap();
        for (Map.Entry<String, String[]> entry : dataParam.entrySet()) {
            if(entry.getValue().length>0&&!entry.getValue()[0].equals(""))
                if(entry.getKey().equals("orderBy"))
                    dataMap.put(entry.getKey(),(entry.getValue()[0].replace(" ","%20")));
                else
                    dataMap.put(entry.getKey(),(entry.getValue()[0]));
        }
        String pid = dataMap.get("pid");
        String num = dataMap.get("num");
        String reonly = dataMap.get("reonly");
        if (StringUtils.isEmpty(pid)) {
            pid = "";
        }
        Result data = DbraLoginUtiles.uploadGet(request,"/file/find?pid=" + pid + "&num=" + num + "&readOnly=" + reonly);
        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, text("查询出错"));
        }
    }

    /**
     * 删除
     */
    @RequestMapping(value = "del")
    @ResponseBody
    public String delete(Map map, HttpServletRequest request, String id) {
        Result data = DbraLoginUtiles.uploadGet(request,"/file/del?id=" + id);
        if(data.getCode()==20000)
            return renderResult(Global.TRUE, text("删除成功！"));
        else
            return renderResult(Global.FALSE, text(data.getMessage()));
    }

    /**
     * 下载
     */
    @RequestMapping(value = "download")
    @ResponseBody
    public String download(Map map, HttpServletRequest request, String id) {
        Result data = DbraLoginUtiles.uploadGet(request,"/file/download?id=" + id);
        if(data.getCode()==20000)
            return renderResult(Global.TRUE, text("删除成功！"));
        else
            return renderResult(Global.FALSE, text(data.getMessage()));
    }

    @RequestMapping(value = "updatePidByUserCode")
    @ResponseBody
    public String updatePidByUserCode(HttpServletRequest request, String pid) {
        Result data = DbraLoginUtiles.uploadGet(request, "/file/updatePid/" + pid);
        if(data.getCode()==20000)
            return renderResult(Global.TRUE, text("更新成功！"));
        else
            return renderResult(Global.FALSE, text(data.getMessage()));
    }

    @RequestMapping(value = "yulan")
    public String yulan(HttpServletRequest request, Model model, String path) {
        model.addAttribute("path", path);
        model.addAttribute("fileService", OAConfigUtil.uploadHtp());
        return "modules/sys/nwmh/viewer";
    }

}
