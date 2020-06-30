package com.jeesite.modules.sys.web.measuring;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.Result;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.DbraLoginUtiles;
import com.jeesite.modules.sys.utils.OALoginUtiles;
import com.jeesite.modules.sys.utils.RequestParameter;
import com.jeesite.modules.sys.utils.SystemException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "${adminPath}/sys/measuring")
public class MeasuringController extends BaseController {
    /**
     * 测点信息维护列表
     */
    @RequestMapping(value = "list")
    public String list(Model model, HttpServletRequest request) {
        Result data;

        List<Map<String, Object>> list;
        Map dataMap = MapUtils.newHashMap();
        try {
            dataMap.put("officeLevel", "分中心");
            data = OALoginUtiles.authGet(request, dataMap, "/office/getListByParentAndLevel");
            list = (List) JSON.parse(data.getData());
            if (list == null) {
                list = new ArrayList<>();
            }
            model.addAttribute("checkCenter", list);

            dataMap.put("officeLevel", "管理站");
            data = OALoginUtiles.authGet(request, dataMap, "/office/getListByParentAndLevel");
            list = (List) JSON.parse(data.getData());
            if (list == null) {
                list = new ArrayList<>();
            }
            model.addAttribute("manageStation", list);

            dataMap.put("officeLevel", "管理所");
            data = OALoginUtiles.authGet(request, dataMap, "/office/getListByParentAndLevel");
            list = (List) JSON.parse(data.getData());
            if (list == null) {
                list = new ArrayList<>();
            }
            model.addAttribute("manageOffice", list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "modules/sys/measuring/measuringList";
    }

    /**
     * 根据管理所查询设备
     *
     * @param model
     * @param request
     * @param officeCode
     * @return
     */
    @RequestMapping(value = "findEquByofficeCode")
    @ResponseBody
    public String findEquByofficeCode(Model model, HttpServletRequest request, String officeCode) {
        Result data = OALoginUtiles.dataGet(request, null, "/deviceQuery/findListByOfficeId/" + officeCode);
        if (data.getCode() == 20000) {
            return data.getData();
        }
        return null;
    }

    /**
     * 测点信息维护列表数据获取
     */
    @RequestMapping(value = "listData")
    @ResponseBody
    public String listData(Model model, HttpServletRequest request) {

        Result data = OALoginUtiles.dataPost(request, RequestParameter.requestParamMap(request), "/point/find");
        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 测点信息维护测点信息
     */
    @RequestMapping(value = "form")
    public String form(HttpServletRequest request, Model model, String pid, String dateNum) {
        if (StringUtils.isEmpty(dateNum)) {
            dateNum = "0";
        }
        Result data = OALoginUtiles.dataGet(request, null, "/point/form/" + pid + "/" + dateNum);
        model.addAttribute("point", JSON.parse(data.getData()));
        return "modules/sys/measuring/measuringForm";
    }

    /**
     * 保存
     */
    @PostMapping(value = "save")
    @ResponseBody
    public String save(HttpServletRequest request) {
        Result data = OALoginUtiles.dataPost(request, RequestParameter.requestParamMap(request), "/point/save");
        if (data.getCode() == 20000) {
            return renderResult(Global.TRUE, text(data.getMessage()));
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 测点信息综合查询列表
     */
    @RequestMapping(value = "listSearch")
    public String listSearch(Model model, HttpServletRequest request) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date date = new Date();
        model.addAttribute("inyear", sdf.format(date));
        Result data;

        List<Map<String, Object>> list;
        Map dataMap = MapUtils.newHashMap();
        try {
            dataMap.put("officeLevel", "分中心");
            data = OALoginUtiles.authGet(request, dataMap, "/office/getListByParentAndLevel");
            list = (List) JSON.parse(data.getData());
            if (list == null) {
                list = new ArrayList<>();
            }
            model.addAttribute("checkCenter", list);

            dataMap.put("officeLevel", "管理站");
            data = OALoginUtiles.authGet(request, dataMap, "/office/getListByParentAndLevel");
            list = (List) JSON.parse(data.getData());
            if (list == null) {
                list = new ArrayList<>();
            }
            model.addAttribute("manageStation", list);

            dataMap.put("officeLevel", "管理所");
            data = OALoginUtiles.authGet(request, dataMap, "/office/getListByParentAndLevel");
            list = (List) JSON.parse(data.getData());
            if (list == null) {
                list = new ArrayList<>();
            }
            model.addAttribute("manageOffice", list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "modules/sys/measuring/measuringListSearch";
    }

    /**
     * 测点信息综合查询列表数据获取
     */
    @RequestMapping(value = "listDataByYear")
    @ResponseBody
    public String listDataByYear(Model model, HttpServletRequest request, String year) {
        Result data = OALoginUtiles.dataPost(request, RequestParameter.requestParamMap(request), "/point/year/" + year);
        model.addAttribute("inyear", year);
        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 测点信息综合查询列表详细信息获取
     */
    @RequestMapping(value = "reads")
    public String reads(Model model, HttpServletRequest request, String year, String pid, String startMonth, String endMonth, String such) {
        model.addAttribute("year", year);
        model.addAttribute("pid", pid);
        model.addAttribute("startMonth", startMonth);
        model.addAttribute("endMonth", endMonth);
        model.addAttribute("such", such);
        return "modules/sys/measuring/measuringReads";
    }

    /**
     * 测点信息综合查询列表数据获取
     */
    @RequestMapping(value = "listDataReads")
    @ResponseBody
    public String listDataReads(Model model, HttpServletRequest request, String pid, String year, String startMonth, String endMonth) {
        Result data;
        if (StringUtils.isNotEmpty(year))
            data = OALoginUtiles.dataGet(request, RequestParameter.requestParamMap(request), "/point/year/" + year + "/" + pid);
        else
            data = OALoginUtiles.dataGet(request, RequestParameter.requestParamMap(request), "/point/month/" + startMonth + "/" + endMonth + "/" + pid);
        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

    /**
     * 测点信息报表查询列表
     */
    @RequestMapping(value = "listSearchReport")
    public String listSearchReport(Model model, HttpServletRequest request) {
        Result data;
        List<Map<String, Object>> list;
        Map dataMap = MapUtils.newHashMap();
        try {
            dataMap.put("officeLevel", "分中心");
            data = OALoginUtiles.authGet(request, dataMap, "/office/getListByParentAndLevel");
            list = (List) JSON.parse(data.getData());
            if (list == null) {
                list = new ArrayList<>();
            }
            model.addAttribute("checkCenter", list);

            dataMap.put("officeLevel", "管理站");
            data = OALoginUtiles.authGet(request, dataMap, "/office/getListByParentAndLevel");
            list = (List) JSON.parse(data.getData());
            if (list == null) {
                list = new ArrayList<>();
            }
            model.addAttribute("manageStation", list);

            dataMap.put("officeLevel", "管理所");
            data = OALoginUtiles.authGet(request, dataMap, "/office/getListByParentAndLevel");
            list = (List) JSON.parse(data.getData());
            if (list == null) {
                list = new ArrayList<>();
            }
            model.addAttribute("manageOffice", list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "modules/sys/measuring/measuringListSearchReport";
    }

    /**
     * 测点信息报表查询列表数据获取
     */
    @RequestMapping(value = "listDataReport")
    @ResponseBody
    public String listDataReport(Model model, HttpServletRequest request, String startMonth, String endMonth) {
        if (Integer.parseInt(startMonth) > Integer.parseInt(endMonth)) {
            return renderResult(Global.FALSE, text("结束日期必须大于开始日期"));
        }
        String strMonth = startMonth.substring(4);
        String strendMonth = endMonth.substring(4);
        int monthqj = Integer.parseInt(strendMonth) - Integer.parseInt(strMonth);
        if (monthqj < 0) {
            monthqj = monthqj + 12;
        }
        if (monthqj < 2) {
            return renderResult(Global.FALSE, text("时间跨度必须大于三个月"));
        }
        Result data = OALoginUtiles.dataPost(request, RequestParameter.requestParamMap(request), "/point/month/" + startMonth + "/" + endMonth);
        if (data.getCode() == 20000) {
            return data.getData();
        } else {
            return renderResult(Global.FALSE, text(data.getMessage()));
        }
    }

}
