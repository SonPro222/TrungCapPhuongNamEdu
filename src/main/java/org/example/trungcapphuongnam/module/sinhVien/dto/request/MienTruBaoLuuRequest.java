package org.example.trungcapphuongnam.module.sinhVien.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.trungcapphuongnam.common.enums.SinhVien.MienTruBaoLuuLoai;
import org.example.trungcapphuongnam.common.enums.SinhVien.PheDuyetTrangThai;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MienTruBaoLuuRequest {
    @NotNull(message = "Sinh viên không được để trống")
    private Long sinhVienId;

    @NotNull(message = "Môn trong chương trình không được để trống")
    private Long chuongTrinhMonId;

    @NotNull(message = "Loại miễn trừ/bảo lưu không được để trống")
    private MienTruBaoLuuLoai loai;

    private String lyDo;
    private String minhChung;
    private PheDuyetTrangThai trangThai;
    private Long nguoiDuyetId;
    private LocalDateTime ngayDuyet;
    private String ghiChu;
}
