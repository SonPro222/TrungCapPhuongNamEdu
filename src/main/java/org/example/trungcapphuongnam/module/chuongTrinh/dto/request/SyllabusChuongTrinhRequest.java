package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;
import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusChuongTrinhRequest {

    @NotNull(message = "chuong_trinh_version_id không được để trống")
    private Long chuongTrinhVersionId;

    private String moTaTongQuan;

    private String mucDich;

    private String yeuCauDaoTao;

    private String phuongPhapDaoTao;

    private String ghiChu;

}
