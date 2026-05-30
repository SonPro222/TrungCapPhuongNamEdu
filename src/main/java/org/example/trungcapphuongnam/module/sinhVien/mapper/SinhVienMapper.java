package org.example.trungcapphuongnam.module.sinhVien.mapper;

import org.example.trungcapphuongnam.module.sinhVien.dto.request.SinhVienRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.request.TiepNhanSinhVienRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.SinhVienResponse;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVien;
import org.springframework.stereotype.Component;

@Component
public class SinhVienMapper {

    public SinhVien toEntity(SinhVienRequest request) {
        if (request == null) return null;
        SinhVien entity = SinhVien.builder().build();
        updateEntity(entity, request);
        return entity;
    }

    public SinhVien toEntity(TiepNhanSinhVienRequest request, String maSinhVien, Long taiKhoanId) {
        if (request == null) return null;
        return SinhVien.builder()
                .maSinhVien(maSinhVien)
                .hoTen(request.getHoTen())
                .ngaySinh(request.getNgaySinh())
                .gioiTinh(request.getGioiTinh())
                .email(request.getEmail())
                .soDienThoai(request.getSoDienThoai())
                .diaChi(request.getDiaChi())
                .diaChiThuongTru(request.getDiaChiThuongTru())
                .soCccd(request.getSoCccd())
                .ngayCapCccd(request.getNgayCapCccd())
                .noiCapCccd(request.getNoiCapCccd())
                .anhChanDungTepId(request.getAnhChanDungTepId())
                .cccdTruocTepId(request.getCccdTruocTepId())
                .cccdSauTepId(request.getCccdSauTepId())
                .bangCapTepId(request.getBangCapTepId())
                .bangCap(request.getBangCap())
                .namTotNghiep(request.getNamTotNghiep())
                .truongTotNghiep(request.getTruongTotNghiep())
                .hoTenCha(request.getHoTenCha())
                .ngheNghiepCha(request.getNgheNghiepCha())
                .sdtCha(request.getSdtCha())
                .emailCha(request.getEmailCha())
                .hoTenMe(request.getHoTenMe())
                .ngheNghiepMe(request.getNgheNghiepMe())
                .sdtMe(request.getSdtMe())
                .emailMe(request.getEmailMe())
                .hoTenNguoiThan(request.getHoTenNguoiThan())
                .quanHeNguoiThan(request.getQuanHeNguoiThan())
                .sdtNguoiThan(request.getSdtNguoiThan())
                .emailNguoiThan(request.getEmailNguoiThan())
                .ghiChuHoSo(request.getGhiChuHoSo())
                .taiKhoanId(taiKhoanId)
                .build();
    }

    public void updateEntity(SinhVien entity, SinhVienRequest request) {
        entity.setMaSinhVien(request.getMaSinhVien());
        entity.setHoTen(request.getHoTen());
        entity.setNgaySinh(request.getNgaySinh());
        entity.setGioiTinh(request.getGioiTinh());
        entity.setEmail(request.getEmail());
        entity.setSoDienThoai(request.getSoDienThoai());
        entity.setDiaChi(request.getDiaChi());
        entity.setDiaChiThuongTru(request.getDiaChiThuongTru());
        entity.setSoCccd(request.getSoCccd());
        entity.setNgayCapCccd(request.getNgayCapCccd());
        entity.setNoiCapCccd(request.getNoiCapCccd());
        entity.setAnhChanDungTepId(request.getAnhChanDungTepId());
        entity.setCccdTruocTepId(request.getCccdTruocTepId());
        entity.setCccdSauTepId(request.getCccdSauTepId());
        entity.setBangCapTepId(request.getBangCapTepId());
        entity.setBangCap(request.getBangCap());
        entity.setNamTotNghiep(request.getNamTotNghiep());
        entity.setTruongTotNghiep(request.getTruongTotNghiep());
        entity.setHoTenCha(request.getHoTenCha());
        entity.setNgheNghiepCha(request.getNgheNghiepCha());
        entity.setSdtCha(request.getSdtCha());
        entity.setEmailCha(request.getEmailCha());
        entity.setHoTenMe(request.getHoTenMe());
        entity.setNgheNghiepMe(request.getNgheNghiepMe());
        entity.setSdtMe(request.getSdtMe());
        entity.setEmailMe(request.getEmailMe());
        entity.setHoTenNguoiThan(request.getHoTenNguoiThan());
        entity.setQuanHeNguoiThan(request.getQuanHeNguoiThan());
        entity.setSdtNguoiThan(request.getSdtNguoiThan());
        entity.setEmailNguoiThan(request.getEmailNguoiThan());
        entity.setGhiChuHoSo(request.getGhiChuHoSo());
        entity.setTrangThai(request.getTrangThai());
        entity.setTaiKhoanId(request.getTaiKhoanId());
    }

    public SinhVienResponse toResponse(SinhVien entity) {
        if (entity == null) return null;
        return SinhVienResponse.builder()
                .id(entity.getId())
                .maSinhVien(entity.getMaSinhVien())
                .hoTen(entity.getHoTen())
                .ngaySinh(entity.getNgaySinh())
                .gioiTinh(entity.getGioiTinh())
                .email(entity.getEmail())
                .soDienThoai(entity.getSoDienThoai())
                .diaChi(entity.getDiaChi())
                .diaChiThuongTru(entity.getDiaChiThuongTru())
                .soCccd(entity.getSoCccd())
                .ngayCapCccd(entity.getNgayCapCccd())
                .noiCapCccd(entity.getNoiCapCccd())
                .anhChanDungTepId(entity.getAnhChanDungTepId())
                .cccdTruocTepId(entity.getCccdTruocTepId())
                .cccdSauTepId(entity.getCccdSauTepId())
                .bangCapTepId(entity.getBangCapTepId())
                .bangCap(entity.getBangCap())
                .namTotNghiep(entity.getNamTotNghiep())
                .truongTotNghiep(entity.getTruongTotNghiep())
                .hoTenCha(entity.getHoTenCha())
                .ngheNghiepCha(entity.getNgheNghiepCha())
                .sdtCha(entity.getSdtCha())
                .emailCha(entity.getEmailCha())
                .hoTenMe(entity.getHoTenMe())
                .ngheNghiepMe(entity.getNgheNghiepMe())
                .sdtMe(entity.getSdtMe())
                .emailMe(entity.getEmailMe())
                .hoTenNguoiThan(entity.getHoTenNguoiThan())
                .quanHeNguoiThan(entity.getQuanHeNguoiThan())
                .sdtNguoiThan(entity.getSdtNguoiThan())
                .emailNguoiThan(entity.getEmailNguoiThan())
                .ghiChuHoSo(entity.getGhiChuHoSo())
                .trangThai(entity.getTrangThai())
                .taiKhoanId(entity.getTaiKhoanId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
