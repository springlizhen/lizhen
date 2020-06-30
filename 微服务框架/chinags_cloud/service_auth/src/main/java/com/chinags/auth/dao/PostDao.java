package com.chinags.auth.dao;

import com.chinags.auth.entity.Office;
import com.chinags.auth.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface PostDao extends JpaRepository<Post, String>, JpaSpecificationExecutor<Post> {

    public Post getAreaByPostCode(String postCode);

    @Query(value = "select count(*) from T_PUB_SYS_POST where post_name = :postName", nativeQuery = true)
    public Long getAreaByPostName(String postName);

    @Modifying
    @Query("update Post c set c.status = 1 where c.postCode = :postCode")
    public void deleteStatus(String postCode);

    public Set<Post> findByPostCodeIn(String[] postCode);
}
