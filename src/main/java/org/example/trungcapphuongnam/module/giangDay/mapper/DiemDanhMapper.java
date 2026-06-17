package org.example.trungcapphuongnam.module.giangDay.mapper;

import org.example.trungcapphuongnam.module.giangDay.dto.request.DiemDanhRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.DiemDanhResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.DiemDanh;
import org.example.trungcapphuongnam.module.giangDay.entity.LichHoc;
import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhan;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVien;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DiemDanhMapper {

    public DiemDanh toEntity(DiemDanhRequest request) {
        if (request == null) return null;
        return DiemDanh.builder()
                .lichHocId(request.getLichHocId())
                .sinhVienId(request.getSinhVienId())
                .trangThai(request.getTrangThai())
                .ghiChu(request.getGhiChu())
                .thoiGianDiemDanh(request.getThoiGianDiemDanh())
                .build();
    }

    public DiemDanhResponse toResponse(DiemDanh entity) {
        if (entity == null) return null;
        return DiemDanhResponse.builder()
                .id(entity.getId())
                .lichHocId(entity.getLichHocId())
                .sinhVienId(entity.getSinhVienId())
                .trangThai(entity.getTrangThai())
                .ghiChu(entity.getGhiChu())
                .thoiGianDiemDanh(entity.getThoiGianDiemDanh())
                .build();
    }

    public DiemDanhResponse toResponse(
            DiemDanh entity,
            Map<Long, LichHoc> lichHocMap,
            Map<Long, LopHocPhan> lopHocPhanMap,
            Map<Long, SinhVien> sinhVienMap
    ) {
        DiemDanhResponse response = toResponse(entity);
        if (response == null || entity == null) return response;

        LichHoc lichHoc = lichHocMap == null ? null : lichHocMap.get(entity.getLichHocId());
        if (lichHoc != null) {
            response.setNgayHoc(lichHoc.getNgayHoc());
            response.setNoiDungBuoiHoc(lichHoc.getNoiDungBuoiHoc());
            response.setLopHocPhanId(lichHoc.getLopHocPhanId());

            LopHocPhan lopHocPhan = lopHocPhanMap == null ? null : lopHocPhanMap.get(lichHoc.getLopHocPhanId());
            if (lopHocPhan != null) {
                response.setMaLop(lopHocPhan.getMaLop());
                response.setTenLop(lopHocPhan.getTenLop());
            }
        }

        SinhVien sinhVien = sinhVienMap == null ? null : sinhVienMap.get(entity.getSinhVienId());
        if (sinhVien != null) {
            response.setMaSinhVien(sinhVien.getMaSinhVien());
            response.setTenSinhVien(sinhVien.getHoTen());
        }

        return response;
    }

    public void updateEntity(DiemDanh entity, DiemDanhRequest request) {
        if (entity == null || request == null) return;
        entity.setLichHocId(request.getLichHocId());
        entity.setSinhVienId(request.getSinhVienId());
        entity.setTrangThai(request.getTrangThai());
        entity.setGhiChu(request.getGhiChu());
        entity.setThoiGianDiemDanh(request.getThoiGianDiemDanh());
    }
}
