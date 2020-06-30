package com.chinags.dbra.dao;

import com.chinags.dbra.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 主题分类
 * @Author : Mark_wang
 * @Date : 2019-9-19
 **/
public interface ThemeDao extends JpaRepository<Theme, String>, JpaSpecificationExecutor<Theme> {

    @Query(value = "select * from t_meta_theme where id=:id", nativeQuery = true)
    Theme findThemeById(@Param("id") String id);

    @Query(value = "select * from t_meta_theme", nativeQuery = true)
    List<Theme> findAll();

    @Query(value = "SELECT a.ID id, A.NAME name, COUNT(b.ID) resourceSum  FROM T_META_THEME a INNER JOIN T_META_RESOURCE b ON a.ID = b.THEME GROUP BY a.ID, a.NAME",nativeQuery = true)
    List<Object> findThemeResourceSum();

}
