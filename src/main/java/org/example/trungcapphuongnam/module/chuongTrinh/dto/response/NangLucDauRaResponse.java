package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;
import lombok.*;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NangLucDauRaResponse {

    private Long id;

    private Long syllabusChuongTrinhId;
    private Long chuongTrinhVersionId;

    private String ma;

    private String noiDung;

    private String loai;

    private Integer thuTu;

    private String ghiChu;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
