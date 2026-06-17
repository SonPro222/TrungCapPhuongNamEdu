package org.example.trungcapphuongnam.module.sinhVien.dto.response;


import lombok.*;
import org.example.trungcapphuongnam.module.sinhVien.enums.MienTruBaoLuuLoai;
import org.example.trungcapphuongnam.module.sinhVien.enums.PheDuyetTrangThai;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MienTruBaoLuuResponse {
    private Long id;
    private Long sinhVienId;
    private String maSinhVien;
    private String hoTen;
    private Long chuongTrinhMonId;
    private MienTruBaoLuuLoai loai;
    private String lyDo;
    private String minhChung;
    private PheDuyetTrangThai trangThai;
    private Long nguoiDuyetId;
    private LocalDateTime ngayDuyet;
    private String ghiChu;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
