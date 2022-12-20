package com.cointizen.paysdk.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 描述：时间转换工具类
 * 作者：Administrator
 * 时间: 2017-11-22 16:18
 */

public class TimeConvertUtils {
    /**
     * 根据时间戳判断多久前
     * @return
     */
//    public static String getStandardDate(String timeStr) {
//        String temp = "";
//        try {
//            long now = System.currentTimeMillis() / 1000;
//            long publish = Long.parseLong(timeStr);
//            long diff = now - publish;
//            long months = diff / (60 * 60 * 24 * 30);
//            long week = diff / (60 * 60 * 24 * 7);
//            long days = diff / (60 * 60 * 24);
//            long hours = (diff - days * (60 * 60 * 24)) / (60 * 60);
//            long minutes = (diff - days * (60 * 60 * 24) - hours * (60 * 60)) / 60;
//            String data = Utils.getData("" + now, "yyyy-MM-dd HH:mm:ss");
//            String data1 = Utils.getData("" + publish, "yyyy-MM-dd HH:mm:ss");
//            MCLog.e(UtilsConstants.Log_SjyVqJCjxJ,UtilsConstants.Log_kMRPVstSez+data+UtilsConstants.Log_bkoKaKgWqc+data1);
//            if (months > 0) {
//                temp = months + UtilsConstants.S_JerHnxUvaa;
//                SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                @SuppressWarnings("unused")
//                long lcc = Long.valueOf(timeStr);
//                int i = Integer.parseInt(timeStr);
//                String times = sdr.format(new Date(i * 1000L));
//                temp = times;
//            }else if (week > 0){
//                temp = week + UtilsConstants.S_UQNyVRNQPo;
//                SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                @SuppressWarnings("unused")
//                long lcc = Long.valueOf(timeStr);
//                int i = Integer.parseInt(timeStr);
//                String times = sdr.format(new Date(i * 1000L));
//                temp = times;
//            } else if (days > 0) {
//                temp = days + UtilsConstants.S_keCFgGQgqb;
//                SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                @SuppressWarnings("unused")
//                long lcc = Long.valueOf(timeStr);
//                int i = Integer.parseInt(timeStr);
//                String times = sdr.format(new Date(i * 1000L));
//                temp = times;
//            } else if (hours > 0) {
//                temp = hours + UtilsConstants.S_bdlTRgaXZP;
//            } else {
//                temp = minutes + UtilsConstants.S_jIyjCYKZGN;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return temp;
//    }

