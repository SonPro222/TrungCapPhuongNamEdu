package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.springframework.stereotype.Component;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.QuyDoiDiemMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.QuyDoiDiemMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.QuyDoiDiemMau;

@Component
public class QuyDoiDiemMauMapper {

    public QuyDoiDiemMau toEntity(QuyDoiDiemMauRequest request) {
        if (request == null) {
            return null;
        }
        QuyDoiDiemMau entity = new QuyDoiDiemMau();
        entity.setSyllabusMonHocGocId(request.getSyllabusMonHocGocId());
        entity.setMa(request.getMa());
        entity.setTen(request.getTen());
        entity.setNguongTu(request.getNguongTu());
        entity.setNguongDen(request.getNguongDen());
        entity.setLoaiMau(request.getLoaiMau());
        entity.setTyLe(request.getTyLe());
        entity.setDiemToiDa(request.getDiemToiDa());
        entity.setThuTu(request.getThuTu());
        entity.setBatBuoc(request.getBatBuoc());
        entity.setDiemQuyDoi(request.getDiemQuyDoi());
        entity.setKetQua(request.getKetQua());
        entity.setCongThuc(request.getCongThuc());
        entity.setGhiChu(request.getGhiChu());
        return entity;
    }

    public void updateEntity(QuyDoiDiemMau entity, QuyDoiDiemMauRequest request) {
        if (entity == null || request == null) {
            return;
        }
        entity.setSyllabusMonHocGocId(request.getSyllabusMonHocGocId());
        entity.setMa(request.getMa());
        entity.setTen(request.getTen());
        entity.setNguongTu(request.getNguongTu());
        entity.setNguongDen(request.getNguongDen());
        entity.setLoaiMau(request.getLoaiMau());
        entity.setTyLe(request.getTyLe());
        entity.setDiemToiDa(request.getDiemToiDa());
        entity.setThuTu(request.getThuTu());
        entity.setBatBuoc(request.getBatBuoc());
        entity.setDiemQuyDoi(request.getDiemQuyDoi());
        entity.setKetQua(request.getKetQua());
        entity.setCongThuc(request.getCongThuc());
        entity.setGhiChu(request.getGhiChu());
    }

    public QuyDoiDiemMauResponse toResponse(QuyDoiDiemMau entity) {
        if (entity == null) {
            return null;
        }
        return QuyDoiDiemMauResponse.builder()
                .id(entity.getId())
                .syllabusMonHocGocId(entity.getSyllabusMonHocGocId())
                .ma(entity.getMa())
                .ten(entity.getTen())
                .nguongTu(entity.getNguongTu())
                .nguongDen(entity.getNguongDen())
                .loaiMau(entity.getLoaiMau())
                .tyLe(entity.getTyLe())
                .diemToiDa(entity.getDiemToiDa())
                .thuTu(entity.getThuTu())
                .batBuoc(entity.getBatBuoc())
                .diemQuyDoi(entity.getDiemQuyDoi())
                .ketQua(entity.getKetQua())
                .congThuc(entity.getCongThuc())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
