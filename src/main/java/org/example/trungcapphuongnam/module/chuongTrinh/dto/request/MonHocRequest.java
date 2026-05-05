package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;
import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonHocRequest {

    private String maMon;

    @NotBlank(message = "ten_mon không được để trống")
    private String tenMon;

    private String moTa;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
