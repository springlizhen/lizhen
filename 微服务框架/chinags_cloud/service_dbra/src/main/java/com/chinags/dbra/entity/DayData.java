package com.chinags.dbra.entity;

import lombok.Data;

/**
 * 日数据条目entity
 * @Author : Mark_Wang
 * @Date : 2020/4/8
 **/
@Data
public class DayData {
    private int id;
    private String time;
    private String type;
    private String value;
}
