package org.example.trungcapphuongnam.module.daoTao.mapper;

import org.example.trungcapphuongnam.module.daoTao.dto.LopHanhChinhRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.LopHanhChinhResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.LopHanhChinh;
import org.springframework.stereotype.Component;

@Component
public class LopHanhChinhMapper {

    public LopHanhChinh toEntity(LopHanhChinhRequest request) {
        if (request == null) return null;
        return LopHanhChinh.builder()
                .maLop(request.getMaLop())
                .tenLop(request.getTenLop())
                .chuongTrinhVersionId(request.getChuongTrinhVersionId())
                .khoaDaoTaoId(request.getKhoaDaoTaoId())
                .siSo(request.getSiSo())
                .trangThai(request.getTrangThai())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public LopHanhChinhResponse toResponse(LopHanhChinh entity) {
        if (entity == null) return null;
        return LopHanhChinhResponse.builder()
                .id(entity.getId())
                .maLop(entity.getMaLop())
                .tenLop(entity.getTenLop())
                .chuongTrinhVersionId(entity.getChuongTrinhVersionId())
                .khoaDaoTaoId(entity.getKhoaDaoTaoId())
                .siSo(entity.getSiSo())
                .trangThai(entity.getTrangThai())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(LopHanhChinh entity, LopHanhChinhRequest request) {
        if (entity == null || request == null) return;
        entity.setMaLop(request.getMaLop());
        entity.setTenLop(request.getTenLop());
        entity.setChuongTrinhVersionId(request.getChuongTrinhVersionId());
        entity.setKhoaDaoTaoId(request.getKhoaDaoTaoId());
        entity.setSiSo(request.getSiSo());
        entity.setTrangThai(request.getTrangThai());
        entity.setGhiChu(request.getGhiChu());
    }
}
