package org.wj.letsrock.utils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-19:14
 **/
public class DateUtil {
    public static final DateTimeFormatter UTC_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public static final DateTimeFormatter DB_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    public static final DateTimeFormatter BLOG_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm");

    public static final DateTimeFormatter BLOG_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

    /**
     * 一天对应的毫秒数
     */
    public static final Long ONE_DAY_MILL = 86400_000L;
    public static final Long ONE_DAY_SECONDS = 86400L;
    public static final Long ONE_MONTH_SECONDS = 31 * 86400L;


    public static final Long THREE_DAY_MILL = 3 * ONE_DAY_MILL;

    /**
     * 毫秒转日期
     *
     * @param timestamp
     * @return
     */
    public static String time2day(long timestamp) {
        return format(BLOG_TIME_FORMAT, timestamp);
    }

    public static String time2day(Timestamp timestamp) {
        return time2day(timestamp.getTime());
    }

    public static LocalDateTime time2LocalTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    public static String time2utc(long timestamp) {
        return format(UTC_FORMAT, timestamp);
    }

    public static String time2date(long timestamp) {
        return format(BLOG_DATE_FORMAT, timestamp);
    }

    public static String time2date(Timestamp timestamp) {
        return time2date(timestamp.getTime());
    }


    public static String format(DateTimeFormatter format, long timestamp) {
        LocalDateTime time = time2LocalTime(timestamp);
        return format.format(time);
    }
    public static long calculateDaysSince(Date createTime) {
        // 1. 将Date转换为LocalDate（忽略时间部分）
        LocalDate createDate = createTime.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        // 2. 获取当前日期（系统默认时区）
        LocalDate currentDate = LocalDate.now();

        // 3. 计算天数差
        return ChronoUnit.DAYS.between(createDate, currentDate);
    }
}
