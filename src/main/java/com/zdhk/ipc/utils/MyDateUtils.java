package com.zdhk.ipc.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Slf4j
public class MyDateUtils {

    public static final SimpleDateFormat sdfLongTimePlus = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat sdfLongCn = new SimpleDateFormat("yyyy年MM月dd日");
    public static final SimpleDateFormat sdfShortLongTimePlusCn = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    public static final SimpleDateFormat sdfMd = new SimpleDateFormat("MM月dd日");
    public static final SimpleDateFormat sdfLong = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat sdfyyyymm = new SimpleDateFormat("yyyy-MM");
    public static final SimpleDateFormat sdfShortU = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
    public static final SimpleDateFormat sdfShort = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat sdlLongStringplus = new SimpleDateFormat("yyyyMMddHHmmssSSS");


    public static Date getDateByyyyMMddHHmmssSSS(String yy)  {
        try{
            return  sdlLongStringplus.parse(yy);
        }catch (Exception e){
            return null;
        }
    }


    public static Date getDateByyyymm(String yyyymm){
        try{
            return  sdfyyyymm.parse(yyyymm);
        }catch (Exception e){
            return null;
        }
    }


    public static Date getDateByyyyyMMddhhmmss(String yyyyMMddhhmmss)  {
        try{
            return  sdfLongTimePlus.parse(yyyyMMddhhmmss);
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 根据传入的格式，将Date转换成该格式
     */
    public static String getDateFromFormat(Date date,SimpleDateFormat format){
        String result = "";
        try{
            result  = format.format(date);
        }catch (Exception e){
            log.error("date.format.wrong");
        }
        return result;
    }

    /**
     * 获取当前时间
     * @return
     */
    public static Date getNowDate(){
        return new Date();
    }

    /**
     * 将豪秒换成Date
     */
    public Date milliSecondToDate(long time){
        return new Date(time);
    }


    /**
     * 获取今天凌晨零点的Date,注意是今天不是明天！
     */
    public static Date getFirstTimeOfToday(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取今天23点59分59秒的毫秒数
     * @param
     */
    public static long getLastTimeOfToday(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime().getTime();
    }

    /**
     * 获取指定的时间,比如(23,59,59,1)表示明天的23点59分59秒的Date
     * @param hour
     * @param minute
     * @param second
     * @param addDay
     * @param args
     * @return
     */
    public static Date getNeedTime(int hour, int minute, int second, int addDay, int ...args){
        Calendar calendar = Calendar.getInstance();
        if(addDay != 0){
            calendar.add(Calendar.DATE,addDay);
        }
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,second);
        if(args.length==1){
            calendar.set(Calendar.MILLISECOND,args[0]);
        }
        return calendar.getTime();
    }

    /**
     * 获取这个月最后一天23小时59分59秒
     * @return
     */
    public static Date getLastTimeOfMonth(){
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        ca.set(Calendar.HOUR_OF_DAY,23);
        ca.set(Calendar.MINUTE,59);
        ca.set(Calendar.SECOND,59);
        ca.set(Calendar.MILLISECOND,999);
        return ca.getTime();
    }



    /**
     * 获取今天是一周中第几天
     * @return
     */
    public static int getDayInWeek(){
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_WEEK);
        if(Calendar.MONDAY==day){
            return 1;
        }else if(Calendar.TUESDAY==day){
            return 2;
        }else if(Calendar.WEDNESDAY==day){
            return 3;
        }else if(Calendar.THURSDAY==day){
            return 4;
        }else if(Calendar.FRIDAY==day){
            return 5;
        }else if(Calendar.SATURDAY==day){
            return 6;
        }else if(Calendar.SUNDAY==day){
            return 7;
        }else{
            return 0;
        }

    }

    public static int getNow(){
        return (int)System.currentTimeMillis()/1000;
    }

    public static void main(String[] args) {
        Date date = new Date();

        System.out.println(getNow());
    }
}
