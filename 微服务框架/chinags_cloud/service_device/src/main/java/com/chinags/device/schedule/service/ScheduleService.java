package com.chinags.device.schedule.service;

import com.chinags.common.entity.PageResult;
import com.chinags.common.lang.StringUtils;
import com.chinags.device.schedule.dao.ScheduleDao;
import com.chinags.device.schedule.entity.Schedule;
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
import java.util.ArrayList;
import java.util.List;

/**
 * 工程进度管理工程进度接口
 * @Author : Mark_wang
 * @Date : 2019-9-9
 **/
@Service
public class ScheduleService {

    @Autowired
    private ScheduleDao scheduleDao;

    /**
     * 保存工程进度
     * @param schedule
     */
    public void save(Schedule schedule) {
        scheduleDao.save(schedule);
    }

    /**
     * 根据id删除工程进度
     * @param id
     */
    public void deleteById(String id) {
        scheduleDao.deleteById(id);
    }

    /**
     * 根据id查询工程进度
     * @param id
     * @return
     */
    public Schedule selectById(String id) {
        return scheduleDao.selectById(id);
    }

    /**
     * 分页排序获取工程进度列表
     * @param schedule
     * @return
     */
    public PageResult<Schedule> findByPage(Schedule schedule) {
        PageRequest pageable;
        if (StringUtils.isNotEmpty(schedule.getOrderBy())) {
            pageable = PageRequest.of(schedule.getPageNo(), schedule.getPageSize(), Sort.Direction.DESC,schedule.getOrderBy());
        } else {
            pageable = PageRequest.of(schedule.getPageNo(), schedule.getPageSize(), Sort.Direction.DESC,"createDate");
        }
        Page<Schedule> page = scheduleDao.findAll(new Specification<Schedule>() {
            @Override
            public Predicate toPredicate(Root<Schedule> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtils.isNotEmpty(schedule.getName())) {
                    predicates.add(cb.like(root.get("name"),"%" + schedule.getName() + "%"));
                }
                if (schedule.getYear() != 0) {
                    predicates.add(cb.equal(root.get("year").as(Integer.class), schedule.getYear()));
                }
                Predicate[] pre = new Predicate[predicates.size()];
                query.where(predicates.toArray(pre));
                return cb.and(predicates.toArray(pre));
            }
        }, pageable);
        PageResult<Schedule> result = PageResult.getPageResult(page);
        return result;
    }

    /**
     * 获取所有的年度
     * @return
     */
    public List<String> findAllYear() {
        return scheduleDao.findAllYear();
    }

    /**
     * 根据id增加完成金额
     * @param money
     * @param id
     */
    public void addEndMoneyById(Double money, String id) {
        scheduleDao.addEndMoneyById(money, id);
    }

    /**
     * 根据id减完成金额
     * @param money
     * @param id
     */
    public void redEndMoneyById(Double money, String id) {
        scheduleDao.redEndMoneyById(money, id);
    }
}
