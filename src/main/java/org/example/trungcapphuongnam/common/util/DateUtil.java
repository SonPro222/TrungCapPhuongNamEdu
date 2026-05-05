package org.example.trungcapphuongnam.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//dùng cho lịch học
public class DateUtil {

    public static String format(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public static LocalDateTime parse(String date) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}