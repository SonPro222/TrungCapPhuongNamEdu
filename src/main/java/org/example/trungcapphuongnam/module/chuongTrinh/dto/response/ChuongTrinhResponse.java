package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhResponse {

    private Long id;

    private Long nganhHeDaoTaoId;
    private String maHe;
    private String tenHe;

    private Long nganhId;
    private String maNganh;
    private String tenNganh;

    private Integer soThang;
    private Integer soKy;

    private String maChuongTrinh;

    private String tenChuongTrinh;

    private String doiTuongTuyenSinh;

    private String thoiGianDaoTao;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
