package org.example.trungcapphuongnam.module.diem.mapper;

import org.example.trungcapphuongnam.module.diem.dto.request.KetQuaMonHocRequest;
import org.example.trungcapphuongnam.module.diem.dto.response.KetQuaMonHocResponse;
import org.example.trungcapphuongnam.module.diem.entity.KetQuaMonHoc;
import org.springframework.stereotype.Component;

@Component
public class KetQuaMonHocMapper {

    public KetQuaMonHoc toEntity(KetQuaMonHocRequest request) {
        if (request == null) return null;
        return KetQuaMonHoc.builder()
                .sinhVienId(request.getSinhVienId())
                .chuongTrinhMonId(request.getChuongTrinhMonId())
                .lopHocPhanId(request.getLopHocPhanId())
                .diemTongKet(request.getDiemTongKet())
                .diemPhanTram(request.getDiemPhanTram())
                .diemQuyDoi(request.getDiemQuyDoi())
                .ketQua(request.getKetQua())
                .soLanHoc(request.getSoLanHoc())
                .ngayCapNhat(request.getNgayCapNhat())
                .build();
    }

    public KetQuaMonHocResponse toResponse(KetQuaMonHoc entity) {
        if (entity == null) return null;
        return KetQuaMonHocResponse.builder()
                .id(entity.getId())
                .sinhVienId(entity.getSinhVienId())
                .chuongTrinhMonId(entity.getChuongTrinhMonId())
                .lopHocPhanId(entity.getLopHocPhanId())
                .diemTongKet(entity.getDiemTongKet())
                .diemPhanTram(entity.getDiemPhanTram())
                .diemQuyDoi(entity.getDiemQuyDoi())
                .ketQua(entity.getKetQua())
                .soLanHoc(entity.getSoLanHoc())
                .ngayCapNhat(entity.getNgayCapNhat())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(KetQuaMonHoc entity, KetQuaMonHocRequest request) {
        if (entity == null || request == null) return;
        entity.setSinhVienId(request.getSinhVienId());
        entity.setChuongTrinhMonId(request.getChuongTrinhMonId());
        entity.setLopHocPhanId(request.getLopHocPhanId());
        entity.setDiemTongKet(request.getDiemTongKet());
        entity.setDiemPhanTram(request.getDiemPhanTram());
        entity.setDiemQuyDoi(request.getDiemQuyDoi());
        entity.setKetQua(request.getKetQua());
        entity.setSoLanHoc(request.getSoLanHoc());
        entity.setNgayCapNhat(request.getNgayCapNhat());
    }
}
