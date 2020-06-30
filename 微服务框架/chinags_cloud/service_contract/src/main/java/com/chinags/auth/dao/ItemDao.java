package com.chinags.auth.dao;

import com.chinags.auth.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


public interface ItemDao extends JpaRepository<Item, String>, JpaSpecificationExecutor<Item> {
    @Query(value = "select * from t_coa_item where id=:id", nativeQuery = true)
    public Item selectItemById(String id);
}
