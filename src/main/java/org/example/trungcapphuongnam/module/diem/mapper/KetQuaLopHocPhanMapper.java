package org.example.trungcapphuongnam.module.diem.mapper;

import org.example.trungcapphuongnam.module.diem.dto.request.KetQuaLopHocPhanRequest;
import org.example.trungcapphuongnam.module.diem.dto.response.KetQuaLopHocPhanResponse;
import org.example.trungcapphuongnam.module.diem.entity.KetQuaLopHocPhan;
import org.springframework.stereotype.Component;

@Component
public class KetQuaLopHocPhanMapper {

    public KetQuaLopHocPhan toDefaultEntity(Long sinhVienId, Long lopHocPhanId) {
        return KetQuaLopHocPhan.builder()
                .sinhVienId(sinhVienId)
                .lopHocPhanId(lopHocPhanId)
                .trangThai("nhap")
                .build();
    }

    public KetQuaLopHocPhan toEntity(KetQuaLopHocPhanRequest request) {
        if (request == null) return null;
        return KetQuaLopHocPhan.builder()
                .sinhVienId(request.getSinhVienId())
                .lopHocPhanId(request.getLopHocPhanId())
                .diemChuyenCan(request.getDiemChuyenCan())
                .diemQuaTrinh(request.getDiemQuaTrinh())
                .diemThi(request.getDiemThi())
                .diemTongKet(request.getDiemTongKet())
                .diemPhanTram(request.getDiemPhanTram())
                .diemQuyDoi(request.getDiemQuyDoi())
                .ketQua(request.getKetQua())
                .trangThai(request.getTrangThai())
                .ngayChot(request.getNgayChot())
                .ngayCongBo(request.getNgayCongBo())
                .build();
    }

    public KetQuaLopHocPhanResponse toResponse(KetQuaLopHocPhan entity) {
        if (entity == null) return null;
        return KetQuaLopHocPhanResponse.builder()
                .id(entity.getId())
                .sinhVienId(entity.getSinhVienId())
                .lopHocPhanId(entity.getLopHocPhanId())
                .diemChuyenCan(entity.getDiemChuyenCan())
                .diemQuaTrinh(entity.getDiemQuaTrinh())
                .diemThi(entity.getDiemThi())
                .diemTongKet(entity.getDiemTongKet())
                .diemPhanTram(entity.getDiemPhanTram())
                .diemQuyDoi(entity.getDiemQuyDoi())
                .ketQua(entity.getKetQua())
                .trangThai(entity.getTrangThai())
                .ngayChot(entity.getNgayChot())
                .ngayCongBo(entity.getNgayCongBo())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(KetQuaLopHocPhan entity, KetQuaLopHocPhanRequest request) {
        if (entity == null || request == null) return;
        entity.setSinhVienId(request.getSinhVienId());
        entity.setLopHocPhanId(request.getLopHocPhanId());
        entity.setDiemChuyenCan(request.getDiemChuyenCan());
        entity.setDiemQuaTrinh(request.getDiemQuaTrinh());
        entity.setDiemThi(request.getDiemThi());
        entity.setDiemTongKet(request.getDiemTongKet());
        entity.setDiemPhanTram(request.getDiemPhanTram());
        entity.setDiemQuyDoi(request.getDiemQuyDoi());
        entity.setKetQua(request.getKetQua());
        entity.setTrangThai(request.getTrangThai());
        entity.setNgayChot(request.getNgayChot());
        entity.setNgayCongBo(request.getNgayCongBo());
    }
}
