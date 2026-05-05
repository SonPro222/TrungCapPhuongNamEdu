package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DieuKienTotNghiepResponse {

    private Long id;

    private Long chuongTrinhVersionId;

    private String noiDung;

    private Integer thuTu;

}
