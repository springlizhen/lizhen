package com.chinags.device.check.dao;

import com.chinags.device.check.entity.Document;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DocumentDao extends JpaRepository<Document, String>, JpaSpecificationExecutor<Document> {
    public Document getDocuementById(String id);


}
