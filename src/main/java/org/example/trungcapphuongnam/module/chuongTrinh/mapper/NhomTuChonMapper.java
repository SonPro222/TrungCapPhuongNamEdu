package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NhomTuChonRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NhomTuChonResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.NhomTuChon;
import org.springframework.stereotype.Component;

@Component
public class NhomTuChonMapper {

    public NhomTuChon toEntity(NhomTuChonRequest request) {
        if (request == null) return null;
        return NhomTuChon.builder()
                .chuongTrinhVersionId(request.getChuongTrinhVersionId())
                .ten(request.getTen())
                .nhomTuChonMauId(request.getNhomTuChonMauId())
                .soMonChon(request.getSoMonChon())
                .soTinChiCanDat(request.getSoTinChiCanDat())
                .ghiChu(request.getGhiChu())
                .ma(request.getMa())
                .moTa(request.getMoTa())

                .build();
    }

    public NhomTuChonResponse toResponse(NhomTuChon entity) {
        if (entity == null) return null;
        return NhomTuChonResponse.builder()
                .id(entity.getId())
                .chuongTrinhVersionId(entity.getChuongTrinhVersionId())
                .nhomTuChonMauId(entity.getNhomTuChonMauId())
                .ten(entity.getTen())
                .soMonChon(entity.getSoMonChon())
                .soTinChiCanDat(entity.getSoTinChiCanDat())
                .ghiChu(entity.getGhiChu())
                .ma(entity.getMa())
                .moTa(entity.getMoTa())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(NhomTuChon entity, NhomTuChonRequest request) {
        if (entity == null || request == null) return;
        entity.setChuongTrinhVersionId(request.getChuongTrinhVersionId());
        entity.setTen(request.getTen());
        entity.setNhomTuChonMauId(request.getNhomTuChonMauId());
        entity.setSoMonChon(request.getSoMonChon());
        entity.setSoTinChiCanDat(request.getSoTinChiCanDat());
        entity.setGhiChu(request.getGhiChu());
        entity.setMa(request.getMa());
        entity.setMoTa(request.getMoTa());


    }
}
