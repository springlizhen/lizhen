package com.chinags.archives.service;

import com.chinags.archives.dao.ClerkDocumentDao;
import com.chinags.archives.entity.ClerkDocument;
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

/**
 * @author XieWenqing
 * @date 2019/11/20 上午 10:30
 */
@Service
public class ClerkDocumentService  {
    @Autowired
    private ClerkDocumentDao clerkDocumentDao;

    /**
     * 根据条件分页查询公共档案
     * @return
     */
    public PageResult<ClerkDocument> myClerkDocumentList(ClerkDocument clerkDocument) {
        PageRequest pageable;
        if (StringUtils.isNotEmpty(clerkDocument.getOrderBy())) {
            String orderBy = clerkDocument.getOrderBy().replaceAll("%20", ",");
            String[] str = orderBy.split(",");
            pageable = PageRequest.of(clerkDocument.getPageNo()==null?0:clerkDocument.getPageNo(), clerkDocument.getPageSize()==null?20: clerkDocument.getPageSize(), str[1].equals("desc")?Sort.Direction.DESC:Sort.Direction.ASC, str[0]);
        } else {
            if (clerkDocument.getPageNo() == null || clerkDocument.getPageSize() == null)
                pageable = PageRequest.of(0, 20, Sort.Direction.DESC,"createDate");
            else
                pageable = PageRequest.of(clerkDocument.getPageNo(), clerkDocument.getPageSize(), Sort.Direction.DESC,"createDate");
        }
        Page<ClerkDocument> page = clerkDocumentDao.findAll(new Specification<ClerkDocument>() {
            @Override
            public Predicate toPredicate(Root<ClerkDocument> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal(root.get("docClass"),"gongcheng"));
                if(StringUtils.isNotEmpty(clerkDocument.getItem())){
                    predicates.add(cb.like(root.get("item").as(String.class), "%" + clerkDocument.getItem() + "%"));
                }
                if(StringUtils.isNotEmpty(clerkDocument.getCaseNo())){
                    predicates.add(cb.like(root.get("caseNo").as(String.class), "%" + clerkDocument.getCaseNo() + "%"));
                }
                if(StringUtils.isNotEmpty(clerkDocument.getCatalogNo())){
                    predicates.add(cb.like(root.get("catalogNo").as(String.class), "%" + clerkDocument.getCatalogNo() + "%"));
                }
                if(StringUtils.isNotEmpty(clerkDocument.getArchivesNo())){
                    predicates.add(cb.like(root.get("archivesNo").as(String.class), "%" + clerkDocument.getArchivesNo() + "%"));
                }
                if(StringUtils.isNotEmpty(clerkDocument.getBoxNo())){
                    predicates.add(cb.like(root.get("boxNo").as(String.class), "%" + clerkDocument.getBoxNo() + "%"));
                }
                if(StringUtils.isNotEmpty(clerkDocument.getStorageTime())){
                    predicates.add(cb.like(root.get("storageTime").as(String.class), "%" + clerkDocument.getStorageTime() + "%"));
                }
                if(StringUtils.isNotEmpty(clerkDocument.getSecretLevel())){
                    predicates.add(cb.like(root.get("secretLevel").as(String.class), "%" + clerkDocument.getSecretLevel() + "%"));
                }
                if(StringUtils.isNotEmpty(clerkDocument.getTitle())){
                    predicates.add(cb.like(root.get("title").as(String.class), "%" + clerkDocument.getTitle() + "%"));
                }
                if(StringUtils.isNotEmpty(clerkDocument.getResponsiblePerson())){
                    predicates.add(cb.like(root.get("responsiblePerson").as(String.class), "%" + clerkDocument.getResponsiblePerson() + "%"));
                }
                if(clerkDocument.getYear() != null){
                    predicates.add(cb.equal(root.get("year").as(Date.class), clerkDocument.getYear()));
                }
                if(StringUtils.isNotEmpty(clerkDocument.getRemarks())){
                    predicates.add(cb.like(root.get("remarks").as(String.class), "%" + clerkDocument.getRemarks() + "%"));
                }
                Predicate[] pre = new Predicate[predicates.size()];
                query.where(predicates.toArray(pre));
                return cb.and(predicates.toArray(pre));
            }
        }, pageable);
        PageResult<ClerkDocument> result = PageResult.getPageResult(page);
        return result;
    }

    /**
     * 保存工程档案案卷著录
     */
    public void save(ClerkDocument clerkDocument) {
        clerkDocumentDao.save(clerkDocument);
    }

    /**
     * 根据id查询工程档案案卷著录
     * @param id
     * @return
     */
    public ClerkDocument selectById(String id) {
        return clerkDocumentDao.selectById(id);
    }

    /**
     * 根据id删除工程档案案卷著录
     */
    public void deleteById(String id) {
        clerkDocumentDao.deleteById(id);
    }

}
