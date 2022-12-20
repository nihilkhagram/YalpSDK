package com.cointizen.paysdk.entity;

/**
 * Created by zhujinzhujin
 * on 2017/1/13.
 */

public class UserDiscountEntity {

    private float discountNum;

    /**
     * 0无折扣 1：首冲 2续冲
     */
    private int discountType;

    public UserDiscountEntity() {
        discountNum = 10;
        discountType = 0;
    }

    public float getDiscountNum() {
        return discountNum;
    }

    public void setDiscountNum(String discountNum) {
        this.discountNum = Float.parseFloat(discountNum);
    }

    public int getDiscountType() {
        return discountType;
    }

    public void setDiscountType(int discountType) {
        this.discountType = discountType;
    }
}
