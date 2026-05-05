package org.example.trungcapphuongnam.module.hocPhi.mapper;

import org.example.trungcapphuongnam.module.hocPhi.dto.request.GiaoDichHocPhiRequest;
import org.example.trungcapphuongnam.module.hocPhi.dto.response.GiaoDichHocPhiResponse;
import org.example.trungcapphuongnam.module.hocPhi.entity.GiaoDichHocPhi;
import org.springframework.stereotype.Component;

@Component
public class GiaoDichHocPhiMapper {

    public GiaoDichHocPhi toEntity(GiaoDichHocPhiRequest request) {
        if (request == null) {
            return null;
        }
        return GiaoDichHocPhi.builder()
                .hocPhiId(request.getHocPhiId())
                .soTien(request.getSoTien())
                .ngayGiaoDich(request.getNgayGiaoDich())
                .phuongThuc(request.getPhuongThuc())
                .maGiaoDich(request.getMaGiaoDich())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public GiaoDichHocPhiResponse toResponse(GiaoDichHocPhi entity) {
        if (entity == null) {
            return null;
        }
        return GiaoDichHocPhiResponse.builder()
                .id(entity.getId())
                .hocPhiId(entity.getHocPhiId())
                .soTien(entity.getSoTien())
                .ngayGiaoDich(entity.getNgayGiaoDich())
                .phuongThuc(entity.getPhuongThuc())
                .maGiaoDich(entity.getMaGiaoDich())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public void updateEntity(GiaoDichHocPhi entity, GiaoDichHocPhiRequest request) {
        entity.setHocPhiId(request.getHocPhiId());
        entity.setSoTien(request.getSoTien());
        entity.setNgayGiaoDich(request.getNgayGiaoDich());
        entity.setPhuongThuc(request.getPhuongThuc());
        entity.setMaGiaoDich(request.getMaGiaoDich());
        entity.setGhiChu(request.getGhiChu());
    }
}
