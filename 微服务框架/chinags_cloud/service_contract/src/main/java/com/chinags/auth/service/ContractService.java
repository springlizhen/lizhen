package com.chinags.auth.service;

import com.chinags.auth.dao.ContractDao;
import com.chinags.auth.dao.ContractUpdateDao;
import com.chinags.auth.entity.*;
import com.chinags.common.entity.BaseEntity;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.lang.StringUtils;
import org.apache.tools.ant.taskdefs.condition.Contains;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ContractService {
    @Autowired
    private ContractDao contractDao;
    @Autowired
    private UpdateUtil updateUtil;
    @Autowired
    private ContractUpdateDao contractUpdateDao;
    @PersistenceContext
    private EntityManager entityManager;


    //public Contract getContractByContractCode(String contractCode){
    // return contractDao.getContractByContractCode(contractCode);

    //  }
    public List<Contract> findAll() {
        PageRequest pageable;
        List<Contract> list = contractDao.findAll();
        return list;

    }
    public Contract selectContractById(String id){
     return contractDao.getContractById(id);

      }
    public List<Map<String,Object>> selectSubContract(){
        return contractDao.selectSubContract();


    }
    public ContractUpdate selectMaxUpdateById(String workId){
        return contractUpdateDao.getMaxUpdateContractById(workId);

    }

    public ContractUpdate selectMaxUpdate(String id){
        return contractUpdateDao.getMaxUpdateContract(id);

    }
    public ContractUpdate selectUpdate(String id){
        return contractUpdateDao.getUpdateContractById(id);

    }
    public PageResult<ContractUpdate> selectUpdateContractById(ContractUpdate contractUpdate){
        PageRequest pageable;
        if(contractUpdate.getOrderBy()==null||contractUpdate.getOrderBy().equals("")){
            pageable = PageRequest.of(contractUpdate.getPageNo(), contractUpdate.getPageSize(),Sort.by(Sort.Direction.DESC,"updateDate"));
        }else{
            pageable = PageRequest.of(contractUpdate.getPageNo(), contractUpdate.getPageSize(), contractUpdate.getDesc(), contractUpdate.getOrderBy());

        }
        Page<ContractUpdate> list = contractUpdateDao.findAll(new Specification<ContractUpdate>() {
            @Override
            public Predicate toPredicate(Root<ContractUpdate> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (contractUpdate.getId() != null && !contractUpdate.getId().equals("")) {
                    predicates.add(cb.like(root.get("contractId").as(String.class), "%" + contractUpdate.getId() + "%"));
                }

                Predicate[] pre = new Predicate[predicates.size()];
                query.where(predicates.toArray(pre));
                return cb.and(predicates.toArray(pre));
            }
        }, pageable);
        PageResult<ContractUpdate> result=PageResult.getPageResult(list);
        return result;

    }
       // return contractDao.getUpdateContractById(id);



    public Result update(Contract contract) {
        Boolean scontract = contractDao.existsById(contract.getId());
        Contract selectContract = contractDao.getContractById(contract.getId());
        //if (StringUtils.isNotNull(contract.getContractObject())) {
        // selectContract.setContractObject(contract.getContractObject());

        // }
        updateUtil.copyNullProperties(contract, selectContract);

        if (!scontract) {
            contractDao.save(contract);
            return new Result(true, StatusCode.OK, "更新成功");

        } else {
            updateUtil.copyNullProperties(contract, selectContract);
            contractDao.save(selectContract);
            return new Result(false, StatusCode.ERROR, "更新失败");

        }
    }
    public void deleteContractById(String id){
         contractDao.deleteById(id);

    }

    public Result saveContract(Contract contract) {
        Boolean scontract = contractDao.existsById(contract.getId());
        Contract selectContract = contractDao.getContractById(contract.getId());
      /*  if(parentContract!=null) {
            contract.setParentCodes(parentContract.getParentCodes() + parentContract.getId() + ",");
            contract.setTreeSorts(parentContract.getTreeSorts() + String.format("%10d", contract.getTreeSort()).replace(" ", "0") + ",");
            contract.setTreeNames(parentContract.getTreeNames() + "/" + contract.getTreeNames());
            if (!scontract) {
                contract.setTreeLeaf("1");
                contract.setTreeLevel(parentContract.getTreeLevel() + 1);
                parentContract.setTreeLeaf("0");
                contractDao.save(parentContract);
            }
        }else{
            contract.setParentCode("0");
            contract.setParentCodes("0,");
            contract.setTreeSort(30);
            contract.setTreeSorts(String.format("%10d", contract.getTreeSort()).replace(" ", "0") + ",");
            contract.setTreeNames(contract.getTitle());
            if (!scontract) {
                contract.setTreeLeaf("1");
                contract.setTreeLevel(0);
            }
        }
        if(scontract){
            contract.setTreeLeaf(selectContract.getTreeLeaf());
            contract.setTreeLevel(selectContract.getTreeLevel());
        }
        */
        if (!scontract) {
            contractDao.save(contract);
            return new Result(true, StatusCode.OK, "保存成功");

        } else {
            updateUtil.copyNullProperties(contract, selectContract);
            contractDao.save(selectContract);


            return new Result(false, StatusCode.OK, "修改成功");


        }
    }
    public PageResult<Contract> selectAll(Contract contract) {
        PageRequest pageable;
      if(contract.getOrderBy()==null||contract.getOrderBy().equals("")){
          pageable = PageRequest.of(contract.getPageNo(), contract.getPageSize(), Sort.by(Sort.Direction.DESC,"createDate"));
      }else{
          pageable = PageRequest.of(contract.getPageNo(), contract.getPageSize(), contract.getDesc(), contract.getOrderBy());

      }

        Page<Contract> list = contractDao.findAll(new Specification<Contract>() {
            @Override
            public Predicate toPredicate(Root<Contract> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (contract.getId() != null && !contract.getId().equals("")) {
                    predicates.add(cb.like(root.get("subContract").as(String.class), "%" + contract.getId() + "%"));
                }
                if (contract.getContractType() != null && !contract.getContractType().equals("")) {
                    predicates.add(cb.like(root.get("contractType").as(String.class), "%" + contract.getContractType() + "%"));
                }
                if (contract.getTitle() != null && !contract.getTitle().equals("")) {
                    predicates.add(cb.like(root.get("title").as(String.class), "%" + contract.getTitle() + "%"));
                }
                if (contract.getOwner() != null && !contract.getOwner().equals("")) {
                    predicates.add(cb.like(root.get("owner").as(String.class), "%" + contract.getOwner() + "%"));
                }
                if (contract.getOwnerAddress() != null && !contract.getOwnerAddress().equals("")) {
                    predicates.add(cb.like(root.get("ownerAddress").as(String.class), "%" + contract.getOwnerAddress() + "%"));
                }
                if (contract.getOwnerContact() != null && !contract.getOwnerContact().equals("")) {
                    predicates.add(cb.like(root.get("ownerContact").as(String.class), "%" + contract.getOwnerContact() + "%"));
                }
                if (contract.getContractCode() != null && !contract.getContractCode().equals("")) {
                    predicates.add(cb.like(root.get("contractCode").as(String.class), "%" + contract.getContractCode() + "%"));
                }
                if (contract.getStatus() != null && !contract.getStatus().equals("")) {
                   // if (contract.getStatus().equals("1")) {
                        predicates.add(cb.like(root.get("status").as(String.class),"%" + contract.getStatus() + "%"));
                   // }
                }

                Predicate[] pre = new Predicate[predicates.size()];
                query.where(predicates.toArray(pre));
                return cb.and(predicates.toArray(pre));
            }
        }, pageable);
        PageResult<Contract> result=PageResult.getPageResult(list);
        return result;

    }

    public PageResult<Map<String,Object>> select(Contract contract,String logincode) {
        PageRequest pageable;
        if(contract.getOrderBy()==null||contract.getOrderBy().equals("")){
            pageable = PageRequest.of(contract.getPageNo(), contract.getPageSize(), Sort.by(Sort.Direction.DESC,"createDate"));
        }else{
            pageable = PageRequest.of(contract.getPageNo(), contract.getPageSize(), contract.getDesc(), contract.getOrderBy());

        }

        List<Map<String,Object>> list = contractDao.selectget(logincode);
        Integer ui = list.size();
        String count = ui.toString();
        Long total = Long.parseLong(count);
        List<Map<String,Object>> content2 = total > pageable.getOffset() ? list : Collections.<Map<String,Object>> emptyList();
        Page<Map<String,Object>> contracts = new PageImpl<>(content2, pageable, total);
        return PageResult.getPageResult(contracts);
//        return result;

    }

    public PageResult<Map<String,Object>> selectTo(Contract contract, String logincode) {
        PageRequest pageable;
        if(contract.getOrderBy()==null||contract.getOrderBy().equals("")){
            pageable = PageRequest.of(contract.getPageNo(), contract.getPageSize(), Sort.by(Sort.Direction.DESC,"createDate"));
        }else{
            pageable = PageRequest.of(contract.getPageNo(), contract.getPageSize(), contract.getDesc(), contract.getOrderBy());

        }
        List<Map<String,Object>> list = contractDao.selectgetTo(logincode);
        Integer ui = list.size();
        String count = ui.toString();
        Long total = Long.parseLong(count);
        List<Map<String,Object>> content2 = total > pageable.getOffset() ? list : Collections.<Map<String,Object>> emptyList();
        Page<Map<String,Object>> contracts = new PageImpl<>(content2, pageable, total);
        return PageResult.getPageResult(contracts);
    }

    public PageResult<Map<String, Object>> selectLb(Contract contract, String logincode) {
        PageRequest pageable;
        if(contract.getOrderBy()==null||contract.getOrderBy().equals("")){
            pageable = PageRequest.of(contract.getPageNo(), contract.getPageSize(), Sort.by(Sort.Direction.DESC,"createDate"));
        }else{
            pageable = PageRequest.of(contract.getPageNo(), contract.getPageSize(), contract.getDesc(), contract.getOrderBy());

        }
        List<Map<String,Object>> list = contractDao.selectgetToLb(logincode);
        Integer ui = list.size();
        String count = ui.toString();
        Long total = Long.parseLong(count);
        List<Map<String,Object>> content2 = total > pageable.getOffset() ? list : Collections.<Map<String,Object>> emptyList();
        Page<Map<String,Object>> contracts = new PageImpl<>(content2, pageable, total);
        return PageResult.getPageResult(contracts);
    }

    public PageResult<Map<String, Object>> selectToYu(Contract contract, String logincode) {
        PageRequest pageable;
        if(contract.getOrderBy()==null||contract.getOrderBy().equals("")){
            pageable = PageRequest.of(contract.getPageNo(), contract.getPageSize(), Sort.by(Sort.Direction.DESC,"createDate"));
        }else{
            pageable = PageRequest.of(contract.getPageNo(), contract.getPageSize(), contract.getDesc(), contract.getOrderBy());

        }
        List<Map<String,Object>> list = contractDao.selectToYu(logincode);
        Integer ui = list.size();
        String count = ui.toString();
        Long total = Long.parseLong(count);
        List<Map<String,Object>> content2 = total > pageable.getOffset() ? list : Collections.<Map<String,Object>> emptyList();
        Page<Map<String,Object>> contracts = new PageImpl<>(content2, pageable, total);
        return PageResult.getPageResult(contracts);
    }

    public PageResult<Map<String, Object>> selectZl(Contract contract, String logincode) {
        PageRequest pageable;
        if(contract.getOrderBy()==null||contract.getOrderBy().equals("")){
            pageable = PageRequest.of(contract.getPageNo(), contract.getPageSize(), Sort.by(Sort.Direction.DESC,"createDate"));
        }else{
            pageable = PageRequest.of(contract.getPageNo(), contract.getPageSize(), contract.getDesc(), contract.getOrderBy());
        }
//        StringBuilder dataSql = new StringBuilder("select a.* FROM T_COA_CONTRACT a inner join(select TITLE,FLOWNAME,SENDUSERNAME,SENDTIME,WORKID,TRACKID,SUBJECTION_TYPE,SUBJECTION_ID  from HORIZON.TW_HZ_WORKLIST where 1=1 and (AUTH_ID=:logincode or AGENT_ID=:logincode)) b on a.WORK_ID = b.WORKID");
//        StringBuilder countSql = new StringBuilder("select count(*) FROM T_COA_CONTRACT a inner join(select TITLE,FLOWNAME,SENDUSERNAME,SENDTIME,WORKID,TRACKID,SUBJECTION_TYPE,SUBJECTION_ID  from HORIZON.TW_HZ_WORKLIST where (AUTH_ID=:logincode or AGENT_ID=:logincode)) b on a.WORK_ID = b.WORKID");
//        if(StringUtils.isNotEmpty(contract.getTitle())){
//            String title = contract.getTitle();
//            dataSql.append(" and a.contractname =:contractname");
//            countSql.append(" and a.contractname =:contractname");
//        }
//        if(StringUtils.isNotEmpty(contract.getContractCode())){
//            String contractCode = contract.getContractCode();
//            dataSql.append(" and a.contract_code =:contractCode");
//            countSql.append(" and a.contract_code =:contractCode");
//        }
//        dataSql.append(" order by b.sendtime desc" );
//        Query dataQuery = entityManager.createNativeQuery(dataSql.toString(),Contract.class);
//        Query countQuery = entityManager.createNativeQuery(countSql.toString());
//        if(StringUtils.isNotEmpty(contract.getTitle())){
//            dataQuery.setParameter("contractname", contract.getTitle());
//            countQuery.setParameter("contractname", contract.getTitle());
//        }
//        if(StringUtils.isNotEmpty(contract.getContractCode())){
//            dataQuery.setParameter("contractCode", contract.getContractCode());
//            countQuery.setParameter("contractCode", contract.getContractCode());
//        }
//        dataQuery.setParameter("logincode", logincode);
//        countQuery.setParameter("logincode", logincode);
        List<Map<String,Object>> mapList = new ArrayList<>();
//        List<Map<String,Object>> list = new ArrayList<>();
        String value = "";
        String value1 = "";
        List<Map<String,Object>> list = contractDao.selectZl();

//        List<Map<String,Object>> listz = contractDao.selectZlM(logincode);


        for(Map<String,Object> map:list){
            for(String key : map.keySet()){
                if(null != contract.getTitle()){
                    if("CONTRACTNAME".equals(key)){
                         value = (String) map.get(key);

                      }
                }
                if(null != contract.getContractCode()){
                    if("CONTRACT_CODE".equals(key)){
                        value1 = (String) map.get(key);

                    }
                }

            }
            if(null != contract.getContractCode()){
                if(value1.contains(contract.getContractCode())){
                        if(null != contract.getTitle()){
                            if(value.contains(contract.getContractCode())){
                                mapList.add(map);
                            }
                        }else{
                            mapList.add(map);
                        }
                }

            }else if(null != contract.getTitle()){
                if(value.contains(contract.getTitle())){
                    if(null != contract.getContractCode()){
                        if(value1.contains(contract.getContractCode())){
                            mapList.add(map);
                        }
                    }else{
                        mapList.add(map);
                    }
                }
            }else {
                mapList = list;
            }

        }

//        if(null != contract.getTitle()&& null != contract.getContractCode()){
//                for(Map<String,Object> map:list){
//                    for(String key : map.keySet()){
//                        if("CONTRACTNAME".equals(key)){
//                           value = (String) map.get(key);
//                        }
//                        if("CONTRACT_CODE".equals(key)){
//                             value1 = (String) map.get(key);
//                        }
//
//                    }
//                    if(null != value && null != value1) {
//                        if (value.contains(contract.getTitle()) && value1.contains(contract.getContractCode())) {
//                            mapList.add(map);
//                        }
//                    }
//                }
//        }else if(null != contract.getTitle()&& null == contract.getContractCode()){
//            for(Map<String,Object> map:list){
//                for(String key : map.keySet()){
//                    if("CONTRACTNAME".equals(key)){
//                        value = (String) map.get(key);
//                    }
//
//
//                }
//                if(null !=value) {
//                    if (value.contains(contract.getTitle())) {
//                        mapList.add(map);
//                    }
//                }
//
//
//            }
//
//
//      }else if(null == contract.getTitle()&& null != contract.getContractCode()){
//            for(Map<String,Object> map:list){
//                for(String key : map.keySet()){
//                    if("CONTRACT_CODE".equals(key)){
//                        value1 = (String) map.get(key);
//                    }
//
//
//                }
//                if(null != value1) {
//                    if (value1.contains(contract.getContractCode())) {
//                        mapList.add(map);
//                    }
//                }
//
//
//            }
//        }else{
//            mapList = list;
//        }
        Integer ui = mapList.size();
        String count = ui.toString();
        Long total = Long.parseLong(count);
        List<Map<String,Object>> content2 = total > pageable.getOffset() ? mapList : Collections.<Map<String,Object>> emptyList();
        Page<Map<String,Object>> contracts = new PageImpl<>(content2, pageable, total);
        return PageResult.getPageResult(contracts);
//        BigDecimal count = (BigDecimal) countQuery.getSingleResult();
//        Long total = count.longValue();
//        List<Contract> content2 = total > pageable.getOffset() ? dataQuery.getResultList() : Collections.<Contract> emptyList();
//        Page<Contract> contracts = new PageImpl<>(content2, pageable, total);
//        return PageResult.getPageResult(contracts);
    }




    public PageResult<Contract> selectZlK(Contract contract, String logincode) {
        PageRequest pageable;
        if(contract.getOrderBy()==null||contract.getOrderBy().equals("")){
            pageable = PageRequest.of(contract.getPageNo(), contract.getPageSize(), Sort.by(Sort.Direction.DESC,"createDate"));
        }else{
            pageable = PageRequest.of(contract.getPageNo(), contract.getPageSize(), contract.getDesc(), contract.getOrderBy());

        }
//        StringBuilder dataSql = new StringBuilder("select a.*,b.FLOWNAME,b.SENDUSERNAME,b.SENDTIME,b.TRACKID,b.SUBJECTION_TYPE FROM T_COA_CONTRACT a inner join(select TITLE,FLOWNAME,SENDUSERNAME,SENDTIME,WORKID,TRACKID,SUBJECTION_TYPE,SUBJECTION_ID  from HORIZON.TW_HZ_WORKLIST where (AUTH_ID=:logincode or AGENT_ID=:logincode)) b on a.WORK_ID = b.WORKID");
//        StringBuilder countSql = new StringBuilder("");
//
//        if(StringUtils.isNotEmpty(contract.getTitle())){
//            String title = contract.getTitle();
//            dataSql.append(" and a.contractname =:title");
//            countSql.append(" and a.contractname =:title");
//        }
//        if(StringUtils.isNotEmpty(contract.getContractCode())){
//            String contractCode = contract.getContractCode();
//            dataSql.append(" and a.contract_code =:contractCode");
//            countSql.append(" and a.contract_code =:contractCode");
//        }
//        dataSql.append("order by b.sendtime desc" );
//        Query dataQuery = entityManager.createNativeQuery(dataSql.toString(), Map.class);
//        Query countQuery = entityManager.createNativeQuery(countSql.toString());
//        if(StringUtils.isNotEmpty(contract.getTitle())){
//            dataQuery.setParameter("contractname", contract.getTitle());
//            countQuery.setParameter("contractname", contract.getTitle());
//
//        }
//        if(StringUtils.isNotEmpty(contract.getContractCode())){
//            dataQuery.setParameter("contractCode", contract.getContractCode());
//            countQuery.setParameter("contractCode", contract.getContractCode());
//
//        }
//        dataQuery.setParameter("logincode", logincode);
//        countQuery.setParameter("logincode", logincode)
        List<Map<String,Object>> mapList = new ArrayList<>();
        String value = "";
        String value1 = "";
        List<Map<String,Object>> list = contractDao.selectZlK();
        for(Map<String,Object> map:list){
            for(String key : map.keySet()){
                if(null != contract.getTitle()){
                    if("CONTRACTNAME".equals(key)){
                        value = (String) map.get(key);
                    }
                }
                if(null != contract.getContractCode()){
                    if("CONTRACT_CODE".equals(key)){
                        value1 = (String) map.get(key);
                    }
                }

            }
            if(null != contract.getContractCode()){
                if(value1.contains(contract.getContractCode())){
                    if(null != contract.getTitle()){
                        if(value.contains(contract.getContractCode())){
                            mapList.add(map);
                        }
                    }else{
                        mapList.add(map);
                    }
                }

            }else if(null != contract.getTitle()){
                if(value.contains(contract.getTitle())){
                    if(null != contract.getContractCode()){
                        if(value1.contains(contract.getContractCode())){
                            mapList.add(map);
                        }
                    }else{
                        mapList.add(map);
                    }
                }
            }else {
                mapList = list;
            }

        }
        Integer ui = mapList.size();
        String count = ui.toString();
        Long total = Long.parseLong(count);
        List<Map<String,Object>> content2 = total > pageable.getOffset() ? mapList : Collections.<Map<String,Object>> emptyList();
        Page<Map<String,Object>> contracts = new PageImpl<>(content2, pageable, total);
        return PageResult.getPageResult(contracts);
//        BigDecimal count = (BigDecimal) countQuery.getSingleResult();
//        Long total = count.longValue();
//        List<Map<String,Object>> content2 = total > pageable.getOffset() ? dataQuery.getResultList() : Collections.<Map<String,Object>> emptyList();
//        Page<Map<String,Object>> contracts = new PageImpl<>(content2, pageable, total);
//        return PageResult.getPageResult(contracts);

    }
    public PageResult<Map<String, Object>> selectZlKU(ContractMter contractMter, String logincode,String contractid) {
        PageRequest pageable;
        if(contractMter.getOrderBy()==null||contractMter.getOrderBy().equals("")){
            pageable = PageRequest.of(contractMter.getPageNo(), contractMter.getPageSize(), Sort.by(Sort.Direction.DESC,"createDate"));
        }else{
            pageable = PageRequest.of(contractMter.getPageNo(), contractMter.getPageSize(), contractMter.getDesc(), contractMter.getOrderBy());
        }
        List<Map<String,Object>> mapList = new ArrayList<>();
        String value = "";
        String value1 = "";
        List<Map<String,Object>> list = contractDao.selectZlKU(contractid);
        Integer ui = list.size();
        String count = ui.toString();
        Long total = Long.parseLong(count);
        List<Map<String,Object>> content2 = total > pageable.getOffset() ? list : Collections.<Map<String,Object>> emptyList();
        Page<Map<String,Object>> contracts = new PageImpl<>(content2, pageable, total);
        return PageResult.getPageResult(contracts);

    }

    public List<Map<String, Object>> selectUi(String logincode) {
        List<Map<String,Object>> list = contractDao.selectUi(logincode);
        List<Map<String,Object>> mapList = new ArrayList<>();
        for(Map<String,Object> to:list){
            List<Contract> contract = new ArrayList<>();
            Map<String,Object> map = new HashMap<>();
            String id = (String) to.get("FLOWID");
            String workid = (String) to.get("WORKID");
            String sendtime = (String) to.get("sendtime");
            String CREATOR = (String) to.get("CREATOR");
            if(id.equals("hetongjiliang")){
                  contract= contractDao.selectBy(workid);
                  map.put("WORKID",workid);
                  map.put("FLOWID",id);
                  map.put("CREATOR",CREATOR);
                  map.put("sendtime",sendtime);
                  map.put("CONTRACTNAME",contract.get(0).getContractname());
                  map.put("CONTRACT_TYPE",contract.get(0).getContractType());

            }else if(id.equals("hetongdengji")){
                contract  = contractDao.selectByworkId(workid);
                map.put("WORKID",workid);
                map.put("FLOWID",id);
                map.put("CREATOR",CREATOR);
                map.put("sendtime",sendtime);
                map.put("CONTRACTNAME",contract.get(0).getContractname());
                map.put("CONTRACT_TYPE",contract.get(0).getContractType());

            }
           mapList.add(map);
        }
        return  mapList;
    }

    public List<Map<String, Object>> selectUiTo(String logincode) {
        List<Map<String,Object>> list = contractDao.selectUiTo(logincode);
        List<Map<String,Object>> mapList = new ArrayList<>();
        for(Map<String,Object> to:list){
            List<Contract> contract = new ArrayList<>();
            Map<String,Object> map = new HashMap<>();
            String id = (String) to.get("FLOWID");
            String workid = (String) to.get("WORKID");
            String sendtime = (String) to.get("sendtime");
            String CREATOR = (String) to.get("CREATOR");
            if(id.equals("hetongjiliang")){
                contract= contractDao.selectBy(workid);
                map.put("WORKID",workid);
                map.put("FLOWID",id);
                map.put("CREATOR",CREATOR);
                map.put("sendtime",sendtime);
                map.put("CONTRACTNAME",contract.get(0).getContractname());
                map.put("CONTRACT_TYPE",contract.get(0).getContractType());

            }else if(id.equals("hetongdengji")){
                contract  = contractDao.selectByworkId(workid);
                map.put("WORKID",workid);
                map.put("FLOWID",id);
                map.put("CREATOR",CREATOR);
                map.put("sendtime",sendtime);
                map.put("CONTRACTNAME",contract.get(0).getContractname());
                map.put("CONTRACT_TYPE",contract.get(0).getContractType());

            }
            mapList.add(map);
        }
        return  mapList;
    }

    public List<Map<String, Object>> selectUiToK(String logincode) {
        List<Map<String,Object>> list = contractDao.selectUiToK(logincode);
        List<Map<String,Object>> mapList = new ArrayList<>();
        for(Map<String,Object> to:list){
            List<Contract> contract = new ArrayList<>();
            Map<String,Object> map = new HashMap<>();
            String id = (String) to.get("FLOWID");
            String workid = (String) to.get("WORKID");
            String sendtime = (String) to.get("sendtime");
            String CREATOR = (String) to.get("CREATOR");
            if(id.equals("hetongjiliang")){
                contract= contractDao.selectBy(workid);
                map.put("WORKID",workid);
                map.put("FLOWID",id);
                map.put("CREATOR",CREATOR);
                map.put("sendtime",sendtime);
                map.put("CONTRACTNAME",contract.get(0).getContractname());
                map.put("CONTRACT_TYPE",contract.get(0).getContractType());

            }else if(id.equals("hetongdengji")){
                contract  = contractDao.selectByworkId(workid);
                map.put("WORKID",workid);
                map.put("FLOWID",id);
                map.put("CREATOR",CREATOR);
                map.put("sendtime",sendtime);
                map.put("CONTRACTNAME",contract.get(0).getContractname());
                map.put("CONTRACT_TYPE",contract.get(0).getContractType());

            }
            mapList.add(map);
        }
        return  mapList;
    }

    /**
     * 根据合同金额排序 lizhen
     * @return
     */
    public List<Contract> selectwu() {
        List<Map<String,Object>> list = contractDao.selectwu();
        List<Contract> contractList = contractDao.selectAll();
        List<Contract> list1 = new ArrayList<>();
        for(int i=0;i<list.size();i++){
//            mon+= list1.get(i).getMoney();
            Contract contract =new Contract();
            Double mon = 0.0000000000;
            String name = (String) list.get(i).get("CUSTOMER");
            for(int k=0;k<contractList.size();k++){
                if(contractList.get(k).getCustomer().equals(name)){
                    mon+= contractList.get(k).getMoney();
                }
            }
           contract.setMoney(mon);
            contract.setCustomer(name);
            list1.add(contract);
        }
        //list 根据money排序
        Collections.sort(list1, new Comparator<Contract>() {
            @Override
            public int compare(Contract u1, Contract u2) {
                if(u1.getMoney() > u2.getMoney()) {
                    //return -1:即为正序排序
                    return -1;
                }else if (u1.getMoney() == u2.getMoney()) {
                    return 0;
                }else {
                    //return 1: 即为倒序排序
                    return 1;
                }
            }
        });

        return list1;

    }
    /***
     * 根据合同计量数排序 lizhen
     * @return
     */
    public List<Map<String, Object>> selectUiToKbZ() {
        //获取到合同的所有建设单位的数量
        List<Map<String,Object>> mapList = new ArrayList<>();
        List<Map<String,Object>> list = contractDao.selectUiToKbZ();
        for(Map<String,Object> map:list){
            Map<String,Object> map1 = new HashMap<>();
            String CUSTOMER = (String)map.get("CUSTOMER");
            BigDecimal count = (BigDecimal) map.get("COUNT");
            List<Contract> list1 = contractDao.getmoney(CUSTOMER);
            Double mon = 0.0000000000;
            for(Contract to:list1){
                if(null !=to.getMoney()){
                        mon+=to.getMoney();
                }
            }
            map1.put("CUSTOMER",CUSTOMER);
            map1.put("COUNT(1)",count);
            map1.put("MONRY",mon);
            mapList.add(map1);

        }
        return  mapList;
    }

    /**
     * 年度合同类型的统计查询
     * @return
     */
    public List<Contract> selectUiToKbZtu() {
        List<Contract> list = contractDao.getType();
        return list;

    }
    //年度合同数量查询
//    public List<Contract> selectUiToKbZP() {
//
//    }
}



