package org.example.trungcapphuongnam.module.giangDay.mapper;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.MonHocRepository;
import org.example.trungcapphuongnam.module.giangDay.dto.request.GiaoVienMonHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.GiaoVienMonHocResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.GiaoVienMonHoc;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GiaoVienMonHocMapper {

    private final GiaoVienRepository giaoVienRepository;
    private final MonHocRepository monHocRepository;

    public GiaoVienMonHoc toEntity(GiaoVienMonHocRequest request) {
        if (request == null) return null;
        return GiaoVienMonHoc.builder()
                .giaoVienId(request.getGiaoVienId())
                .monHocId(request.getMonHocId())
                .trangThai(request.getTrangThai() != null ? request.getTrangThai() : "dang_hieu_luc")
                .ghiChu(request.getGhiChu())
                .build();
    }

    public GiaoVienMonHocResponse toResponse(GiaoVienMonHoc entity) {
        if (entity == null) return null;
        GiaoVienMonHocResponse response = GiaoVienMonHocResponse.builder()
                .id(entity.getId())
                .giaoVienId(entity.getGiaoVienId())
                .monHocId(entity.getMonHocId())
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

        if (entity.getMonHocId() != null) {
            monHocRepository.findById(entity.getMonHocId()).ifPresent(mh -> {
                response.setMaMon(mh.getMaMon());
                response.setTenMon(mh.getTenMon());
            });
        }

        return response;
    }

    public void updateEntity(GiaoVienMonHoc entity, GiaoVienMonHocRequest request) {
        if (entity == null || request == null) return;
        entity.setGiaoVienId(request.getGiaoVienId());
        entity.setMonHocId(request.getMonHocId());
        if (request.getTrangThai() != null) entity.setTrangThai(request.getTrangThai());
        entity.setGhiChu(request.getGhiChu());
    }
}
