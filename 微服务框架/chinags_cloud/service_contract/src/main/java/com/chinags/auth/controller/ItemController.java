package com.chinags.auth.controller;

import com.chinags.auth.entity.Item;
import com.chinags.auth.service.ItemService;
import com.chinags.common.controller.BaseController;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Api("项目管理controller")
@RestController
@CrossOrigin
@RequestMapping("/item")
public class ItemController extends BaseController {


    @Autowired
    private ItemService itemService;


    @ApiOperation("查询")
    @RequestMapping(value = "/selectItem", method = RequestMethod.GET)
    public Result select(String id) {
        Item item=itemService.selectItemById(id);
        if(StringUtils.isNotNull(item)) {
            return  new Result(true, StatusCode.OK, "查询成功",item);
        }else{
            return new Result(true, StatusCode.OK, "查询失败，未查询到数据", item);
        }
    }



    @ApiOperation("查询")
    @RequestMapping(value = "/selectAll", method = RequestMethod.POST)
    public Result selectAll(){
        List<Item> list=itemService.findAll();
        if(!StringUtils.isEmpty(list)) {

        return  new Result(true, StatusCode.OK, "查询成功",list);
        }else{
            return new Result(true, StatusCode.OK, "查询失败，未查询到数据", list);

        }



    }
    @ApiOperation("删除")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Result deleteItemById(String id) {
        itemService.deleteItemById(id);
            return  new Result(true, StatusCode.OK, "删除成功");
    }
    @ApiOperation("提交")
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public Result update(@RequestParam("id")String id){
         itemService.update(id);
        return new Result(true, StatusCode.OK,"提交成功" );

    }
    @ApiOperation("保存")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(@RequestBody Item item){
        return itemService.save(item);

    }
    @ApiOperation("审批")
    @RequestMapping(value = "/approval", method = RequestMethod.GET)
    public Result approval(@RequestParam("approveType") String result, @RequestParam(required = false,value="approver")String approver, @RequestParam("id")String id,@RequestParam(required = false,value="subApprover")String subApprover){

        return itemService.approval(result,approver,id,subApprover);
    }
    @ApiOperation("条件查询")
    @RequestMapping(value = "/select", method = RequestMethod.POST)
    public Result select(@RequestBody Item item) {
        PageResult<Item> list = itemService.select(item);
        if(!StringUtils.isEmpty(list.getList())) {
           return new Result(true, StatusCode.OK, "查询成功", list);
        }else{
           return new Result(true, StatusCode.OK, "查询失败，未查询到数据", list);

       }
        }


}
