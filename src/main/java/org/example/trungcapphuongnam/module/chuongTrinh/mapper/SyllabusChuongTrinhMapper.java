package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusChuongTrinhRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusChuongTrinhResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusChuongTrinh;
import org.springframework.stereotype.Component;

@Component
public class SyllabusChuongTrinhMapper {

    public SyllabusChuongTrinh toEntity(SyllabusChuongTrinhRequest request) {
        if (request == null) return null;
        return SyllabusChuongTrinh.builder()
                .chuongTrinhVersionId(request.getChuongTrinhVersionId())
                .moTaTongQuan(request.getMoTaTongQuan())
                .mucDich(request.getMucDich())
                .yeuCauDaoTao(request.getYeuCauDaoTao())
                .phuongPhapDaoTao(request.getPhuongPhapDaoTao())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public SyllabusChuongTrinhResponse toResponse(SyllabusChuongTrinh entity) {
        if (entity == null) return null;
        return SyllabusChuongTrinhResponse.builder()
                .id(entity.getId())
                .chuongTrinhVersionId(entity.getChuongTrinhVersionId())
                .moTaTongQuan(entity.getMoTaTongQuan())
                .mucDich(entity.getMucDich())
                .yeuCauDaoTao(entity.getYeuCauDaoTao())
                .phuongPhapDaoTao(entity.getPhuongPhapDaoTao())
                .ghiChu(entity.getGhiChu())
                .build();
    }

    public void updateEntity(SyllabusChuongTrinh entity, SyllabusChuongTrinhRequest request) {
        if (entity == null || request == null) return;
        entity.setChuongTrinhVersionId(request.getChuongTrinhVersionId());
        entity.setMoTaTongQuan(request.getMoTaTongQuan());
        entity.setMucDich(request.getMucDich());
        entity.setYeuCauDaoTao(request.getYeuCauDaoTao());
        entity.setPhuongPhapDaoTao(request.getPhuongPhapDaoTao());
        entity.setGhiChu(request.getGhiChu());
    }
}
