package org.example.trungcapphuongnam.common.exception;


import org.springframework.http.HttpStatus;

public enum ErrorCode {

    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy dữ liệu"),
    DUPLICATE_RESOURCE(HttpStatus.BAD_REQUEST, "Dữ liệu đã tồn tại"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Yêu cầu không hợp lệ"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}