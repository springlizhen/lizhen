package com.jeesite.modules.sys.web.derivce;

import com.jeesite.common.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Mr.Zhang
 * @version V1.0
 * @date
 * @since 1.8
 */
@Controller
@RequestMapping(value = "{adminPath}/sys/contract")
public class tractController extends BaseController {

    protected Map map = null;

    /**
     * 获取数据
     */

    /**
     * 查询列表
     */
    @RequestMapping(value = "listCheck1")
    public String listCheck1(Model model, HttpServletRequest request) {
        return "modules/sys/derivce/check/check1";
    }@RequestMapping(value = "listCheck2")
    public String listCheck2(Model model, HttpServletRequest request) {
        return "modules/sys/derivce/check/check2";
    }@RequestMapping(value = "listCheck3")
    public String listCheck3(Model model, HttpServletRequest request) {
        return "modules/sys/derivce/check/check3";
    }

}
