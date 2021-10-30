package com.jsy.basic.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class LocalTimeUtil {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    //  获得某天最大时间戳 2020-02-19 23:59:59
    public static long getEndOfDay(LocalDateTime date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.toInstant(ZoneOffset.of("+8")).toEpochMilli()), ZoneId.systemDefault());
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant()).getTime();
    }
    //  获得某天最小时间戳 2020-02-17 00:00:00
    public static long getStartOfDay(LocalDateTime date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.toInstant(ZoneOffset.of("+8")).toEpochMilli()), ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant()).getTime();
    }


    //  LocalDateTime 2021-2-21T00:00:00
    public static LocalDateTime getStart(int i){
        LocalDate today = LocalDate.now();
        LocalDate localDate = today.minusDays(i);
        long timestamp = localDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
        LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
        return localDateTime;
    }
    //  LocalDateTime 2021-2-21T23:59:59
    public static LocalDateTime getEnd(int i){
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime endOfDay = localDateTime.minusDays(i).with(LocalTime.MAX);
        return endOfDay;
    }


}
