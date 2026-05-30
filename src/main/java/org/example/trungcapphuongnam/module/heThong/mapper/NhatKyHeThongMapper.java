package org.example.trungcapphuongnam.module.heThong.mapper;

import org.example.trungcapphuongnam.module.heThong.dto.request.NhatKyHeThongRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.NhatKyHeThongResponse;
import org.example.trungcapphuongnam.module.heThong.entity.NhatKyHeThong;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoan;
import org.springframework.stereotype.Component;

@Component
public class NhatKyHeThongMapper {

    public NhatKyHeThongResponse toResponse(NhatKyHeThong entity) {
        if (entity == null) {
            return null;
        }

        return NhatKyHeThongResponse.builder()
                .id(entity.getId())
                .taiKhoanId(entity.getTaiKhoan() == null ? null : entity.getTaiKhoan().getId())
                .taiKhoanEmail(entity.getTaiKhoanEmail())
                .tenTaiKhoan(entity.getTenTaiKhoan())
                .loaiTaiKhoan(entity.getLoaiTaiKhoan())
                .doiTuongLoai(entity.getDoiTuongLoai())
                .doiTuongId(entity.getDoiTuongId())
                .doiTuongMa(entity.getDoiTuongMa())
                .doiTuongTen(entity.getDoiTuongTen())
                .doiTuongEmail(entity.getDoiTuongEmail())
                .moduleNghiepVu(entity.getModuleNghiepVu())
                .chucNang(entity.getChucNang())
                .moTa(entity.getMoTa())
                .phuongThuc(entity.getPhuongThuc())
                .duongDan(entity.getDuongDan())
                .ketQua(entity.getKetQua())
                .hanhDong(entity.getHanhDong())
                .bangTacDong(entity.getBangTacDong())
                .banGhiId(entity.getBanGhiId())
                .banGhiMa(entity.getBanGhiMa())
                .banGhiTen(entity.getBanGhiTen())
                .noiDungCu(entity.getNoiDungCu())
                .noiDungMoi(entity.getNoiDungMoi())
                .duLieuTimKiem(entity.getDuLieuTimKiem())
                .ipAddress(entity.getIpAddress())
                .userAgent(entity.getUserAgent())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public NhatKyHeThong toEntity(NhatKyHeThongRequest request) {
        if (request == null) {
            return null;
        }

        NhatKyHeThong entity = new NhatKyHeThong();
        updateEntity(entity, request);
        return entity;
    }

    public void updateEntity(NhatKyHeThong entity, NhatKyHeThongRequest request) {
        if (entity == null || request == null) {
            return;
        }

        if (request.getTaiKhoanId() != null) {
            TaiKhoan taiKhoan = new TaiKhoan();
            taiKhoan.setId(request.getTaiKhoanId());
            entity.setTaiKhoan(taiKhoan);
        } else {
            entity.setTaiKhoan(null);
        }

        entity.setHanhDong(request.getHanhDong());
        entity.setBangTacDong(request.getBangTacDong());
        entity.setBanGhiId(request.getBanGhiId());
        entity.setBanGhiMa(request.getBanGhiMa());
        entity.setBanGhiTen(request.getBanGhiTen());
        entity.setNoiDungCu(request.getNoiDungCu());
        entity.setNoiDungMoi(request.getNoiDungMoi());
        entity.setDuLieuTimKiem(request.getDuLieuTimKiem());
        entity.setIpAddress(request.getIpAddress());
        entity.setUserAgent(request.getUserAgent());
        entity.setTaiKhoanEmail(request.getTaiKhoanEmail());
        entity.setTenTaiKhoan(request.getTenTaiKhoan());
        entity.setLoaiTaiKhoan(request.getLoaiTaiKhoan());
        entity.setDoiTuongLoai(request.getDoiTuongLoai());
        entity.setDoiTuongId(request.getDoiTuongId());
        entity.setDoiTuongMa(request.getDoiTuongMa());
        entity.setDoiTuongTen(request.getDoiTuongTen());
        entity.setDoiTuongEmail(request.getDoiTuongEmail());
        entity.setModuleNghiepVu(request.getModuleNghiepVu());
        entity.setChucNang(request.getChucNang());
        entity.setMoTa(request.getMoTa());
        entity.setPhuongThuc(request.getPhuongThuc());
        entity.setDuongDan(request.getDuongDan());
        entity.setKetQua(request.getKetQua());
    }
}