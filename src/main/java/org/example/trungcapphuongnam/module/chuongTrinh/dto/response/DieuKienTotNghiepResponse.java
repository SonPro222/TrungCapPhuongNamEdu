package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;
import lombok.*;
import java.time.LocalDateTime;


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

    private String ma;

    private String ghiChu;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
