package com.chinags.layer.dao.master;

import com.chinags.layer.entity.master.DeviceParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface DeviceParamDao extends JpaRepository<DeviceParam, String>, JpaSpecificationExecutor<DeviceParam> {

    @Query(value = "select t.*,field_name,a.device_name from t_enm_device a left join t_enm_device_param t on a.id=t.device_id left join t_enm_field d on d.id=t.code_id where parent_code=:id", nativeQuery = true)
    public List<Map<String,Object>> getDeviceByCodeId(String id);
    @Query(value = "select * from (select a.value1,field_name from t_enm_device_param a left join t_enm_field t on a.code_id=t.id left join t_enm_device d on d.id=a.device_id where device_code=:id and field_class='1' )pivot (max(value1) for field_name in('进水池桩号' 进水池桩号,'出水池桩号'出水池桩号,'主机组' 主机组,'调节机组' 调节机组,'桩号' 桩号,'是否接入自动化' 是否接入自动化,'工程等级' 工程等级,'经度' 经度,'纬度' 纬度,'启用状态' 启用状态,'是否交叉建筑物' 是否交叉建筑物))", nativeQuery = true)
    public List<Map<String,Object>> getAACParamByCodeId(String id);

    @Query(value = "select * from (select a.value1,field_name from t_enm_device_param a left join t_enm_field t on a.code_id=t.id left join t_enm_device d on d.id=a.device_id where device_code=:id and field_class='1' )pivot (max(value1) for field_name in('启用状态' 启用状态,'桩号' 桩号,'是否接入自动化' 是否接入自动化,'工程等级' 工程等级,'经度' 经度,'纬度' 纬度))", nativeQuery = true)
    public List<Map<String,Object>> getAADParamByCodeId(String id);
    @Query(value = "select * from (select a.value1,field_name from t_enm_device_param a left join t_enm_field t on a.code_id=t.id left join t_enm_device d on d.id=a.device_id where device_code=:id and field_class='1' )pivot (max(value1) for field_name in('启用状态' 启用状态,'闸站类型' 闸站类型,'闸孔数量' 闸孔数量,'岸别' 岸别,'桩号' 桩号,'是否接入自动化' 是否接入自动化,'工程等级' 工程等级,'经度' 经度,'纬度' 纬度))", nativeQuery = true)
    public List<Map<String,Object>> getDeviceParamByCodeId(String id);
}