    public static String getTodayDateTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        return format.format(new Date());
    }

    /**
     * 掉此方法输入所要转换的时间输入例如（UtilsConstants.S_UlbthKMwCI）返回时间戳
     *
     * @param time
     * @return
     */
    public String data(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat(UtilsConstants.S_wveZdJeZcJ,
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    public static String data1(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss",
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    public static String getTodayDateTimes() {
        SimpleDateFormat format = new SimpleDateFormat(UtilsConstants.S_tvJJxfaSdE,
                Locale.getDefault());
        return format.format(new Date());
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentTime_Today() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return sdf.format(new Date());
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentTime1() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(new Date());
    }

    /**
     * 获取当前时间戳
     */
    public static String getNowTime(){
        long timecurrentTimeMillis = System.currentTimeMillis();
        return String.valueOf(timecurrentTimeMillis);
    }

    /**
     * 调此方法输入所要转换的时间输入例如（"2014-06-14-16-09-00"）返回时间戳
     *
     * @param time
     * @return
     */
    public static String dataOne(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss",
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    public static String getTimestamp(String time, String type) {
        SimpleDateFormat sdr = new SimpleDateFormat(type, Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（UtilsConstants.S_UlbthKMwCI）
     *
     * @param time
     * @return
     */
    public static String times(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat(UtilsConstants.S_wveZdJeZcJ);
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014-06-14  16:09:00"）
     *
     * @param time
     * @return
     */
    public static String timedate(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014-06-14  16:09:00"）
     *
     * @param time
     * @return
     */
    public static String timedate2(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("MM-dd HH:mm:ss");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }


    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014-06-14  16:09:00"）
     *
     * @param time
     * @return
     */
    public static String timedate3(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }


    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（UtilsConstants.S_rSdeZQBblD）
     *
     * @param time
     * @return
     */
    public static String timet(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }

    /**
     * @param //time斜杠分开
     * @return
     */
    public static String timeslash(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy/MM/dd,HH:mm");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }

    /**
     * @param
     * //年月日
     * @return
     */
    public static String timeslashData(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy.MM.dd");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
//      int i = Integer.parseInt(time);
        String times = sdr.format(new Date(lcc * 1000L));
        return times;

    }

    /**
     * @param //time斜杠分开
     * @return
     */
    public static String timeMinute(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("HH:mm");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }

    public static String tim(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyyMMdd HH:mm");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    public static String time(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    // 调用此方法输入所要转换的时间戳例如（1402733340）输出（UtilsConstants.S_UlbthKMwCI）
    public static String times(long timeStamp) {
        SimpleDateFormat sdr = new SimpleDateFormat(UtilsConstants.S_XOhWAbcIZL);
        return sdr.format(new Date(timeStamp)).replaceAll("#",
                getWeek(timeStamp));

    }

    private static String getWeek(long timeStamp) {
        int mydate = 0;
        String week = null;
        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date(timeStamp));
        mydate = cd.get(Calendar.DAY_OF_WEEK);
        // 获取指定日期转换成星期几
        if (mydate == 1) {
            week = UtilsConstants.S_saTKmONYxR;
        } else if (mydate == 2) {
            week = UtilsConstants.S_ezWcGBFGSG;
        } else if (mydate == 3) {
            week = UtilsConstants.S_MvaYJvylwx;
        } else if (mydate == 4) {
            week = UtilsConstants.S_YxHNZymszh;
        } else if (mydate == 5) {
            week = UtilsConstants.S_QLeOmqgVOh;
        } else if (mydate == 6) {
            week = UtilsConstants.S_SoWeWoFZyG;
        } else if (mydate == 7) {
            week = UtilsConstants.S_CKVGlpSCZO;
        }
        return week;
    }

    // 并用分割符把时间分成时间数组
    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014-06-14-16-09-00"）
     *
     * @param time
     * @return
     */
    public String timesOne(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }

    public static String timesTwo(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }

    /**
     * 并用分割符把时间分成时间数组
     *
     * @param time
     * @return
     */
    public static String[] timestamp(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat(UtilsConstants.S_wveZdJeZcJ);
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        String[] fenge = times.split(UtilsConstants.S_nIIQJQIifF);
        return fenge;
    }

    /**
     * 根据传递的类型格式化时间
     *
     * @param str
     * @param type
     *            例如：yy-MM-dd
     * @return
     */
    public static String getDateTimeByMillisecond(String str, String type) {

        Date date = new Date(Long.valueOf(str));

        SimpleDateFormat format = new SimpleDateFormat(type);

        String time = format.format(date);

        return time;
    }

    /**
     * 分割符把时间分成时间数组
     *
     * @param time
     * @return
     */
    public String[] division(String time) {

        String[] fenge = time.split(UtilsConstants.S_nIIQJQIifF);

        return fenge;

    }

    /**
     * 输入时间戳变星期
     *
     * @param time
     * @return
     */
    public static String changeweek(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat(UtilsConstants.S_wveZdJeZcJ);
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        Date date = null;
        int mydate = 0;
        String week = null;
        try {
            date = sdr.parse(times);
            Calendar cd = Calendar.getInstance();
            cd.setTime(date);
            mydate = cd.get(Calendar.DAY_OF_WEEK);
            // 获取指定日期转换成星期几
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (mydate == 1) {
            week = UtilsConstants.S_FDdPsGLZbn;
        } else if (mydate == 2) {
            week = UtilsConstants.S_FWjyDkToQo;
        } else if (mydate == 3) {
            week = UtilsConstants.S_RYeMtFGqlS;
        } else if (mydate == 4) {
            week = UtilsConstants.S_zOyTeXeSXd;
        } else if (mydate == 5) {
            week = UtilsConstants.S_rIEgLGrPCp;
        } else if (mydate == 6) {
            week = UtilsConstants.S_ebnCGxrtVu;
        } else if (mydate == 7) {
            week = UtilsConstants.S_BMHrvyqLlR;
        }
        return week;

    }

    /**
     * 获取日期和星期　例如：２０１４－１１－１３　１１:００　星期一
     *
     * @param time
     * @param type
     * @return
     */
    public static String getDateAndWeek(String time, String type) {
        return getDateTimeByMillisecond(time + "000", type) + "  "
                + changeweekOne(time);
    }

    /**
     * 输入时间戳变星期
     *
     * @param time
     * @return
     */
    public static String changeweekOne(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        Date date = null;
        int mydate = 0;
        String week = null;
        try {
            date = sdr.parse(times);
            Calendar cd = Calendar.getInstance();
            cd.setTime(date);
            mydate = cd.get(Calendar.DAY_OF_WEEK);
            // 获取指定日期转换成星期几
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (mydate == 1) {
            week = UtilsConstants.S_FDdPsGLZbn;
        } else if (mydate == 2) {
            week = UtilsConstants.S_FWjyDkToQo;
        } else if (mydate == 3) {
            week = UtilsConstants.S_RYeMtFGqlS;
        } else if (mydate == 4) {
            week = UtilsConstants.S_zOyTeXeSXd;
        } else if (mydate == 5) {
            week = UtilsConstants.S_rIEgLGrPCp;
        } else if (mydate == 6) {
            week = UtilsConstants.S_ebnCGxrtVu;
        } else if (mydate == 7) {
            week = UtilsConstants.S_BMHrvyqLlR;
        }
        return week;

    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(new Date());
    }

    /**
     * 输入日期如（2014年06月14日16时09分00秒）返回（星期数）
     *
     * @param time
     * @return
     */
    public String week(String time) {
        Date date = null;
        SimpleDateFormat sdr = new SimpleDateFormat(UtilsConstants.S_wveZdJeZcJ);
        int mydate = 0;
        String week = null;
        try {
            date = sdr.parse(time);
            Calendar cd = Calendar.getInstance();
            cd.setTime(date);
            mydate = cd.get(Calendar.DAY_OF_WEEK);
            // 获取指定日期转换成星期几
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (mydate == 1) {
            week = UtilsConstants.S_FDdPsGLZbn;
        } else if (mydate == 2) {
            week = UtilsConstants.S_FWjyDkToQo;
        } else if (mydate == 3) {
            week = UtilsConstants.S_RYeMtFGqlS;
        } else if (mydate == 4) {
            week = UtilsConstants.S_zOyTeXeSXd;
        } else if (mydate == 5) {
            week = UtilsConstants.S_rIEgLGrPCp;
        } else if (mydate == 6) {
            week = UtilsConstants.S_ebnCGxrtVu;
        } else if (mydate == 7) {
            week = UtilsConstants.S_BMHrvyqLlR;
        }
        return week;
    }

    /**
     * 输入日期如（2014-06-14-16-09-00）返回（星期数）
     *
     * @param time
     * @return
     */
    public String weekOne(String time) {
        Date date = null;
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        int mydate = 0;
        String week = null;
        try {
            date = sdr.parse(time);
            Calendar cd = Calendar.getInstance();
            cd.setTime(date);
            mydate = cd.get(Calendar.DAY_OF_WEEK);
            // 获取指定日期转换成星期几
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (mydate == 1) {
            week = UtilsConstants.S_FDdPsGLZbn;
        } else if (mydate == 2) {
            week = UtilsConstants.S_FWjyDkToQo;
        } else if (mydate == 3) {
            week = UtilsConstants.S_RYeMtFGqlS;
        } else if (mydate == 4) {
            week = UtilsConstants.S_zOyTeXeSXd;
        } else if (mydate == 5) {
            week = UtilsConstants.S_rIEgLGrPCp;
        } else if (mydate == 6) {
            week = UtilsConstants.S_ebnCGxrtVu;
        } else if (mydate == 7) {
            week = UtilsConstants.S_BMHrvyqLlR;
        }
        return week;
    }
}
