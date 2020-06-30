package com.bjpowernode.p2p.model.vo;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName：PaginationVo
 * Package:com.bjpowernode.p2p.model.vo
 * Description:
 *
 * @date:2019/2/23 23:04
 * @author:guoxin@bjpowernode.com
 */
public class PaginationVo<T> implements Serializable {

    /**
     * 总记录数
     *
     *
     *
     * **/
    private  Long total;
    /**
     *
     *
     *
     * 产品数据集合
     *
     *
     *
     * **/
    private List<T> dataList;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
