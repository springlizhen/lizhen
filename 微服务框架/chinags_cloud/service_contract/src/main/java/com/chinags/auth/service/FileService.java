package com.chinags.auth.service;

import com.chinags.auth.dao.ContractDao;
import com.chinags.auth.dao.FileDao;
import com.chinags.auth.entity.Contract;
import com.chinags.auth.entity.File;
import com.chinags.auth.entity.UpdateUtil;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

@Service
public class FileService {
    @Autowired
    private FileDao fileDao;
    @Autowired
    private UpdateUtil updateUtil;


    public File selectFileById(String id) {
        return fileDao.selectFileById(id);
    }


    public PageResult<File> selectFile(File file){
        PageRequest pageable;
        if(file.getOrderBy()==null||file.getOrderBy().equals("")){
            pageable = PageRequest.of(file.getPageNo(), file.getPageSize(), Sort.by(Sort.Direction.DESC,"createDate"));
        }else{
            pageable = PageRequest.of(file.getPageNo(), file.getPageSize(), file.getDesc(), file.getOrderBy());

        }
        Page<File> list=  fileDao.findAll(new Specification<File>() {
            @Override
            public Predicate toPredicate(Root<File> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (file.getFileName() != null && !file.getFileName().equals("")) {
                    predicates.add(cb.like(root.get("fileName").as(String.class), "%" + file.getFileName() + "%"));
                }
                if (file.getFileExplain() != null && !file.getFileExplain().equals("")) {
                    predicates.add(cb.like(root.get("fileExplain").as(String.class), "%" + file.getFileExplain() + "%"));
                }
                if (file.getFileBasis() != null && !file.getFileBasis().equals("")) {
                    predicates.add(cb.like(root.get("fileBasis").as(String.class), "%" + file.getFileBasis() + "%"));
                }
                if (file.getUploadTime() != null && !file.getUploadTime().equals("")) {
                    predicates.add(cb.equal(root.get("uploadTime").as(Date.class),  file.getUploadTime()));
                }

                Predicate[] pre = new Predicate[predicates.size()];
                query.where(predicates.toArray(pre));
                return cb.and(predicates.toArray(pre));
            }
        },pageable);
        PageResult<File> result=PageResult.getPageResult(list);

        return result;

    }
    public Result saveFile(File file) {
        Boolean scontract = fileDao.existsById(file.getId());
        File selectFile = fileDao.selectFileById(file.getId());
        if (!scontract) {
            fileDao.save(file);
            return new Result(true, StatusCode.OK, "保存成功");

        } else {
            updateUtil.copyNullProperties(file, selectFile);
            fileDao.save(selectFile);
            return new Result(true, StatusCode.OK, "保存成功");
        }
    }




}
