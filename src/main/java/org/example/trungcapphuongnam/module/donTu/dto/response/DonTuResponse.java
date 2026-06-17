package org.example.trungcapphuongnam.module.donTu.dto.response;

import lombok.*;
import org.example.trungcapphuongnam.module.donTu.enums.DonTuTrangThai;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonTuResponse {

    private Long id;
    private String maDon;

    private Long loaiDonTuId;
    private String loaiDonTuMa;
    private String loaiDonTuTen;

    private Long sinhVienId;
    private String maSinhVien;
    private String hoTenSinhVien;

    private String tieuDe;
    private String noiDung;
    private String lyDo;
    private String duLieu;

    private DonTuTrangThai trangThai;

    private LocalDateTime ngayGui;
    private LocalDateTime ngayTiepNhan;
    private LocalDateTime ngayDuyet;
    private LocalDateTime ngayTuChoi;
    private LocalDateTime ngayHoanTat;

    private Long nguoiTiepNhanId;
    private Long nguoiDuyetId;

    private String yKienXuLy;
    private String lyDoTuChoi;

    private String doiTuongLienQuanType;
    private Long doiTuongLienQuanId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
