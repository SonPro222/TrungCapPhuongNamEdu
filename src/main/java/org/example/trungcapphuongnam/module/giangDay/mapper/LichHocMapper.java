package org.example.trungcapphuongnam.module.giangDay.mapper;

import org.example.trungcapphuongnam.module.giangDay.dto.request.LichHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LichHocPreviewItemResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LichHocResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.CaHoc;
import org.example.trungcapphuongnam.module.giangDay.entity.GiaoVien;
import org.example.trungcapphuongnam.module.giangDay.entity.LichHoc;
import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.entity.PhongHoc;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLichHoc;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class LichHocMapper {

    public LichHoc toEntity(LichHocRequest request) {
        if (request == null) return null;
        return LichHoc.builder()
                .lopHocPhanId(request.getLopHocPhanId())
                .giaoVienId(request.getGiaoVienId())
                .phongHocId(request.getPhongHocId())
                .caHocId(request.getCaHocId())
                .ngayHoc(request.getNgayHoc())
                .noiDungBuoiHoc(request.getNoiDungBuoiHoc())
                .trangThai(request.getTrangThai())
                .ghiChu(request.getGhiChu())
                .vuotKhungKy(request.getVuotKhungKy())
                .ngayKetThucGoc(request.getNgayKetThucGoc())
                .soNgayVuotKhungKy(request.getSoNgayVuotKhungKy())
                .build();
    }

    public LichHocRequest toRequestFromPreviewItem(Long lopHocPhanId, LichHocPreviewItemResponse item) {
        if (item == null) return null;
        return LichHocRequest.builder()
                .lopHocPhanId(lopHocPhanId)
                .giaoVienId(item.getGiaoVienId())
                .phongHocId(item.getPhongHocId())
                .caHocId(item.getCaHocId())
                .caHocIds(item.getCaHocId() == null ? null : List.of(item.getCaHocId()))
                .thuTrongTuan(tinhThuTrongTuan(item.getNgayHoc()))
                .ngayHoc(item.getNgayHoc())
                .noiDungBuoiHoc(item.getNoiDungBuoiHoc())
                .trangThai(TrangThaiLichHoc.du_kien)
                .ghiChu(null)
                .choPhepVuotNgayKetThuc(item.getVuotKhungKy())
                .vuotKhungKy(item.getVuotKhungKy())
                .ngayKetThucGoc(item.getNgayKetThucGoc())
                .soNgayVuotKhungKy(item.getSoNgayVuotKhungKy())
                .build();
    }

    public LichHocResponse toResponse(LichHoc entity) {
        if (entity == null) return null;
        return LichHocResponse.builder()
                .id(entity.getId())
                .lopHocPhanId(entity.getLopHocPhanId())
                .giaoVienId(entity.getGiaoVienId())
                .phongHocId(entity.getPhongHocId())
                .caHocId(entity.getCaHocId())
                .ngayHoc(entity.getNgayHoc())
                .thuTrongTuan(tinhThuTrongTuan(entity.getNgayHoc()))
                .ngayTrongThang(entity.getNgayHoc() == null ? null : entity.getNgayHoc().getDayOfMonth())
                .noiDungBuoiHoc(entity.getNoiDungBuoiHoc())
                .trangThai(entity.getTrangThai())
                .ghiChu(entity.getGhiChu())
                .vuotKhungKy(entity.getVuotKhungKy())
                .ngayKetThucGoc(entity.getNgayKetThucGoc())
                .soNgayVuotKhungKy(entity.getSoNgayVuotKhungKy())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public LichHocResponse toResponse(
            LichHoc entity,
            Map<Long, LopHocPhan> lopHocPhanMap,
            Map<Long, GiaoVien> giaoVienMap,
            Map<Long, PhongHoc> phongHocMap,
            Map<Long, CaHoc> caHocMap,
            Long soBuoiDaXep
    ) {
        LichHocResponse response = toResponse(entity);
        if (response == null || entity == null) return response;

        LopHocPhan lopHocPhan = lopHocPhanMap == null ? null : lopHocPhanMap.get(entity.getLopHocPhanId());
        if (lopHocPhan != null) {
            response.setMaLop(lopHocPhan.getMaLop());
            response.setTenLop(lopHocPhan.getTenLop());
            response.setSoBuoiHoc(lopHocPhan.getSoBuoiHoc());
            if (soBuoiDaXep != null) {
                response.setSoBuoiDaXep(soBuoiDaXep);
                long soBuoiHoc = lopHocPhan.getSoBuoiHoc() == null ? 0L : lopHocPhan.getSoBuoiHoc();
                response.setSoBuoiConLai(Math.max(soBuoiHoc - soBuoiDaXep, 0));
            }
        }

        GiaoVien giaoVien = giaoVienMap == null ? null : giaoVienMap.get(entity.getGiaoVienId());
        if (giaoVien != null) {
            response.setMaGiaoVien(giaoVien.getMaGiaoVien());
            response.setTenGiaoVien(giaoVien.getHoTen());
        }

        PhongHoc phongHoc = phongHocMap == null ? null : phongHocMap.get(entity.getPhongHocId());
        if (phongHoc != null) {
            response.setMaPhong(phongHoc.getMaPhong());
            response.setTenPhong(phongHoc.getTenPhong());
        }

        CaHoc caHoc = caHocMap == null ? null : caHocMap.get(entity.getCaHocId());
        if (caHoc != null) {
            response.setMaCa(caHoc.getMaCa());
            response.setTenCa(caHoc.getTenCa());
            response.setGioBatDau(caHoc.getGioBatDau());
            response.setGioKetThuc(caHoc.getGioKetThuc());
        }

        return response;
    }

    public void updateEntity(LichHoc entity, LichHocRequest request) {
        if (entity == null || request == null) return;
        entity.setLopHocPhanId(request.getLopHocPhanId());
        entity.setGiaoVienId(request.getGiaoVienId());
        entity.setPhongHocId(request.getPhongHocId());
        entity.setCaHocId(request.getCaHocId());
        entity.setNgayHoc(request.getNgayHoc());
        entity.setNoiDungBuoiHoc(request.getNoiDungBuoiHoc());
        entity.setTrangThai(request.getTrangThai());
        entity.setGhiChu(request.getGhiChu());
        entity.setVuotKhungKy(request.getVuotKhungKy());
        entity.setNgayKetThucGoc(request.getNgayKetThucGoc());
        entity.setSoNgayVuotKhungKy(request.getSoNgayVuotKhungKy());
    }

    private Integer tinhThuTrongTuan(LocalDate ngayHoc) {
        if (ngayHoc == null) return null;
        int dayOfWeek = ngayHoc.getDayOfWeek().getValue();
        return dayOfWeek == 7 ? 8 : dayOfWeek + 1;
    }
}
