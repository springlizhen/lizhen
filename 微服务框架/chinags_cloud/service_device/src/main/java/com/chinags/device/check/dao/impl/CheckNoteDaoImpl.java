package com.chinags.device.check.dao.impl;

import com.chinags.common.utils.StringUtils;
import com.chinags.device.check.dao.plus.CheckNoteDaoPlus;
import com.chinags.device.check.entity.CheckNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CheckNoteDaoImpl implements CheckNoteDaoPlus {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Map<String, Object>> getCheckNoteByOffice(String checkCenter, String manageStation, String manageOffice) {
        StringBuilder dataSql = new StringBuilder("select longitude longitude,latitude latitude,to_char(create_date,'hh24:mi:ss') createDate  from T_COA_CHECK_NOTE where 1=1 ");
        if(StringUtils.isNotEmpty(checkCenter)){
            dataSql.append(" and check_center =:checkCenter ");
        }
        if(StringUtils.isNotEmpty(manageStation)){
            dataSql.append(" and manage_station=:manageStation");
        }
        if(StringUtils.isNotEmpty(manageOffice)){
            dataSql.append(" and manage_office=:manageOffice");
        }
        Query dataQuery = entityManager.createNativeQuery(dataSql.toString());
        if(StringUtils.isNotEmpty(checkCenter)){
            dataQuery.setParameter("checkCenter", checkCenter);
        }
        if(StringUtils.isNotEmpty(manageStation)){
            dataQuery.setParameter("manageStation", manageStation);
        }
        if(StringUtils.isNotEmpty(manageOffice)){
            dataQuery.setParameter("manageOffice", manageOffice);
        }
        return dataQuery.getResultList();
    }

    @Override
    public List<Map<String, Object>> getCheckByOffice(String checkCenter, String manageStation, String manageOffice) {
        StringBuilder dataSql = new StringBuilder("select longitude longitude,latitude latitude from T_COA_CHECK where 1=1 ");

        if(StringUtils.isNotEmpty(checkCenter)){
            dataSql.append(" and check_center =:checkCenter ");
        }
        if(StringUtils.isNotEmpty(manageStation)){
            dataSql.append(" and manage_station=:manageStation");
        }
        if(StringUtils.isNotEmpty(manageOffice)){
            dataSql.append(" and manage_office=:manageOffice");
        }
        Query dataQuery = entityManager.createNativeQuery(dataSql.toString());
        if(StringUtils.isNotEmpty(checkCenter)){
            dataQuery.setParameter("checkCenter", checkCenter);
        }
        if(StringUtils.isNotEmpty(manageStation)){
            dataQuery.setParameter("manageStation", manageStation);
        }
        if(StringUtils.isNotEmpty(manageOffice)){
            dataQuery.setParameter("manageOffice", manageOffice);
        }

        return dataQuery.getResultList();    }

    @Override
    public List<Map<String, Object>> getCheckNote(String checkPerson, String createDate) {
        StringBuilder dataSql = new StringBuilder("select longitude longitude,latitude latitude,to_char(create_date,'hh24:mi:ss') createDate  from T_COA_CHECK_NOTE where 1=1 ");
        if(StringUtils.isNotEmpty(checkPerson)){
            dataSql.append(" and check_person like '%'||:checkPerson ||'%'");
        }
        if(StringUtils.isNotEmpty(createDate)){
            dataSql.append(" and to_char(create_date,'yyyy-MM-dd')=:createDate");
        }
        Query dataQuery = entityManager.createNativeQuery(dataSql.toString());
        if(StringUtils.isNotEmpty(checkPerson)){
            dataQuery.setParameter("checkPerson", checkPerson);
        }
        if(StringUtils.isNotEmpty(createDate)){
            dataQuery.setParameter("createDate", createDate);
        }

        return dataQuery.getResultList();
    }
    @Override
    public Page<CheckNote> selectCheckNote(CheckNote checkNote, Pageable pageable) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder dataSql = new StringBuilder("SELECT * FROM T_COA_CHECK_NOTE where 1=1");
        StringBuilder countSql = new StringBuilder("SELECT count(*) FROM T_COA_CHECK_NOTE where 1=1");


        if(StringUtils.isNotEmpty(checkNote.getCheckCenter())){
            dataSql.append(" and check_center =:checkCenter ");
            countSql.append(" and check_center =:checkCenter ");

        }
        if(StringUtils.isNotEmpty(checkNote.getManageStation())){
            dataSql.append(" and manage_station=:manageStation");
            countSql.append(" and manage_station=:manageStation");
        }
        if(StringUtils.isNotEmpty(checkNote.getManageOffice())){
            dataSql.append(" and manage_office=:manageOffice");
            countSql.append(" and manage_office=:manageOffice");

        }

        if(StringUtils.isNotEmpty(checkNote.getId())){
            dataSql.append(" and id=:id");
            countSql.append(" and id=:id");

        }
        if(StringUtils.isNotEmpty(checkNote.getCheckPerson())){
            dataSql.append(" and check_person like concat(concat('%', :checkPerson),'%')");
            countSql.append(" and check_person like concat(concat('%', :checkPerson),'%')");

        }
        if(StringUtils.isNotEmpty(checkNote.getCheckName())){
            dataSql.append(" and check_name like concat(concat('%', :checkName),'%')");
            countSql.append(" and check_name like concat(concat('%', :checkName),'%')");

        }
        if(StringUtils.isNotEmpty(checkNote.getLatitude())){
            dataSql.append(" and latitude like concat(concat('%', :latitude),'%')");
            countSql.append(" and latitude like concat(concat('%', :latitude),'%')");

        }
        if(StringUtils.isNotEmpty(checkNote.getLongitude())){
            dataSql.append(" and longitude like concat(concat('%', :longitude),'%')");
            countSql.append(" and longitude like concat(concat('%', :longitude),'%')");

        }
        if(checkNote.getCreateDate()!=null && !"".equals(checkNote.getCreateDate())){
            dataSql.append(" and to_char(create_date,'yyyy-mm-dd') =:createDate");
            countSql.append(" and to_char(create_date,'yyyy-mm-dd') =:createDate");
        }
        if(checkNote.getPartoal()!=null && !"".equals(checkNote.getPartoal())){
            dataSql.append(" and to_char(partoal,'yyyy-mm-dd') =:partoal");
            countSql.append(" and to_char(partoal,'yyyy-mm-dd') =:partoal");
        }


        //组装sql语句
        if(StringUtils.isEmpty(checkNote.getOrderBy())){
            dataSql.append(" order by partoal desc" );

        }
        //创建本地sql查询实例
        Query dataQuery = entityManager.createNativeQuery(dataSql.toString(), CheckNote.class);
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        //设置参数
        if(StringUtils.isNotEmpty(checkNote.getCheckCenter())){
            dataQuery.setParameter("checkCenter", checkNote.getCheckCenter());
            countQuery.setParameter("checkCenter", checkNote.getCheckCenter());

        }
        if(StringUtils.isNotEmpty(checkNote.getManageStation())){
            dataQuery.setParameter("manageStation", checkNote.getManageStation());
            countQuery.setParameter("manageStation", checkNote.getManageStation());

        }
        if(StringUtils.isNotEmpty(checkNote.getManageOffice())){
            dataQuery.setParameter("manageOffice", checkNote.getManageOffice());
            countQuery.setParameter("manageOffice", checkNote.getManageOffice());

        }
        if (StringUtils.isNotEmpty(checkNote.getCheckPerson())) {
            dataQuery.setParameter("checkPerson", checkNote.getCheckPerson());
            countQuery.setParameter("checkPerson", checkNote.getCheckPerson());

        }
        if (StringUtils.isNotEmpty(checkNote.getCheckName())) {
            dataQuery.setParameter("checkName", checkNote.getCheckName());
            countQuery.setParameter("checkName", checkNote.getCheckName());

        }
        if (StringUtils.isNotEmpty(checkNote.getId())) {
            dataQuery.setParameter("id", checkNote.getId());
            countQuery.setParameter("id", checkNote.getId());

        }
        if (StringUtils.isNotEmpty(checkNote.getLongitude())) {
            dataQuery.setParameter("longitude", checkNote.getLongitude());
            countQuery.setParameter("longitude", checkNote.getLongitude());

        }
        if (StringUtils.isNotEmpty(checkNote.getLatitude())) {
            dataQuery.setParameter("latitude", checkNote.getLatitude());
            countQuery.setParameter("latitude", checkNote.getLatitude());

        }
        if (checkNote.getCreateDate()!=null && !"".equals(checkNote.getCreateDate())) {
            dataQuery.setParameter("createDate", sdf.format(checkNote.getCreateDate()));
            countQuery.setParameter("createDate", sdf.format(checkNote.getCreateDate()));

        }
        if (checkNote.getPartoal()!=null && !"".equals(checkNote.getPartoal())) {
            dataQuery.setParameter("partoal", sdf.format(checkNote.getPartoal()));
            countQuery.setParameter("partoal", sdf.format(checkNote.getPartoal()));

        }

        //设置分页
        BigDecimal count = (BigDecimal) countQuery.getSingleResult();
        Long total = count.longValue();
        List<CheckNote> content2 = total > pageable.getOffset() ? dataQuery.getResultList() : Collections.<CheckNote> emptyList();
        return new PageImpl<>(content2, pageable, total);

    }
    @Override
    public Page<CheckNote> selectCheckNoteTo(List<CheckNote> list3, Pageable pageable, CheckNote checkNote) {

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder dataSql = new StringBuilder("SELECT * FROM T_COA_CHECK_NOTE where 1=1");
        StringBuilder countSql = new StringBuilder("SELECT count(*) FROM T_COA_CHECK_NOTE where 1=1");


        if(StringUtils.isNotEmpty(checkNote.getCheckCenter())){
            dataSql.append(" and check_center =:checkCenter ");
            countSql.append(" and check_center =:checkCenter ");

        }
        if(StringUtils.isNotEmpty(checkNote.getManageStation())){
            dataSql.append(" and manage_station=:manageStation");
            countSql.append(" and manage_station=:manageStation");
        }
        if(StringUtils.isNotEmpty(checkNote.getManageOffice())){
            dataSql.append(" and manage_office=:manageOffice");
            countSql.append(" and manage_office=:manageOffice");

        }

        if(StringUtils.isNotEmpty(checkNote.getId())){
            dataSql.append(" and id=:id");
            countSql.append(" and id=:id");

        }
        if(StringUtils.isNotEmpty(checkNote.getCheckPerson())){
            dataSql.append(" and check_person like concat(concat('%', :checkPerson),'%')");
            countSql.append(" and check_person like concat(concat('%', :checkPerson),'%')");

        }
        if(StringUtils.isNotEmpty(checkNote.getCheckName())){
            dataSql.append(" and check_name like concat(concat('%', :checkName),'%')");
            countSql.append(" and check_name like concat(concat('%', :checkName),'%')");

        }
        if(StringUtils.isNotEmpty(checkNote.getLatitude())){
            dataSql.append(" and latitude like concat(concat('%', :latitude),'%')");
            countSql.append(" and latitude like concat(concat('%', :latitude),'%')");

        }
        if(StringUtils.isNotEmpty(checkNote.getLongitude())){
            dataSql.append(" and longitude like concat(concat('%', :longitude),'%')");
            countSql.append(" and longitude like concat(concat('%', :longitude),'%')");

        }
        if(checkNote.getCreateDate()!=null && !"".equals(checkNote.getCreateDate())){
            dataSql.append(" and to_char(create_date,'yyyy-mm-dd') =:createDate");
            countSql.append(" and to_char(create_date,'yyyy-mm-dd') =:createDate");
        }
        if(checkNote.getPartoal()!=null && !"".equals(checkNote.getPartoal())){
            dataSql.append(" and to_char(partoal,'yyyy-mm-dd') =:partoal");
            countSql.append(" and to_char(partoal,'yyyy-mm-dd') =:partoal");
        }


        //组装sql语句
        if(StringUtils.isEmpty(checkNote.getOrderBy())){
            dataSql.append(" order by partoal desc" );

        }
        //创建本地sql查询实例
        Query dataQuery = entityManager.createNativeQuery(dataSql.toString(), CheckNote.class);
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        //设置参数
        if(StringUtils.isNotEmpty(checkNote.getCheckCenter())){
            dataQuery.setParameter("checkCenter", checkNote.getCheckCenter());
            countQuery.setParameter("checkCenter", checkNote.getCheckCenter());

        }
        if(StringUtils.isNotEmpty(checkNote.getManageStation())){
            dataQuery.setParameter("manageStation", checkNote.getManageStation());
            countQuery.setParameter("manageStation", checkNote.getManageStation());

        }
        if(StringUtils.isNotEmpty(checkNote.getManageOffice())){
            dataQuery.setParameter("manageOffice", checkNote.getManageOffice());
            countQuery.setParameter("manageOffice", checkNote.getManageOffice());

        }
        if (StringUtils.isNotEmpty(checkNote.getCheckPerson())) {
            dataQuery.setParameter("checkPerson", checkNote.getCheckPerson());
            countQuery.setParameter("checkPerson", checkNote.getCheckPerson());

        }
        if (StringUtils.isNotEmpty(checkNote.getCheckName())) {
            dataQuery.setParameter("checkName", checkNote.getCheckName());
            countQuery.setParameter("checkName", checkNote.getCheckName());

        }
        if (StringUtils.isNotEmpty(checkNote.getId())) {
            dataQuery.setParameter("id", checkNote.getId());
            countQuery.setParameter("id", checkNote.getId());

        }
        if (StringUtils.isNotEmpty(checkNote.getLongitude())) {
            dataQuery.setParameter("longitude", checkNote.getLongitude());
            countQuery.setParameter("longitude", checkNote.getLongitude());

        }
        if (StringUtils.isNotEmpty(checkNote.getLatitude())) {
            dataQuery.setParameter("latitude", checkNote.getLatitude());
            countQuery.setParameter("latitude", checkNote.getLatitude());

        }
        if (checkNote.getCreateDate()!=null && !"".equals(checkNote.getCreateDate())) {
            dataQuery.setParameter("createDate", sdf.format(checkNote.getCreateDate()));
            countQuery.setParameter("createDate", sdf.format(checkNote.getCreateDate()));

        }
        if (checkNote.getPartoal()!=null && !"".equals(checkNote.getPartoal())) {
            dataQuery.setParameter("partoal", sdf.format(checkNote.getPartoal()));
            countQuery.setParameter("partoal", sdf.format(checkNote.getPartoal()));

        }

        //设置分页
        BigDecimal count = (BigDecimal) countQuery.getSingleResult();
        //剩余的条数
        Integer total1 = list3.size() - pageable.getPageNumber()*pageable.getPageSize();
        Long total = count.longValue();
        List<CheckNote> list = new ArrayList<>();
        if(pageable.getPageNumber() !=0){
            Integer iu = 0;
            if(total1>20){
                iu = pageable.getPageNumber()*pageable.getPageSize();
            }else{
                iu = list3.size();
            }
            for(int i=list3.size()-total1;i<iu;i++){
                list.add(list3.get(i));
            }
        }else{
            Integer op = 0;
            if(list3.size()>20){
                op = 20;
            }else{
                op = list3.size();
            }
            for(int i=0;i<op;i++){
                list.add(list3.get(i));
            }
        }


        List<CheckNote> content2 = total > pageable.getOffset() ? list : Collections.<CheckNote> emptyList();
        return new PageImpl<>(content2, pageable, total);
    }
}
