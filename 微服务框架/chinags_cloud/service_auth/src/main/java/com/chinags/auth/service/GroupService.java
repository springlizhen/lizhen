package com.chinags.auth.service;

import com.chinags.auth.dao.GroupDao;
import com.chinags.auth.entity.Group;
import com.chinags.auth.entity.Post;
import com.chinags.common.entity.PageResult;
import com.chinags.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Zhang
 */
@Service
public class GroupService extends BaseService<Group>{

    @Autowired
    private GroupDao groupDao;

    public Group getId(String groupCode){
        return groupDao.getFirstById(groupCode);
    }

    /**
     * 查询全部列表
     * @return office分页数据
     */
    public PageResult find(Group group) {
        PageRequest pageable;
        if(group.getOrderBy()==null|| "".equals(group.getOrderBy())) {
            pageable = PageRequest.of(group.getPageNo(), group.getPageSize(),Sort.Direction.ASC,"groupSort");
        } else {
            pageable = PageRequest.of(group.getPageNo(), group.getPageSize(), group.getDesc(), group.getOrderBy());
        }
        //数据简单转换，转换为前台框架所需要分页json
        return PageResult.getPageResult(groupDao.findAll(
                (Specification<Group>) (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (group.getGroupName() != null && !"".equals(group.getGroupName())) {
                        predicates.add(cb.like(root.get("groupName").as(String.class), "%" + group.getGroupName() + "%"));
                    }
                    predicates.add(cb.notEqual(root.get("status").as(String.class), Group.STATUS_DELETE));
                    Predicate[] pre = new Predicate[predicates.size()];
                    query.where(predicates.toArray(pre));
                    return cb.and(predicates.toArray(pre));
                },pageable));
    }

    /**
     * 保存机构
     * @param group 岗位数据
     */
    @Transactional(rollbackOn = Exception.class)
    public void save(Group group){
        groupDao.save(group);
    }

    /**
     * 删除机构
     * @param group group实体
     */
    @Transactional
    public Boolean delete(Group group){
        groupDao.delete(group);
        return true;
    }

}
