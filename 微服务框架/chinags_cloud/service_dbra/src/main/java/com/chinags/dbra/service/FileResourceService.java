package com.chinags.dbra.service;

import com.chinags.common.entity.PageResult;
import com.chinags.common.lang.StringUtils;
import com.chinags.dbra.dao.FileResourceDao;
import com.chinags.dbra.entity.FileResource;
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
 * @Author : Mark_wang
 * @Date : 2019-8-29
 **/
@Service
public class FileResourceService {

    @Autowired
    private FileResourceDao fileResourceDao;

    /**
     * 分页查询档案API接口
     * @param entity
     * @return
     */
    public PageResult<FileResource> findAllByPage(FileResource entity, String type) {
        PageRequest pageable;
        Sort sort;
        if (StringUtils.isNotEmpty(entity.getOrderBy())) {
            sort = new Sort(Sort.Direction.DESC,entity.getOrderBy());
        } else {
            sort = new Sort(Sort.Direction.DESC, "createDate");
        }
        pageable = PageRequest.of(entity.getPageNo(), entity.getPageSize(), sort);
        Page<FileResource> page = fileResourceDao.findAll(new Specification<FileResource>() {
            @Override
            public Predicate toPredicate(Root<FileResource> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (com.chinags.common.lang.StringUtils.isNotEmpty(entity.getName())) {
                    predicates.add(cb.like(root.get("name"),"%" + entity.getName() + "%"));
                }
                if (com.chinags.common.lang.StringUtils.isNotEmpty(type)) {
                    predicates.add(cb.equal(root.get("type"), type));
                }
                Predicate[] pre = new Predicate[predicates.size()];
                query.where(predicates.toArray(pre));
                return cb.and(predicates.toArray(pre));
            }
        }, pageable);
        return PageResult.getPageResult(page);
    }

    /**
     * 保存
     * @param fileResource
     * @return
     */
    public FileResource save(FileResource fileResource) {
        return fileResourceDao.save(fileResource);
    }

    /**
     * 根据id查询档案API服务
     * @param id
     * @return
     */
    public FileResource selectById(String id) {
        return fileResourceDao.selectById(id);
    }

    /**
     * 查询所有发布的档案API
     * @return
     */
    public List<FileResource> findAll() {
        return fileResourceDao.findAll();
    }

    /**
     * 增加档案API的申请次数
     * @param id
     */
    public void updateApplyNumById(String id) {
        fileResourceDao.updateApplyNumById(id);
    }

    /**
     * 增加档案下载次数
     * @param id
     */
    public void updateDownNumById(String id) {
        fileResourceDao.updateDownNumById(id);
    }
    /**
     * 根据id删除档案服务
     * @param id
     */
    public void deleteById(String id) {
        fileResourceDao.deleteById(id);
    }

    /**
     * 获取去重后的文件服务分类
     * @return
     */
    public List<String> findAllType() {
       return fileResourceDao.findAllType();
    }

    /**
     * 根据id修改开放状态
     * @param id
     * @param status
     */
    public void updateStatusById(String id, String status) {
        fileResourceDao.updateStatusById(id, status);
    }
}
