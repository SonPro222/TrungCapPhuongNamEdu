package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;
import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DieuKienMonHocRequest {

    @NotNull(message = "syllabus_mon_id không được để trống")
    private Long syllabusMonId;

    @NotBlank(message = "loai không được để trống")
    private String loai;

    @NotBlank(message = "noi_dung không được để trống")
    private String noiDung;

    private Integer thuTu;

}
