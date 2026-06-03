package org.example.trungcapphuongnam.module.lms.mapper;

import org.example.trungcapphuongnam.module.lms.dto.request.CauHoiVersionRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.CauHoiVersionResponse;
import org.example.trungcapphuongnam.module.lms.entity.CauHoiVersion;

public final class CauHoiVersionMapper {
    private CauHoiVersionMapper() {}

    public static CauHoiVersionResponse toResponse(CauHoiVersion entity) {
        if (entity == null) return null;
        return CauHoiVersionResponse.builder()
            .id(entity.getId())
            .cauHoiGocId(entity.getCauHoiGocId())
            .versionNo(entity.getVersionNo())
            .noiDung(entity.getNoiDung())
            .loaiCauHoi(entity.getLoaiCauHoi())
            .mucDo(entity.getMucDo())
            .diemMacDinh(entity.getDiemMacDinh())
            .giaiThichDapAn(entity.getGiaiThichDapAn())
            .trangThai(entity.getTrangThai())
            .nangLucDauRaId(entity.getNangLucDauRaId())
            .nguoiDuyetTaiKhoanId(entity.getNguoiDuyetTaiKhoanId())
            .thoiGianDuyet(entity.getThoiGianDuyet())
            .noiDungHash(entity.getNoiDungHash())
            .isLocked(entity.getIsLocked())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    public static CauHoiVersion toEntity(CauHoiVersionRequest request) {
        if (request == null) return null;
        return CauHoiVersion.builder()
            .cauHoiGocId(request.getCauHoiGocId())
            .versionNo(request.getVersionNo())
            .noiDung(request.getNoiDung())
            .loaiCauHoi(request.getLoaiCauHoi())
            .mucDo(request.getMucDo())
            .diemMacDinh(request.getDiemMacDinh())
            .giaiThichDapAn(request.getGiaiThichDapAn())
            .trangThai(request.getTrangThai())
            .nangLucDauRaId(request.getNangLucDauRaId())
            .nguoiDuyetTaiKhoanId(request.getNguoiDuyetTaiKhoanId())
            .thoiGianDuyet(request.getThoiGianDuyet())
            .noiDungHash(request.getNoiDungHash())
            .isLocked(request.getIsLocked())
            .build();
    }

    public static void updateEntity(CauHoiVersion entity, CauHoiVersionRequest request) {
        if (entity == null || request == null) return;
        entity.setCauHoiGocId(request.getCauHoiGocId());
        entity.setVersionNo(request.getVersionNo());
        entity.setNoiDung(request.getNoiDung());
        entity.setLoaiCauHoi(request.getLoaiCauHoi());
        entity.setMucDo(request.getMucDo());
        entity.setDiemMacDinh(request.getDiemMacDinh());
        entity.setGiaiThichDapAn(request.getGiaiThichDapAn());
        entity.setTrangThai(request.getTrangThai());
        entity.setNangLucDauRaId(request.getNangLucDauRaId());
        entity.setNguoiDuyetTaiKhoanId(request.getNguoiDuyetTaiKhoanId());
        entity.setThoiGianDuyet(request.getThoiGianDuyet());
        entity.setNoiDungHash(request.getNoiDungHash());
        entity.setIsLocked(request.getIsLocked());
    }
}
