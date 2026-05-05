package org.example.trungcapphuongnam.module.lms.mapper;

import org.example.trungcapphuongnam.module.lms.dto.request.CauTraLoiSinhVienRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.CauTraLoiSinhVienResponse;
import org.example.trungcapphuongnam.module.lms.entity.CauTraLoiSinhVien;

public final class CauTraLoiSinhVienMapper {
    private CauTraLoiSinhVienMapper() {}

    public static CauTraLoiSinhVienResponse toResponse(CauTraLoiSinhVien entity) {
        if (entity == null) return null;
        return CauTraLoiSinhVienResponse.builder()
            .id(entity.getId())
            .lanLamBaiId(entity.getLanLamBaiId())
            .cauHoiId(entity.getCauHoiId())
            .noiDungTraLoi(entity.getNoiDungTraLoi())
            .diem(entity.getDiem())
            .laDung(entity.getLaDung())
            .nhanXet(entity.getNhanXet())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    public static CauTraLoiSinhVien toEntity(CauTraLoiSinhVienRequest request) {
        if (request == null) return null;
        return CauTraLoiSinhVien.builder()
            .lanLamBaiId(request.getLanLamBaiId())
            .cauHoiId(request.getCauHoiId())
            .noiDungTraLoi(request.getNoiDungTraLoi())
            .diem(request.getDiem())
            .laDung(request.getLaDung())
            .nhanXet(request.getNhanXet())
            .build();
    }

    public static void updateEntity(CauTraLoiSinhVien entity, CauTraLoiSinhVienRequest request) {
        if (entity == null || request == null) return;
        entity.setLanLamBaiId(request.getLanLamBaiId());
        entity.setCauHoiId(request.getCauHoiId());
        entity.setNoiDungTraLoi(request.getNoiDungTraLoi());
        entity.setDiem(request.getDiem());
        entity.setLaDung(request.getLaDung());
        entity.setNhanXet(request.getNhanXet());
    }
}
