package org.example.trungcapphuongnam.module.giangDay.mapper;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.daoTao.entity.KhungKy;
import org.example.trungcapphuongnam.module.daoTao.repository.KhungKyRepository;
import org.example.trungcapphuongnam.module.giangDay.dto.response.GiaoVienDangKyNhomCaResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.CaHoc;
import org.example.trungcapphuongnam.module.giangDay.entity.GiaoVien;
import org.example.trungcapphuongnam.module.giangDay.entity.GiaoVienDangKyNhomCa;
import org.example.trungcapphuongnam.module.giangDay.repository.CaHocRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GiaoVienDangKyNhomCaMapper {

    private final GiaoVienRepository giaoVienRepository;
    private final KhungKyRepository khungKyRepository;
    private final CaHocRepository caHocRepository;

    public GiaoVienDangKyNhomCaResponse toResponse(GiaoVienDangKyNhomCa entity) {
        GiaoVienDangKyNhomCaResponse res = new GiaoVienDangKyNhomCaResponse();
        res.setId(entity.getId());
        res.setGiaoVienId(entity.getGiaoVienId());
        res.setKhungKyId(entity.getKhungKyId());
        res.setThuTrongTuan(entity.getThuTrongTuan());
        res.setCaBatDauId(entity.getCaBatDauId());
        res.setCaKetThucId(entity.getCaKetThucId());
        res.setMucDoUuTien(entity.getMucDoUuTien());
        res.setGhiChu(entity.getGhiChu());
        res.setCreatedAt(entity.getCreatedAt());
        res.setUpdatedAt(entity.getUpdatedAt());

        // Lookup GiaoVien
        if (entity.getGiaoVienId() != null) {
            giaoVienRepository.findById(entity.getGiaoVienId()).ifPresent(gv -> {
                res.setMaGiaoVien(gv.getMaGiaoVien());
                res.setHoTenGiaoVien(gv.getHoTen());
            });
        }

        // Lookup KhungKy
        if (entity.getKhungKyId() != null) {
            khungKyRepository.findById(entity.getKhungKyId()).ifPresent(ky -> {
                res.setMaKy(ky.getMaKy());
                res.setTenKy(ky.getTenKy());
            });
        }

        // Lookup CaBatDau
        if (entity.getCaBatDauId() != null) {
            caHocRepository.findById(entity.getCaBatDauId()).ifPresent(ca -> {
                res.setTenCaBatDau(ca.getTenCa());
                res.setThuTuBatDau(ca.getThuTu());
            });
        }

        // Lookup CaKetThuc
        if (entity.getCaKetThucId() != null) {
            caHocRepository.findById(entity.getCaKetThucId()).ifPresent(ca -> {
                res.setTenCaKetThuc(ca.getTenCa());
                res.setThuTuKetThuc(ca.getThuTu());
            });
        }

        return res;
    }
}
