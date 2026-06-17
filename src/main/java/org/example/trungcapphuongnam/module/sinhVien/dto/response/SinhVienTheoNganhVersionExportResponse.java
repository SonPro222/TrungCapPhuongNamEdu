package org.example.trungcapphuongnam.module.sinhVien.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.trungcapphuongnam.module.sinhVien.enums.SinhVienTrangThai;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class SinhVienTheoNganhVersionExportResponse {

    private String maSinhVien;

    private String hoTen;

    private String gmail;

    private String tenNganh;

    private String chuongTrinh;

    private String version;

    private LocalDate ngayNhapHoc;

    private SinhVienTrangThai trangThai;
}