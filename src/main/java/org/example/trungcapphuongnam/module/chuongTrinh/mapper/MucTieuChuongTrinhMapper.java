package org.example.trungcapphuongnam.module.chuongTrinh.mapper;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.MucTieuChuongTrinhRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.MucTieuChuongTrinhResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.MucTieuChuongTrinh;
import org.springframework.stereotype.Component;

@Component
public class MucTieuChuongTrinhMapper {

    public MucTieuChuongTrinh toEntity(MucTieuChuongTrinhRequest request) {
        if (request == null) return null;
        return MucTieuChuongTrinh.builder()
                .syllabusChuongTrinhId(request.getSyllabusChuongTrinhId())
                .loai(request.getLoai())
                .noiDung(request.getNoiDung())
                .thuTu(request.getThuTu())
                .ma(request.getMa())
                .ghiChu(request.getGhiChu())

                .build();
    }

    public MucTieuChuongTrinhResponse toResponse(MucTieuChuongTrinh entity) {
        if (entity == null) return null;
        return MucTieuChuongTrinhResponse.builder()
                .id(entity.getId())
                .syllabusChuongTrinhId(entity.getSyllabusChuongTrinhId())
                .loai(entity.getLoai())
                .noiDung(entity.getNoiDung())
                .thuTu(entity.getThuTu())
                .ma(entity.getMa())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(MucTieuChuongTrinh entity, MucTieuChuongTrinhRequest request) {
        if (entity == null || request == null) return;
        entity.setSyllabusChuongTrinhId(request.getSyllabusChuongTrinhId());
        entity.setLoai(request.getLoai());
        entity.setNoiDung(request.getNoiDung());
        entity.setThuTu(request.getThuTu());
        entity.setMa(request.getMa());
        entity.setGhiChu(request.getGhiChu());


    }
}
