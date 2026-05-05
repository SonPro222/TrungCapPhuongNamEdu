package org.example.trungcapphuongnam.common.exception;


public final class ExceptionMessage {

    private ExceptionMessage() {}

    // ================= AUTH =================
    public static final String UNAUTHORIZED =
            "Chưa đăng nhập hoặc token không hợp lệ";

    public static final String FORBIDDEN =
            "Không có quyền truy cập tài nguyên này";

    public static final String INVALID_CREDENTIALS =
            "Sai tài khoản hoặc mật khẩu";

    // ================= TOKEN =================
    public static final String TOKEN_EXPIRED =
            "Token đã hết hạn";

    public static final String TOKEN_INVALID =
            "Token không hợp lệ";

    // ================= USER =================
    public static final String USER_NOT_FOUND =
            "Không tìm thấy người dùng";

    public static final String USER_ALREADY_EXISTS =
            "Người dùng đã tồn tại";

    // ================= GENERAL =================
    public static final String BAD_REQUEST =
            "Dữ liệu không hợp lệ";

    public static final String INTERNAL_ERROR =
            "Lỗi hệ thống, vui lòng thử lại sau";

    // ================= ACCESS =================
    public static final String ACCESS_DENIED =
            "Không có quyền truy cập tài nguyên này";
}