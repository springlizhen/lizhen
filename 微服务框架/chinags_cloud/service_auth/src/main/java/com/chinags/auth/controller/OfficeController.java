package com.chinags.auth.controller;

import com.chinags.auth.entity.Office;
import com.chinags.auth.entity.SysUser;
import com.chinags.auth.service.EmployeeService;
import com.chinags.auth.service.OfficeService;
import com.chinags.auth.service.SysUserService;
import com.chinags.common.collect.ListUtils;
import com.chinags.common.collect.MapUtils;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
/**
 * 菜单类
 *
 * @author Mr.Zhang
 * @version V1.0
 * @date
 * @since 1.8
 */

@Api("机构管理controller")
@RestController
@CrossOrigin
@RequestMapping("/office")
public class OfficeController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(OfficeController.class);

    @Autowired
    private OfficeService officeService;

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private EmployeeService employeeService;

    /**
     * 分页获取区域信息
     *
     * @return
     */
    @ApiOperation("分页获取机构信息")
    @RequestMapping(value = "/pageList", method = RequestMethod.GET)
    @ResponseBody
    public Result officeListPage(Office office) {
        PageResult<Office> page;
        try {
            String parentCode = office.getParentCode() == null ? "0" : office.getParentCode();
            office.setParentCode(parentCode);
            //类似查询全部
            office.setPageSize(100000000);
            page = officeService.find(office);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", page);
    }

    @ApiOperation("获取机构信息")
    @RequestMapping(value = "/officeList", method = RequestMethod.GET)
    @ResponseBody
    public Result officeList(String officeLevel) {
        List<Map<String, Object>> list = null;
        try {
            list = officeService.findOffice(officeLevel);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", list);
    }
    @ApiOperation("获取子级")
    @RequestMapping(value = "/ziji", method = RequestMethod.GET)
    @ResponseBody
    public Result ziji(String officeCode) {
        List<String> list = new ArrayList<>();
        try {
            list = officeService.getziji(officeCode);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", list);
    }
    @ApiOperation("获取子级")
    @RequestMapping(value = "/officeListFenzhongxin", method = RequestMethod.GET)
    @ResponseBody
    public Result officeListFenzhongxin(String officeName) {
        List<String> list = new ArrayList<>();
        try {
            list = officeService.getFenzhongxin(officeName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", list);
    }

    @ApiOperation("获取机构详细信息")
    @RequestMapping(value = "/QueryBytreeDate", method = RequestMethod.POST)
    @ResponseBody
    public Result QueryBytreeDate(String userCode) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<Office> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        list1 = officeService.QueryBytreeDate(userCode);
        if (list1.size() > 0) {
            Office office = list1.get(0);
            if (null != office) {
                String codes = office.getParentCodes();
                String officeCode = office.getOfficeCode();
                if ("37000000".equals(officeCode)) {
                    return new Result(true, StatusCode.OK, "获取成功", list2);
                } else if ("3700000001".equals(officeCode)) {
                    list1 = officeService.QueryByofficeCode(officeCode);
                    for (Office tt : list1) {
                        list2.add(tt.getOfficeCode());
                    }
                    list2.add(officeCode);
                    return new Result(true, StatusCode.OK, "获取成功", list2);
                }
                String[] code = codes.split(",");
                for (String ti : code) {
                    if ("37000000".equals(ti)) {
                        return new Result(true, StatusCode.OK, "获取成功", list2);
                    } else if ("3700000001".equals(ti)) {
                        //获取下级的所有级别
                        list1 = officeService.QueryByofficeCode(officeCode);
                        for (Office tt : list1) {
                            list2.add(tt.getOfficeCode());
                        }
                        list2.add(officeCode);
                        return new Result(true, StatusCode.OK, "获取成功", list2);
                    }
                }

            }
        } else {
            return new Result(true, StatusCode.OK, "获取成功", list2);
        }
        return new Result(true, StatusCode.OK, "获取成功", list2);


    }
    @ApiOperation("三级联动")
    @RequestMapping(value = "/officeListToLbZuLo", method = RequestMethod.GET)
    @ResponseBody
    public Result officeListToLbZuLo(String officeCode) {
        List<Office> list = new ArrayList<>();
        try {
            list = officeService.selectByofficeCodeTo(officeCode);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", list);
    }
    @ApiOperation("获取管理所和管理站")
    @RequestMapping(value = "/shangji", method = RequestMethod.GET)
    @ResponseBody
    public Result shangji(String officeCode) {
        List<Map<String,Object>> list = new ArrayList<>();
        Office office = new Office();
        Map<String,Object> map = new HashMap<>();
        try {
            office = officeService.getshangji(officeCode);
            if(null!=office) {
                Office office1 = officeService.getshangji(office.getParentCode());
                map.put("管理站",office1.getOfficeName());
                if(null != office1){
                    Office office2 = officeService.getshangji(office1.getParentCode());
                    map.put("分中心",office2.getOfficeName());
                }
            }
            list.add(map);


        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", list);
    }
    @ApiOperation("获取用户信息")
    @RequestMapping(value = "/officeListToLbZu", method = RequestMethod.POST)
    @ResponseBody
    public Result officeListToLbZu(String userCode) {
        List<Office> list = null;
        List<Office> list1 = new ArrayList<>();
        //获取到用户当前的
        list = officeService.officeListTo(userCode);
        if (list.size() > 0) {
            Office office = list.get(0);
            //省中心下所有部门的返回情
            String val = office.getParentCodes();
            String gt = office.getOfficeCode();
            String[] toval = val.split(",");
            for (String k : toval) {
                //如果是省中心下的所属机构
                if ("37000000".equals(k)) {
                    String value = "3700000001";
                    // 获取所有分中心
                    list1 = officeService.selectByofficeCodeTo(value);
                    return new Result(true, StatusCode.OK, "所有数据", list1);
                } else if ("3700000001".equals(k)) {
                    //获取该机构的父级
                    String value = office.getParentCode();
                    String jibie = office.getOfficeLevel();
                    if("管理站".equals(jibie)){
                        Office office1 =  officeService.selectByofficezl(gt);
                        Office office2 =  officeService.selectByofficezl(office1.getParentCode());
                        list1.add(office1);
                        list1.add(office2);
                        List<Office> officeList = officeService.selectByParentCode(value);
                        if (officeList.size() > 0) {
                            //获取到同级别下的所有类型的数据
                            for (Office to : officeList) {
                                List<Office> tolist = new ArrayList<>();
                                String officecode = to.getOfficeCode();
                                tolist = officeService.selectByofficeCodeTo(officecode);
                                list1.addAll(tolist);
                            }
                            return new Result(true, StatusCode.OK, "这是管理站数据", list1);
                        }
                    }else if("管理所".equals(jibie)){
                        Office office1 =  officeService.selectByofficezl(gt);
                        String gy = office1.getParentCode();
                        Office office2 =  officeService.selectByofficezl(gy);
                        Office office3 =  officeService.selectByofficezl(office2.getParentCode());
                        list1.add(office3);
                        list1.add(office1);
                        list1.add(office2);
                        List<Office> officeList = officeService.selectByParentCode(value);
                        if (officeList.size() > 0) {
                            //获取到同级别下的所有类型的数据
                            for (Office to : officeList) {
                                List<Office> tolist = new ArrayList<>();
                                String officecode = to.getOfficeCode();
                                tolist = officeService.selectByofficeCodeTo(officecode);
                                list1.addAll(tolist);
                            }
                            return new Result(true, StatusCode.OK, "这是管理所数据", list1);
                        }
                    }else if ("分中心".equals(jibie)){
                        list1 =  officeService.selectByofficeCodeTo(gt);
                        list1.add(office);
                        return new Result(true, StatusCode.OK, "这是分中心数据", list1);
                    }
                    //获取该机构同级别数据
                }
            }

            if ("37000000".equals(gt) || "3700000001".equals(gt)) {
                String value = "3700000001";
                list1 = officeService.selectByofficeCodeTo(value);
                return new Result(true, StatusCode.OK, "所有数据", list1);
            }


//                    String val = office.getOfficeCode();
//                    if ("37000000".equals(val) || "3700000001".equals(val)) {
//                        String value = "3700000001";
//                        list1 = officeService.selectByofficeCodeTo(value);
//                        return new Result(true, StatusCode.OK, "所有数据", list1);
//
//                    }
            //用户是管理员和超级管理员的特殊情况
        } else {
            String value = "3700000001";
            list1 = officeService.selectByofficeCodeTo(value);
            return new Result(true, StatusCode.OK, "所有数据", list1);
        }
        return new Result(true, StatusCode.OK, "获取成功", list1);
    }

    /**
     * 返回类型
     *
     * @param userCode
     * @return
     */
    @ApiOperation("根据用户id获取机构信息")
    @RequestMapping(value = "/officeListTo", method = RequestMethod.GET)
    @ResponseBody
    public Result officeListTo(String userCode) {
        List<String> list = new ArrayList<>();
        try {
            list = officeService.selectByorgId(userCode);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", list);
    }
    /**
     * 根据名字获取机构
     *
     * @param userCode
     * @return
     */
    @ApiOperation("根据用户id获取机构信息")
    @RequestMapping(value = "/pid", method = RequestMethod.GET)
    @ResponseBody
    public Result pid(String sendUserName) {
        List<String> list= new ArrayList<>();
        if(!"超级管理员".equals(sendUserName) && !"管理员".equals(sendUserName)){
            List<String> list1 = sysUserService.selectByName(sendUserName);
            if(list1.size()>0){
                String id = list1.get(0);
               List<Office> list2 = officeService.officeListTo(id);
               if(list2.size()>0){
                   list.add(list2.get(0).getOfficeName());
               }

            }
        }else{
            list.add("省中心");
        }

        return new Result(true, StatusCode.OK, "获取成功", list);
    }

    /**
     * 根据编码获取信息
     *
     * @param officeCode
     * @return
     */
    @ApiOperation("根据用户id获取机构信息")
    @RequestMapping(value = "/officeListToLb", method = RequestMethod.GET)
    @ResponseBody
    public Result officeListToLb(String officeCode) {
        List<String> list = new ArrayList<>();
        try {
            list = officeService.selectByofficeCode(officeCode);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", list);
    }
    /**
     * 起草处室
     */
    @ApiOperation("根据用户登录名获取用户数据")
    @RequestMapping(value = "/officeListToLbLo", method = RequestMethod.GET)
    @ResponseBody
    public Result officeListToLbLo(String userName) {
        List<Office> list = new ArrayList<>();
        List<String> list1 = new ArrayList<>();
        try {
            if("system".equals(userName) || "admin".equals(userName)){
                list1.add("省中心");
                return new Result(true, StatusCode.OK, "获取成功", list1);
            }else {
                list = officeService.selectByofficeUserName(userName);
                Office office = list.get(0);
                //确定当前用户属于省中心还是分中心
                if("37000000".equals(office.getOfficeCode())){
                    list1.add("省中心");
                    return new Result(true, StatusCode.OK, "获取成功", list1);
                }else if("3700000001".equals(office.getOfficeCode())){
                    list1.add("分中心");
                    return new Result(true, StatusCode.OK, "获取成功", list1);
                }else if(!"37000000".equals(office.getOfficeCode()) && !"3700000001".equals(office.getOfficeCode())){
                    String codes = office.getParentCodes();
                    String u = "";
                    String[] code = codes.split(",");
                    for(String ti:code){
                        if("37000000".equals(ti)){
                            u ="1";
                        }else if("3700000001".equals(ti)){
                            u = "2";
                        }
                    }
                    //省中心的下属
                    if("1".equals(u)){
                         if(1 == office.getTreeLevel()){
                            list1.add(office.getOfficeName());
                             return new Result(true, StatusCode.OK, "获取成功", list1);
                         }else{
                             String part = office.getParentCode();
                             Office office1 = officeService.selectByoffice(part);
                             list1.add(office1.getOfficeName());
                             return new Result(true, StatusCode.OK, "获取成功", list1);
                         }
                    }else if("2".equals(u)){
                        if(1 == office.getTreeLevel()){
                            list1.add(office.getOfficeName());
                            return new Result(true, StatusCode.OK, "获取成功", list1);
                        }else if(2 == office.getTreeLevel()){
                            list1.add(office.getOfficeName());
                            return new Result(true, StatusCode.OK, "获取成功", list1);
                        }else if(3 == office.getTreeLevel()){
                            String part = office.getParentCode();
                            Office office1 = officeService.selectByoffice(part);
                            list1.add(office1.getOfficeName());
                            return new Result(true, StatusCode.OK, "获取成功", list1);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", list1);
    }

    /**
     * 获取区域信息
     *
     * @return
     */
    @ApiOperation("获取机构信息")
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public Result officeForm(String officeCode, String parentCode) {
        Office office = new Office();
        try {
            if (StringUtils.isEmpty(parentCode)) {
                office = officeService.getAreaByOfficeCode(officeCode);
            } else {
                Office o = officeService.getAreaByOfficeCode(parentCode);
                office.setParentCode(o.getOfficeCode());
                office.setTreeNames(o.getOfficeName() + "/" + o.getOfficeName() + "/" + o.getOfficeName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", office);
    }
    /**
     * 获取人员信息
     *
     * @return
     */
    @ApiOperation("获取人员信息")
    @RequestMapping(value = "/toUser", method = RequestMethod.GET)
    public Result toUser(String userCode) {
        List<Office> list = new ArrayList<>();
        List<String> list1 = new ArrayList<>();
        list = officeService.officeListTo(userCode);
        if(list.size()>0){
            Office office = list.get(0);
            String officeCode = office.getOfficeCode();
            String level = office.getOfficeLevel();
            String[] to = office.getParentCodes().split(",");
            if("37000000".equals(officeCode)){
                String value = "3700000001";
                list1 = officeService.selectByofficeCode(value);
                list1.add("1");
                return new Result(true, StatusCode.OK, "获取成功", list1);
            }
            if("3700000001".equals(officeCode)){
                String value = "3700000001";
                list1 = officeService.selectByofficeCode(value);
                list1.add("1");
                return new Result(true, StatusCode.OK, "获取成功", list1);
            }
            for(String k:to){
                //说明是省中心下的机构，可以看到所有分中心
                if("37000000".equals(k)){
                     String value = "3700000001";
                    list1 = officeService.selectByofficeCode(value);
                    list1.add("1");
                    return new Result(true, StatusCode.OK, "获取成功", list1);
                }
                // 如果是分中心
               if("3700000001".equals(k)){
                   //该机构父级id
                   String parcode = office.getOfficeCode();
                   if("分中心".equals(level)){
                       list1 = officeService.selectByofficeCode(officeCode);
                       list1.add(officeCode);
                       return new Result(true, StatusCode.OK, "获取成功", list1);
                   }else if("管理站".equals(level)){
                       list1 = officeService.selectByofficeCode(parcode);
                       list1.add(officeCode);
                       return new Result(true, StatusCode.OK, "获取成功", list1);
                   }else if("管理所".equals(level)){
                       Office office1 = officeService.selectByoffice(parcode);
                       String par = office1.getParentCode();
                       list1 = officeService.selectByofficeCode(parcode);
                       return new Result(true, StatusCode.OK, "获取成功", list1);
                   }

               }
            }

        }else{
            String value = "3700000001";
            list1 = officeService.selectByofficeCode(value);
            list1.add("1");
            return new Result(true, StatusCode.OK, "获取成功", list1);
        }
        return new Result(true, StatusCode.OK, "获取成功", list1);
    }
    /**
     * 获取是否为省中心是否为分中心
     *
     * @return
     */
    @ApiOperation("获取是否为省中心是否为分中心")
    @RequestMapping(value = "/Yu", method = RequestMethod.POST)
    public Result Yu(String userCode) {
        List<Office> list = new ArrayList<>();
        List<Object> list1 = new ArrayList<>();
        list = officeService.officeListTo(userCode);
        if(list.size()>0){
            Office office = list.get(0);
            String officeCode = office.getOfficeCode();
            String level = office.getOfficeLevel();
            String[] to = office.getParentCodes().split(",");
            if("37000000".equals(officeCode)){
                list1.add("1");
                return new Result(true, StatusCode.OK, "获取成功", list1);
            }
            for(String k:to){
                //说明是省中心下的机构，可以看到所有分中心
                if("37000000".equals(k)){
                    list1.add("1");
                    return new Result(true, StatusCode.OK, "获取成功", list1);
                }
            }

        }else{
            list1.add("1");
            return new Result(true, StatusCode.OK, "获取成功", list1);
        }
        return new Result(true, StatusCode.OK, "获取成功", list1);
    }

    /**
     * 菜单树结构
     *
     * @return
     */
    @ApiOperation("机构树结构")
    @RequestMapping(value = "/treeData", method = RequestMethod.GET)
    public Result treeData(String excludeCode) {
        List list = ListUtils.newArrayList();
        List<Office> offices;
        try {
            offices = officeService.treeData(excludeCode);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }

        for (Office a : offices) {
            HashMap map = MapUtils.newHashMap();
            map.put("id", a.getId());
            map.put("pId", a.getParentCode());
            map.put("name", a.getOfficeName());
            map.put("title", a.getOfficeName());
            list.add(map);
        }
        return new Result(true, StatusCode.OK, "获取成功", list);
    }

    /**
     * 菜单树结构
     *
     * @return
     */
    @ApiOperation("机构附带员工树结构")
    @RequestMapping(value = "/treeDataPeople", method = RequestMethod.GET)
    public Result treeDataPeople(String excludeCode) {
        List list = ListUtils.newArrayList();
        List<Office> offices;
        try {
            offices = officeService.treeData(excludeCode);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }

        for (Office a : offices) {
            HashMap map = MapUtils.newHashMap();
            map.put("isParent", true);
            map.put("name", a.getOfficeName());
            map.put("pId", a.getParentCode());
            map.put("id", a.getId());
            map.put("title", a.getOfficeName());
            list.add(map);
            List<Map<String, Object>> employeeList = employeeService.findEmpByOfficeCode(a.getId());
            for (Map e : employeeList) {
                HashMap m = MapUtils.newHashMap();
                m.put("name", e.get("EMP_NAME"));
                m.put("pId", e.get("OFFICE_CODE"));
                m.put("id", e.get("LOGIN_CODE"));
                list.add(m);
            }
        }
        return new Result(true, StatusCode.OK, "获取成功", list);
    }

    @ApiOperation("保存机构")
    @RequestMapping(value = "/saveAndUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Result saveAndUpdate(@RequestBody Office office) {
        if (office.getIsNewRecord()) {
            office.setOfficeCode(office.getViewCode());
            Office officeParent;
            officeParent = officeService.getAreaByOfficeCode(office.getOfficeCode());
            if (officeParent != null) {
                return new Result(true, StatusCode.ERROR, "机构编码已存在");
            }
        }
        office.setCreateBy(getLoginUser().getUsercode());
        office.setUpdateBy(getLoginUser().getUsercode());
        office.setCreateDate(new Date());
        office.setUpdateDate(new Date());
        this.officeService.save(office);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @ApiOperation("删除机构")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Result delete(Office office) {
        if (officeService.delete(office)) {
            return new Result(true, StatusCode.OK, "删除成功");
        } else {
            return new Result(true, StatusCode.ERROR, "请先删除子菜单");
        }
    }

    @ApiOperation("停用机构")
    @RequestMapping(value = "/disable", method = RequestMethod.GET)
    @ResponseBody
    public Result disable(String id) {
        if (StringUtils.isNotEmpty(id)) {
            return officeService.disable(id);
        } else {
            return new Result(true, StatusCode.ERROR, "未选择机构");
        }
    }

    @ApiOperation("启用机构")
    @RequestMapping(value = "/enable", method = RequestMethod.GET)
    @ResponseBody
    public Result enable(String id) {
        if (StringUtils.isNotEmpty(id)) {
            String result = officeService.enable(id);
            return new Result(true, StatusCode.OK, result);
        } else {
            return new Result(true, StatusCode.ERROR, "未选择机构");
        }
    }

    /**
     * 获取机构对应最近的指定机构类型
     *
     * @return
     */
    @ApiOperation("获取机构对应最近的指定机构类型")
    @RequestMapping(value = "/orgOfficeTypeEnd", method = RequestMethod.GET)
    @ResponseBody
    public Result getOrgOfficeTypeEnd(String id, String type) {
        if (StringUtils.isEmpty(id)) {
            id = getLoginUser().getUsercode();
        }
        if (!"system".equals(id)) {
            SysUser sysUserByUserCode = sysUserService.getSysUserByUserCode(id);
            String officeCode = sysUserByUserCode.getEmployee().getOfficeCode();
            if (StringUtils.isNotEmpty(type)) {
                Office office = officeService.getOrgOfficeTypeEnd(officeCode, type);
                return new Result(true, StatusCode.OK, "获取成功", office);
            } else {
                return new Result(true, StatusCode.ERROR, "未传入类型或用户参数错误！");
            }
        }
        return new Result(true, StatusCode.ERROR, "超级管理员无机构！");
    }

    /**
     * 获取用户机构对应所有子机构id
     *
     * @return
     */
    @ApiOperation("获取机构对应所有子机构id")
    @RequestMapping(value = "/getUserOffice", method = RequestMethod.GET)
    @ResponseBody
    public Result getUserOffice(String userCode) {
        if (StringUtils.isEmpty(userCode)) {
            userCode = getLoginUser().getUsercode();
        }
        if (!"system".equals(userCode)) {
            SysUser sysUserByUserCode = sysUserService.getSysUserByUserCode(userCode);
            String officeCode = sysUserByUserCode.getEmployee().getOfficeCode();
            String officeName = sysUserByUserCode.getEmployee().getOfficeName();
            Map<String, String> map = MapUtils.newHashMap();
            map.put("code", officeCode);
            map.put("name", officeName);
            if (getLoginUser() != null) {
                return new Result(true, StatusCode.OK, "获取成功", map);
            } else {
                return new Result(true, StatusCode.ERROR, "用户参数错误！");
            }
        }
        return new Result(true, StatusCode.ERROR, "超级管理员无机构！");
    }

    /**
     * 获取用户机构对应所有子机构id
     *
     * @return
     */
    @ApiOperation("获取机构对应所有子机构id")
    @RequestMapping(value = "/orgOfficeChirldsId", method = RequestMethod.GET)
    @ResponseBody
    public Result getOrgOfficeChirldsId(String id, String typeValue) {
        if (StringUtils.isEmpty(id)) {
            id = getLoginUser().getUsercode();
        }
        if (!"system".equals(id)) {
            SysUser sysUserByUserCode = sysUserService.getSysUserByUserCode(id);
            String officeCode = sysUserByUserCode.getEmployee().getOfficeCode();
            if (getLoginUser() != null) {
                List<String> officeIdString = officeService.getOrgOfficeChirldsId(officeCode, typeValue);
                return new Result(true, StatusCode.OK, "获取成功", officeIdString);
            } else {
                return new Result(true, StatusCode.ERROR, "用户参数错误！");
            }
        }
        return new Result(true, StatusCode.ERROR, "超级管理员无机构！");
    }

    /**
     * 获取机构对应所有子机构
     *
     * @return
     */
    @ApiOperation("获取机构对应所有子机构")
    @RequestMapping(value = "/orgOfficeChirlds", method = RequestMethod.GET)
    @ResponseBody
    public Result getOrgOfficeChirlds(String id, String typeValue) {
        if (!"system".equals(id)) {
            if (StringUtils.isNotEmpty(id)) {
                List<Office> officeIdString = officeService.getOrgOfficeChirlds(id, typeValue);
                return new Result(true, StatusCode.OK, "获取成功", officeIdString);
            } else {
                return new Result(true, StatusCode.ERROR, "未传入id！");
            }
        }
        return new Result(true, StatusCode.ERROR, "超级管理员无机构！");
    }

    /**
     * 获取机构对应最近的指定机构类型
     *
     * @return
     */
    @ApiOperation("获取机构对应最近的指定机构类型")
    @RequestMapping(value = "/getofficeEndType", method = RequestMethod.GET)
    @ResponseBody
    public Result getofficeEndType(String id, String type) {
        if (StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(type)) {
            Office office = officeService.getOrgOfficeTypeEnd(id, type);
            return new Result(true, StatusCode.OK, "获取成功", office != null ? office.getId() : "");
        } else {
            return new Result(true, StatusCode.ERROR, "未传入类型或机构参数错误！");
        }
    }

    /**
     * 根据父级ID和机构等级查询机构列表
     * parentCode（非必填）
     * officeLevel（分中心/管理站/管理所）
     *
     * @return
     */
    @ApiOperation("根据父级ID和机构等级查询机构列表")
    @RequestMapping(value = "/getListByParentAndLevel", method = RequestMethod.GET)
    @ResponseBody
    public Result getListByParentAndLevel(String parentCode, String officeLevel) {
        List list = ListUtils.newArrayList();
        List<Office> offices;
        try {
            offices = officeService.getListByParentAndLevel(parentCode, officeLevel);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }

        for (Office o : offices) {
            HashMap map = MapUtils.newHashMap();
            map.put("officeCode", o.getOfficeCode());
            map.put("officeName", o.getOfficeName());
            map.put("parentCode", o.getParentCode());
            list.add(map);
        }
        return new Result(true, StatusCode.OK, "获取成功", list);
    }

}
