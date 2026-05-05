package org.example.trungcapphuongnam.module.thi.mapper;

import org.example.trungcapphuongnam.module.thi.dto.request.DeThiRequest;
import org.example.trungcapphuongnam.module.thi.dto.response.DeThiResponse;
import org.example.trungcapphuongnam.module.thi.entity.DeThi;
import org.springframework.stereotype.Component;

@Component
public class DeThiMapper {
    public DeThi toEntity(DeThiRequest request) {
        if (request == null) return null;
        return DeThi.builder()
                .lichThiId(request.getLichThiId())
                .lopHocPhanId(request.getLopHocPhanId())
                .giaoVienId(request.getGiaoVienId())
                .maDe(request.getMaDe())
                .tieuDe(request.getTieuDe())
                .noiDungDe(request.getNoiDungDe())
                .fileDeThi(request.getFileDeThi())
                .trangThai(request.getTrangThai())
                .build();
    }

    public void updateEntity(DeThi entity, DeThiRequest request) {
        entity.setLichThiId(request.getLichThiId());
        entity.setLopHocPhanId(request.getLopHocPhanId());
        entity.setGiaoVienId(request.getGiaoVienId());
        entity.setMaDe(request.getMaDe());
        entity.setTieuDe(request.getTieuDe());
        entity.setNoiDungDe(request.getNoiDungDe());
        entity.setFileDeThi(request.getFileDeThi());
        entity.setTrangThai(request.getTrangThai());
    }

    public DeThiResponse toResponse(DeThi entity) {
        if (entity == null) return null;
        return DeThiResponse.builder()
                .id(entity.getId())
                .lichThiId(entity.getLichThiId())
                .lopHocPhanId(entity.getLopHocPhanId())
                .giaoVienId(entity.getGiaoVienId())
                .maDe(entity.getMaDe())
                .tieuDe(entity.getTieuDe())
                .noiDungDe(entity.getNoiDungDe())
                .fileDeThi(entity.getFileDeThi())
                .trangThai(entity.getTrangThai())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
