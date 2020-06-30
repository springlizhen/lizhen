package com.bjpowernode.p2p.model.vo;

import java.io.Serializable;

/**
 * ClassName：BiduserTop
 * Package:com.bjpowernode.p2p.model.vo
 * Description:
 *
 * @date:2019/2/28 21:20
 * @author:guoxin@bjpowernode.com
 */
public class BiduserTop implements Serializable {
    private String phone;
    /*
    * 累计投资金额
    *
    * */
    private Double score;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
