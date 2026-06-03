package org.example.trungcapphuongnam.module.giangDay;

import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice(basePackages = "org.example.trungcapphuongnam.module.giangDay")
public class GiangDayExceptionHandler {

    // 404 - không tìm thấy bản ghi
    @ExceptionHandler(GiangDayNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(GiangDayNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.fail(ex.getMessage()));
    }

    // 400 - LỖI NGHIỆP VỤ TỪ VALIDATOR (đây là cái trước đây bị rớt nên FE không thấy thông báo)
    @ExceptionHandler(GiangDayException.class)
    public ResponseEntity<ApiResponse<Void>> handleGiangDay(GiangDayException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.fail(ex.getMessage()));
    }

    // 400 - tham số sai
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.fail(ex.getMessage()));
    }

    // 400 - body JSON sai định dạng hoặc enum không hợp lệ (vd: vaiTro, loaiLopHocPhan, trạng thái sai giá trị)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.fail(
                "Dữ liệu gửi lên không hợp lệ hoặc sai định dạng (kiểm tra vai trò, loại lớp, trạng thái, ngày tháng)."));
    }

    // 400 - sai kiểu tham số trên URL/query (vd: ?vaiTro=abc, id không phải số)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.fail(
                "Giá trị tham số '" + ex.getName() + "' không hợp lệ."));
    }

    // 400 - lỗi @Valid trên DTO (phòng khi sau này bật bean validation), trả đúng message của field đầu tiên
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValid(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "Dữ liệu không hợp lệ";
        return ResponseEntity.badRequest().body(ApiResponse.fail(message));
    }

    // 500 - các lỗi không lường trước: trả thông báo chung, không lộ chi tiết nội bộ
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(
                "Đã xảy ra lỗi không mong muốn ở máy chủ. Vui lòng thử lại hoặc liên hệ quản trị."));
    }
}