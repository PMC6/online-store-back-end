/**
 * @author： fanzhonghao
 * @date: 19-1-2 08 20
 * @version: 1.0
 * @description:
 */
package com.fenlan.spring.shop.service;

import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class TimeService {
    public Date[] timeSelector(int beforeNum, String type){
        Date[] dates = null;
        if (type.equals("monthly"))
        {
            dates = getMonthTime(beforeNum);
        }else if (type.equals("daily")){
            dates = getDayTime(beforeNum);
        }else if (type.equals("yearly")){
            dates = getYearTime(beforeNum);
        }else if (type.equals("weekly")){
            dates = getWeekTime(beforeNum);
        }
        return dates;
    }

    public Date getDate(String date, String type) throws Exception{
        if (type.equals("daily")){
            if (date.length() < 6) throw new Exception("date is lose");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
            calendar.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6)) - 1);
            calendar.set(Calendar.DATE, Integer.parseInt(date.substring(6, 8)));
            calendar = setTimeZero(calendar);
            return calendar.getTime();
        }else if (type.equals("weekly")){
            if (date.length() != 8) throw new Exception("date is not complete");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
            calendar.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6)) - 1);
            calendar.set(Calendar.DATE, Integer.parseInt(date.substring(6,8)));
            for (;!calendar.getTime().toString().substring(0,3).equals("Sun");){
                calendar.add(Calendar.DATE, -1);
            }
            calendar = setTimeZero(calendar);
            return calendar.getTime();
        }else if (type.equals("monthly")){
            if (date.length() < 6) throw new Exception("date is not complete");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
            calendar.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6)) - 1);
            calendar.set(Calendar.DATE, 1);
            calendar = setTimeZero(calendar);
            return calendar.getTime();
        }else if (type.equals("yearly")){
            if (date.length() < 4) throw new Exception("date is not complete");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.DATE, 1);
            calendar = setTimeZero(calendar);
            return calendar.getTime();
        }
        return null;
    }

    private Calendar setTimeZero(Calendar calendar){
        calendar.add(Calendar.HOUR, -calendar.getTime().getHours());
        calendar.add(Calendar.MINUTE, -calendar.getTime().getMinutes());
        calendar.add(Calendar.SECOND, -calendar.getTime().getSeconds());
        return calendar;
    }

    /**
     * 获取beforeMonthNum月之前月份的第一天和最后一天的时间
     * @param beforeMonthNum
     * @return
     */
    private Date[] getMonthTime(int beforeMonthNum){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 0-beforeMonthNum);
        cal.set(Calendar.DATE, 1);
        Date[] dates = new Date[2];
        cal.add(Calendar.HOUR, -cal.getTime().getHours());
        cal.add(Calendar.MINUTE, -cal.getTime().getMinutes());
        cal.add(Calendar.SECOND, -cal.getTime().getSeconds());
        dates[0] = cal.getTime();//要的月份的第一天
        cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.add(Calendar.DATE, 1);
        dates[1] = cal.getTime();//最后一天
        return dates;
    }


    private Date[] getYearTime(int yearNum){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 0-yearNum);
        cal.set(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_YEAR, 1);

        cal.add(Calendar.HOUR, -cal.getTime().getHours());
        cal.add(Calendar.MINUTE, -cal.getTime().getMinutes());
        cal.add(Calendar.SECOND, -cal.getTime().getSeconds());
        Date[] dates = new Date[2];
        dates[0] = cal.getTime();//第一天
        cal.add(Calendar.DAY_OF_YEAR, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_YEAR));
        dates[1] = cal.getTime();//最后一天
        return dates;
    }

    private Date[] getDayTime(long dayNum){
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        Long time = date.getTime();
        time -= 86400000 * dayNum;
        Date date1 = new  Date(time);
        calendar.setTime(date1);
        calendar.add(Calendar.HOUR, -calendar.getTime().getHours());
        calendar.add(Calendar.MINUTE, -calendar.getTime().getMinutes());
        calendar.add(Calendar.SECOND, -calendar.getTime().getSeconds());
        Date[] dates = new Date[2];
        dates[0] = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        dates[1] = calendar.getTime();
        return dates;
    }

    private Date[] getWeekTime(long weekNum){
        Calendar calendar = Calendar.getInstance();
        Date[] dates = new Date[2];
        Date date = new Date();
        Long time = date.getTime() - 86400000 * weekNum * 7;
        calendar.setTime(new Date(time));
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek() - day);
        calendar.add(Calendar.HOUR, -calendar.getTime().getHours());
        calendar.add(Calendar.MINUTE, -calendar.getTime().getMinutes());
        calendar.add(Calendar.SECOND, -calendar.getTime().getSeconds());
        dates[0] = calendar.getTime();
        calendar.add(Calendar.DATE, 7);
        dates[1] = calendar.getTime();
        return dates;
    }


}
