package org.example.trungcapphuongnam.module.daoTao.mapper;

import org.example.trungcapphuongnam.module.daoTao.dto.NganhHeDaoTaoRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.NganhHeDaoTaoResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.LoaiChuongTrinh;
import org.example.trungcapphuongnam.module.daoTao.entity.Nganh;
import org.example.trungcapphuongnam.module.daoTao.entity.NganhHeDaoTao;
import org.example.trungcapphuongnam.module.daoTao.entity.TrinhDoDaoTao;
import org.springframework.stereotype.Component;

@Component
public class NganhHeDaoTaoMapper {

    public NganhHeDaoTao toEntity(NganhHeDaoTaoRequest request) {
        if (request == null) return null;
        return NganhHeDaoTao.builder()
                .nganhId(request.getNganhId())
                .trinhDoId(request.getTrinhDoId())
                .loaiChuongTrinhId(request.getLoaiChuongTrinhId())
                .maHe(request.getMaHe())
                .tenHe(request.getTenHe())
                .soThang(request.getSoThang())
                .soKy(request.getSoKy())
                .trangThai(request.getTrangThai())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public NganhHeDaoTaoResponse toResponse(NganhHeDaoTao entity, Nganh nganh, TrinhDoDaoTao trinhDo, LoaiChuongTrinh loaiChuongTrinh) {
        if (entity == null) return null;

        String tenNganh  = nganh  != null ? nganh.getTenNganh()  : null;
        String maNganh   = nganh  != null ? nganh.getMaNganh()   : null;
        String tenTrinhDo = trinhDo != null ? trinhDo.getTenTrinhDo() : null;
        String maTrinhDo  = trinhDo != null ? trinhDo.getMaTrinhDo()  : null;
        String tenLoai   = loaiChuongTrinh != null ? loaiChuongTrinh.getTenLoai() : null;
        String maLoai    = loaiChuongTrinh != null ? loaiChuongTrinh.getMaLoai()  : null;

        String label = buildLabel(tenNganh, tenTrinhDo, tenLoai, entity.getSoThang(), entity.getSoKy());

        return NganhHeDaoTaoResponse.builder()
                .id(entity.getId())
                .nganhId(entity.getNganhId())
                .maNganh(maNganh)
                .tenNganh(tenNganh)
                .trinhDoId(entity.getTrinhDoId())
                .maTrinhDo(maTrinhDo)
                .tenTrinhDo(tenTrinhDo)
                .loaiChuongTrinhId(entity.getLoaiChuongTrinhId())
                .maLoai(maLoai)
                .tenLoai(tenLoai)
                .maHe(entity.getMaHe())
                .tenHe(entity.getTenHe())
                .soThang(entity.getSoThang())
                .soKy(entity.getSoKy())
                .trangThai(entity.getTrangThai())
                .ghiChu(entity.getGhiChu())
                .label(label)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(NganhHeDaoTao entity, NganhHeDaoTaoRequest request) {
        if (entity == null || request == null) return;
        entity.setNganhId(request.getNganhId());
        entity.setTrinhDoId(request.getTrinhDoId());
        entity.setLoaiChuongTrinhId(request.getLoaiChuongTrinhId());
        entity.setMaHe(request.getMaHe());
        entity.setTenHe(request.getTenHe());
        entity.setSoThang(request.getSoThang());
        entity.setSoKy(request.getSoKy());
        entity.setTrangThai(request.getTrangThai());
        entity.setGhiChu(request.getGhiChu());
    }

    private String buildLabel(String tenNganh, String tenTrinhDo, String tenLoai, Integer soThang, Integer soKy) {
        StringBuilder sb = new StringBuilder();
        if (tenNganh  != null) sb.append(tenNganh);
        if (tenTrinhDo != null) sb.append(" - ").append(tenTrinhDo);
        if (tenLoai   != null) sb.append(" - ").append(tenLoai);
        if (soThang   != null) sb.append(" - ").append(soThang).append(" tháng");
        if (soKy      != null) sb.append(" - ").append(soKy).append(" kỳ");
        return sb.toString();
    }
}
