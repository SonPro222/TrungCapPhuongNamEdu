package org.example.trungcapphuongnam.module.diem.mapper;

import org.example.trungcapphuongnam.module.diem.dto.request.BaiNopSinhVienRequest;
import org.example.trungcapphuongnam.module.diem.dto.response.BaiNopSinhVienResponse;
import org.example.trungcapphuongnam.module.diem.entity.BaiNopSinhVien;
import org.springframework.stereotype.Component;

@Component
public class BaiNopSinhVienMapper {

    public BaiNopSinhVien toEntity(BaiNopSinhVienRequest request) {
        if (request == null) return null;
        return BaiNopSinhVien.builder()
                .baiKiemTraId(request.getBaiKiemTraId())
                .sinhVienId(request.getSinhVienId())
                .noiDungBaiLam(request.getNoiDungBaiLam())
                .fileDinhKem(request.getFileDinhKem())
                .thoiGianNop(request.getThoiGianNop())
                .trangThai(request.getTrangThai())
                .build();
    }

    public BaiNopSinhVienResponse toResponse(BaiNopSinhVien entity) {
        if (entity == null) return null;
        return BaiNopSinhVienResponse.builder()
                .id(entity.getId())
                .baiKiemTraId(entity.getBaiKiemTraId())
                .sinhVienId(entity.getSinhVienId())
                .noiDungBaiLam(entity.getNoiDungBaiLam())
                .fileDinhKem(entity.getFileDinhKem())
                .thoiGianNop(entity.getThoiGianNop())
                .trangThai(entity.getTrangThai())
                .build();
    }

    public void updateEntity(BaiNopSinhVien entity, BaiNopSinhVienRequest request) {
        if (entity == null || request == null) return;
        entity.setBaiKiemTraId(request.getBaiKiemTraId());
        entity.setSinhVienId(request.getSinhVienId());
        entity.setNoiDungBaiLam(request.getNoiDungBaiLam());
        entity.setFileDinhKem(request.getFileDinhKem());
        entity.setThoiGianNop(request.getThoiGianNop());
        entity.setTrangThai(request.getTrangThai());
    }
}
