package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViTriViecLamResponse {

    private Long id;

    private Long chuongTrinhVersionId;

    private String ten;

    private String moTa;

    private Integer thuTu;

}
