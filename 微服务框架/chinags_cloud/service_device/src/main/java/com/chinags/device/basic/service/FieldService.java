package com.chinags.device.basic.service;

import com.chinags.common.entity.PageResult;
import com.chinags.common.service.BaseService;
import com.chinags.common.utils.StringUtils;
import com.chinags.device.basic.dao.DerviceOfficeDao;
import com.chinags.device.basic.dao.DerviceOfficePbDao;
import com.chinags.device.basic.dao.FieldDao;
import com.chinags.device.basic.entity.DerviceOffice;
import com.chinags.device.basic.entity.DerviceOfficePb;
import com.chinags.device.basic.entity.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class FieldService extends BaseService<Field> {
    @Autowired
    private FieldDao fieldDao;

    @Autowired
    private DerviceOfficeDao derviceOfficeDao;

    @Autowired
    private DerviceOfficePbDao derviceOfficePbDao;

    /**
     * 查询全部列表
     * @return
     */
    public List<Field> findAll(Field field) {
        Example<Field> ex = Example.of(field);
        return fieldDao.findAll(ex);
    }

    /**
     * 查询全部列表
     * @return
     */
    public List<Field> getfindAll(Field field) {
        Example<Field> ex = Example.of(field);
        List<Field> fields = fieldDao.findAll(ex);
        if(field.getFieldGroup()!=null){
            //public field
//            List<Field> fieldPublic = fieldDao.getFieldsByFieldGroupIsNull();
            //Add
//            fields.addAll(fieldPublic);
            //public office field
            DerviceOffice one = derviceOfficeDao.getAreaByOfficeCode(field.getFieldGroup());
            if(one.getFieldGroup()!=null){
                DerviceOfficePb derviceOfficePb = derviceOfficePbDao.getOne(one.getFieldGroup());
                String[] split = derviceOfficePb.getChecked().split(",");
                List<String> ids = Arrays.asList(split);
                List<Field> fieldsByIdIn = fieldDao.getFieldsByIdIn(ids);
                fields.addAll(fieldsByIdIn);
                //treeset去重
                Set<Field> set = new TreeSet<>(Comparator.comparing(Field::getId));
                set.addAll(fields);
                fields.clear();
                fields.addAll(set);
            }
        }

        return fields;
    }
    /**
     * 获得代码字段信息 by id
     * @param id
     * @return
     */
    public Field getFieldById(String id){
        return fieldDao.getFieldById(id);
    }

    /**
     * 查询全部列表
     * @return field分页数据
     */
    public PageResult find(Field field) {
        PageRequest pageable;
        if(field.getOrderBy()==null|| "".equals(field.getOrderBy())) {
            pageable = PageRequest.of(field.getPageNo(), field.getPageSize(), Sort.Direction.DESC,"updateDate");
        } else {
            pageable = PageRequest.of(field.getPageNo(), field.getPageSize(), field.getDesc(), field.getOrderBy());
        }
        //数据简单转换，转换为前台框架所需要分页json
        return PageResult.getPageResult(fieldDao.findAll(
                (Specification<Field>) (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (field.getFieldName() !=null && !"".equals(field.getFieldName())) {
                        predicates.add(cb.like(root.get("fieldName").as(String.class), "%" + field.getFieldName() + "%"));
                    }
                    if (field.getFieldCode() !=null && !"".equals(field.getFieldCode())) {
                        predicates.add(cb.like(root.get("fieldCode").as(String.class), "%" + field.getFieldCode() + "%"));
                    }
                    if (field.getFieldType() != null && !field.getFieldType().equals("")) {
                        predicates.add(cb.equal(root.get("fieldType").as(String.class), field.getFieldType()));
                    }
                    if (field.getFieldClass() != null && !field.getFieldClass().equals("")) {
                        predicates.add(cb.equal(root.get("fieldClass").as(String.class), field.getFieldClass()));
                    }
                    if (field.getFieldGroup() != null && !field.getFieldGroup().equals("")) {
                        predicates.add(cb.equal(root.get("fieldGroup").as(String.class), field.getFieldGroup()));
                    }
                    if (field.getFieldGroup() != null && !field.getFieldGroup().equals("")) {
                        predicates.add(cb.equal(root.get("fieldGroup").as(String.class), field.getFieldGroup()));
                    }
                    if (field.getFieldPb() != null && !field.getFieldPb().equals("")) {
                        predicates.add(cb.equal(root.get("fieldPb").as(String.class), field.getFieldPb()));
                    }else{
                        predicates.add(cb.notEqual(root.get("fieldPb").as(String.class), Field.STATUS_DELETE));
                    }
                    predicates.add(cb.notEqual(root.get("status").as(String.class), Field.STATUS_DELETE));
                    Predicate[] pre = new Predicate[predicates.size()];
                    query.where(predicates.toArray(pre));
                    return cb.and(predicates.toArray(pre));
                },pageable));
    }

    /**
     * 保存代码字段
     * @param field 代码字段数据
     */
    @Transactional(rollbackOn = Exception.class)
    public void save(Field field){
        fieldDao.save(field);
    }

    /**
     * 删除代码字段
     * @param id
     * @return
     */
    public Field delete(String id) {
        Field field = fieldDao.getFieldById(id);
        field.setStatus(Field.STATUS_DELETE);
        return fieldDao.save(field);
    }

    /**
     * 获取树形结构
     * @param deviceId 设备id
     * @return Office集合
     */
    public List<Field> treeData(String deviceId) {

        return fieldDao.findAll((Specification<Field>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("status").as(String.class), 0));
            if(StringUtils.isNotEmpty(deviceId)){
                predicates.add(cb.equal(root.get("fieldGroup").as(String.class), deviceId));
            }
            Predicate[] pre = new Predicate[predicates.size()];
            query.where(predicates.toArray(pre));
            return cb.and(predicates.toArray(pre));
        });
    }

    public List<Field> selectByDeviceClass(String deviceClass) {
        return fieldDao.selectByDeviceClass(deviceClass);
    }
}
