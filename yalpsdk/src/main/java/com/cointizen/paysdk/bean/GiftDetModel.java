package com.cointizen.paysdk.bean;

/**
 * 描述：礼包详情
 * 时间: 2018-10-18 13:31
 */

public class GiftDetModel {

    /**
     * id : 2
     * giftbag_name : 测试测试2
     * start_time : 1539826200
     * end_time : 0
     * desribe : 测试测试测试测试测试测试测试测试测试测试测试测试测试测试
     * digest : 测试测试测试测试测试测试
     * icon :
     * game_name : 测试游戏(安卓版)
     * record_novice : null
     * received : 0
     * surplus : 1
     */

    private String id;
    private String giftbag_name;
    private String start_time;
    private String end_time;
    private String desribe;
    private String digest;
    private String icon;
    private String game_name;
    private String record_novice;
    private String notice;
    private int received;
    private int surplus;
    private int novice_num;

    public int getNovice_num() {
        return novice_num;
    }

    public void setNovice_num(int novice_num) {
        this.novice_num = novice_num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGiftbag_name() {
        return giftbag_name;
    }

    public void setGiftbag_name(String giftbag_name) {
        this.giftbag_name = giftbag_name;
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

    public String getDesribe() {
        return desribe;
    }

    public void setDesribe(String desribe) {
        this.desribe = desribe;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public String getRecord_novice() {
        return record_novice;
    }

    public void setRecord_novice(String record_novice) {
        this.record_novice = record_novice;
    }

    public int getReceived() {
        return received;
    }

    public void setReceived(int received) {
        this.received = received;
    }

    public int getSurplus() {
        return surplus;
    }

    public void setSurplus(int surplus) {
        this.surplus = surplus;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
