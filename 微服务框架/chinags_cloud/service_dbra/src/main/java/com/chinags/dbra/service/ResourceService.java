package com.chinags.dbra.service;

import com.chinags.common.entity.BaseEntity;
import com.chinags.common.entity.PageResult;
import com.chinags.common.lang.StringUtils;
import com.chinags.dbra.dao.ResourceDao;
import com.chinags.dbra.dao.ThemeDao;
import com.chinags.dbra.entity.DbTable;
import com.chinags.dbra.entity.OtherResource;
import com.chinags.dbra.entity.Resource;
import com.chinags.dbra.entity.Theme;
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
 * @author Mark_Wang
 * @date 2019/7/17
 **/
@Service
public class ResourceService {

    @Autowired
    private ResourceDao resourceDao;

    @Autowired
    private ThemeDao themeDao;

    /**
     * 分页查询所有的数据资源
     * @param entity
     * @return
     */
    public PageResult<Resource> findByPage(BaseEntity entity) {
        PageRequest pageable;
        Sort sort = new Sort(Sort.Direction.DESC,"createDate");
        pageable = PageRequest.of(entity.getPageNo(), entity.getPageSize(), sort);
        Page<Resource> page = resourceDao.findAll(pageable);
        return PageResult.getPageResult(page);
    }

    /**
     * 保存数据资源
     * @param resource
     * @return
     */
    public Resource save(Resource resource) {
        return resourceDao.save(resource);
    }

    /**
     * 根据id获取资源服务信息
     * @param id
     * @return
     */
    public Resource findById(String id) {
        return resourceDao.selectById(id);
    }

    /**
     * 根据id获取资源服务信息供前端显示
     * @param id
     * @return
     */
    public Resource findOneById(String id) {
        Resource resource = resourceDao.selectById(id);
        return resource;
    }

    /**
     * 根据数据库id获取资源服务信息
     * @param dbId
     * @return
     */
    public List<Resource> findByDbId(String dbId) {
        return resourceDao.selectByDbId(dbId);
    }

    /**
     * 根据数据表id获取资源服务信息
     * @param tbId
     * @return
     */
    public List<Resource> findByTbId(String tbId) {
        return resourceDao.selectByTbId(tbId);
    }

    /**
     * 根据id增加浏览次数
     * @param id
     */
    public void updateCatNumById(String id) {
        resourceDao.updateCatNumById(id);
    }

    /**
     * 根据ID增加下载次数
     * @param id
     */
    public void updateDownNumById(String id) {
        resourceDao.updateDownNumById(id);
    }

    /**
     * 根据主题分页查询
     * @param pageNo
     * @param pageSize
     * @param theme
     * @return
     */
    public PageResult<Resource> findByTheme(int pageNo, int pageSize, String orderBy, String theme) {
        PageRequest pageable;
        Sort sort;
        if (StringUtils.isNotEmpty(orderBy)) {
            sort = new Sort(Sort.Direction.DESC,orderBy);
        } else {
            sort = new Sort(Sort.Direction.DESC,"createBy");
        }
        pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Resource> page = resourceDao.findAll(new Specification<Resource>() {
            @Override
            public Predicate toPredicate(Root<Resource> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtils.isNotEmpty(theme)) {
                    predicates.add(cb.equal(root.get("theme").as(String.class), theme));
                }
                Predicate[] pre = new Predicate[predicates.size()];
                query.where(predicates.toArray(pre));
                return cb.and(predicates.toArray(pre));
            }
        }, pageable);
        return PageResult.getPageResult(page);
    }

    /**
     * 统计数据集数量
     * @return
     */
    public int countResource() {
        return resourceDao.countResource();
    }

    /**
     * 统计下载量
     * @return
     */
    public int sumDownNum() {
        return resourceDao.sumDownNum();
    }

    /**
     * 所有资源目录表接口
     * @return
     */
    public List<String> findAllTables() {
        return resourceDao.findAllTables();
    }

    /**
     * 根据id修改开放状态
     * @param id
     * @param status
     */
    public void updateStatusById(String id, String status) {
        resourceDao.updateStatusById(id, status);
    }

}
