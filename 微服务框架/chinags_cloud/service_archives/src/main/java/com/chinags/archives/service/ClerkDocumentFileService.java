package com.chinags.archives.service;

import com.chinags.archives.dao.ClerkDocumentFileDao;
import com.chinags.archives.entity.ClerkDocumentFile;
import com.chinags.common.entity.PageResult;
import com.chinags.common.lang.StringUtils;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author XieWenqing
 * @date 2019/9/29 下午 2:47
 */
@Service
public class ClerkDocumentFileService {
    @Autowired
    private ClerkDocumentFileDao clerkDocumentFileDao;

    /**
     * 根据条件分页查询公共档案
     * @return
     */
    public PageResult<ClerkDocumentFile> findClerkDocumentFilesByIsPublic(ClerkDocumentFile clerkDocumentFile){
        PageRequest pageable;
        if (StringUtils.isNotEmpty(clerkDocumentFile.getOrderBy())) {
            pageable = PageRequest.of(clerkDocumentFile.getPageNo(), clerkDocumentFile.getPageSize(), Sort.Direction.DESC,clerkDocumentFile.getOrderBy(), "id");
        } else {
            if (clerkDocumentFile.getPageNo() == null || clerkDocumentFile.getPageSize() == null)
                pageable = PageRequest.of(0, 20, Sort.Direction.DESC,"id");
            else
                pageable = PageRequest.of(clerkDocumentFile.getPageNo(), clerkDocumentFile.getPageSize(), Sort.Direction.DESC,"id");
        }
        Page<ClerkDocumentFile> page = clerkDocumentFileDao.findAll(new Specification<ClerkDocumentFile>() {
            @Override
            public Predicate toPredicate(Root<ClerkDocumentFile> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal(root.get("isPublic"),"是"));
                Predicate[] pre = new Predicate[predicates.size()];
                query.where(predicates.toArray(pre));
                return cb.and(predicates.toArray(pre));
            }
        }, pageable);
        PageResult<ClerkDocumentFile> result = PageResult.getPageResult(page);
        return result;
    }

    /**
     * 根据id查询档案
     * @param id
     * @return
     */
    public ClerkDocumentFile findById(String id) {
        return clerkDocumentFileDao.selectById(id);
    }


    /**
     * 根据工程档案案卷著录id查询卷内文件list
     * @param id
     * @return
     */
    public List<ClerkDocumentFile> findClerkDocumentFileByCkId(String id){
        return clerkDocumentFileDao.findClerkDocumentFileByCkId(id);
    }

    /**
     * 查询当前登录用户卷内文件
     * @return
     */
    public PageResult<ClerkDocumentFile> fileListData(ClerkDocumentFile clerkDocumentFile, String userCode) {
        PageRequest pageable;
        if (StringUtils.isNotEmpty(clerkDocumentFile.getOrderBy())) {
            String orderBy = clerkDocumentFile.getOrderBy().replaceAll("%20", ",");
            String[] str = orderBy.split(",");
            pageable = PageRequest.of(clerkDocumentFile.getPageNo()==null?0:clerkDocumentFile.getPageNo(), clerkDocumentFile.getPageSize()==null?20: clerkDocumentFile.getPageSize(), str[1].equals("desc")?Sort.Direction.DESC:Sort.Direction.ASC, str[0]);
        } else {
            if (clerkDocumentFile.getPageNo() == null || clerkDocumentFile.getPageSize() == null)
                pageable = PageRequest.of(0, 20, Sort.Direction.DESC,"sort");
            else
                pageable = PageRequest.of(clerkDocumentFile.getPageNo(), clerkDocumentFile.getPageSize(), Sort.Direction.DESC,"sort");
        }
        Page<ClerkDocumentFile> page = clerkDocumentFileDao.findAll(new Specification<ClerkDocumentFile>() {
            @Override
            public Predicate toPredicate(Root<ClerkDocumentFile> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal(root.get("createBy"), userCode));
                predicates.add(cb.equal(root.get("docClass"), "gongcheng"));
                if(StringUtils.isEmpty(clerkDocumentFile.getClerkDocumentId())){
                    if(!"1".equals(clerkDocumentFile.getClerkDocumentId2())){
                        predicates.add(cb.equal(root.get("clerkDocumentId"), clerkDocumentFile.getClerkDocumentId2()));
                    }
                }else{
                    predicates.add(cb.equal(root.get("clerkDocumentId"), clerkDocumentFile.getClerkDocumentId()));
                }
                if(StringUtils.isNotEmpty(clerkDocumentFile.getTitle())){
                    predicates.add(cb.like(root.get("title").as(String.class), "%" + clerkDocumentFile.getTitle() + "%"));
                }
                if(StringUtils.isNotEmpty(clerkDocumentFile.getArchivesNo())){
                    predicates.add(cb.like(root.get("archivesNo").as(String.class), "%" + clerkDocumentFile.getArchivesNo() + "%"));
                }
                if(StringUtils.isNotEmpty(clerkDocumentFile.getResponsiblePerson())){
                    predicates.add(cb.like(root.get("responsiblePerson").as(String.class), "%" + clerkDocumentFile.getResponsiblePerson() + "%"));
                }
                if(StringUtils.isNotEmpty(clerkDocumentFile.getDocumentType())){
                    predicates.add(cb.equal(root.get("documentType").as(String.class), clerkDocumentFile.getDocumentType()));
                }
                if(StringUtils.isNotEmpty(clerkDocumentFile.getBoxNo())){
                    predicates.add(cb.like(root.get("boxNo").as(String.class), "%" + clerkDocumentFile.getBoxNo() + "%"));
                }
                if(StringUtils.isNotEmpty(clerkDocumentFile.getRemarks())){
                    predicates.add(cb.like(root.get("remarks").as(String.class), "%" + clerkDocumentFile.getRemarks() + "%"));
                }
                if(StringUtils.isNotEmpty(clerkDocumentFile.getStorageTime())){
                    predicates.add(cb.equal(root.get("storageTime").as(String.class), clerkDocumentFile.getStorageTime()));
                }
                if(StringUtils.isNotEmpty(clerkDocumentFile.getIsPublic())){
                    predicates.add(cb.equal(root.get("isPublic").as(String.class), clerkDocumentFile.getIsPublic()));
                }
                if(clerkDocumentFile.getStartDate_gte() != null && clerkDocumentFile.getStartDate_lte() != null){
                    predicates.add(cb.between(root.get("startDate").as(Date.class), clerkDocumentFile.getStartDate_gte(), clerkDocumentFile.getStartDate_lte()));
                }
                if(clerkDocumentFile.getEndDate_gte() != null && clerkDocumentFile.getEndDate_lte() != null){
                    predicates.add(cb.between(root.get("endDate").as(Date.class), clerkDocumentFile.getEndDate_gte(), clerkDocumentFile.getEndDate_lte()));
                }
                if(clerkDocumentFile.getStartDate_gte() != null && clerkDocumentFile.getStartDate_lte() == null){
                    predicates.add(cb.greaterThanOrEqualTo(root.get("startDate").as(Date.class), clerkDocumentFile.getStartDate_gte()));
                }
                if(clerkDocumentFile.getStartDate_gte() == null && clerkDocumentFile.getStartDate_lte() != null){
                    predicates.add(cb.lessThanOrEqualTo(root.get("startDate").as(Date.class), clerkDocumentFile.getStartDate_lte()));
                }
                if(clerkDocumentFile.getEndDate_gte() != null && clerkDocumentFile.getEndDate_lte() == null){
                    predicates.add(cb.greaterThanOrEqualTo(root.get("endDate").as(Date.class), clerkDocumentFile.getEndDate_gte()));
                }
                if(clerkDocumentFile.getEndDate_gte() == null && clerkDocumentFile.getEndDate_lte() != null){
                    predicates.add(cb.lessThanOrEqualTo(root.get("endDate").as(Date.class), clerkDocumentFile.getEndDate_lte()));
                }

                Predicate[] pre = new Predicate[predicates.size()];
                query.where(predicates.toArray(pre));
                return cb.and(predicates.toArray(pre));
            }
        }, pageable);
        PageResult<ClerkDocumentFile> result = PageResult.getPageResult(page);
        return result;
    }

    /**
     * 查询当前登录用户卷内文件
     * @return
     */
    public PageResult<ClerkDocumentFile> fileAllListData(ClerkDocumentFile clerkDocumentFile) {
        PageRequest pageable;
        if (StringUtils.isNotEmpty(clerkDocumentFile.getOrderBy())) {
            String orderBy = clerkDocumentFile.getOrderBy().replaceAll("%20", ",");
            String[] str = orderBy.split(",");
            pageable = PageRequest.of(clerkDocumentFile.getPageNo()==null?0:clerkDocumentFile.getPageNo(), clerkDocumentFile.getPageSize()==null?20: clerkDocumentFile.getPageSize(), str[1].equals("desc")?Sort.Direction.DESC:Sort.Direction.ASC, str[0]);
        } else {
            if (clerkDocumentFile.getPageNo() == null || clerkDocumentFile.getPageSize() == null)
                pageable = PageRequest.of(0, 20, Sort.Direction.DESC,"sort");
            else
                pageable = PageRequest.of(clerkDocumentFile.getPageNo(), clerkDocumentFile.getPageSize(), Sort.Direction.DESC,"sort");
        }
        Page<ClerkDocumentFile> page = clerkDocumentFileDao.findAll(new Specification<ClerkDocumentFile>() {
            @Override
            public Predicate toPredicate(Root<ClerkDocumentFile> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal(root.get("docClass"), "gongcheng"));
                if(StringUtils.isEmpty(clerkDocumentFile.getClerkDocumentId())){
                    if(!"1".equals(clerkDocumentFile.getClerkDocumentId2())){
                        predicates.add(cb.equal(root.get("clerkDocumentId"), clerkDocumentFile.getClerkDocumentId2()));
                    }
                }else{
                    predicates.add(cb.equal(root.get("clerkDocumentId"), clerkDocumentFile.getClerkDocumentId()));
                }
                if(StringUtils.isNotEmpty(clerkDocumentFile.getTitle())){
                    predicates.add(cb.like(root.get("title").as(String.class), "%" + clerkDocumentFile.getTitle() + "%"));
                }
                if(StringUtils.isNotEmpty(clerkDocumentFile.getArchivesNo())){
                    predicates.add(cb.like(root.get("archivesNo").as(String.class), "%" + clerkDocumentFile.getArchivesNo() + "%"));
                }
                if(StringUtils.isNotEmpty(clerkDocumentFile.getResponsiblePerson())){
                    predicates.add(cb.like(root.get("responsiblePerson").as(String.class), "%" + clerkDocumentFile.getResponsiblePerson() + "%"));
                }
                if(StringUtils.isNotEmpty(clerkDocumentFile.getDocumentType())){
                    predicates.add(cb.equal(root.get("documentType").as(String.class), clerkDocumentFile.getDocumentType()));
                }
                if(StringUtils.isNotEmpty(clerkDocumentFile.getBoxNo())){
                    predicates.add(cb.like(root.get("boxNo").as(String.class), "%" + clerkDocumentFile.getBoxNo() + "%"));
                }
                if(StringUtils.isNotEmpty(clerkDocumentFile.getRemarks())){
                    predicates.add(cb.like(root.get("remarks").as(String.class), "%" + clerkDocumentFile.getRemarks() + "%"));
                }
                if(StringUtils.isNotEmpty(clerkDocumentFile.getStorageTime())){
                    predicates.add(cb.equal(root.get("storageTime").as(String.class), clerkDocumentFile.getStorageTime()));
                }
                if(StringUtils.isNotEmpty(clerkDocumentFile.getIsPublic())){
                    predicates.add(cb.equal(root.get("isPublic").as(String.class), clerkDocumentFile.getIsPublic()));
                }
                if(clerkDocumentFile.getStartDate_gte() != null && clerkDocumentFile.getStartDate_lte() != null){
                    predicates.add(cb.between(root.get("startDate").as(Date.class), clerkDocumentFile.getStartDate_gte(), clerkDocumentFile.getStartDate_lte()));
                }
                if(clerkDocumentFile.getEndDate_gte() != null && clerkDocumentFile.getEndDate_lte() != null){
                    predicates.add(cb.between(root.get("endDate").as(Date.class), clerkDocumentFile.getEndDate_gte(), clerkDocumentFile.getEndDate_lte()));
                }
                if(clerkDocumentFile.getStartDate_gte() != null && clerkDocumentFile.getStartDate_lte() == null){
                    predicates.add(cb.greaterThanOrEqualTo(root.get("startDate").as(Date.class), clerkDocumentFile.getStartDate_gte()));
                }
                if(clerkDocumentFile.getStartDate_gte() == null && clerkDocumentFile.getStartDate_lte() != null){
                    predicates.add(cb.lessThanOrEqualTo(root.get("startDate").as(Date.class), clerkDocumentFile.getStartDate_lte()));
                }
                if(clerkDocumentFile.getEndDate_gte() != null && clerkDocumentFile.getEndDate_lte() == null){
                    predicates.add(cb.greaterThanOrEqualTo(root.get("endDate").as(Date.class), clerkDocumentFile.getEndDate_gte()));
                }
                if(clerkDocumentFile.getEndDate_gte() == null && clerkDocumentFile.getEndDate_lte() != null){
                    predicates.add(cb.lessThanOrEqualTo(root.get("endDate").as(Date.class), clerkDocumentFile.getEndDate_lte()));
                }

                Predicate[] pre = new Predicate[predicates.size()];
                query.where(predicates.toArray(pre));
                return cb.and(predicates.toArray(pre));
            }
        }, pageable);
        PageResult<ClerkDocumentFile> result = PageResult.getPageResult(page);
        return result;
    }

    /**
     * 保存工程档案卷内文件
     */
    public void save(ClerkDocumentFile clerkDocumentFile) {
        clerkDocumentFileDao.save(clerkDocumentFile);
    }

    /**
     * 根据id删除工程档案卷内文件
     */
    public void deleteById(String id) {
        clerkDocumentFileDao.deleteById(id);
    }

    /**
     * 根据卷内文件id和案卷题名查找记录，案卷标题可为空
     */
    public List<Map<String, Object>> findClerkDocumentFile(String id, String title){
        return clerkDocumentFileDao.findClerkDocumentFile(id, title);
    }
}
