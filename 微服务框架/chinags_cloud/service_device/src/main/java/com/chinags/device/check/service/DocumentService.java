package com.chinags.device.check.service;

import com.chinags.common.entity.PageResult;
import com.chinags.common.service.BaseService;
import com.chinags.device.check.dao.DocumentDao;
import com.chinags.device.check.dao.StandardDao;
import com.chinags.device.check.entity.Document;
import com.chinags.device.check.entity.Standard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


@Service
public class DocumentService extends BaseService<Document>{

    @Autowired
    private DocumentDao documentDao;



    public void save(Document document){
        documentDao.save(document);

    }
    public Document selectDocument(String id){
        return documentDao.getDocuementById(id);

    }



    public void delete(Document document ){
        documentDao.delete(document);
    }

    public PageResult find(Document document) {
        PageRequest pageable;
        if(document.getOrderBy()==null|| "".equals(document.getOrderBy())) {
            pageable = PageRequest.of(document.getPageNo(), document.getPageSize(), Sort.Direction.ASC,"createDate");
        } else {
            pageable = PageRequest.of(document.getPageNo(), document.getPageSize(), document.getDesc(), document.getOrderBy());
        }
        //数据简单转换，转换为前台框架所需要分页json
        return PageResult.getPageResult(documentDao.findAll(
                (Specification<Document>) (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (document.getId() !=null && !"".equals(document.getId())) {
                        predicates.add(cb.like(root.get("id").as(String.class), "%" + document.getId() + "%"));
                    }
                    if (document.getDocumentId() !=null && !"".equals(document.getDocumentId())) {
                        predicates.add(cb.like(root.get("documentId").as(String.class), "%" + document.getDocumentId() + "%"));
                    }
                    if (document.getDocumentName() !=null && !"".equals(document.getDocumentName())) {
                        predicates.add(cb.like(root.get("documentName").as(String.class), "%" + document.getDocumentName() + "%"));
                    }


                    Predicate[] pre = new Predicate[predicates.size()];
                    query.where(predicates.toArray(pre));
                    return cb.and(predicates.toArray(pre));
                },pageable));
    }


}
