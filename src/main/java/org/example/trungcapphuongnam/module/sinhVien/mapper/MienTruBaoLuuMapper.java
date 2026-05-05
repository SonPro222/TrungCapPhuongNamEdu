package org.example.trungcapphuongnam.module.sinhVien.mapper;

import org.example.trungcapphuongnam.module.sinhVien.dto.request.MienTruBaoLuuRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.MienTruBaoLuuResponse;
import org.example.trungcapphuongnam.module.sinhVien.entity.MienTruBaoLuu;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVien;
import org.springframework.stereotype.Component;

@Component
public class MienTruBaoLuuMapper {

    public MienTruBaoLuu toEntity(MienTruBaoLuuRequest request, SinhVien sinhVien) {
        if (request == null) return null;
        return MienTruBaoLuu.builder()
                .sinhVien(sinhVien)
                .chuongTrinhMonId(request.getChuongTrinhMonId())
                .loai(request.getLoai())
                .lyDo(request.getLyDo())
                .minhChung(request.getMinhChung())
                .trangThai(request.getTrangThai())
                .nguoiDuyetId(request.getNguoiDuyetId())
                .ngayDuyet(request.getNgayDuyet())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public void updateEntity(MienTruBaoLuu entity, MienTruBaoLuuRequest request, SinhVien sinhVien) {
        entity.setSinhVien(sinhVien);
        entity.setChuongTrinhMonId(request.getChuongTrinhMonId());
        entity.setLoai(request.getLoai());
        entity.setLyDo(request.getLyDo());
        entity.setMinhChung(request.getMinhChung());
        entity.setTrangThai(request.getTrangThai());
        entity.setNguoiDuyetId(request.getNguoiDuyetId());
        entity.setNgayDuyet(request.getNgayDuyet());
        entity.setGhiChu(request.getGhiChu());
    }

    public MienTruBaoLuuResponse toResponse(MienTruBaoLuu entity) {
        if (entity == null) return null;
        SinhVien sv = entity.getSinhVien();
        return MienTruBaoLuuResponse.builder()
                .id(entity.getId())
                .sinhVienId(sv != null ? sv.getId() : null)
                .maSinhVien(sv != null ? sv.getMaSinhVien() : null)
                .hoTen(sv != null ? sv.getHoTen() : null)
                .chuongTrinhMonId(entity.getChuongTrinhMonId())
                .loai(entity.getLoai())
                .lyDo(entity.getLyDo())
                .minhChung(entity.getMinhChung())
                .trangThai(entity.getTrangThai())
                .nguoiDuyetId(entity.getNguoiDuyetId())
                .ngayDuyet(entity.getNgayDuyet())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
