package com.chinags.dbra.service;

import com.chinags.common.entity.PageResult;
import com.chinags.dbra.dao.ThemeDao;
import com.chinags.dbra.entity.Theme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 主题分类
 * @Author : Mark_wang
 * @Date : 2019-9-19
 **/
@Service
public class ThemeService {
    @Autowired
    private ThemeDao themeDao;

    /**
     * 分页查询所有数据
     * @return
     */
    public PageResult<Theme> findAllByPage(Theme theme) {
        PageRequest pageable;
        Sort sort = new Sort(Sort.Direction.DESC,"updateDate");
        pageable = PageRequest.of(theme.getPageNo(), theme.getPageSize(), sort);
        Page<Theme> page = themeDao.findAll(pageable);
        return PageResult.getPageResult(page);
    }

    /**
     * 查询所有数据
     * @return
     */
    public List<Theme> findAll() {
        return themeDao.findAll();
    }

    /**
     * 保存
     * @param theme
     */
    public void save(Theme theme) {
        themeDao.save(theme);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public Theme findThemeById(String id) {
        return themeDao.findThemeById(id);
    }

    /**
     * 根据id删除
     * @param id
     */
    public void deleteById(String id) {
        themeDao.deleteById(id);
    }

    /**
     * 统计每个主题分类下的数据集数量
     * @return
     */
    public List<Object> findThemeResourceSum() {
        return themeDao.findThemeResourceSum();
    }
}
