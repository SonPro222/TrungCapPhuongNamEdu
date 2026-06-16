package org.example.trungcapphuongnam.module.donTu.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonTuCreateRequest {

    private Long loaiDonTuId;

    private String loaiDonTuMa;

    @NotNull(message = "Sinh viên không được để trống")
    private Long sinhVienId;

    private String tieuDe;

    private String noiDung;

    private String lyDo;

    private String duLieu;
}
