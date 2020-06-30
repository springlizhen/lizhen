package com.jeesite.modules.comm.web;

import com.jeesite.common.utils.Oa;
import com.jeesite.common.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"${adminPath}/oaTo"})
public class OaToController extends BaseController {
    public OaToController() {
    }
    @RequestMapping({"oaTo"})
    public String oa(Model model) {
        model.addAttribute("oa", new Oa());
        return "modules/contract/todoApplicationTo";
    }
}
