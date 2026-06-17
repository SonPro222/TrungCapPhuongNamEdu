package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhVersionRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhVersionResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersion;
import org.example.trungcapphuongnam.module.chuongTrinh.enums.TrangThaiChuongTrinhVersion;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhVersionTongHopViewRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

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
                .laHienHanh(request.getLaHienHanh())
                .trangThai(request.getTrangThai())
                .build();
    }

    public ChuongTrinhVersionResponse toResponse(ChuongTrinhVersion entity) {
        if (entity == null) return null;

        // Tổng giờ/tín chỉ lấy từ DB view v_chuong_trinh_version_tong_hop,
        // view này tính từ: chuong_trinh_mon → v_syllabus_mon_hoc_tong_hop.
        // KHÔNG dùng số lưu cứng trên entity, KHÔNG dùng dữ liệu FE gửi lên.
        var tongHop = tongHopRepository.findById(entity.getId()).orElse(null);

        TrangThaiChuongTrinhVersion trangThai = trangThaiHieuDung(entity);

        int soMonChuaCoSyllabus = (int) tongHopRepository.countMonChuaCoSyllabus(entity.getId());
        String canhBao = soMonChuaCoSyllabus > 0
                ? "Có " + soMonChuaCoSyllabus + " môn chưa có syllabus áp dụng, tổng giờ/tín chỉ có thể chưa đầy đủ."
                : null;

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
                // Nguồn: v_chuong_trinh_version_tong_hop (tính từ syllabus áp dụng)
                .tongTinChi(tongHop != null ? tongHop.getTongTinChi() : null)
                .tongSoGio(tongHop != null ? tongHop.getTongSoGio() : null)
                .tongGioLyThuyet(tongHop != null ? tongHop.getTongGioLyThuyet() : null)
                .tongGioThucHanh(tongHop != null ? tongHop.getTongGioThucHanh() : null)
                .tongGioKiemTra(tongHop != null ? tongHop.getTongGioKiemTra() : null)
                .laHienHanh(entity.getLaHienHanh())
                .trangThai(trangThai)
                .conHieuLucTheoNgay(conHieuLucTheoNgay(entity))
                .duocPhepChinhSua(duocPhepChinhSua(entity))
                .duocPhepVanHanh(duocPhepVanHanh(entity))
                .lyDoTrangThai(lyDoTrangThai(entity))
                .soMonChuaCoSyllabus(soMonChuaCoSyllabus)
                .canhBaoTongHop(canhBao)
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
        entity.setLaHienHanh(request.getLaHienHanh());
        entity.setTrangThai(request.getTrangThai());
    }

    private TrangThaiChuongTrinhVersion trangThaiHieuDung(ChuongTrinhVersion entity) {
        if (entity.getTrangThai() != null) {
            return entity.getTrangThai();
        }
        return Boolean.TRUE.equals(entity.getLaHienHanh())
                ? TrangThaiChuongTrinhVersion.HIEN_HANH
                : TrangThaiChuongTrinhVersion.DANG_SOAN;
    }

    private boolean conHieuLucTheoNgay(ChuongTrinhVersion entity) {
        if (entity.getNgayApDung() == null || entity.getNgayHetHieuLuc() == null) {
            return false;
        }
        LocalDate today = LocalDate.now();
        return !today.isBefore(entity.getNgayApDung()) && !today.isAfter(entity.getNgayHetHieuLuc());
    }

    private boolean duocPhepChinhSua(ChuongTrinhVersion entity) {
        TrangThaiChuongTrinhVersion trangThai = trangThaiHieuDung(entity);
        return trangThai == TrangThaiChuongTrinhVersion.DANG_SOAN
                || trangThai == TrangThaiChuongTrinhVersion.CHO_AP_DUNG
                || trangThai == TrangThaiChuongTrinhVersion.HIEN_HANH;
    }

    private boolean duocPhepVanHanh(ChuongTrinhVersion entity) {
        return trangThaiHieuDung(entity) == TrangThaiChuongTrinhVersion.HIEN_HANH
                && conHieuLucTheoNgay(entity);
    }

    private String lyDoTrangThai(ChuongTrinhVersion entity) {
        TrangThaiChuongTrinhVersion trangThai = trangThaiHieuDung(entity);
        return switch (trangThai) {
            case DANG_SOAN -> "Version đang soạn, được cấu hình nhưng chưa vận hành chính thức.";
            case CHO_AP_DUNG -> "Version chờ áp dụng, chưa vận hành nếu chưa tới ngày áp dụng.";
            case HIEN_HANH -> conHieuLucTheoNgay(entity)
                    ? "Version đang hiện hành và còn trong thời gian hiệu lực."
                    : "Version có trạng thái hiện hành nhưng không nằm trong khoảng ngày hiệu lực.";
            case KHOA -> "Version đã khóa, không được sửa cấu trúc và không vận hành mới.";
            case HET_HIEU_LUC -> "Version đã hết hiệu lực, chỉ dùng để xem lịch sử.";
            case HUY -> "Version đã hủy, không dùng để vận hành.";
        };
    }
}
