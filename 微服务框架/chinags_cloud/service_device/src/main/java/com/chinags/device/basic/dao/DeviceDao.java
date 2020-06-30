package com.chinags.device.basic.dao;

import com.chinags.device.basic.dao.plus.DeviceDaoPlus;
import com.chinags.device.basic.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface DeviceDao extends JpaRepository<Device, String>, JpaSpecificationExecutor<Device>, DeviceDaoPlus {

    public Device getAreaById(String id);

    public List<Device> getAreaByParentCodesLike(String parentCodes);

    @Query(value = "select count(t) from Device t where parent_codes LIKE CONCAT('%',:parentCodes,'%')")
    public Integer getParentCodesCount(@Param("parentCodes") String parentCodes);

    @Query(value = "select count(t) from Device t where parent_codes LIKE CONCAT('%',:parentCodes,'%') and status=0")
    public Integer getStopParentCodesCount(@Param("parentCodes") String parentCodes);

    @Query(value = "select deviceCode from Device t where parentCodes LIKE CONCAT('%',:parentCodes,'%')")
    public List<String> getDeviceParentCodes(@Param("parentCodes") String parentCodes);

    @Query(value = "select min(t.treeLevel) from Device t where t.orgId=:orgId")
    public String getOrgId(@Param("orgId") String orgId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Device SET treeLeaf='1' WHERE id=:id")
    void updateApplyNumById(String id);

    /**
     * 根据id设备
     */
    @Query(value = "SELECT * FROM T_ENM_DEVICE WHERE ID=:id", nativeQuery = true)
    Device selectById(String id);

    /**
     * 根据机构id查询设备idList
     */
    @Query(value = "SELECT id FROM T_ENM_DEVICE WHERE org_id=:orgId", nativeQuery = true)
    List<String> selectByOrgId(String orgId);
    /**
     * 根据id查询数量
     */
    @Query(value = "SELECT * FROM T_ENM_DEVICE WHERE parent_code=:id", nativeQuery = true)
    List<Device> selectByDevice(String id);

    /**
     * 根据传感器类型名称查询设备信息
     * @param cgqTypeName
     * @return
     */
    @Query(value = "SELECT t FROM Device t WHERE cgqStatus='是' and cgqTypeName LIKE CONCAT('%',:cgqTypeName,'%')")
    List<Device> findByCgqTypeName(String cgqTypeName);

    /**
     * 根据设施设备代码查询设备信息
     * @param deviceCode
     * @return
     */
    @Query(value = "SELECT * FROM T_ENM_DEVICE WHERE device_code=:deviceCode", nativeQuery = true)
    Device selectByDeviceCode(String deviceCode);

    /**
     * 根据管理所ID查询人工测点设备列表
     * @param officeId
     * @return
     */
    @Query(value = "SELECT * FROM T_ENM_DEVICE WHERE OFFICE_ID=:officeId and cgq_status='是' and (cgq_type_name = '高程' or cgq_type_name = '水平')", nativeQuery = true)
    List<Device> findListByOfficeId(String officeId);
    /**
     * 查询具有人工测点的最顶级设备列表
     * @return
     */
    @Query(value = "SELECT d.* FROM T_ENM_DEVICE d WHERE d.TREE_LEVEL = 0 AND " +
            "(SELECT count( p.id ) FROM T_ENM_POINT p LEFT JOIN T_ENM_DEVICE d2 ON p.pid = d2.id " +
            "WHERE p.TYPE = '人工测点' AND ( d2.id = d.id OR d2.PARENT_CODES LIKE '%,' || d.id || ',%' ) ) > 0", nativeQuery = true)
    List<Device> findIsHavePointTopList();

    /**
     * 返回传感器参数为否的设备
     * @return
     */
    @Query(value = "SELECT * FROM T_ENM_DEVICE WHERE CGQ_STATUS ='否'", nativeQuery = true)
    List<Device> selectAll();


    @Query(value = "SELECT * FROM T_ENM_DEVICE WHERE id=:id", nativeQuery = true)
    Device selectByDeviceName(String id);
    @Query(value = "SELECT * FROM T_ENM_DEVICE WHERE ORG_ID =:ti", nativeQuery = true)
    public List<Device> selectByOrgIdL(String ti);
    @Query(value = "SELECT * FROM T_ENM_DEVICE WHERE CGQ_STATUS ='否' and ORG_ID =:t and DEVICE_CLASS =:ty and DEVICE_NAME like concat(concat('%', :kpName),'%')", nativeQuery = true)
    public List<Device> selectByOrgIdTo(String t, String ty,String kpName);
    @Query(value = "SELECT * FROM T_ENM_DEVICE WHERE CGQ_STATUS ='否' and ORG_ID =:tocode and DEVICE_NAME like concat(concat('%', :kpName),'%')", nativeQuery = true)
    List<Device> selectByOrgIdToX(String tocode,String kpName);
    @Query(value = "SELECT * FROM T_ENM_DEVICE WHERE CGQ_STATUS ='否' and ORG_ID =:tocode and DEVICE_CLASS =:ty", nativeQuery = true)
    List<Device> selectByOrgIdB(String tocode, String ty);
    @Query(value = "SELECT * FROM T_ENM_DEVICE WHERE CGQ_STATUS ='否' and ORG_ID =:tocode ", nativeQuery = true)
    List<Device> selectByOrgIdLO(String tocode);
    @Query(value = "SELECT * FROM T_ENM_DEVICE WHERE ORG_ID =:officeCode", nativeQuery = true)
    List<Device> selectByOrgIdZo(String officeCode);
    @Query(value = "SELECT * FROM T_ENM_DEVICE WHERE ID =:deceiveId", nativeQuery = true)
    Device getbyId(String deceiveId);
    @Query(value = "SELECT * FROM T_ENM_DEVICE WHERE DEVICE_CLASS='AAC' and CGQ_STATUS='是' and CGQ_VALUE='水平'", nativeQuery = true)
    List<Device> selectByDeviceTu();
    @Query(value = "SELECT * FROM T_ENM_DEVICE WHERE DEVICE_CLASS='AAC' and CGQ_STATUS='是' and CGQ_VALUE='高程'", nativeQuery = true)
    List<Device> selectByGco();

    /**
     * 查询设备列表给工程安全阈值使用
     * 查询传感器属性为是的设备
     */
    @Query(value = "SELECT * FROM T_ENM_DEVICE WHERE CGQ_STATUS='是' and id not in (:deceiveIds) and CGQ_TYPE_NAME in ('高程', '水平')", nativeQuery = true)
    List<Device> selectForThresholdVal(String[] deceiveIds);
    @Query(value = "SELECT * FROM T_ENM_DEVICE WHERE org_id =:officeCode and device_class_name =:deviceClassName", nativeQuery = true)
    List<Device> selectByOfficeCode( String officeCode, String deviceClassName);
}
