package org.example.trungcapphuongnam.module.hocPhi.mapper;


import org.example.trungcapphuongnam.module.hocPhi.enums.TrangThaiHocPhi;
import org.example.trungcapphuongnam.module.hocPhi.dto.request.HocPhiRequest;
import org.example.trungcapphuongnam.module.hocPhi.dto.response.HocPhiResponse;
import org.example.trungcapphuongnam.module.hocPhi.entity.HocPhi;
import org.example.trungcapphuongnam.common.util.HocPhiUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class HocPhiMapper {

    public HocPhi toEntity(HocPhiRequest request) {
        if (request == null) {
            return null;
        }
        TrangThaiHocPhi trangThai = request.getTrangThai() != null
                ? request.getTrangThai()
                : HocPhiUtil.tinhTrangThai(request.getSoTienPhaiDong(), request.getSoTienDaDong(), request.getHanDong());

        return HocPhi.builder()
                .sinhVienChuongTrinhId(request.getSinhVienChuongTrinhId())
                .tenKhoanThu(request.getTenKhoanThu())
                .soTienPhaiDong(request.getSoTienPhaiDong())
                .soTienDaDong(request.getSoTienDaDong())
                .hanDong(request.getHanDong())
                .ngayDong(request.getNgayDong())
                .trangThai(trangThai)
                .ghiChu(request.getGhiChu())
                .build();
    }

    public HocPhiResponse toResponse(HocPhi entity) {
        if (entity == null) {
            return null;
        }
        BigDecimal phaiDong = HocPhiUtil.safe(entity.getSoTienPhaiDong());
        BigDecimal daDong = HocPhiUtil.safe(entity.getSoTienDaDong());
        BigDecimal conLai = phaiDong.subtract(daDong);
        if (conLai.compareTo(BigDecimal.ZERO) < 0) {
            conLai = BigDecimal.ZERO;
        }

        return HocPhiResponse.builder()
                .id(entity.getId())
                .sinhVienChuongTrinhId(entity.getSinhVienChuongTrinhId())
                .tenKhoanThu(entity.getTenKhoanThu())
                .soTienPhaiDong(entity.getSoTienPhaiDong())
                .soTienDaDong(entity.getSoTienDaDong())
                .soTienConLai(conLai)
                .hanDong(entity.getHanDong())
                .ngayDong(entity.getNgayDong())
                .trangThai(entity.getTrangThai())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(HocPhi entity, HocPhiRequest request) {
        entity.setSinhVienChuongTrinhId(request.getSinhVienChuongTrinhId());
        entity.setTenKhoanThu(request.getTenKhoanThu());
        entity.setSoTienPhaiDong(request.getSoTienPhaiDong());
        entity.setSoTienDaDong(request.getSoTienDaDong());
        entity.setHanDong(request.getHanDong());
        entity.setNgayDong(request.getNgayDong());
        entity.setTrangThai(request.getTrangThai() != null
                ? request.getTrangThai()
                : HocPhiUtil.tinhTrangThai(request.getSoTienPhaiDong(), request.getSoTienDaDong(), request.getHanDong()));
        entity.setGhiChu(request.getGhiChu());
    }
}
