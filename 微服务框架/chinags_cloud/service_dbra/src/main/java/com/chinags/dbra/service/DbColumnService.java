package com.chinags.dbra.service;

import com.chinags.common.entity.BaseEntity;
import com.chinags.common.entity.PageResult;
import com.chinags.dbra.dao.DbColumnDao;
import com.chinags.dbra.entity.DbColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 数据字段实现
 * @author Mark_Wang
 * @date 2019/6/28
 * @time 11:08
 **/
@Service
public class DbColumnService {

    @Autowired
    private DbColumnDao dbColumnDao;

    /**
     * 分页获取所有的数据字段
     * @return
     */
    public PageResult<DbColumn> findAllByPage(BaseEntity entity) {
        PageRequest pageable;
        Sort sort = new Sort(Sort.Direction.DESC,"updateDate");
        pageable = PageRequest.of(entity.getPageNo(), entity.getPageSize(), sort);
        Page<DbColumn> page = dbColumnDao.findAll(pageable);
        return PageResult.getPageResult(page);
    }

    /**
     * 根据数据表id分页查询
     * @param tbId
     * @param num
     * @param size
     * @return
     */
    public List<DbColumn> findDbColumnsByTbIdAndPage(String tbId, int num, int size) {
        return dbColumnDao.findDbColumnsByTbIdAndPage(tbId, num, size);
    }

    /**
     * 查询数据库下字段配置数量
     * @param dbId
     * @return
     */
    public Long findCountByDbId(String dbId) {
        return dbColumnDao.findCountByDbId(dbId);
    }

    /**
     * 根据数据库id分页查询
     * @param dbId
     * @param num
     * @param size
     * @return
     */
    public List<DbColumn> findDbColumnsByDbIdAndPage(String dbId, int num, int size) {
        return dbColumnDao.findDbColumnsByDbIdAndPage(dbId, num, size);
    }

    /**
     * 查询数据库下字段配置数量
     * @param tbId
     * @return
     */
    public Long findCountByTbId(String tbId) {
        return dbColumnDao.findCountByTbId(tbId);
    }

    /**
     * 获取所有的数据字段
     * @return
     */
    public List<DbColumn> findAll() {
        return dbColumnDao.findAll();
    }

    /**
     * 根据字段id获取数据字段配置信息
     * @param id 数据字段id
     * @return
     */
    public DbColumn findDbColumnById(String id) {
        return dbColumnDao.findDbColumnById(id);
    }

    /**
     * 根据表id获取该表下配置的所有字段信息
     * @param tbId 配置表id
     * @return
     */
    public List<DbColumn> findDbColumnsByTbId(String tbId) {
        return dbColumnDao.findDbColumnsByTbId(tbId);
    }

    /**
     * 根据数据库id查询字段信息
     * @param dbId
     * @return
     */
    public List<DbColumn> findDbColumnsByDbId(String dbId) {
        return dbColumnDao.findDbColumnsByDbId(dbId);
    }

    /**
     * 保存数据字段配置
     * @param dbColumn
     * @return
     */
    public DbColumn save(DbColumn dbColumn) {
        if (StringUtils.isEmpty(dbColumn.getCreateDate())) {
            dbColumn.setCreateDate(new Date());
        }
        dbColumn.setUpdateDate(new Date());
        return dbColumnDao.save(dbColumn);
    }

    /**
     * 根据id删除数据字段配置
     * @param id
     */
    public void deleteById(String id) {
        dbColumnDao.deleteById(id);
    }

    /**
     * 根据数据表id删除数据字段配置信息
     * @param tbId
     */
    public void deleteByTbId(String tbId) {
        dbColumnDao.deleteByTbId(tbId);
    }

    /**
     * 根据数据表id，数据库id，字段名查询数据库字段配置
     * @param tbId
     * @param dbId
     * @param name
     * @return
     */
    public List<DbColumn> getDbColumnByTbIdAndDbIdAndName(String tbId, String dbId, String name) {
        return dbColumnDao.getDbColumnByTbIdAndDbIdAndName(tbId, dbId, name);
    }

}
