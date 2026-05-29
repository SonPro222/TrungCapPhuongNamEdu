package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;
import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DieuKienMonHocRequest {

    @NotNull(message = "syllabus_mon_id không được để trống")
    private Long syllabusMonId;
    @NotBlank(message = "ten không được để trống")
    private String ten;

    private Integer soLuong;

    private String yeuCau;
    @NotBlank(message = "loai không được để trống")
    private String loai;

    @NotBlank(message = "noi_dung không được để trống")
    private String noiDung;

    private Integer thuTu;

    private String ma;

    private String ghiChu;



}
