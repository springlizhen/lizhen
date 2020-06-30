package com.chinags.auth.dao;

import com.chinags.auth.entity.Contract;
import com.chinags.auth.entity.File;
import com.chinags.auth.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


public interface FileDao extends JpaRepository<File, String>, JpaSpecificationExecutor<File> {
    @Query(value = "select * from t_coa_file where id=:id", nativeQuery = true)
    public File selectFileById(String id);

}
