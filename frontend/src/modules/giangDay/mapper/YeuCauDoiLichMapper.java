package org.example.trungcapphuongnam.module.giangDay.mapper;

import org.example.trungcapphuongnam.module.giangDay.entity.YeuCauDoiLich;
import org.example.trungcapphuongnam.module.giangDay.dto.request.YeuCauDoiLichRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.YeuCauDoiLichResponse;
import org.springframework.stereotype.Component;

@Component
public class YeuCauDoiLichMapper {

    public YeuCauDoiLich toEntity(YeuCauDoiLichRequest request) {
        if (request == null) return null;
        return YeuCauDoiLich.builder()
                .lichHocCuId(request.getLichHocCuId())
                .giaoVienId(request.getGiaoVienId())
                .ngayMoi(request.getNgayMoi())
                .caHocMoiId(request.getCaHocMoiId())
                .phongHocMoiId(request.getPhongHocMoiId())
                .lyDo(request.getLyDo())
                .trangThai(request.getTrangThai())
                .nguoiDuyetId(request.getNguoiDuyetId())
                .ngayDuyet(request.getNgayDuyet())
                .build();
    }

    public YeuCauDoiLichResponse toResponse(YeuCauDoiLich entity) {
        if (entity == null) return null;
        return YeuCauDoiLichResponse.builder()
                .id(entity.getId())
                .lichHocCuId(entity.getLichHocCuId())
                .giaoVienId(entity.getGiaoVienId())
                .ngayMoi(entity.getNgayMoi())
                .caHocMoiId(entity.getCaHocMoiId())
                .phongHocMoiId(entity.getPhongHocMoiId())
                .lyDo(entity.getLyDo())
                .trangThai(entity.getTrangThai())
                .nguoiDuyetId(entity.getNguoiDuyetId())
                .ngayDuyet(entity.getNgayDuyet())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(YeuCauDoiLich entity, YeuCauDoiLichRequest request) {
        if (entity == null || request == null) return;
        entity.setLichHocCuId(request.getLichHocCuId());
        entity.setGiaoVienId(request.getGiaoVienId());
        entity.setNgayMoi(request.getNgayMoi());
        entity.setCaHocMoiId(request.getCaHocMoiId());
        entity.setPhongHocMoiId(request.getPhongHocMoiId());
        entity.setLyDo(request.getLyDo());
        entity.setTrangThai(request.getTrangThai());
        entity.setNguoiDuyetId(request.getNguoiDuyetId());
        entity.setNgayDuyet(request.getNgayDuyet());
    }
}
