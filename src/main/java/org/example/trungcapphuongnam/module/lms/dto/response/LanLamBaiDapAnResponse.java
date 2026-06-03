package org.example.trungcapphuongnam.module.lms.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LanLamBaiDapAnResponse {
    private Long id;

    private Long lanLamBaiCauHoiId;

    private Long dapAnCauHoiVersionId;

    private String noiDungSnapshot;

    private Boolean laDapAnDungSnapshot;

    private Integer thuTu;

}
