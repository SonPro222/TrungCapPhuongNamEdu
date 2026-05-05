package org.example.trungcapphuongnam.module.thi.mapper;

import org.example.trungcapphuongnam.module.thi.dto.request.LichThiRequest;
import org.example.trungcapphuongnam.module.thi.dto.response.LichThiResponse;
import org.example.trungcapphuongnam.module.thi.entity.LichThi;
import org.springframework.stereotype.Component;

@Component
public class LichThiMapper {
    public LichThi toEntity(LichThiRequest request) {
        if (request == null) return null;
        return LichThi.builder()
                .lopHocPhanId(request.getLopHocPhanId())
                .baiKiemTraId(request.getBaiKiemTraId())
                .ngayThi(request.getNgayThi())
                .caHocId(request.getCaHocId())
                .phongHocId(request.getPhongHocId())
                .hinhThucThi(request.getHinhThucThi())
                .trangThai(request.getTrangThai())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public void updateEntity(LichThi entity, LichThiRequest request) {
        entity.setLopHocPhanId(request.getLopHocPhanId());
        entity.setBaiKiemTraId(request.getBaiKiemTraId());
        entity.setNgayThi(request.getNgayThi());
        entity.setCaHocId(request.getCaHocId());
        entity.setPhongHocId(request.getPhongHocId());
        entity.setHinhThucThi(request.getHinhThucThi());
        entity.setTrangThai(request.getTrangThai());
        entity.setGhiChu(request.getGhiChu());
    }

    public LichThiResponse toResponse(LichThi entity) {
        if (entity == null) return null;
        return LichThiResponse.builder()
                .id(entity.getId())
                .lopHocPhanId(entity.getLopHocPhanId())
                .baiKiemTraId(entity.getBaiKiemTraId())
                .ngayThi(entity.getNgayThi())
                .caHocId(entity.getCaHocId())
                .phongHocId(entity.getPhongHocId())
                .hinhThucThi(entity.getHinhThucThi())
                .trangThai(entity.getTrangThai())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
