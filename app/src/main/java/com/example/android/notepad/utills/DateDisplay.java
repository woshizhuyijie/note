package com.example.android.notepad.utills;

import android.widget.TextView;

import com.example.android.notepad.R;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;

public class DateDisplay {
   static final String pattern_Default = "yyyy-MM-dd HH:mm";
   static final  String pattern_Hm="HH时mm分钟";
    static final  String pattern_Day="dd日 HH时";
    static final  String pattern_M="MM月dd日";
    static  final String pattern_year="yyyy年MM月dd日";
    public static String showTime(Long time){
        //set update_time
        //earn text_date set time
        // 定义格式模式
        SimpleDateFormat simpleDateFormat = null;
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();//系统时间
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneOffset.ofHours(8));//从数据库中获取的时间

        //判断时间 如果是当天的话就显示小时和分钟
        /*if(now.getDayOfMonth()==dateTime.getDayOfMonth()){
            simpleDateFormat=new SimpleDateFormat(pattern_Hm);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return simpleDateFormat.format(new Date(time)).toString();
        }else if(now.getMonth()==dateTime.getMonth()){//如果同月就显示日期
            simpleDateFormat=new SimpleDateFormat(pattern_Day);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return simpleDateFormat.format(new Date(time)).toString();
        } else if (now.getYear()==dateTime.getYear()) {
            simpleDateFormat=new SimpleDateFormat(pattern_M);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return simpleDateFormat.format(new Date(time)).toString();
        }else {
            simpleDateFormat=new SimpleDateFormat(pattern_year);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return simpleDateFormat.format(new Date(time)).toString();
        }*/
        simpleDateFormat=new SimpleDateFormat(pattern_Default);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return simpleDateFormat.format(new Date(time)).toString();
        //如果同年就显示月份日期
        // 将当前时间格式化为字符串
        //String formattedDate = simpleDateFormat.format(currentDate);
        //return "";
    }
}
