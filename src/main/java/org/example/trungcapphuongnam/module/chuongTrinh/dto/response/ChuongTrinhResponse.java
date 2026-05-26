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

    private Long nganhLoaiChuongTrinhId;

    private Long nganhId;

    private Long trinhDoId;

    private Long loaiChuongTrinhId;

    private String maChuongTrinh;

    private String tenChuongTrinh;

    private String doiTuongTuyenSinh;

    private String thoiGianDaoTao;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
