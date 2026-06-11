package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhomTuChonResponse {

    private Long id;

    private Long chuongTrinhVersionId;
    private Long nhomTuChonMauId;
    private String ten;

    private Integer soMonChon;

    private BigDecimal soTinChiCanDat;

    private String ghiChu;

    private String ma;

    private String moTa;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
