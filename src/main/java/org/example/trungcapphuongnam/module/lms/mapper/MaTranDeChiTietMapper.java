package org.example.trungcapphuongnam.module.lms.mapper;

import org.example.trungcapphuongnam.module.lms.dto.request.MaTranDeChiTietRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.MaTranDeChiTietResponse;
import org.example.trungcapphuongnam.module.lms.entity.MaTranDeChiTiet;

public final class MaTranDeChiTietMapper {
    private MaTranDeChiTietMapper() {}

    public static MaTranDeChiTietResponse toResponse(MaTranDeChiTiet entity) {
        if (entity == null) return null;
        return MaTranDeChiTietResponse.builder()
            .id(entity.getId())
            .maTranDeId(entity.getMaTranDeId())
            .syllabusChuongBaiId(entity.getSyllabusChuongBaiId())
            .mucDo(entity.getMucDo())
            .loaiCauHoi(entity.getLoaiCauHoi())
            .soCau(entity.getSoCau())
            .diemMoiCau(entity.getDiemMoiCau())
            .build();
    }

    public static MaTranDeChiTiet toEntity(MaTranDeChiTietRequest request) {
        if (request == null) return null;
        return MaTranDeChiTiet.builder()
            .maTranDeId(request.getMaTranDeId())
            .syllabusChuongBaiId(request.getSyllabusChuongBaiId())
            .mucDo(request.getMucDo())
            .loaiCauHoi(request.getLoaiCauHoi())
            .soCau(request.getSoCau())
            .diemMoiCau(request.getDiemMoiCau())
            .build();
    }

    public static void updateEntity(MaTranDeChiTiet entity, MaTranDeChiTietRequest request) {
        if (entity == null || request == null) return;
        entity.setMaTranDeId(request.getMaTranDeId());
        entity.setSyllabusChuongBaiId(request.getSyllabusChuongBaiId());
        entity.setMucDo(request.getMucDo());
        entity.setLoaiCauHoi(request.getLoaiCauHoi());
        entity.setSoCau(request.getSoCau());
        entity.setDiemMoiCau(request.getDiemMoiCau());
    }
}
