package org.example.trungcapphuongnam.common.util;

public final class TextUtil {
    private TextUtil() {}

    public static String trimToNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    public static String trimRequired(String value) {
        String trimmed = trimToNull(value);
        if (trimmed == null) {
            throw new IllegalArgumentException("Giá trị không được để trống");
        }
        return trimmed;
    }
}
