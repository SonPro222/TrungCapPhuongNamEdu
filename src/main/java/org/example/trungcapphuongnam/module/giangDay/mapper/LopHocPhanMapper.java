package org.example.trungcapphuongnam.module.giangDay.mapper;

import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.dto.request.LopHocPhanRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LopHocPhanResponse;
import org.springframework.stereotype.Component;

@Component
public class LopHocPhanMapper {

    public LopHocPhan toEntity(LopHocPhanRequest request) {
        if (request == null) return null;
        return LopHocPhan.builder()
                .chuongTrinhMonId(request.getChuongTrinhMonId())
                .maLop(request.getMaLop())
                .tenLop(request.getTenLop())
                .soLuongToiDa(request.getSoLuongToiDa())
                .soLuongHienTai(request.getSoLuongHienTai())
                .ngayBatDau(request.getNgayBatDau())
                .ngayKetThuc(request.getNgayKetThuc())
                .trangThai(request.getTrangThai())
                .build();
    }

    public LopHocPhanResponse toResponse(LopHocPhan entity) {
        if (entity == null) return null;
        return LopHocPhanResponse.builder()
                .id(entity.getId())
                .chuongTrinhMonId(entity.getChuongTrinhMonId())
                .maLop(entity.getMaLop())
                .tenLop(entity.getTenLop())
                .soLuongToiDa(entity.getSoLuongToiDa())
                .soLuongHienTai(entity.getSoLuongHienTai())
                .ngayBatDau(entity.getNgayBatDau())
                .ngayKetThuc(entity.getNgayKetThuc())
                .trangThai(entity.getTrangThai())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(LopHocPhan entity, LopHocPhanRequest request) {
        if (entity == null || request == null) return;
        entity.setChuongTrinhMonId(request.getChuongTrinhMonId());
        entity.setMaLop(request.getMaLop());
        entity.setTenLop(request.getTenLop());
        entity.setSoLuongToiDa(request.getSoLuongToiDa());
        entity.setSoLuongHienTai(request.getSoLuongHienTai());
        entity.setNgayBatDau(request.getNgayBatDau());
        entity.setNgayKetThuc(request.getNgayKetThuc());
        entity.setTrangThai(request.getTrangThai());
    }
}
