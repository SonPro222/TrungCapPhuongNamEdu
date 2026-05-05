package org.example.trungcapphuongnam.module.thi.mapper;

import org.example.trungcapphuongnam.module.thi.dto.request.BaiThiSinhVienRequest;
import org.example.trungcapphuongnam.module.thi.dto.response.BaiThiSinhVienResponse;
import org.example.trungcapphuongnam.module.thi.entity.BaiThiSinhVien;
import org.springframework.stereotype.Component;

@Component
public class BaiThiSinhVienMapper {
    public BaiThiSinhVien toEntity(BaiThiSinhVienRequest request) {
        if (request == null) return null;
        return BaiThiSinhVien.builder()
                .deThiId(request.getDeThiId())
                .sinhVienId(request.getSinhVienId())
                .noiDungBaiLam(request.getNoiDungBaiLam())
                .fileBaiLam(request.getFileBaiLam())
                .thoiGianBatDau(request.getThoiGianBatDau())
                .thoiGianNop(request.getThoiGianNop())
                .trangThai(request.getTrangThai())
                .build();
    }

    public void updateEntity(BaiThiSinhVien entity, BaiThiSinhVienRequest request) {
        entity.setDeThiId(request.getDeThiId());
        entity.setSinhVienId(request.getSinhVienId());
        entity.setNoiDungBaiLam(request.getNoiDungBaiLam());
        entity.setFileBaiLam(request.getFileBaiLam());
        entity.setThoiGianBatDau(request.getThoiGianBatDau());
        entity.setThoiGianNop(request.getThoiGianNop());
        entity.setTrangThai(request.getTrangThai());
    }

    public BaiThiSinhVienResponse toResponse(BaiThiSinhVien entity) {
        if (entity == null) return null;
        return BaiThiSinhVienResponse.builder()
                .id(entity.getId())
                .deThiId(entity.getDeThiId())
                .sinhVienId(entity.getSinhVienId())
                .noiDungBaiLam(entity.getNoiDungBaiLam())
                .fileBaiLam(entity.getFileBaiLam())
                .thoiGianBatDau(entity.getThoiGianBatDau())
                .thoiGianNop(entity.getThoiGianNop())
                .trangThai(entity.getTrangThai())
                .build();
    }
}
