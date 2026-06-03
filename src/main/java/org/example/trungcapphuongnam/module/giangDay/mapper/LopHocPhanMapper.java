package org.example.trungcapphuongnam.module.giangDay.mapper;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMon;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.MonHoc;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhMonRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.MonHocRepository;
import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.dto.request.LopHocPhanRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LopHocPhanResponse;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LopHocPhanMapper {

    private final MonHocRepository monHocRepository;
    private final ChuongTrinhMonRepository chuongTrinhMonRepository;

    public LopHocPhan toEntity(LopHocPhanRequest request) {
        if (request == null) return null;
        return LopHocPhan.builder()
                .chuongTrinhMonId(request.getChuongTrinhMonId())
                .monHocId(request.getMonHocId())
                .loaiLopHocPhan(request.getLoaiLopHocPhan())
                .siSoToiThieu(request.getSiSoToiThieu())
                .maLop(request.getMaLop())
                .tenLop(request.getTenLop())
                .soLuongToiDa(request.getSoLuongToiDa())
                .soBuoiHoc(request.getSoBuoiHoc())
                .soLuongHienTai(request.getSoLuongHienTai())
                .ngayBatDau(request.getNgayBatDau())
                .ngayKetThuc(request.getNgayKetThuc())
                .trangThai(request.getTrangThai())
                .build();
    }

    public LopHocPhanResponse toResponse(LopHocPhan entity) {
        if (entity == null) return null;

        ChuongTrinhMon chuongTrinhMon = entity.getChuongTrinhMonId() == null
                ? null
                : chuongTrinhMonRepository.findById(entity.getChuongTrinhMonId()).orElse(null);

        Long monHocId = chuongTrinhMon != null && chuongTrinhMon.getMonHocId() != null
                ? chuongTrinhMon.getMonHocId()
                : entity.getMonHocId();

        MonHoc monHoc = monHocId == null
                ? null
                : monHocRepository.findById(monHocId).orElse(null);

        return LopHocPhanResponse.builder()
                .id(entity.getId())
                .chuongTrinhMonId(entity.getChuongTrinhMonId())
                .monHocId(monHocId)
                .maMonHoc(monHoc != null ? monHoc.getMaMon() : null)
                .tenMonHoc(monHoc != null ? monHoc.getTenMon() : null)
                .maMonTrongCt(chuongTrinhMon != null ? chuongTrinhMon.getMaMonTrongCt() : null)
                .soTinChi(chuongTrinhMon != null ? chuongTrinhMon.getSoTinChi() : null)
                .loaiLopHocPhan(entity.getLoaiLopHocPhan())
                .siSoToiThieu(entity.getSiSoToiThieu())
                .soBuoiHoc(entity.getSoBuoiHoc())
                .maLop(entity.getMaLop())
                .tenLop(entity.getTenLop())
                .soLuongToiDa(entity.getSoLuongToiDa())
                .soLuongHienTai(entity.getSoLuongHienTai())
                .ngayBatDau(entity.getNgayBatDau())
                .ngayKetThuc(entity.getNgayKetThuc())
                .trangThai(entity.getTrangThai())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
    public void updateEntity(LopHocPhan entity, LopHocPhanRequest request) {
        if (entity == null || request == null) return;
        entity.setChuongTrinhMonId(request.getChuongTrinhMonId());
        entity.setMonHocId(request.getMonHocId());
        entity.setLoaiLopHocPhan(request.getLoaiLopHocPhan());
        entity.setSiSoToiThieu(request.getSiSoToiThieu());
        entity.setMaLop(request.getMaLop());
        entity.setTenLop(request.getTenLop());
        entity.setSoLuongToiDa(request.getSoLuongToiDa());
        entity.setSoBuoiHoc(request.getSoBuoiHoc());
        entity.setSoLuongHienTai(request.getSoLuongHienTai());
        entity.setNgayBatDau(request.getNgayBatDau());
        entity.setNgayKetThuc(request.getNgayKetThuc());
        entity.setTrangThai(request.getTrangThai());
    }
}
