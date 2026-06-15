package org.example.trungcapphuongnam.module.giangDay.mapper;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.daoTao.repository.KhungKyRepository;
import org.example.trungcapphuongnam.module.giangDay.dto.request.GiangVienDangKyGiangDayRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.GiangVienDangKyGiangDayResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.GiangVienDangKyGiangDay;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GiangVienDangKyGiangDayMapper {

    private final GiaoVienRepository giaoVienRepository;
    private final KhungKyRepository khungKyRepository;

    public GiangVienDangKyGiangDay toEntity(GiangVienDangKyGiangDayRequest request) {
        if (request == null) return null;
        return GiangVienDangKyGiangDay.builder()
                .giaoVienId(request.getGiaoVienId())
                .khungKyId(request.getKhungKyId())
                .soTietDangKy(request.getSoTietDangKy())
                .soTietDaPhanCong(0)
                .trangThai(request.getTrangThai() != null ? request.getTrangThai() : "dang_hieu_luc")
                .ghiChu(request.getGhiChu())
                .build();
    }

    public GiangVienDangKyGiangDayResponse toResponse(GiangVienDangKyGiangDay entity) {
        if (entity == null) return null;
        int soTietDangKy = entity.getSoTietDangKy() != null ? entity.getSoTietDangKy() : 0;
        int soTietDaPhanCong = entity.getSoTietDaPhanCong() != null ? entity.getSoTietDaPhanCong() : 0;

        GiangVienDangKyGiangDayResponse response = GiangVienDangKyGiangDayResponse.builder()
                .id(entity.getId())
                .giaoVienId(entity.getGiaoVienId())
                .khungKyId(entity.getKhungKyId())
                .soTietDangKy(soTietDangKy)
                .soTietDaPhanCong(soTietDaPhanCong)
                .soTietConLai(soTietDangKy - soTietDaPhanCong)
                .trangThai(entity.getTrangThai())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();

        if (entity.getGiaoVienId() != null) {
            giaoVienRepository.findById(entity.getGiaoVienId()).ifPresent(gv -> {
                response.setMaGiaoVien(gv.getMaGiaoVien());
                response.setHoTenGiaoVien(gv.getHoTen());
            });
        }

        if (entity.getKhungKyId() != null) {
            khungKyRepository.findById(entity.getKhungKyId()).ifPresent(ky -> {
                response.setMaKy(ky.getMaKy());
                response.setTenKy(ky.getTenKy());
            });
        }

        return response;
    }

    public void updateEntity(GiangVienDangKyGiangDay entity, GiangVienDangKyGiangDayRequest request) {
        if (entity == null || request == null) return;
        entity.setGiaoVienId(request.getGiaoVienId());
        entity.setKhungKyId(request.getKhungKyId());
        entity.setSoTietDangKy(request.getSoTietDangKy());
        if (request.getTrangThai() != null) entity.setTrangThai(request.getTrangThai());
        entity.setGhiChu(request.getGhiChu());
    }
}
