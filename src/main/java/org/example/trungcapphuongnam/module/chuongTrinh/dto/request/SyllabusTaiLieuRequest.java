package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;
import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusTaiLieuRequest {

    @NotNull(message = "syllabus_mon_id không được để trống")
    private Long syllabusMonId;
    private Boolean batBuoc;

    private Integer thuTu;
    @NotBlank(message = "ten không được để trống")
    private String ten;

    private String tacGia;

    private Integer namXuatBan;

    private String nhaXuatBan;

    private String loai;

    private String ghiChu;

    private String ma;

    private String duongDan;


}
