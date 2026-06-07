package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.QuyDoiDiemRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.QuyDoiDiemResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.QuyDoiDiem;
import org.springframework.stereotype.Component;

@Component
public class QuyDoiDiemMapper {

    public QuyDoiDiem toEntity(QuyDoiDiemRequest request) {
        if (request == null) {
            return null;
        }

        return QuyDoiDiem.builder()
                .chuongTrinhMonId(request.getChuongTrinhMonId())
                .syllabusMonHocId(request.getSyllabusMonHocId())
                .nguongTu(request.getNguongTu())
                .nguongDen(request.getNguongDen())
                .diemQuyDoi(request.getDiemQuyDoi())
                .ketQua(request.getKetQua())
                .congThuc(request.getCongThuc())
                .ghiChu(request.getGhiChu())
                .ma(request.getMa())
                .ten(request.getTen())
                .loaiMau(request.getLoaiMau())
                .tyLe(request.getTyLe())
                .diemToiDa(request.getDiemToiDa())
                .thuTu(request.getThuTu())
                .batBuoc(request.getBatBuoc())
                .build();
    }

    public QuyDoiDiemResponse toResponse(QuyDoiDiem entity) {
        if (entity == null) {
            return null;
        }

        return QuyDoiDiemResponse.builder()
                .id(entity.getId())
                .chuongTrinhMonId(entity.getChuongTrinhMonId())
                .syllabusMonHocId(entity.getSyllabusMonHocId())
                .nguongTu(entity.getNguongTu())
                .nguongDen(entity.getNguongDen())
                .diemQuyDoi(entity.getDiemQuyDoi())
                .ketQua(entity.getKetQua())
                .congThuc(entity.getCongThuc())
                .ghiChu(entity.getGhiChu())
                .ma(entity.getMa())
                .ten(entity.getTen())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .loaiMau(entity.getLoaiMau())
                .tyLe(entity.getTyLe())
                .diemToiDa(entity.getDiemToiDa())
                .thuTu(entity.getThuTu())
                .batBuoc(entity.getBatBuoc())
                .build();
    }

    public void updateEntity(QuyDoiDiem entity, QuyDoiDiemRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setChuongTrinhMonId(request.getChuongTrinhMonId());
        entity.setSyllabusMonHocId(request.getSyllabusMonHocId());
        entity.setNguongTu(request.getNguongTu());
        entity.setNguongDen(request.getNguongDen());
        entity.setDiemQuyDoi(request.getDiemQuyDoi());
        entity.setKetQua(request.getKetQua());
        entity.setCongThuc(request.getCongThuc());
        entity.setGhiChu(request.getGhiChu());
        entity.setMa(request.getMa());
        entity.setTen(request.getTen());
        entity.setLoaiMau(request.getLoaiMau());
        entity.setTyLe(request.getTyLe());
        entity.setDiemToiDa(request.getDiemToiDa());
        entity.setThuTu(request.getThuTu());
        entity.setBatBuoc(request.getBatBuoc());
    }
}