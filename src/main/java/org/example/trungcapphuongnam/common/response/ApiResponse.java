package org.example.trungcapphuongnam.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder().success(true).message("OK").data(data).build();
    }

    public static <T> ApiResponse<T> created(T data) {
        return ApiResponse.<T>builder().success(true).message("CREATED").data(data).build();
    }

    public static <T> ApiResponse<T> fail(String message) {
        return ApiResponse.<T>builder().success(false).message(message).data(null).build();
    }
    public static <T> ApiResponse<T> deleted() {
        return ApiResponse.<T>builder()
                .success(true)
                .message("DELETED")
                .data(null)
                .build();
    }
}
