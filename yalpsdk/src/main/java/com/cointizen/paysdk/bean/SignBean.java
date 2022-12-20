package com.cointizen.paysdk.bean;

/**
 * 描述：签到
 * 时间: 2018-11-26 16:00
 */

public class SignBean {

    /**
     * point_arr : {"day1":9,"day2":11,"day3":13,"day4":15,"day5":17,"day6":19,"day7":21}
     * today_signed : 1
     * signed_day : 1
     * total_sign : 1
     * totay_point : 9
     */

    private PointArrBean point_arr;
    private int today_signed;
    private int signed_day;
    private int total_sign;
    private int totay_point;

    public PointArrBean getPoint_arr() {
        return point_arr;
    }

    public void setPoint_arr(PointArrBean point_arr) {
        this.point_arr = point_arr;
    }

    public int getToday_signed() {
        return today_signed;
    }

    public void setToday_signed(int today_signed) {
        this.today_signed = today_signed;
    }

    public int getSigned_day() {
        return signed_day;
    }

    public void setSigned_day(int signed_day) {
        this.signed_day = signed_day;
    }

    public int getTotal_sign() {
        return total_sign;
    }

    public void setTotal_sign(int total_sign) {
        this.total_sign = total_sign;
    }

    public int getTotay_point() {
        return totay_point;
    }

    public void setTotay_point(int totay_point) {
        this.totay_point = totay_point;
    }

    public static class PointArrBean {

        /**
         * day1 : 9
         * day2 : 11
         * day3 : 13
         * day4 : 15
         * day5 : 17
         * day6 : 19
         * day7 : 21
         */

        private int day1;
        private int day2;
        private int day3;
        private int day4;
        private int day5;
        private int day6;
        private int day7;

        public int getDay1() {
            return day1;
        }

        public void setDay1(int day1) {
            this.day1 = day1;
        }

        public int getDay2() {
            return day2;
        }

        public void setDay2(int day2) {
            this.day2 = day2;
        }

        public int getDay3() {
            return day3;
        }

        public void setDay3(int day3) {
            this.day3 = day3;
        }

        public int getDay4() {
            return day4;
        }

        public void setDay4(int day4) {
            this.day4 = day4;
        }

        public int getDay5() {
            return day5;
        }

        public void setDay5(int day5) {
            this.day5 = day5;
        }

        public int getDay6() {
            return day6;
        }

        public void setDay6(int day6) {
            this.day6 = day6;
        }

        public int getDay7() {
            return day7;
        }

        public void setDay7(int day7) {
            this.day7 = day7;
        }
    }
}
