package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.QuyDoiDiemRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.QuyDoiDiemResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.QuyDoiDiem;
import org.springframework.stereotype.Component;

@Component
public class QuyDoiDiemMapper {

    public QuyDoiDiem toEntity(QuyDoiDiemRequest request) {
        if (request == null) return null;
        return QuyDoiDiem.builder()
                .chuongTrinhMonId(request.getChuongTrinhMonId())
                .nguongTu(request.getNguongTu())
                .nguongDen(request.getNguongDen())
                .diemQuyDoi(request.getDiemQuyDoi())
                .ketQua(request.getKetQua())
                .congThuc(request.getCongThuc())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public QuyDoiDiemResponse toResponse(QuyDoiDiem entity) {
        if (entity == null) return null;
        return QuyDoiDiemResponse.builder()
                .id(entity.getId())
                .chuongTrinhMonId(entity.getChuongTrinhMonId())
                .nguongTu(entity.getNguongTu())
                .nguongDen(entity.getNguongDen())
                .diemQuyDoi(entity.getDiemQuyDoi())
                .ketQua(entity.getKetQua())
                .congThuc(entity.getCongThuc())
                .ghiChu(entity.getGhiChu())
                .build();
    }

    public void updateEntity(QuyDoiDiem entity, QuyDoiDiemRequest request) {
        if (entity == null || request == null) return;
        entity.setChuongTrinhMonId(request.getChuongTrinhMonId());
        entity.setNguongTu(request.getNguongTu());
        entity.setNguongDen(request.getNguongDen());
        entity.setDiemQuyDoi(request.getDiemQuyDoi());
        entity.setKetQua(request.getKetQua());
        entity.setCongThuc(request.getCongThuc());
        entity.setGhiChu(request.getGhiChu());
    }
}
