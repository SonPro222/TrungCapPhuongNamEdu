package org.example.trungcapphuongnam.common.exception;

import jakarta.validation.ConstraintViolationException;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.heThong.HeThongException;
import org.example.trungcapphuongnam.module.heThong.HeThongNotFoundException;
import org.example.trungcapphuongnam.module.sinhVien.SinhVienException;
import org.example.trungcapphuongnam.module.sinhVien.SinhVienNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Objects;
import java.util.stream.Collectors;
import org.example.trungcapphuongnam.module.giangDay.GiangDayException;
import org.example.trungcapphuongnam.module.giangDay.GiangDayNotFoundException;
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({
            ResourceNotFoundException.class,
            HeThongNotFoundException.class,
            SinhVienNotFoundException.class,
            GiangDayNotFoundException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleNotFound(RuntimeException ex) {
        log.warn("NOT_FOUND type={} message={}", ex.getClass().getSimpleName(), ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail(ex.getMessage()));
    }

    @ExceptionHandler({
            BadRequestException.class,
            DuplicateResourceException.class,
            HeThongException.class,
            SinhVienException.class,
            GiangDayException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(RuntimeException ex) {
        log.warn("BAD_REQUEST type={} message={}", ex.getClass().getSimpleName(), ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> tenTruongTiengViet(e.getField()) + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));

        if (message == null || message.trim().isEmpty()) {
            message = "Dữ liệu gửi lên không hợp lệ";
        }

        log.warn("VALIDATION_ERROR message={}", message);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(message));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getMessage())
                .filter(Objects::nonNull)
                .collect(Collectors.joining("; "));

        if (message == null || message.trim().isEmpty()) {
            message = "Dữ liệu gửi lên không hợp lệ";
        }

        log.warn("CONSTRAINT_VIOLATION message={}", message);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(message));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingParam(MissingServletRequestParameterException ex) {
        String message = "Thiếu tham số bắt buộc: " + tenTruongTiengViet(ex.getParameterName());
        log.warn("MISSING_PARAMETER message={}", message);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(message));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = "Dữ liệu không đúng định dạng: " + tenTruongTiengViet(ex.getName());

        Class<?> requiredType = ex.getRequiredType();
        if (requiredType != null && Long.class.equals(requiredType)) {
            message = tenTruongTiengViet(ex.getName()) + " phải là số";
        }

        log.warn("TYPE_MISMATCH field={} value={} message={}", ex.getName(), ex.getValue(), message);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        log.warn("MESSAGE_NOT_READABLE message={}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail("Dữ liệu gửi lên không đúng định dạng hoặc thiếu nội dung"));
    }

    @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
    public ResponseEntity<ApiResponse<Void>> handleApiNotFound(Exception ex) {
        String path = null;

        if (ex instanceof NoResourceFoundException noResourceFoundException) {
            path = noResourceFoundException.getResourcePath();
        }

        if (ex instanceof NoHandlerFoundException noHandlerFoundException) {
            path = noHandlerFoundException.getRequestURL();
        }

        String message = "Đường dẫn API không tồn tại";
        if (path != null && !path.trim().isEmpty()) {
            message = "Đường dẫn API không tồn tại: /" + path.replaceFirst("^/+", "");
        }

        log.warn("API_NOT_FOUND message={}", message);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail(message));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        String method = ex.getMethod() == null ? "" : ex.getMethod();
        String message = "Phương thức " + method + " không được hỗ trợ cho đường dẫn này";

        log.warn("METHOD_NOT_SUPPORTED message={}", message);

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponse.fail(message));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        log.warn("MEDIA_TYPE_NOT_SUPPORTED message={}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(ApiResponse.fail("Kiểu dữ liệu gửi lên không được hỗ trợ"));
    }

    @ExceptionHandler({MultipartException.class, MaxUploadSizeExceededException.class})
    public ResponseEntity<ApiResponse<Void>> handleMultipart(Exception ex) {
        log.warn("MULTIPART_ERROR type={} message={}", ex.getClass().getSimpleName(), ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail("File upload không hợp lệ hoặc vượt quá dung lượng cho phép"));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrity(DataIntegrityViolationException ex) {
        String rawMessage = ex.getMostSpecificCause() == null ? ex.getMessage() : ex.getMostSpecificCause().getMessage();
        String message = thongBaoRangBuocDuLieu(rawMessage);

        log.warn("DATA_INTEGRITY_ERROR raw={} message={}", rawMessage, message);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        log.error("SYSTEM_ERROR type={} message={}", ex.getClass().getSimpleName(), ex.getMessage(), ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail("Hệ thống đang gặp lỗi. Vui lòng thử lại hoặc liên hệ quản trị viên."));
    }

    private String thongBaoRangBuocDuLieu(String rawMessage) {
        if (rawMessage == null || rawMessage.trim().isEmpty()) {
            return "Dữ liệu đang bị ràng buộc hoặc bị trùng khóa";
        }

        String msg = rawMessage.toLowerCase();

        if (msg.contains("foreign key") || msg.contains("violates foreign key constraint")) {
            return "Không thể thực hiện vì dữ liệu đang liên kết với dữ liệu khác";
        }

        if (msg.contains("duplicate") || msg.contains("unique") || msg.contains("violates unique constraint")) {
            return "Dữ liệu đã tồn tại, vui lòng kiểm tra lại thông tin bị trùng";
        }

        if (msg.contains("not-null") || msg.contains("null value")) {
            return "Thiếu dữ liệu bắt buộc, vui lòng kiểm tra lại thông tin";
        }

        return "Dữ liệu đang bị ràng buộc hoặc bị trùng khóa";
    }

    private String tenTruongTiengViet(String field) {
        if (field == null || field.trim().isEmpty()) {
            return "Dữ liệu";
        }

        return switch (field) {
            case "id" -> "ID";
            case "nganhId" -> "Ngành";
            case "chuongTrinhId" -> "Chương trình";
            case "chuongTrinhVersionId" -> "Version chương trình";
            case "sinhVienId" -> "Sinh viên";
            case "lopHocPhanId" -> "Lớp học phần";
            case "chuongTrinhMonId" -> "Môn trong chương trình";
            case "hoTen" -> "Họ tên";
            case "email" -> "Gmail";
            case "soDienThoai" -> "Số điện thoại";
            case "ngaySinh" -> "Ngày sinh";
            case "gioiTinh" -> "Giới tính";
            case "diaChi" -> "Địa chỉ";
            case "diaChiThuongTru" -> "Địa chỉ thường trú";
            case "soCccd" -> "Số CCCD";
            case "ngayCapCccd" -> "Ngày cấp CCCD";
            case "noiCapCccd" -> "Nơi cấp CCCD";
            case "bangCap" -> "Bằng cấp";
            case "namTotNghiep" -> "Năm tốt nghiệp";
            case "truongTotNghiep" -> "Trường tốt nghiệp";
            case "hoTenCha" -> "Họ tên cha";
            case "sdtCha" -> "Số điện thoại cha";
            case "hoTenMe" -> "Họ tên mẹ";
            case "sdtMe" -> "Số điện thoại mẹ";
            case "hoTenNguoiThan" -> "Họ tên người thân";
            case "quanHeNguoiThan" -> "Quan hệ người thân";
            case "sdtNguoiThan" -> "Số điện thoại người thân";
            case "emailNguoiThan" -> "Gmail người thân";
            case "module" -> "Module";
            case "nghiepVu" -> "Nghiệp vụ";
            case "doiTuongId" -> "Đối tượng nghiệp vụ";
            case "nguoiGuiLoai" -> "Loại người gửi";
            case "nguoiGuiId" -> "Người gửi";
            case "nguoiGuiTen" -> "Tên người gửi";
            default -> field;
        };
    }
}
