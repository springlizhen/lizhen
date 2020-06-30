package com.jeesite.modules.sys.web.nwmh;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.file.utils.FileUploadUtils;
import com.jeesite.modules.sys.entity.Post;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.entity.Role;
import com.jeesite.modules.sys.utils.DictionaryUtils;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import com.jeesite.modules.sys.utils.OAUserUtils;
import com.jeesite.modules.sys.utils.RequestParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 文章管理Controller
 *
 * @author suijinchi
 * @version 2020-3-9
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/nwmh/article")
public class ArticleController extends BaseController {

    @ModelAttribute
    public Map get(String articleCode, HttpServletRequest request) {
        if (articleCode != null) {
            Map dataMap = MapUtils.newHashMap();
            dataMap.put("articleCode", articleCode);
            Result data = OALoginUtiles.authGet(request, dataMap, "/article/form");
            return (Map) JSON.parse(data.getData());
        }
        return null;
    }

    @RequestMapping(value = "list")
    public String list(Model model, HttpServletRequest request) {

        return "modules/sys/nwmh/article/articleList";
    }

    @RequestMapping(value = "listData")
    @ResponseBody
    public String listData(HttpServletRequest request) {
        Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request), "/article/pageList");
        return data.getData();
    }

	@RequestMapping(value = "form")
	public String form(String op, Model model, HttpServletRequest request, Map map) {
		JSONObject article = (JSONObject) map.get("map");
		// 操作类型：add: 新增； edit: 编辑；
		model.addAttribute("op", op);
		model.addAttribute("article",article);
		model.addAttribute("isShow", DictionaryUtils.setMap(null,null,"隐藏","显示"));
		model.addAttribute("isIndexCarousel", DictionaryUtils.setMap(null,null,"否","是"));
		model.addAttribute("uuid" , UUID.randomUUID());
		return "modules/sys/nwmh/article/articleForm";
	}

    @PostMapping(value = "save")
    @ResponseBody
    public String save(HttpServletRequest request, HttpServletResponse response) {
        Result data = OALoginUtiles.authPost(request, RequestParameter.requestParamMapPost(request),"/article/saveAndUpdate");
        if (data.getCode()==20000) {
            return renderResult(Global.TRUE, text(data.getMessage()));
        } else{
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    @RequestMapping({"delete"})
    @ResponseBody
    public String delete(HttpServletRequest request) {
        Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request), "/article/delete");
        if (data.getCode() == 20000) {
            return renderResult(Global.TRUE, text(data.getMessage()));
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

}
