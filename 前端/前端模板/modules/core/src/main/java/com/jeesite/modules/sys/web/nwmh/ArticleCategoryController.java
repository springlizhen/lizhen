package com.jeesite.modules.sys.web.nwmh;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.utils.DictionaryUtils;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import com.jeesite.modules.sys.utils.RequestParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 文章分类管理Controller
 *
 * @author suijinchi
 * @version 2020-3-9
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/nwmh/articleCategory")
public class ArticleCategoryController extends BaseController {

	/**
	 * 获取数据
	 */
	@ModelAttribute
	public Map get(String categoryCode, String parentCode, HttpServletRequest request) {
		if(categoryCode!=null || parentCode!=null) {
			Map dataMap = MapUtils.newHashMap();
			dataMap.put("categoryCode", categoryCode);
			dataMap.put("parentCode", parentCode);
			Result data = OALoginUtiles.authGet(request,dataMap,"/articleCategory/form");
			return (Map) JSON.parse(data.getData());
		}
		return null;
	}

    @RequestMapping(value = "list")
    public String list() {
        return "modules/sys/nwmh/articleCategory/articleCategoryList";
    }

    @RequestMapping(value = "listData")
    @ResponseBody
    public String listData(HttpServletRequest request) {
        Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request), "/articleCategory/pageList");
        return data.getData();
    }

    @RequestMapping({"form"})
    public String form(HttpServletRequest request, Model model, Map map) {
        JSONObject articleCategory = (JSONObject) map.get("map");
        model.addAttribute("articleCategory", articleCategory);
		model.addAttribute("isShow", DictionaryUtils.setMap(null,null,"隐藏","显示"));
        return "modules/sys/nwmh/articleCategory/articleCategoryForm";
    }

	@PostMapping({"save"})
	@ResponseBody
	public String save(HttpServletRequest request) {
		Result data = OALoginUtiles.authPost(request, RequestParameter.requestParamMap(request),"/articleCategory/saveAndUpdate");
		if (data.getCode()==20000) {
			return renderResult(Global.TRUE, text(data.getMessage()));
		} else {
			return renderResult(Global.FALSE, text(data.getMessage()));
		}
	}

	@RequestMapping({"treeData"})
	@ResponseBody
	public String treeData(HttpServletRequest request) {
		Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request),"/articleCategory/treeData");
		return data.getData();
	}

    @RequestMapping({"delete"})
    @ResponseBody
    public String delete(HttpServletRequest request) {
        Result data = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request), "/articleCategory/delete");
        if (data.getCode() == 20000) {
            return renderResult(Global.TRUE, text(data.getMessage()));
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

	@RequestMapping({"selectByLevelAndParentName"})
	@ResponseBody
	public String selectByLevelAndParentName(HttpServletRequest request) {
		Result result = OALoginUtiles.authGet(request, RequestParameter.requestParamMap(request), "/articleCategory/selectByLevelAndParentName");
		return result.getData();
	}

}
