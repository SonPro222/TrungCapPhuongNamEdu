package org.example.trungcapphuongnam.module.lms.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.KetQuaDat;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiKetQuaBaiTap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KetQuaBaiTapLopResponse {

    private Long id;

    private Long baiTapLopId;

    private Long sinhVienId;

    private Long lanLamDuocChonId;

    private BigDecimal diemHeThong;

    private BigDecimal diemGiaoVienChinhSua;

    private BigDecimal diemCuoiCung;

    private KetQuaDat ketQua;

    private TrangThaiKetQuaBaiTap trangThai;

    private Long diemChiTietId;

    private String nhanXet;

    private LocalDateTime ngayTinhDiem;

    private LocalDateTime ngayChot;

    private Long nguoiChotId;

    private LocalDateTime ngayDaySangDiem;

    private Long nguoiDaySangDiemId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
