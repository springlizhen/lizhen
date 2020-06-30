package com.chinags.common.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.jaxb.PageAdapter;

import java.util.List;

public class PageResult<T> {
    /**
     * 数据总数
     */
    private long count;
    /**
     * 数据
     */
    private List<T> list;
    /**
     * 当前页
     */
    private Integer pageNo;
    /**
     * 当前页数量
     */
    private Integer pageSize;

    private PageImpl<T> page;

    public PageResult(Page page) {
        this.page = (PageImpl<T>) page;
    }

    public PageResult(List data) {
        this.list = data;
        this.pageNo = 1;
        this.pageSize = 20;
        this.count = data.size();
        this.page = new PageImpl<T>(data);

    }

    public PageResult(List data, Long count, int pageNo) {
        this.list = data;
        this.pageNo = pageNo;
        this.pageSize = 20;
        this.count = count;
        this.page = new PageImpl<T>(data);

    }

    public static PageResult getPageResult (Page page){
        return new PageResult(page);
    }
    public static PageResult getPageResult (List data, Long count, int pageNo){
        return new PageResult(data, count, pageNo);
    }

    public static PageResult getPageResult (List data){
        return new PageResult(data);
    }

    public long getCount() {
        return count==0?page.getTotalElements():count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<T> getList() {
        return page.getContent();
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Integer getPageNo() {
        return pageNo==null?page.getNumber()+1:pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize==null?page.getSize():pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
