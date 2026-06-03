package org.example.trungcapphuongnam.module.lms.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LanLamBaiDapAnRequest {
    private Long lanLamBaiCauHoiId;

    private Long dapAnCauHoiVersionId;

    private String noiDungSnapshot;

    private Boolean laDapAnDungSnapshot;

    private Integer thuTu;

}
