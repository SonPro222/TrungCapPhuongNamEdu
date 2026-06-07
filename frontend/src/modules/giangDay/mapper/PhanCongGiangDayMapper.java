package org.example.trungcapphuongnam.module.giangDay.mapper;

import org.example.trungcapphuongnam.module.giangDay.entity.PhanCongGiangDay;
import org.example.trungcapphuongnam.module.giangDay.dto.request.PhanCongGiangDayRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.PhanCongGiangDayResponse;
import org.springframework.stereotype.Component;

@Component
public class PhanCongGiangDayMapper {

    public PhanCongGiangDay toEntity(PhanCongGiangDayRequest request) {
        if (request == null) return null;
        return PhanCongGiangDay.builder()
                .lopHocPhanId(request.getLopHocPhanId())
                .giaoVienId(request.getGiaoVienId())
                .vaiTro(request.getVaiTro())
                .build();
    }

    public PhanCongGiangDayResponse toResponse(PhanCongGiangDay entity) {
        if (entity == null) return null;
        return PhanCongGiangDayResponse.builder()
                .id(entity.getId())
                .lopHocPhanId(entity.getLopHocPhanId())
                .giaoVienId(entity.getGiaoVienId())
                .vaiTro(entity.getVaiTro())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public void updateEntity(PhanCongGiangDay entity, PhanCongGiangDayRequest request) {
        if (entity == null || request == null) return;
        entity.setLopHocPhanId(request.getLopHocPhanId());
        entity.setGiaoVienId(request.getGiaoVienId());
        entity.setVaiTro(request.getVaiTro());
    }
}
