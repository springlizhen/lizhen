package com.chinags.auth.service;

import com.chinags.auth.dao.OfficeDao;
import com.chinags.auth.dao.PostDao;
import com.chinags.auth.entity.Office;
import com.chinags.auth.entity.Post;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Zhang
 */
@Service
public class PostService extends BaseService<Post>{

    @Autowired
    private PostDao postDao;

    /**
     * 获取树形结构
     * @return Post集合
     */
    public List<Post> treeData(Post post){
        //菜单状态 正常
        post.setStatus("0");
        Sort sort = new Sort(Sort.Direction.ASC, "postSort");
        Example<Post> ex = Example.of(post);
        return postDao.findAll(ex, sort);
    }

    public Set<Post> inPostCode(String postCodes){
        return postDao.findByPostCodeIn(postCodes.split(","));
    }

    /**
     * 查询全部列表
     * @return office分页数据
     */
    public PageResult find(Post post) {
        PageRequest pageable;
        if(post.getOrderBy()==null|| "".equals(post.getOrderBy())) {
            pageable = PageRequest.of(post.getPageNo(), post.getPageSize(),Sort.Direction.ASC,"postSort");
        } else {
            pageable = PageRequest.of(post.getPageNo(), post.getPageSize(), post.getDesc(), post.getOrderBy());
        }
        //数据简单转换，转换为前台框架所需要分页json
        return PageResult.getPageResult(postDao.findAll(
                (Specification<Post>) (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (post.getPostCode() != null && !"".equals(post.getPostCode())) {
                        predicates.add(cb.like(root.get("postCode").as(String.class), "%" + post.getPostCode() + "%"));
                    }
                    if (post.getPostName() != null && !"".equals(post.getPostName())) {
                        predicates.add(cb.like(root.get("postName").as(String.class), "%" + post.getPostName() + "%"));
                    }
                    if (post.getPostType() != null && !"".equals(post.getPostType())) {
                        predicates.add(cb.equal(root.get("postType").as(String.class), post.getPostType()));
                    }
                    if (post.getStatus() != null && !post.getStatus().equals("")) {
                        predicates.add(cb.equal(root.get("status").as(String.class), post.getStatus()));
                    }
                    if (post.getOfficeCode() != null && !"".equals(post.getOfficeCode().getOfficeCode())) {
                        Join<Post, Office> join = root.join("officeCode", JoinType.LEFT);
                        predicates.add(cb.equal(join.get("officeCode"), post.getOfficeCode().getOfficeCode()));
                    }
                    predicates.add(cb.notEqual(root.get("status").as(String.class), Post.STATUS_DELETE));
                    Predicate[] pre = new Predicate[predicates.size()];
                    query.where(predicates.toArray(pre));
                    return cb.and(predicates.toArray(pre));
                },pageable));
    }

    /**
     * 保存机构
     * @param post 岗位数据
     */
    @Transactional(rollbackOn = Exception.class)
    public void save(Post post){
        postDao.save(post);
    }

    /**
     * 删除机构
     * @param post post实体
     */
    @Transactional
    public Boolean delete(Post post){
        postDao.deleteStatus(post.getPostCode());
        return true;
    }

    /**
     * 获得机构信息
     * @param postCode post code
     * @return 机构信息
     */
    public Post getAreaByPostCode(String postCode){
        return postDao.getAreaByPostCode(postCode);
    }

    /**
     * 停用岗位
     * @param id post code
     */
    public Result disable(String id) {
        Post post = getAreaByPostCode(id);
        post.setStatus("2");
        postDao.save(post);
        return new Result(true, StatusCode.OK, "停用岗位'"+post.getPostName() +"'成功");
    }

    /**
     * 启用岗位
     * @param id office code
     */
    public String enable(String id) {
        Post post = getAreaByPostCode(id);
        post.setStatus("0");
        postDao.save(post);
        return "启用岗位'"+post.getPostName()+"'成功";
    }

    public String checkPostName(String postName) {
        Long count = postDao.getAreaByPostName(postName);
        return count==0?"true":"false";
    }
}
