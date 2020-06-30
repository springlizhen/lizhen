package com.chinags.archives.dao;

import com.chinags.archives.entity.DocumentPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author XieWenqing
 * @date 2019/11/21 下午 5:30
 */
public interface DocumentPermissionDao extends JpaRepository<DocumentPermission, String>, JpaSpecificationExecutor<DocumentPermission> {
    /**
     * 当前用户是否能查看该文档，返回1能看0不能看
     * @param userCode 用户id
     * @param cdfId 文档id
     */
    @Query(value = "SELECT count(*) FROM T_COA_DOCUMENT_PERMISSION WHERE (  ( user_id=:userCode and permission_time = '0' ) or ( user_id=:userCode and SYSDATE between start_date and end_date and permission_time = '1' )  ) and cdf_id=:cdfId", nativeQuery = true)
    Integer cansee(String userCode, String cdfId);

    /**
     * 删除工程档案案卷著录
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM T_COA_DOCUMENT_PERMISSION WHERE CDF_ID=:cdfId and PERMISSION_TIME='0'", nativeQuery = true)
    void deleteListByCdfId(String cdfId);

    /**
     * 管理员保存文档时无法给自己授权，为防止这一情况，保存时检查一遍当前登录用户是否有权限，没有的话进行添加
     * @param userCode
     * @param cdfId
     * @return
     */
    @Query(value = "SELECT count(*) FROM T_COA_DOCUMENT_PERMISSION WHERE USER_ID=:userCode and CDF_ID=:cdfId", nativeQuery = true)
    Integer countLoginUser(String userCode, String cdfId);

    /**
     * 当前登录用户可查看的卷内文件idList
     * @param userCode 用户id
     */
    @Query(value = "SELECT cdf_id FROM T_COA_DOCUMENT_PERMISSION WHERE (  ( user_id=:userCode and permission_time = '0' ) or ( user_id=:userCode and SYSDATE between start_date and end_date and permission_time = '1' )  ) ", nativeQuery = true)
    List<String> CdfIdList(String userCode);
}
