package com.chinags.auth.controller;

        import com.chinags.auth.entity.Contract;
        import com.chinags.auth.entity.ContractMter;
        import com.chinags.auth.entity.ContractUpdate;
        import com.chinags.auth.service.ContractService;
        import com.chinags.common.collect.ListUtils;
        import com.chinags.common.controller.BaseController;
        import com.chinags.common.entity.BaseEntity;
        import com.chinags.common.entity.PageResult;
        import com.chinags.common.entity.Result;
        import com.chinags.common.entity.StatusCode;
        import com.chinags.common.utils.StringUtils;
        import io.swagger.annotations.Api;
        import io.swagger.annotations.ApiOperation;
        import org.bouncycastle.jcajce.provider.digest.MD2;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.data.domain.Page;
        import org.springframework.web.bind.annotation.*;

        import javax.servlet.http.HttpServletRequest;
        import javax.servlet.http.HttpServletResponse;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;


@Api("合同管理controller")
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
@RequestMapping("/contract")
public class ContractController extends BaseController {
    @Autowired
    private ContractService contractService;
    @ApiOperation("条件查询")
    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public Result select(Contract contract) {
        PageResult<Contract> list=contractService.selectAll(contract);
        if(!StringUtils.isEmpty(list.getList())) {
            return new Result(true, StatusCode.OK, "查询成功", list);
        }else{
            return new Result(true, StatusCode.OK, "查询失败，未查询到数据", list);
        }
    }
    @ApiOperation("查询")
    @RequestMapping(value = "/selectUpdateContractById", method = RequestMethod.GET)
    public ContractUpdate selectUpdateContractById(String workId, HttpServletRequest request, HttpServletResponse response) {
        ContractUpdate contractUpdate=contractService.selectMaxUpdateById(workId);
        return   contractUpdate ;
    }
    @ApiOperation("查询")
    @RequestMapping(value = "/subContract", method = RequestMethod.GET)
    public List<Map<String,Object>> subContract() {
        List<Map<String,Object>> list=contractService.selectSubContract();
        return list;
    }
    @ApiOperation("查询")
    @RequestMapping(value = "/selectTo", method = RequestMethod.POST)
    @ResponseBody
    public Result selectTo(Contract contract) {
        PageResult<Map<String,Object>> page;
        try {
            //类似查询全部
            String logincode = getLoginUser().getLogincode();
            page = contractService.select(contract,logincode);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", page);
    }
    @ApiOperation("合同登记的查询")
    @RequestMapping(value = "/listDataTo", method = RequestMethod.POST)
    @ResponseBody
    public Result listDataTo(@RequestBody Contract contract) {
        PageResult<Map<String,Object>> page;
        try {
            //类似查询全部
            String logincode = getLoginUser().getLogincode();
            page = contractService.selectZl(contract,logincode);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", page);
    }
    @ApiOperation("合同我的发起的查询")
    @RequestMapping(value = "/selectUi", method = RequestMethod.POST)
    @ResponseBody
    public  Result selectUi() {
        List<Map<String,Object>> list = new ArrayList<>();
        try {
            //类似查询全部
            String logincode = getLoginUser().getLogincode();
//            String logincode =  "system";
            list = contractService.selectUi(logincode);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new Result(true, StatusCode.OK, "获取成功", list);
    }
    @ApiOperation("合同我的待办的查询")
    @RequestMapping(value = "/selectUiTo", method = RequestMethod.POST)
    @ResponseBody
    public  Result selectUiTo() {
        List<Map<String,Object>> list = new ArrayList<>();
        try {
            //类似查询全部
            String logincode = getLoginUser().getLogincode();
//            String logincode =  "system";
            list = contractService.selectUiTo(logincode);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new Result(true, StatusCode.OK, "获取成功", list);
    }
    @ApiOperation("合同我的已办查询")
    @RequestMapping(value = "/selectUiToK", method = RequestMethod.POST)
    @ResponseBody
    public  Result selectUiToK() {
        List<Map<String,Object>> list = new ArrayList<>();
        try {
            //类似查询全部
            String logincode = getLoginUser().getLogincode();
//            String logincode =  "system";
            list = contractService.selectUiToK(logincode);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new Result(true, StatusCode.OK, "获取成功", list);
    }
    @ApiOperation("合同金额的查询")
    @RequestMapping(value = "/selectUiToKb", method = RequestMethod.POST)
    @ResponseBody
    public  Result selectUiToKb() {
        List<Contract> list = new ArrayList<>();
        try {
            //类似查询全部
//            String logincode = getLoginUser().getLogincode();
//            String logincode =  "system";
//            list = contractService.selectUiToK(logincode);
            list  = contractService.selectwu();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new Result(true, StatusCode.OK, "获取成功", list);
    }
    @ApiOperation("合同签订数量的查询")
    @RequestMapping(value = "/selectUiToKbZ", method = RequestMethod.POST)
    @ResponseBody
    public  Result selectUiToKbZ() {
        List<Map<String,Object>> list = new ArrayList<>();
        try {
            //类似查询全部
//            String logincode = getLoginUser().getLogincode();
//            String logincode =  "system";
//            list = contractService.selectUiToK(logincode);
            list  = contractService.selectUiToKbZ();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new Result(true, StatusCode.OK, "获取成功", list);
    }
    @ApiOperation("年度合同类型统计查询")
    @RequestMapping(value = "/selectUiToKbZtu", method = RequestMethod.POST)
    @ResponseBody
    public  Result selectUiToKbZtu() {
        List<Contract> list = new ArrayList<>();
        try {
            //类似查询全部
//            String logincode = getLoginUser().getLogincode();
//            String logincode =  "system";
//            list = contractService.selectUiToK(logincode);
            list  = contractService.selectUiToKbZtu();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(true, StatusCode.OK, "获取成功", list);
    }
//    @ApiOperation("年度合同数量查询")
//    @RequestMapping(value = "/selectUiToKbZP", method = RequestMethod.POST)
//    @ResponseBody
//    public  Result selectUiToKbZP() {
//        List<Map<String,Object>> list = new ArrayList<>();
//        try {
//            //类似查询全部
////            String logincode = getLoginUser().getLogincode();
////            String logincode =  "system";
////            list = contractService.selectUiToK(logincode);
//            list  = contractService.selectUiToKbZP();
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//        return new Result(true, StatusCode.OK, "获取成功", list);
//    }
    @ApiOperation("合同计量的查询")
    @RequestMapping(value = "/listDataToK", method = RequestMethod.POST)
    @ResponseBody
    public Result listDataToK(@RequestBody Contract contract) {
        PageResult<Contract> page;
        try {
            //类似查询全部
            String logincode = getLoginUser().getLogincode();
            page = contractService.selectZlK(contract,logincode);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", page);
    }
    @ApiOperation("根据id查询合同计量")
    @RequestMapping(value = "/listDataToKL", method = RequestMethod.POST)
    public Result findById(@RequestBody ContractMter contractMter) {
        PageResult<Map<String,Object>> page;
        try {
            //类似查询全部
            String logincode = getLoginUser().getLogincode();
            String contractid = contractMter.getContractid();
            page = contractService.selectZlKU(contractMter,logincode,contractid);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", page);
    }

    @ApiOperation("合同计量待办申请查询")
    @RequestMapping(value = "/selectLb", method = RequestMethod.POST)
    @ResponseBody
    public Result selectLb(Contract contract) {
        PageResult<Map<String,Object>> page;
        try {
            //类似查询全部
            String logincode = getLoginUser().getLogincode();
            page = contractService.selectLb(contract,logincode);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", page);
    }
    @ApiOperation("查询当前登录人")
    @RequestMapping(value = "/getcode", method = RequestMethod.GET)
    @ResponseBody
    public List<String> selectLb() {
        List<String> list  =new ArrayList<>();
        list.add(getLoginUser().getLogincode());
        return list;


    }
    @ApiOperation("查询")
    @RequestMapping(value = "/selectToKb", method = RequestMethod.POST)
    @ResponseBody
    public Result selectToKb(Contract contract) {
        PageResult<Map<String,Object>> page;
        try {
            //类似查询全部
            String logincode = getLoginUser().getLogincode();
            page = contractService.selectTo(contract,logincode);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", page);
    }
    @ApiOperation("合同计量已办查询")
    @RequestMapping(value = "/selectToYu", method = RequestMethod.POST)
    @ResponseBody
    public Result selectToYu(Contract contract) {
        PageResult<Map<String,Object>> page;
        try {
            //类似查询全部
            String logincode = getLoginUser().getLogincode();
            page = contractService.selectToYu(contract,logincode);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
        return new Result(true, StatusCode.OK, "获取成功", page);
    }
    @ApiOperation("查询")
    @RequestMapping(value = "/listUpdate", method = RequestMethod.GET)
    public Result listUpdate(ContractUpdate contractUpdate) {
        PageResult<ContractUpdate> list=contractService.selectUpdateContractById(contractUpdate);
        if(!StringUtils.isEmpty(list.getList())) {
            return new Result(true, StatusCode.OK, "查询成功", list);
        }else{
            return new Result(true, StatusCode.OK, "查询失败，未查询到数据", list);

        }
    }
    @ApiOperation("查询")
    @RequestMapping(value = "/selectUpdateContract", method = RequestMethod.GET)
    public ContractUpdate selectUpdateContract(String id, HttpServletRequest request, HttpServletResponse response) {
        ContractUpdate contractUpdate=contractService.selectMaxUpdate(id);
        return   contractUpdate ;
    }
    @ApiOperation("查询")
    @RequestMapping(value = "/selectUpdate", method = RequestMethod.GET)
    public Result selectUpdate(String id) {
        ContractUpdate contractUpdate=contractService.selectUpdate(id);
        if(contractUpdate!=null) {
            return new Result(true, StatusCode.OK, "查询成功", contractUpdate);
        }else{
            return new Result(true, StatusCode.OK, "查询失败，未查询到数据", contractUpdate);

        }

    }
    @ApiOperation("删除")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Result deleteById(String id) {
        contractService.deleteContractById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }
    @ApiOperation("查询")
    @RequestMapping(value = "/selectContract", method = RequestMethod.GET)
    public Contract selectById(String id, HttpServletRequest request, HttpServletResponse response) {
        Contract contract=contractService.selectContractById(id);
        return   contract ;

    }
    @ApiOperation("查询")
    @RequestMapping(value = "/queryContract", method = RequestMethod.GET)
    public Result selectcontract(String id, HttpServletRequest request, HttpServletResponse response) {
        Contract contract=contractService.selectContractById(id);

        if(contract!=null) {
            return new Result(true, StatusCode.OK, "查询成功", contract);
        }else{
            return new Result(true, StatusCode.OK, "查询失败，未查询到数据", contract);

        }

    }
    @ApiOperation("查询")
    @RequestMapping(value = "/selectAll", method = RequestMethod.POST)
    public Result selectAll(){
        List<Map<String,Object>> map= ListUtils.newArrayList();
        List<Contract> list=contractService.findAll();
        for(int i=0;i<list.size();i++){
            Contract contract=list.get(i);
            HashMap  hashMap=new HashMap();
            hashMap.put("id",contract.getId());
            hashMap.put("name",contract.getTitle());
            map.add(hashMap);
        }
        if(!StringUtils.isEmpty(list)) {
            return new Result(true, StatusCode.OK, "查询成功", map);
        }else{
            return new Result(true, StatusCode.OK, "查询失败，未查询到数据", map);
        }

    }

    @ApiOperation("创建合同")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(@RequestBody Contract contract){
        return contractService.saveContract(contract);

    }
    @ApiOperation("变更合同")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result update(Contract contract) {

        return contractService.update(contract);
    }
}
