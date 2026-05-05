package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;
import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusTaiLieuRequest {

    @NotNull(message = "syllabus_mon_id không được để trống")
    private Long syllabusMonId;

    @NotBlank(message = "ten không được để trống")
    private String ten;

    private String tacGia;

    private Integer namXuatBan;

    private String nhaXuatBan;

    private String loai;

    private String ghiChu;

}
