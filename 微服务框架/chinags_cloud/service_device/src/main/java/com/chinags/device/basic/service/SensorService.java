package com.chinags.device.basic.service;

import com.chinags.common.entity.PageResult;
import com.chinags.device.basic.dao.SensorDao;
import com.chinags.device.basic.entity.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class SensorService {
    @Autowired
    private SensorDao sensorDao;

    /**
     * 获得代码字段信息 by id
     * @param id
     * @return
     */
    public Sensor getSensorById(String id){
        return sensorDao.getSensorById(id);
    }

    /**
     * 查询全部列表
     * @return field分页数据
     */
    public PageResult find(Sensor sensor) {
        PageRequest pageable;
        if(sensor.getOrderBy()==null|| "".equals(sensor.getOrderBy())) {
            pageable = PageRequest.of(sensor.getPageNo(), sensor.getPageSize(), Sort.Direction.ASC,"createDate");
        } else {
            pageable = PageRequest.of(sensor.getPageNo(), sensor.getPageSize(), sensor.getDesc(), sensor.getOrderBy());
        }
        //数据简单转换，转换为前台框架所需要分页json
        return PageResult.getPageResult(sensorDao.findAll(
                (Specification<Sensor>) (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    Predicate[] pre = new Predicate[predicates.size()];
                    if (sensor.getFieldCode() !=null && !"".equals(sensor.getFieldCode())) {
                        predicates.add(cb.like(root.get("fieldCode").as(String.class), "%" + sensor.getFieldCode() + "%"));
                    }
                    predicates.add(cb.notEqual(root.get("status").as(String.class), 1));
                    query.where(predicates.toArray(pre));
                    return cb.and(predicates.toArray(pre));
                },pageable));
    }

    /**
     * 保存代码字段
     * @param sensor 代码字段数据
     */
    @Transactional(rollbackOn = Exception.class)
    public void save(Sensor sensor){
        sensorDao.save(sensor);
    }

    /**
     * 删除代码字段
     * @param id
     * @return
     */
    public Sensor delete(String id) {
        Sensor field = sensorDao.getSensorById(id);
        field.setStatus(Sensor.STATUS_DELETE);
        return sensorDao.save(field);
    }

}
