package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhMonRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhMonResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMon;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.MonHoc;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.MonHocRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocTongHopViewRepository;
import org.springframework.stereotype.Component;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.view.SyllabusMonHocTongHopView;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocTongHopViewRepository;
@Component
@RequiredArgsConstructor
public class ChuongTrinhMonMapper {
    private final SyllabusMonHocTongHopViewRepository syllabusTongHopRepository;

    private final MonHocRepository monHocRepository;

    public ChuongTrinhMon toEntity(ChuongTrinhMonRequest request) {
        if (request == null) return null;
        return ChuongTrinhMon.builder()
                .chuongTrinhVersionId(request.getChuongTrinhVersionId())
                .monHocId(request.getMonHocId())
                .maMonTrongCt(request.getMaMonTrongCt())
                .khungKyId(request.getKhungKyId())
                .nhomKienThucId(request.getNhomKienThucId())
                .loai(request.getLoai())
                .loaiHocPhan(request.getLoaiHocPhan())
                .loaiPhamVi(chuanHoaLoaiPhamVi(request.getLoaiPhamVi()))
                .batBuoc(request.getBatBuoc())
                .laMonDieuKien(request.getLaMonDieuKien())
                .thuTu(request.getThuTu())
//                .soTinChi(request.getSoTinChi())
//                .tongGio(request.getTongGio())
//                .gioLyThuyet(request.getGioLyThuyet())
//                .gioThucHanh(request.getGioThucHanh())
//                .gioKiemTra(request.getGioKiemTra())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public ChuongTrinhMonResponse toResponse(ChuongTrinhMon entity) {
        if (entity == null) return null;

        MonHoc monHoc = entity.getMonHocId() == null
                ? null
                : monHocRepository.findById(entity.getMonHocId()).orElse(null);
        SyllabusMonHocTongHopView tongHop = entity.getId() == null
                ? null
                : syllabusTongHopRepository.findByChuongTrinhMonId(entity.getId()).orElse(null);
        return ChuongTrinhMonResponse.builder()
                .id(entity.getId())
                .chuongTrinhVersionId(entity.getChuongTrinhVersionId())
                .monHocId(entity.getMonHocId())
                .maMonHoc(monHoc != null ? monHoc.getMaMon() : null)
                .tenMonHoc(monHoc != null ? monHoc.getTenMon() : null)
                .maMonTrongCt(entity.getMaMonTrongCt())
                .khungKyId(entity.getKhungKyId())
                .nhomKienThucId(entity.getNhomKienThucId())
                .loai(entity.getLoai())
                .loaiHocPhan(entity.getLoaiHocPhan())
                .loaiPhamVi(entity.getLoaiPhamVi())
                .batBuoc(entity.getBatBuoc())
                .laMonDieuKien(entity.getLaMonDieuKien())
                .thuTu(entity.getThuTu())
                .soTinChi(tongHop != null ? tongHop.getSoTinChi() : null)
                .tongGio(tongHop != null ? tongHop.getTongGio() : null)
                .gioLyThuyet(tongHop != null ? tongHop.getGioLyThuyet() : null)
                .gioThucHanh(tongHop != null ? tongHop.getGioThucHanh() : null)
                .gioKiemTra(tongHop != null ? tongHop.getGioKiemTra() : null)
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(ChuongTrinhMon entity, ChuongTrinhMonRequest request) {
        if (entity == null || request == null) return;
        entity.setChuongTrinhVersionId(request.getChuongTrinhVersionId());
        entity.setMonHocId(request.getMonHocId());
        entity.setMaMonTrongCt(request.getMaMonTrongCt());
        entity.setKhungKyId(request.getKhungKyId());
        entity.setNhomKienThucId(request.getNhomKienThucId());
        entity.setLoai(request.getLoai());
        entity.setLoaiHocPhan(request.getLoaiHocPhan());
        entity.setLoaiPhamVi(chuanHoaLoaiPhamVi(request.getLoaiPhamVi()));
        entity.setBatBuoc(request.getBatBuoc());
        entity.setLaMonDieuKien(request.getLaMonDieuKien());
        entity.setThuTu(request.getThuTu());
//        entity.setSoTinChi(request.getSoTinChi());
//        entity.setTongGio(request.getTongGio());
//        entity.setGioLyThuyet(request.getGioLyThuyet());
//        entity.setGioThucHanh(request.getGioThucHanh());
//        entity.setGioKiemTra(request.getGioKiemTra());
        entity.setGhiChu(request.getGhiChu());
    }

    private String chuanHoaLoaiPhamVi(String value) {
        if (value == null || value.isBlank()) {
            return "mon_chuyen_nganh";
        }

        return value.trim();
    }
}


//package org.example.trungcapphuongnam.module.chuongTrinh.mapper;
//
//import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhMonRequest;
//import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhMonResponse;
//import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMon;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ChuongTrinhMonMapper {
//
//    public ChuongTrinhMon toEntity(ChuongTrinhMonRequest request) {
//        if (request == null) return null;
//        return ChuongTrinhMon.builder()
//                .chuongTrinhVersionId(request.getChuongTrinhVersionId())
//                .monHocId(request.getMonHocId())
//                .maMonTrongCt(request.getMaMonTrongCt())
//                .khungKyId(request.getKhungKyId())
//                .nhomKienThucId(request.getNhomKienThucId())
//                .loai(request.getLoai())
//                .loaiHocPhan(request.getLoaiHocPhan())
//                .loaiPhamVi(chuanHoaLoaiPhamVi(request.getLoaiPhamVi()))
//                .batBuoc(request.getBatBuoc())
//                .laMonDieuKien(request.getLaMonDieuKien())
//                .thuTu(request.getThuTu())
//                .soTinChi(request.getSoTinChi())
//                .tongGio(request.getTongGio())
//                .gioLyThuyet(request.getGioLyThuyet())
//                .gioThucHanh(request.getGioThucHanh())
//                .gioKiemTra(request.getGioKiemTra())
//                .ghiChu(request.getGhiChu())
//
//                .build();
//    }
//
//    public ChuongTrinhMonResponse toResponse(ChuongTrinhMon entity) {
//        if (entity == null) return null;
//        return ChuongTrinhMonResponse.builder()
//                .id(entity.getId())
//                .chuongTrinhVersionId(entity.getChuongTrinhVersionId())
//                .monHocId(entity.getMonHocId())
//                .maMonTrongCt(entity.getMaMonTrongCt())
//                .khungKyId(entity.getKhungKyId())
//                .nhomKienThucId(entity.getNhomKienThucId())
//                .loai(entity.getLoai())
//                .loaiHocPhan(entity.getLoaiHocPhan())
//                .loaiPhamVi(entity.getLoaiPhamVi())
//                .batBuoc(entity.getBatBuoc())
//                .laMonDieuKien(entity.getLaMonDieuKien())
//                .thuTu(entity.getThuTu())
//                .soTinChi(entity.getSoTinChi())
//                .tongGio(entity.getTongGio())
//                .gioLyThuyet(entity.getGioLyThuyet())
//                .gioThucHanh(entity.getGioThucHanh())
//                .gioKiemTra(entity.getGioKiemTra())
//                .ghiChu(entity.getGhiChu())
//                .createdAt(entity.getCreatedAt())
//                .updatedAt(entity.getUpdatedAt())
//                .build();
//    }
//
//    public void updateEntity(ChuongTrinhMon entity, ChuongTrinhMonRequest request) {
//        if (entity == null || request == null) return;
//        entity.setChuongTrinhVersionId(request.getChuongTrinhVersionId());
//        entity.setMonHocId(request.getMonHocId());
//        entity.setMaMonTrongCt(request.getMaMonTrongCt());
//        entity.setKhungKyId(request.getKhungKyId());
//        entity.setNhomKienThucId(request.getNhomKienThucId());
//        entity.setLoai(request.getLoai());
//        entity.setLoaiHocPhan(request.getLoaiHocPhan());
//        entity.setLoaiPhamVi(chuanHoaLoaiPhamVi(request.getLoaiPhamVi()));
//        entity.setBatBuoc(request.getBatBuoc());
//        entity.setLaMonDieuKien(request.getLaMonDieuKien());
//        entity.setThuTu(request.getThuTu());
//        entity.setSoTinChi(request.getSoTinChi());
//        entity.setTongGio(request.getTongGio());
//        entity.setGioLyThuyet(request.getGioLyThuyet());
//        entity.setGioThucHanh(request.getGioThucHanh());
//        entity.setGioKiemTra(request.getGioKiemTra());
//        entity.setGhiChu(request.getGhiChu());
//
//    }
//    private String chuanHoaLoaiPhamVi(String value) {
//        if (value == null || value.isBlank()) {
//            return "mon_chuyen_nganh";
//        }
//
//        return value.trim();
//    }
//}
