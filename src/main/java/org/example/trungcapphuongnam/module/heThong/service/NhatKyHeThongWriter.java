package org.example.trungcapphuongnam.module.heThong.service;

public interface NhatKyHeThongWriter {

    void ghi(
            String hanhDong,
            String bangTacDong,
            Long banGhiId,
            String noiDungCu,
            String noiDungMoi
    );

    void ghiChiTiet(
            String moduleNghiepVu,
            String chucNang,
            String hanhDong,
            String bangTacDong,
            Long banGhiId,
            String banGhiMa,
            String banGhiTen,
            String noiDungCu,
            String noiDungMoi,
            String moTa
    );
}