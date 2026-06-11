package org.example.trungcapphuongnam.module.giangDay.mapper;

import org.example.trungcapphuongnam.module.giangDay.dto.request.PhanCongGiangDayRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.PhanCongGiangDayResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.GiaoVien;
import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.entity.PhanCongGiangDay;
import org.springframework.stereotype.Component;

import java.util.Map;

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

    public PhanCongGiangDayResponse toResponse(
            PhanCongGiangDay entity,
            Map<Long, LopHocPhan> lopHocPhanMap,
            Map<Long, GiaoVien> giaoVienMap
    ) {
        PhanCongGiangDayResponse response = toResponse(entity);
        if (response == null || entity == null) return response;

        LopHocPhan lopHocPhan = lopHocPhanMap == null ? null : lopHocPhanMap.get(entity.getLopHocPhanId());
        if (lopHocPhan != null) {
            response.setMaLop(lopHocPhan.getMaLop());
            response.setTenLop(lopHocPhan.getTenLop());
        }

        GiaoVien giaoVien = giaoVienMap == null ? null : giaoVienMap.get(entity.getGiaoVienId());
        if (giaoVien != null) {
            response.setMaGiaoVien(giaoVien.getMaGiaoVien());
            response.setTenGiaoVien(giaoVien.getHoTen());
        }

        return response;
    }

    public void updateEntity(PhanCongGiangDay entity, PhanCongGiangDayRequest request) {
        if (entity == null || request == null) return;
        entity.setLopHocPhanId(request.getLopHocPhanId());
        entity.setGiaoVienId(request.getGiaoVienId());
        entity.setVaiTro(request.getVaiTro());
    }
}
