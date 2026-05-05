package org.example.trungcapphuongnam.module.chuongTrinh.mapper;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NangLucDauRaRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NangLucDauRaResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.NangLucDauRa;
import org.springframework.stereotype.Component;

@Component
public class NangLucDauRaMapper {

    public NangLucDauRa toEntity(NangLucDauRaRequest request) {
        if (request == null) return null;
        return NangLucDauRa.builder()
                .chuongTrinhVersionId(request.getChuongTrinhVersionId())
                .ma(request.getMa())
                .noiDung(request.getNoiDung())
                .loai(request.getLoai())
                .thuTu(request.getThuTu())
                .build();
    }

    public NangLucDauRaResponse toResponse(NangLucDauRa entity) {
        if (entity == null) return null;
        return NangLucDauRaResponse.builder()
                .id(entity.getId())
                .chuongTrinhVersionId(entity.getChuongTrinhVersionId())
                .ma(entity.getMa())
                .noiDung(entity.getNoiDung())
                .loai(entity.getLoai())
                .thuTu(entity.getThuTu())
                .build();
    }

    public void updateEntity(NangLucDauRa entity, NangLucDauRaRequest request) {
        if (entity == null || request == null) return;
        entity.setChuongTrinhVersionId(request.getChuongTrinhVersionId());
        entity.setMa(request.getMa());
        entity.setNoiDung(request.getNoiDung());
        entity.setLoai(request.getLoai());
        entity.setThuTu(request.getThuTu());
    }
}
