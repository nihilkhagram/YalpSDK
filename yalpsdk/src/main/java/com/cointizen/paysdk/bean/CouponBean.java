package com.cointizen.paysdk.bean;

import java.io.Serializable;

public class CouponBean implements Serializable {
    /**
     * id : 5
     * mold : 0
     * game_id : 0
     * coupon_name : 代金券6号
     * money : 0.50
     * limit_money : 2.00
     * limit : 1
     * start_time : 2020/29/02
     * end_time : 永久
     * type : 1
     * coupon_game : 所有游戏
     */

    private int statusType;  //券的类型 1：未领取  2：已领取可使用  3已使用   4已过期
    private String id;
    private int mold;
    private int game_id;
    private String coupon_name;
    private String money;
    private String limit_money;
    private int limit;
    private String start_time;
    private String end_time;
    private int type;
    private String coupon_game;
    private int limit_num;

    public int getLimit_num() {
        return limit_num;
    }

    public void setLimit_num(int limit_num) {
        this.limit_num = limit_num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMold() {
        return mold;
    }

    public void setMold(int mold) {
        this.mold = mold;
    }

    public int getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public String getCoupon_name() {
        return coupon_name;
    }

    public void setCoupon_name(String coupon_name) {
        this.coupon_name = coupon_name;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getLimit_money() {
        return limit_money;
    }

    public void setLimit_money(String limit_money) {
        this.limit_money = limit_money;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCoupon_game() {
        return coupon_game;
    }

    public void setCoupon_game(String coupon_game) {
        this.coupon_game = coupon_game;
    }

    public int getStatusType() {
        return statusType;
    }

    public void setStatusType(int statusType) {
        this.statusType = statusType;
    }


//    /**
//     * id : 23
//     * user_id : 21
//     * user_name : z123123
//     * coupon_id : 16
//     * coupon_name : 代金券222
//     * coupon_type : 0
//     * game_ids : 1,2,3
//     * subtract_money : 22
//     * require_money : 0
//     * start_time : 1575216000
//     * end_time : 1577635200
//     * create_time : 1575341629
//     * use_time : 0
//     * status : 1
//     */
//
//    private int statusType;  //券的类型 1：可领取  2：已领取  3已失效
//    private String coupon_id;
//    private String coupon_name;
//    private String coupon_type;
//    private String subtract_money;
//    private String start_time;
//    private String end_time;
//    private String require_money; //满足金额
//    private int status; //使用状态 0已使用 1已过期
//
//    public int getStatus() {
//        return status;
//    }
//
//    public void setStatus(int status) {
//        this.status = status;
//    }
//
//    public String getRequire_money() {
//        return require_money;
//    }
//
//    public void setRequire_money(String require_money) {
//        this.require_money = require_money;
//    }
//
//
//    public String getCoupon_id() {
//        return coupon_id;
//    }
//
//    public void setCoupon_id(String coupon_id) {
//        this.coupon_id = coupon_id;
//    }
//
//    public String getCoupon_name() {
//        return coupon_name;
//    }
//
//    public void setCoupon_name(String coupon_name) {
//        this.coupon_name = coupon_name;
//    }
//
//    public String getCoupon_type() {
//        return coupon_type;
//    }
//
//    public void setCoupon_type(String coupon_type) {
//        this.coupon_type = coupon_type;
//    }
//
//    public String getSubtract_money() {
//        return subtract_money;
//    }
//
//    public void setSubtract_money(String subtract_money) {
//        this.subtract_money = subtract_money;
//    }
//
//    public String getStart_time() {
//        return start_time;
//    }
//
//    public void setStart_time(String start_time) {
//        this.start_time = start_time;
//    }
//
//    public String getEnd_time() {
//        return end_time;
//    }
//
//    public void setEnd_time(String end_time) {
//        this.end_time = end_time;
//    }
}
