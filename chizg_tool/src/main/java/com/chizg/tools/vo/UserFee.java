package com.chizg.tools.vo;

import java.math.BigDecimal;

public class UserFee {
    private Integer userId;
    private BigDecimal ppsFee;
    private BigDecimal pplnsFee;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getPpsFee() {
        return ppsFee;
    }

    public void setPpsFee(BigDecimal ppsFee) {
        this.ppsFee = ppsFee;
    }

    public BigDecimal getPplnsFee() {
        return pplnsFee;
    }

    public void setPplnsFee(BigDecimal pplnsFee) {
        this.pplnsFee = pplnsFee;
    }

    public UserFee() {
    }

    public UserFee(Integer userId, BigDecimal ppsFee, BigDecimal pplnsFee) {
        this.userId = userId;
        this.ppsFee = ppsFee;
        this.pplnsFee = pplnsFee;
    }
}
