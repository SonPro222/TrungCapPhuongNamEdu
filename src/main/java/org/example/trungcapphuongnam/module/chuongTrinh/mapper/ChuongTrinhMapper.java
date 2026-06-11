package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinh;
import org.example.trungcapphuongnam.module.daoTao.entity.Nganh;
import org.example.trungcapphuongnam.module.daoTao.entity.NganhHeDaoTao;
import org.springframework.stereotype.Component;

@Component
public class ChuongTrinhMapper {

    public ChuongTrinh toEntity(ChuongTrinhRequest request) {
        if (request == null) return null;
        return ChuongTrinh.builder()
                .nganhHeDaoTaoId(request.getNganhHeDaoTaoId())
                .maChuongTrinh(request.getMaChuongTrinh())
                .tenChuongTrinh(request.getTenChuongTrinh())
                .doiTuongTuyenSinh(request.getDoiTuongTuyenSinh())
                .thoiGianDaoTao(request.getThoiGianDaoTao())
                .build();
    }

    public ChuongTrinhResponse toResponse(ChuongTrinh entity) {
        if (entity == null) return null;
        return ChuongTrinhResponse.builder()
                .id(entity.getId())
                .nganhHeDaoTaoId(entity.getNganhHeDaoTaoId())
                .nganhId(entity.getNganhId())
                .maChuongTrinh(entity.getMaChuongTrinh())
                .tenChuongTrinh(entity.getTenChuongTrinh())
                .doiTuongTuyenSinh(entity.getDoiTuongTuyenSinh())
                .thoiGianDaoTao(entity.getThoiGianDaoTao())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /** Overload với enriched data từ service */
    public ChuongTrinhResponse toResponseEnriched(ChuongTrinh entity, Nganh nganh, NganhHeDaoTao nganhHe) {
        ChuongTrinhResponse r = toResponse(entity);
        if (r == null) return null;

        if (nganhHe != null) {
            r.setNganhHeDaoTaoId(nganhHe.getId());
            r.setMaHe(nganhHe.getMaHe());

            String tenHe = nganhHe.getTenHe();
            if (tenHe == null || tenHe.isBlank()) {
                tenHe = nganhHe.getMaHe() != null ? nganhHe.getMaHe() : ("Hệ #" + nganhHe.getId());
            }

            r.setTenHe(tenHe);
            r.setNganhId(nganhHe.getNganhId());

            r.setSoThang(nganhHe.getSoThang());
            r.setSoKy(nganhHe.getSoKy());
        }

        if (nganh != null) {
            r.setMaNganh(nganh.getMaNganh());
            r.setTenNganh(nganh.getTenNganh());
        }

        return r;
    }

    public void updateEntity(ChuongTrinh entity, ChuongTrinhRequest request) {
        if (entity == null || request == null) return;
        entity.setNganhHeDaoTaoId(request.getNganhHeDaoTaoId());
        entity.setMaChuongTrinh(request.getMaChuongTrinh());
        entity.setTenChuongTrinh(request.getTenChuongTrinh());
        entity.setDoiTuongTuyenSinh(request.getDoiTuongTuyenSinh());
        entity.setThoiGianDaoTao(request.getThoiGianDaoTao());
    }
}
