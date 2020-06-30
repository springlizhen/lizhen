package com.chinags.device.basic.dao.impl;

import com.chinags.common.utils.StringUtils;
import com.chinags.device.basic.dao.plus.DeviceDaoPlus;
import com.chinags.device.basic.entity.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class DeviceDaoImpl implements DeviceDaoPlus {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Device> findAllPage(Device device, Pageable pageable) {
        StringBuilder dataSql = new StringBuilder("SELECT * FROM t_enm_device d LEFT Join T_ENM_DERVICE_OFFICE o on d.device_class = o.office_code");
        StringBuilder countSql = new StringBuilder("SELECT count(*) FROM t_enm_device d LEFT Join T_ENM_DERVICE_OFFICE o on d.device_class = o.office_code");
        if(StringUtils.isNotEmpty(device.getFieldCode())&&StringUtils.isNotEmpty(device.getFieldValue())){
            dataSql.append(" LEFT Join T_ENM_DEVICE_PARAM p on d.id = p.device_id");
            countSql.append(" LEFT Join T_ENM_DEVICE_PARAM p on d.id = p.device_id");
        }
        if(StringUtils.isNotEmpty(device.getNodeid())){
            device.setParentCode(device.getNodeid());
        }
        //拼接where条件
        StringBuilder whereSql = new StringBuilder(" WHERE d.status != 1");
        if (StringUtils.isNotEmpty(device.getFieldCode())&&StringUtils.isNotEmpty(device.getFieldValue())) {
            whereSql.append(" AND p.code_id = :fieldCode");
            whereSql.append(" AND p.value1 like concat(concat('%', :fieldValue),'%')");
        }

        if (StringUtils.isNotEmpty(device.getDeviceName())) {
            whereSql.append(" AND d.device_name like concat(concat('%', :deviceName),'%')");
        }
        if (StringUtils.isNotEmpty(device.getDeviceCode())) {
            whereSql.append(" AND d.device_code like concat(concat('%', :deviceCode),'%')");
        }
        if (StringUtils.isNotEmpty(device.getOrgId())) {
//            whereSql.append(" AND d.org_id in (:orgIds)");
            whereSql.append(" AND d.org_id = :orgId");
        }
        if (device.getTreeLevel()!=null) {
//            whereSql.append(" AND d.org_id in (:orgIds)");
            whereSql.append(" AND d.tree_level = :treeLevel");
        }
        if (StringUtils.isNotEmpty(device.getSchUnit())) {
            whereSql.append(" AND d.sch_unit like concat(concat('%', :schUnit),'%')");
        }
        if (StringUtils.isNotEmpty(device.getCtlStatus())) {
            whereSql.append(" AND d.ctl_status = :ctlStatus");
        }
        if (StringUtils.isNotEmpty(device.getUseStatus())) {
            whereSql.append(" AND d.use_status = :useStatus");
        }
        if (StringUtils.isNotEmpty(device.getDeviceClass())) {
//            whereSql.append(" AND (o.parent_codes like concat(concat('%', :deviceClass),'%') or o.office_code = :deviceClassOC)");
            whereSql.append(" AND d.device_class = :deviceClass");
        }
        if (StringUtils.isNotEmpty(device.getParentCode())) {
            whereSql.append(" AND d.parent_code = :parentCode");
        }
        countSql.append(whereSql);
        //组装sql语句
        if(StringUtils.isNotEmpty(device.getOrderBy())) {
            whereSql.append(" order by " + device.getOrderBy());
            switch (device.getDesc()){
                case ASC:
                    whereSql.append(" asc");
                    break;
                case DESC:
                    whereSql.append(" desc");
            }
        }else{
            whereSql.append(" order by d.tree_sorts,d.tree_level,d.tree_sort asc");
        }
        dataSql.append(whereSql);

        //创建本地sql查询实例
        Query dataQuery = entityManager.createNativeQuery(dataSql.toString(), Device.class);
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        //设置参数
        if (StringUtils.isNotEmpty(device.getFieldCode())&&StringUtils.isNotEmpty(device.getFieldValue())) {
            dataQuery.setParameter("fieldCode", device.getFieldCode());
            dataQuery.setParameter("fieldValue", device.getFieldValue());
            countQuery.setParameter("fieldCode", device.getFieldCode());
            countQuery.setParameter("fieldValue", device.getFieldValue());
        }
        if (StringUtils.isNotEmpty(device.getDeviceName())) {
            dataQuery.setParameter("deviceName", device.getDeviceName());
            countQuery.setParameter("deviceName", device.getDeviceName());
        }
        if (StringUtils.isNotEmpty(device.getDeviceCode())) {
            dataQuery.setParameter("deviceCode", device.getDeviceCode());
            countQuery.setParameter("deviceCode", device.getDeviceCode());
        }
        if (StringUtils.isNotEmpty(device.getOrgId())) {
            dataQuery.setParameter("orgId", device.getOrgId());
            countQuery.setParameter("orgId", device.getOrgId());
        }
        if (StringUtils.isNotEmpty(device.getSchUnit())) {
            dataQuery.setParameter("schUnit", device.getSchUnit());
            countQuery.setParameter("schUnit", device.getSchUnit());
        }
        if (StringUtils.isNotEmpty(device.getCtlStatus())) {
            dataQuery.setParameter("ctlStatus", device.getCtlStatus());
            countQuery.setParameter("ctlStatus", device.getCtlStatus());
        }
        if (StringUtils.isNotEmpty(device.getUseStatus())) {
            dataQuery.setParameter("useStatus", device.getUseStatus());
            countQuery.setParameter("useStatus", device.getUseStatus());
        }
        if (StringUtils.isNotEmpty(device.getDeviceClass())) {
            dataQuery.setParameter("deviceClass", device.getDeviceClass());
            countQuery.setParameter("deviceClass", device.getDeviceClass());
//            dataQuery.setParameter("deviceClass", device.getDeviceClass()+",");
//            countQuery.setParameter("deviceClass", device.getDeviceClass()+",");
//            dataQuery.setParameter("deviceClassOC", device.getDeviceClass());
//            countQuery.setParameter("deviceClassOC", device.getDeviceClass());
        }
        if (StringUtils.isNotEmpty(device.getParentCode())) {
            dataQuery.setParameter("parentCode", device.getParentCode());
            countQuery.setParameter("parentCode", device.getParentCode());
        }
        if (device.getTreeLevel()!=null) {
            dataQuery.setParameter("treeLevel", device.getTreeLevel());
            countQuery.setParameter("treeLevel", device.getTreeLevel());
        }
        //设置分页
        BigDecimal count = (BigDecimal) countQuery.getSingleResult();
        Long total = count.longValue();
        List<Device> content2 = total > pageable.getOffset() ? dataQuery.getResultList() : Collections.<Device> emptyList();
        return new PageImpl<>(content2, pageable, total);
    }

    @Override
    public List<Device> findListByCondition(Device device) {
        //创建sql语句
        StringBuilder dataSql = new StringBuilder("SELECT d.* FROM t_enm_device d");

        //拼接where条件
        StringBuilder whereSql = new StringBuilder(" WHERE d.cgq_status = '是'");
        if (StringUtils.isNotEmpty(device.getId())) {
            whereSql.append(" AND d.ID = :deviceId");
        }
        if (StringUtils.isNotEmpty(device.getPlanParentId())) {
            whereSql.append(" AND d.PLAN_PARENT_ID = :planParentId");
        }
        if (StringUtils.isNotEmpty(device.getStationId())) {
            whereSql.append(" AND d.STATION_ID = :stationId");
        }
        if (StringUtils.isNotEmpty(device.getOfficeId())) {
            whereSql.append(" AND d.OFFICE_ID = :officeId");
        }
        if (StringUtils.isNotEmpty(device.getCgqGcdCode())) {
            whereSql.append(" AND d.CGQ_GCD_CODE like concat(concat('%', :cgqGcdCode),'%')");
        }
        if (StringUtils.isNotEmpty(device.getCgqTypeName())) {
            whereSql.append(" AND d.CGQ_TYPE_NAME like concat(concat('%', :cgqTypeName),'%')");
        }

        //拼接order条件
        if(StringUtils.isNotEmpty(device.getOrderBy())) {
            whereSql.append(" order by " + device.getOrderBy());
            switch (device.getDesc()){
                case ASC:
                    whereSql.append(" asc");
                    break;
                case DESC:
                    whereSql.append(" desc");
            }
        }else{
            whereSql.append(" order by d.tree_sorts,d.tree_level,d.tree_sort asc");
        }
        dataSql.append(whereSql);

        //创建本地sql查询实例
        Query dataQuery = entityManager.createNativeQuery(dataSql.toString(), Device.class);

        //设置查询参数
        if (StringUtils.isNotEmpty(device.getId())) {
            dataQuery.setParameter("deviceId", device.getId());
        }
        if (StringUtils.isNotEmpty(device.getPlanParentId())) {
            dataQuery.setParameter("planParentId", device.getPlanParentId());
        }
        if (StringUtils.isNotEmpty(device.getStationId())) {
            dataQuery.setParameter("stationId", device.getStationId());
        }
        if (StringUtils.isNotEmpty(device.getOfficeId())) {
            dataQuery.setParameter("officeId", device.getOfficeId());
        }
        if (StringUtils.isNotEmpty(device.getCgqGcdCode())) {
            dataQuery.setParameter("cgqGcdCode", device.getCgqGcdCode());
        }
        if (StringUtils.isNotEmpty(device.getCgqTypeName())) {
            dataQuery.setParameter("cgqTypeName", device.getCgqTypeName());
        }

        return dataQuery.getResultList();
    }
}
