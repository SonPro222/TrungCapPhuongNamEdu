package org.example.trungcapphuongnam.common.util;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public final class GiangDayValidateUtil {
    private GiangDayValidateUtil() {}

    public static void requireEndTimeAfterStart(LocalTime start, LocalTime end, String message) {
        if (start != null && end != null && !end.isAfter(start)) throw new IllegalArgumentException(message);
    }

    public static void requireEndDateNotBeforeStart(LocalDate start, LocalDate end, String message) {
        if (start != null && end != null && end.isBefore(start)) throw new IllegalArgumentException(message);
    }

    public static void requireEndDateTimeAfterStart(LocalDateTime start, LocalDateTime end, String message) {
        if (start != null && end != null && end.isBefore(start)) throw new IllegalArgumentException(message);
    }

    public static void requireNonNegative(Integer value, String field) {
        if (value != null && value < 0) throw new IllegalArgumentException(field + " không được âm");
    }
}
