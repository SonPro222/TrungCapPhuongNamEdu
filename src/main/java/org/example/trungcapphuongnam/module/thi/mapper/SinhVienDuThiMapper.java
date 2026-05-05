package org.example.trungcapphuongnam.module.thi.mapper;

import org.example.trungcapphuongnam.module.thi.dto.request.SinhVienDuThiRequest;
import org.example.trungcapphuongnam.module.thi.dto.response.SinhVienDuThiResponse;
import org.example.trungcapphuongnam.module.thi.entity.SinhVienDuThi;
import org.springframework.stereotype.Component;

@Component
public class SinhVienDuThiMapper {
    public SinhVienDuThi toEntity(SinhVienDuThiRequest request) {
        if (request == null) return null;
        return SinhVienDuThi.builder()
                .lichThiId(request.getLichThiId())
                .sinhVienId(request.getSinhVienId())
                .duDieuKienThi(request.getDuDieuKienThi())
                .lyDoKhongDuDieuKien(request.getLyDoKhongDuDieuKien())
                .trangThai(request.getTrangThai())
                .build();
    }

    public void updateEntity(SinhVienDuThi entity, SinhVienDuThiRequest request) {
        entity.setLichThiId(request.getLichThiId());
        entity.setSinhVienId(request.getSinhVienId());
        entity.setDuDieuKienThi(request.getDuDieuKienThi());
        entity.setLyDoKhongDuDieuKien(request.getLyDoKhongDuDieuKien());
        entity.setTrangThai(request.getTrangThai());
    }

    public SinhVienDuThiResponse toResponse(SinhVienDuThi entity) {
        if (entity == null) return null;
        return SinhVienDuThiResponse.builder()
                .id(entity.getId())
                .lichThiId(entity.getLichThiId())
                .sinhVienId(entity.getSinhVienId())
                .duDieuKienThi(entity.getDuDieuKienThi())
                .lyDoKhongDuDieuKien(entity.getLyDoKhongDuDieuKien())
                .trangThai(entity.getTrangThai())
                .build();
    }
}
