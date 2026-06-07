package org.example.trungcapphuongnam.module.chuongTrinh.mapper;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhVersionRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhVersionResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersion;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhVersionTongHopViewRepository;
import org.springframework.stereotype.Component;
@RequiredArgsConstructor
@Component
public class ChuongTrinhVersionMapper {
    private final ChuongTrinhVersionTongHopViewRepository tongHopRepository;
    public ChuongTrinhVersion toEntity(ChuongTrinhVersionRequest request) {
        if (request == null) return null;
        return ChuongTrinhVersion.builder()
                .chuongTrinhId(request.getChuongTrinhId())
                .maVersion(request.getMaVersion())
                .tenVersion(request.getTenVersion())
                .ngayApDung(request.getNgayApDung())
                .ngayHetHieuLuc(request.getNgayHetHieuLuc())
                .soQuyetDinh(request.getSoQuyetDinh())
                .ngayQuyetDinh(request.getNgayQuyetDinh())
                .nguoiKy(request.getNguoiKy())
                .coQuanBanHanh(request.getCoQuanBanHanh())
                .fileQuyetDinh(request.getFileQuyetDinh())
//                .tongTinChi(request.getTongTinChi())
//                .tongSoGio(request.getTongSoGio())
//                .tongGioLyThuyet(request.getTongGioLyThuyet())
//                .tongGioThucHanh(request.getTongGioThucHanh())
//                .tongGioKiemTra(request.getTongGioKiemTra())
                .laHienHanh(request.getLaHienHanh())

                .build();
    }

    public ChuongTrinhVersionResponse toResponse(ChuongTrinhVersion entity) {
        if (entity == null) return null;
        var tongHop = tongHopRepository.findById(entity.getId()).orElse(null);
        return ChuongTrinhVersionResponse.builder()
                .id(entity.getId())
                .chuongTrinhId(entity.getChuongTrinhId())
                .maVersion(entity.getMaVersion())
                .tenVersion(entity.getTenVersion())
                .ngayApDung(entity.getNgayApDung())
                .ngayHetHieuLuc(entity.getNgayHetHieuLuc())
                .soQuyetDinh(entity.getSoQuyetDinh())
                .ngayQuyetDinh(entity.getNgayQuyetDinh())
                .nguoiKy(entity.getNguoiKy())
                .coQuanBanHanh(entity.getCoQuanBanHanh())
                .fileQuyetDinh(entity.getFileQuyetDinh())
                .tongTinChi(tongHop != null ? tongHop.getTongTinChi() : null)
                .tongSoGio(tongHop != null ? tongHop.getTongSoGio() : null)
                .tongGioLyThuyet(tongHop != null ? tongHop.getTongGioLyThuyet() : null)
                .tongGioThucHanh(tongHop != null ? tongHop.getTongGioThucHanh() : null)
                .tongGioKiemTra(tongHop != null ? tongHop.getTongGioKiemTra() : null)
                .laHienHanh(entity.getLaHienHanh())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(ChuongTrinhVersion entity, ChuongTrinhVersionRequest request) {
        if (entity == null || request == null) return;
        entity.setChuongTrinhId(request.getChuongTrinhId());
        entity.setMaVersion(request.getMaVersion());
        entity.setTenVersion(request.getTenVersion());
        entity.setNgayApDung(request.getNgayApDung());
        entity.setNgayHetHieuLuc(request.getNgayHetHieuLuc());
        entity.setSoQuyetDinh(request.getSoQuyetDinh());
        entity.setNgayQuyetDinh(request.getNgayQuyetDinh());
        entity.setNguoiKy(request.getNguoiKy());
        entity.setCoQuanBanHanh(request.getCoQuanBanHanh());
        entity.setFileQuyetDinh(request.getFileQuyetDinh());
//        entity.setTongTinChi(request.getTongTinChi());
//        entity.setTongSoGio(request.getTongSoGio());
//        entity.setTongGioLyThuyet(request.getTongGioLyThuyet());
//        entity.setTongGioThucHanh(request.getTongGioThucHanh());
//        entity.setTongGioKiemTra(request.getTongGioKiemTra());
        entity.setLaHienHanh(request.getLaHienHanh());

    }
}
