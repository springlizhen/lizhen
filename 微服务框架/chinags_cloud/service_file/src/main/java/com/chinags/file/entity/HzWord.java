package com.chinags.file.entity;

import lombok.Data;

/**
 * @Author : Mark_Wang
 * @Date : 2020/5/7
 **/
@Data
public class HzWord {
    /**
     * 慧正id
     */
    private String id;
    /**
     * word地址
     */
    private String path;
    /**
     * 公文标题
     */
    private String title;
}
