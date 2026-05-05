package org.example.trungcapphuongnam.module.lms.mapper;

import org.example.trungcapphuongnam.module.lms.dto.request.KetQuaBaiTapLopRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.KetQuaBaiTapLopResponse;
import org.example.trungcapphuongnam.module.lms.entity.KetQuaBaiTapLop;

public final class KetQuaBaiTapLopMapper {
    private KetQuaBaiTapLopMapper() {}

    public static KetQuaBaiTapLopResponse toResponse(KetQuaBaiTapLop entity) {
        if (entity == null) return null;
        return KetQuaBaiTapLopResponse.builder()
            .id(entity.getId())
            .baiTapLopId(entity.getBaiTapLopId())
            .sinhVienId(entity.getSinhVienId())
            .lanLamDuocChonId(entity.getLanLamDuocChonId())
            .diemHeThong(entity.getDiemHeThong())
            .diemGiaoVienChinhSua(entity.getDiemGiaoVienChinhSua())
            .diemCuoiCung(entity.getDiemCuoiCung())
            .ketQua(entity.getKetQua())
            .trangThai(entity.getTrangThai())
            .diemChiTietId(entity.getDiemChiTietId())
            .nhanXet(entity.getNhanXet())
            .ngayTinhDiem(entity.getNgayTinhDiem())
            .ngayChot(entity.getNgayChot())
            .nguoiChotId(entity.getNguoiChotId())
            .ngayDaySangDiem(entity.getNgayDaySangDiem())
            .nguoiDaySangDiemId(entity.getNguoiDaySangDiemId())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    public static KetQuaBaiTapLop toEntity(KetQuaBaiTapLopRequest request) {
        if (request == null) return null;
        return KetQuaBaiTapLop.builder()
            .baiTapLopId(request.getBaiTapLopId())
            .sinhVienId(request.getSinhVienId())
            .lanLamDuocChonId(request.getLanLamDuocChonId())
            .diemHeThong(request.getDiemHeThong())
            .diemGiaoVienChinhSua(request.getDiemGiaoVienChinhSua())
            .diemCuoiCung(request.getDiemCuoiCung())
            .ketQua(request.getKetQua())
            .trangThai(request.getTrangThai())
            .diemChiTietId(request.getDiemChiTietId())
            .nhanXet(request.getNhanXet())
            .ngayTinhDiem(request.getNgayTinhDiem())
            .ngayChot(request.getNgayChot())
            .nguoiChotId(request.getNguoiChotId())
            .ngayDaySangDiem(request.getNgayDaySangDiem())
            .nguoiDaySangDiemId(request.getNguoiDaySangDiemId())
            .build();
    }

    public static void updateEntity(KetQuaBaiTapLop entity, KetQuaBaiTapLopRequest request) {
        if (entity == null || request == null) return;
        entity.setBaiTapLopId(request.getBaiTapLopId());
        entity.setSinhVienId(request.getSinhVienId());
        entity.setLanLamDuocChonId(request.getLanLamDuocChonId());
        entity.setDiemHeThong(request.getDiemHeThong());
        entity.setDiemGiaoVienChinhSua(request.getDiemGiaoVienChinhSua());
        entity.setDiemCuoiCung(request.getDiemCuoiCung());
        entity.setKetQua(request.getKetQua());
        entity.setTrangThai(request.getTrangThai());
        entity.setDiemChiTietId(request.getDiemChiTietId());
        entity.setNhanXet(request.getNhanXet());
        entity.setNgayTinhDiem(request.getNgayTinhDiem());
        entity.setNgayChot(request.getNgayChot());
        entity.setNguoiChotId(request.getNguoiChotId());
        entity.setNgayDaySangDiem(request.getNgayDaySangDiem());
        entity.setNguoiDaySangDiemId(request.getNguoiDaySangDiemId());
    }
}
