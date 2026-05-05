package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NangLucDauRaResponse {

    private Long id;

    private Long chuongTrinhVersionId;

    private String ma;

    private String noiDung;

    private String loai;

    private Integer thuTu;

}
