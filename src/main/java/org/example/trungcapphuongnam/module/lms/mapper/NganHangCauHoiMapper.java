package org.example.trungcapphuongnam.module.lms.mapper;

import org.example.trungcapphuongnam.module.lms.dto.request.NganHangCauHoiRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.NganHangCauHoiResponse;
import org.example.trungcapphuongnam.module.lms.entity.NganHangCauHoi;

public final class NganHangCauHoiMapper {
    private NganHangCauHoiMapper() {}

    public static NganHangCauHoiResponse toResponse(NganHangCauHoi entity) {
        if (entity == null) return null;
        return NganHangCauHoiResponse.builder()
            .id(entity.getId())
            .chuongTrinhMonId(entity.getChuongTrinhMonId())
            .nguoiTaoTaiKhoanId(entity.getNguoiTaoTaiKhoanId())
            .nguonTao(entity.getNguonTao())
            .noiDung(entity.getNoiDung())
            .loaiCauHoi(entity.getLoaiCauHoi())
            .mucDo(entity.getMucDo())
            .diemMacDinh(entity.getDiemMacDinh())
            .giaiThichDapAn(entity.getGiaiThichDapAn())
            .trangThai(entity.getTrangThai())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    public static NganHangCauHoi toEntity(NganHangCauHoiRequest request) {
        if (request == null) return null;
        return NganHangCauHoi.builder()
            .chuongTrinhMonId(request.getChuongTrinhMonId())
            .nguoiTaoTaiKhoanId(request.getNguoiTaoTaiKhoanId())
            .nguonTao(request.getNguonTao())
            .noiDung(request.getNoiDung())
            .loaiCauHoi(request.getLoaiCauHoi())
            .mucDo(request.getMucDo())
            .diemMacDinh(request.getDiemMacDinh())
            .giaiThichDapAn(request.getGiaiThichDapAn())
            .trangThai(request.getTrangThai())
            .build();
    }

    public static void updateEntity(NganHangCauHoi entity, NganHangCauHoiRequest request) {
        if (entity == null || request == null) return;
        entity.setChuongTrinhMonId(request.getChuongTrinhMonId());
        entity.setNguoiTaoTaiKhoanId(request.getNguoiTaoTaiKhoanId());
        entity.setNguonTao(request.getNguonTao());
        entity.setNoiDung(request.getNoiDung());
        entity.setLoaiCauHoi(request.getLoaiCauHoi());
        entity.setMucDo(request.getMucDo());
        entity.setDiemMacDinh(request.getDiemMacDinh());
        entity.setGiaiThichDapAn(request.getGiaiThichDapAn());
        entity.setTrangThai(request.getTrangThai());
    }
}
