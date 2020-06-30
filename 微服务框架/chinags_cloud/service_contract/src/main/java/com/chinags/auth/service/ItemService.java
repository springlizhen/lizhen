package com.chinags.auth.service;

import com.chinags.auth.dao.ItemDao;
import com.chinags.auth.entity.Contract;
import com.chinags.auth.entity.Item;
import com.chinags.auth.entity.UpdateUtil;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ItemService {
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private UpdateUtil updateUtil;

   /**
     * 查询全部列表
     * @return
     */
    public List<Item> findAll() {
        return itemDao.findAll();
    }
    public Result save(Item item){
        Boolean selectItem=itemDao.existsById(item.getId());
        if(!selectItem){
            item.setStatus("0");
            item.setApproverRole("0");

            itemDao.save(item);
            return new Result(true, StatusCode.OK, "保存成功");
        }else{
            updateUtil.copyNullProperties(item, selectItem);
            itemDao.save(item);
            return new Result(true, StatusCode.OK, "保存失败");

        }

    }
    public Item selectItemById(String id) {
        return itemDao.selectItemById(id);
    }
    public void deleteItemById(String id) {
         itemDao.deleteById(id);
    }
    public void update(String id){
        Item item=itemDao.getOne(id);
        item.setStatus("1");
        item.setApprovalResult("0");
        itemDao.save(item);



    }

    public Result approval(String result,String approver,String id,String subApprover){
        String status=null;
        Item item=itemDao.getOne(id);
        if("1".equals(item.getApproverRole())) {
            item.setApprovalResult(result);
            item.setApprover(approver);

            if("1".equals(result)){
                status="审批流程完成";
                //item.setStatus("1");
            }else{
                status="当前审批未完成";
            }
        }

            if("0".equals(item.getApproverRole())) {

                if ("1".equals(result)) {
                    item.setApproverRole("1");
                    item.setApprovalResult("0");
                    item.setSubApprover(subApprover);

                    status="当前审批通过到下级审批";

                }else{
                    item.setSubApprover(subApprover);

                    item.setApprovalResult(result);
                    status="当前审批未完成";

                }
            }


        //item.setApprovalResult(result);
            itemDao.save(item);
            return new Result(true, StatusCode.OK,status );


        }

    public PageResult<Item> select(Item item){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        PageRequest pageable;
        if(item.getOrderBy()==null||item.getOrderBy().equals("")){
            pageable = PageRequest.of(item.getPageNo(), item.getPageSize(), Sort.by(Sort.Direction.DESC,"createDate"));
        }else{
            pageable = PageRequest.of(item.getPageNo(), item.getPageSize(), item.getDesc(), item.getOrderBy());

        }
        Page<Item> list=itemDao.findAll(new Specification<Item>() {
            @Override
            public Predicate toPredicate(Root<Item> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (item.getId() != null && !item.getId().equals("")) {
                    predicates.add(cb.like(root.get("id").as(String.class), "%" + item.getId() + "%"));
                }
                if (item.getItemCode() != null && !item.getItemCode().equals("")) {
                    predicates.add(cb.like(root.get("itemCode").as(String.class), "%" + item.getItemCode() + "%"));
                }
                if (item.getTitle() != null && !item.getTitle().equals("")) {
                    predicates.add(cb.like(root.get("title").as(String.class), "%" + item.getTitle() + "%"));
                }

                if (item.getItemLeader() != null && !item.getItemLeader().equals("")) {
                    predicates.add(cb.like(root.get("itemLeader").as(String.class), "%" + item.getItemLeader() + "%"));
                }
                if (item.getSelectMinStartDate()!= null && !item.getSelectMinStartDate().equals("")) {
                        predicates.add(cb.greaterThanOrEqualTo(root.get("startDate").as(Date.class), item.getSelectMinStartDate()));

                    }

                if (item.getSelectMaxStartDate() != null && !item.getSelectMaxStartDate().equals("")) {

                        predicates.add(cb.lessThanOrEqualTo(root.get("startDate").as(Date.class),   item.getSelectMaxStartDate()));

                }
                if (item.getSelectMinEndDate() != null && !item.getSelectMinEndDate().equals("")) {
                        predicates.add(cb.greaterThanOrEqualTo(root.get("endDate").as(Date.class),  item.getSelectMinEndDate()));

                }
                if (item.getSelectMaxEndDate() != null && !item.getSelectMaxEndDate().equals("")) {

                        predicates.add(cb.lessThanOrEqualTo(root.get("endDate").as(Date.class),  item.getSelectMaxEndDate()));

                }
                if (item.getStatus() != null && !item.getStatus().equals("")) {
                    predicates.add(cb.like(root.get("status").as(String.class), "%" + item.getStatus() + "%"));
                }
                if (item.getApproverRole() != null && !item.getApproverRole().equals("")) {
                    predicates.add(cb.like(root.get("approverRole").as(String.class), "%" + item.getApproverRole() + "%"));
                }
                if (item.getApprovalResult() != null && !item.getApprovalResult().equals("")) {
                    predicates.add(cb.like(root.get("approvalResult").as(String.class), "%" + item.getApprovalResult() + "%"));
                }

                Predicate[] pre = new Predicate[predicates.size()];
                query.where(predicates.toArray(pre));
                return cb.and(predicates.toArray(pre));
            }
        }, pageable);
        PageResult<Item> result=PageResult.getPageResult(list);
        return result;

    }






}
