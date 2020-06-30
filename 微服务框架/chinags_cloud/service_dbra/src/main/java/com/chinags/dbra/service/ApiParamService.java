package com.chinags.dbra.service;

import com.chinags.common.lang.StringUtils;
import com.chinags.dbra.dao.ApiParamDao;
import com.chinags.dbra.entity.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author : Mark_wang
 * @Date : 2019-8-27
 **/
@Service
public class ApiParamService {
    @Autowired
    private ApiParamDao apiParamDao;

    /**
     * 保存接口参数信息
     * @param apiParam
     * @return
     */
    public ApiParam save(ApiParam apiParam) {
       return apiParamDao.save(apiParam);
    }

    /**
     * 根据id删除接口参数信息
     * @param id
     */
    public void deleteById(String id) {
        apiParamDao.deleteById(id);
    }

    /**
     * 根据类型和apiId查询接口参数信息
     * @param type
     * @param apiId
     * @return
     */
    public List<ApiParam> find(String type, String apiId) {
        if (StringUtils.isNotEmpty(type)) {
            return apiParamDao.findByTypeAndApiId(type, apiId);
        } else {
            return apiParamDao.findByApiId(apiId);
        }
    }

    /**
     * 根据id查询接口参数
     * @param id
     * @return
     */
    public ApiParam selectById(String id) {
        return apiParamDao.selectById(id);
    }

    /**
     * 根据apiId删除参数
     * @param apiId
     */
    public void deleteByApiId(String apiId) {
        apiParamDao.deleteByApiId(apiId);
    }

    /**
     * 根据用户修改apiId
     * @param apiId
     * @param userCode
     */
    public void updateApiIdByUserCode(String apiId, String userCode) {
        apiParamDao.updateApiIdByUserCode(apiId, userCode);
    }

    /**
     * 资源目录默认添加参数
     * @param apiId
     */
    public void defaultAddByApiId(String apiId) {
        ApiParam apiParam = new ApiParam();
        apiParam.setApiId(apiId);
        apiParam.setColType("字符");
        apiParam.setName("token");
        apiParam.setIsNull("是");
        apiParam.setType("ty");
        apiParam.setRemarks("用户token");
        apiParamDao.save(apiParam);
        apiParam = new ApiParam();
        apiParam.setApiId(apiId);
        apiParam.setColType("数值");
        apiParam.setName("pageNo");
        apiParam.setIsNull("是");
        apiParam.setType("sy");
        apiParam.setRemarks("第几页");
        apiParamDao.save(apiParam);
        apiParam = new ApiParam();
        apiParam.setApiId(apiId);
        apiParam.setColType("数值");
        apiParam.setName("pageSize");
        apiParam.setIsNull("是");
        apiParam.setType("sy");
        apiParam.setRemarks("每页数据量");
        apiParamDao.save(apiParam);
    }
}
