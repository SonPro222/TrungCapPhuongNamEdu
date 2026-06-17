package org.example.trungcapphuongnam.module.donTu.service.impl;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.donTu.DonTuException;
import org.example.trungcapphuongnam.module.donTu.DonTuNotFoundException;
import org.example.trungcapphuongnam.module.donTu.dto.request.DonTuDuyetBaoLuuRequest;
import org.example.trungcapphuongnam.module.donTu.dto.request.DonTuDuyetKhoiPhucDiemDanhRequest;
import org.example.trungcapphuongnam.module.donTu.dto.request.DonTuXuLyRequest;
import org.example.trungcapphuongnam.module.donTu.dto.response.DonTuDetailResponse;
import org.example.trungcapphuongnam.module.donTu.dto.response.DonTuResponse;
import org.example.trungcapphuongnam.module.donTu.entity.DonTu;
import org.example.trungcapphuongnam.module.donTu.entity.DonTuLichSu;
import org.example.trungcapphuongnam.module.donTu.enums.DonTuHanhDong;
import org.example.trungcapphuongnam.module.donTu.enums.DonTuTrangThai;
import org.example.trungcapphuongnam.module.donTu.enums.LoaiDonTuMa;
import org.example.trungcapphuongnam.module.donTu.mapper.DonTuMapper;
import org.example.trungcapphuongnam.module.donTu.repository.DonTuLichSuRepository;
import org.example.trungcapphuongnam.module.donTu.repository.DonTuRepository;
import org.example.trungcapphuongnam.module.donTu.service.AdminDonTuService;
import org.example.trungcapphuongnam.module.giangDay.entity.DiemDanh;
import org.example.trungcapphuongnam.module.giangDay.entity.DiemDanhLichSu;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiDiemDanh;
import org.example.trungcapphuongnam.module.giangDay.repository.DiemDanhLichSuRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.DiemDanhRepository;
import org.example.trungcapphuongnam.module.sinhVien.dto.request.SinhVienBaoLuuRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.SinhVienBaoLuuResponse;
import org.example.trungcapphuongnam.module.sinhVien.service.SinhVienBaoLuuService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminDonTuServiceImpl implements AdminDonTuService {

    private final DonTuRepository donTuRepository;
    private final DonTuLichSuRepository donTuLichSuRepository;
    private final DiemDanhRepository diemDanhRepository;
    private final DiemDanhLichSuRepository diemDanhLichSuRepository;
    private final SinhVienBaoLuuService sinhVienBaoLuuService;
    private final DonTuMapper mapper;
    private final DonTuServiceImpl donTuServiceHelper;

    @Override
    @Transactional(readOnly = true)
    public Page<DonTuResponse> findAll(Long sinhVienId, String loaiDonTuMa, DonTuTrangThai trangThai, Pageable pageable) {
        Specification<DonTu> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (sinhVienId != null) {
                predicates.add(cb.equal(root.get("sinhVien").get("id"), sinhVienId));
            }
            if (loaiDonTuMa != null && !loaiDonTuMa.isBlank()) {
                predicates.add(cb.equal(root.get("loaiDonTu").get("ma"), loaiDonTuMa));
            }
            if (trangThai != null) {
                predicates.add(cb.equal(root.get("trangThai"), trangThai));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return donTuRepository.findAll(spec, pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public DonTuDetailResponse findById(Long id) {
        DonTu donTu = donTuServiceHelper.getById(id);
        List<DonTuLichSu> lichSu = donTuLichSuRepository.findByDonTuIdOrderByCreatedAtAsc(donTu.getId());
        return mapper.toDetailResponse(donTu, lichSu);
    }

    @Override
    public DonTuResponse tiepNhan(Long id, DonTuXuLyRequest request) {
        DonTu donTu = donTuServiceHelper.getById(id);

        if (donTu.getTrangThai() != DonTuTrangThai.cho_duyet
                && donTu.getTrangThai() != DonTuTrangThai.yeu_cau_bo_sung) {
            throw new DonTuException("Chỉ có thể tiếp nhận đơn ở trạng thái chờ duyệt hoặc yêu cầu bổ sung");
        }

        DonTuTrangThai cu = donTu.getTrangThai();
        donTu.setTrangThai(DonTuTrangThai.dang_xu_ly);
        donTu.setNgayTiepNhan(LocalDateTime.now());
        donTu.setNguoiTiepNhanId(request != null ? request.getNguoiThucHienId() : null);

        DonTu saved = donTuRepository.save(donTu);

        donTuServiceHelper.ghiLichSu(saved, DonTuHanhDong.tiep_nhan, cu, DonTuTrangThai.dang_xu_ly,
                request != null ? request.getNguoiThucHienId() : null,
                request != null ? request.getGhiChu() : null);

        return mapper.toResponse(saved);
    }

    @Override
    public DonTuResponse yeuCauBoSung(Long id, DonTuXuLyRequest request) {
        DonTu donTu = donTuServiceHelper.getById(id);

        if (donTu.getTrangThai() != DonTuTrangThai.cho_duyet
                && donTu.getTrangThai() != DonTuTrangThai.dang_xu_ly) {
            throw new DonTuException("Chỉ có thể yêu cầu bổ sung khi đơn đang ở trạng thái chờ duyệt hoặc đang xử lý");
        }

        DonTuTrangThai cu = donTu.getTrangThai();
        donTu.setTrangThai(DonTuTrangThai.yeu_cau_bo_sung);
        if (request != null && request.getYKienXuLy() != null) {
            donTu.setYKienXuLy(request.getYKienXuLy());
        }

        DonTu saved = donTuRepository.save(donTu);

        donTuServiceHelper.ghiLichSu(saved, DonTuHanhDong.yeu_cau_bo_sung, cu, DonTuTrangThai.yeu_cau_bo_sung,
                request != null ? request.getNguoiThucHienId() : null,
                request != null ? request.getGhiChu() : null);

        return mapper.toResponse(saved);
    }

    @Override
    public DonTuResponse tuChoi(Long id, DonTuXuLyRequest request) {
        DonTu donTu = donTuServiceHelper.getById(id);

        if (donTu.getTrangThai() == DonTuTrangThai.da_hoan_tat
                || donTu.getTrangThai() == DonTuTrangThai.da_huy
                || donTu.getTrangThai() == DonTuTrangThai.tu_choi) {
            throw new DonTuException("Không thể từ chối đơn ở trạng thái " + donTu.getTrangThai().name());
        }

        DonTuTrangThai cu = donTu.getTrangThai();
        donTu.setTrangThai(DonTuTrangThai.tu_choi);
        donTu.setNgayTuChoi(LocalDateTime.now());
        donTu.setNguoiDuyetId(request != null ? request.getNguoiThucHienId() : null);
        donTu.setLyDoTuChoi(request != null ? request.getLyDoTuChoi() : null);

        DonTu saved = donTuRepository.save(donTu);

        donTuServiceHelper.ghiLichSu(saved, DonTuHanhDong.tu_choi, cu, DonTuTrangThai.tu_choi,
                request != null ? request.getNguoiThucHienId() : null,
                request != null ? request.getLyDoTuChoi() : null);

        return mapper.toResponse(saved);
    }

    @Override
    public DonTuResponse duyetBaoLuu(Long id, DonTuDuyetBaoLuuRequest request) {
        DonTu donTu = donTuServiceHelper.getById(id);

        if (!LoaiDonTuMa.bao_luu.name().equals(donTu.getLoaiDonTu().getMa())) {
            throw new DonTuException("Đơn này không phải loại bảo lưu");
        }

        if (donTu.getTrangThai() != DonTuTrangThai.cho_duyet
                && donTu.getTrangThai() != DonTuTrangThai.dang_xu_ly) {
            throw new DonTuException("Chỉ có thể duyệt đơn ở trạng thái chờ duyệt hoặc đang xử lý");
        }

        // Build request bảo lưu từ đơn
        SinhVienBaoLuuRequest baoLuuRequest = SinhVienBaoLuuRequest.builder()
                .sinhVienId(donTu.getSinhVien().getId())
                .sinhVienChuongTrinhId(request.getSinhVienChuongTrinhId())
                .ngayBatDau(request.getNgayBatDau())
                .ngayKetThucDuKien(request.getNgayKetThucDuKien())
                .soKyBaoLuu(request.getSoKyBaoLuu())
                .lyDo(request.getLyDo() != null ? request.getLyDo() : donTu.getLyDo())
                .ghiChu(request.getGhiChu())
                .build();

        // Gọi service bảo lưu hiện có - không tự viết lại logic
        SinhVienBaoLuuResponse baoLuuResponse = sinhVienBaoLuuService.baoLuu(baoLuuRequest);

        DonTuTrangThai cu = donTu.getTrangThai();
        donTu.setTrangThai(DonTuTrangThai.da_hoan_tat);
        donTu.setNgayDuyet(LocalDateTime.now());
        donTu.setNgayHoanTat(LocalDateTime.now());
        donTu.setNguoiDuyetId(request.getNguoiThucHienId());
        donTu.setYKienXuLy(request.getYKienXuLy());
        donTu.setDoiTuongLienQuanType("sinh_vien_bao_luu");
        donTu.setDoiTuongLienQuanId(baoLuuResponse.getId());

        DonTu saved = donTuRepository.save(donTu);

        donTuServiceHelper.ghiLichSu(saved, DonTuHanhDong.duyet, cu, DonTuTrangThai.da_hoan_tat,
                request.getNguoiThucHienId(), request.getYKienXuLy());
        donTuServiceHelper.ghiLichSu(saved, DonTuHanhDong.hoan_tat_nghiep_vu, DonTuTrangThai.da_hoan_tat, DonTuTrangThai.da_hoan_tat,
                request.getNguoiThucHienId(), "Đã tạo hồ sơ bảo lưu id=" + baoLuuResponse.getId());

        return mapper.toResponse(saved);
    }

    @Override
    public DonTuResponse duyetKhoiPhucDiemDanhGmail(Long id, DonTuDuyetKhoiPhucDiemDanhRequest request) {
        DonTu donTu = donTuServiceHelper.getById(id);

        if (!LoaiDonTuMa.khoi_phuc_diem_danh_gmail.name().equals(donTu.getLoaiDonTu().getMa())) {
            throw new DonTuException("Đơn này không phải loại khôi phục điểm danh Gmail");
        }

        if (donTu.getTrangThai() != DonTuTrangThai.cho_duyet
                && donTu.getTrangThai() != DonTuTrangThai.dang_xu_ly) {
            throw new DonTuException("Chỉ có thể duyệt đơn ở trạng thái chờ duyệt hoặc đang xử lý");
        }

        if (request.getDiemDanhId() == null) {
            throw new DonTuException("Phải cung cấp diemDanhId để cập nhật điểm danh");
        }

        DiemDanh diemDanh = diemDanhRepository.findById(request.getDiemDanhId())
                .orElseThrow(() -> new DonTuException("Điểm danh không tồn tại với id = " + request.getDiemDanhId()));

        // Mặc định vang_co_phep nếu không truyền
        TrangThaiDiemDanh trangThaiMoi = request.getTrangThaiMoi() != null
                ? request.getTrangThaiMoi()
                : TrangThaiDiemDanh.vang_co_phep;

        // Chỉ cho phép: co_mat, vang_co_phep, di_muon
        if (trangThaiMoi != TrangThaiDiemDanh.co_mat
                && trangThaiMoi != TrangThaiDiemDanh.vang_co_phep
                && trangThaiMoi != TrangThaiDiemDanh.di_muon) {
            throw new DonTuException("Trạng thái điểm danh mới chỉ được là: co_mat, vang_co_phep, di_muon");
        }

        TrangThaiDiemDanh trangThaiCu = diemDanh.getTrangThai();
        String ghiChuCu = diemDanh.getGhiChu();

        String ghiChuMoi = request.getGhiChuMoi() != null
                ? request.getGhiChuMoi()
                : "Khôi phục theo đơn " + donTu.getMaDon();

        diemDanh.setTrangThai(trangThaiMoi);
        diemDanh.setGhiChu(ghiChuMoi);
        diemDanhRepository.save(diemDanh);

        // Ghi lịch sử điểm danh
        DiemDanhLichSu diemDanhLichSu = DiemDanhLichSu.builder()
                .diemDanh(diemDanh)
                .donTuId(donTu.getId())
                .trangThaiCu(trangThaiCu)
                .trangThaiMoi(trangThaiMoi)
                .ghiChuCu(ghiChuCu)
                .ghiChuMoi(ghiChuMoi)
                .nguoiThucHienId(request.getNguoiThucHienId())
                .lyDo(request.getLyDo())
                .build();
        diemDanhLichSuRepository.save(diemDanhLichSu);

        DonTuTrangThai cu = donTu.getTrangThai();
        donTu.setTrangThai(DonTuTrangThai.da_hoan_tat);
        donTu.setNgayDuyet(LocalDateTime.now());
        donTu.setNgayHoanTat(LocalDateTime.now());
        donTu.setNguoiDuyetId(request.getNguoiThucHienId());
        donTu.setYKienXuLy(request.getYKienXuLy());
        donTu.setDoiTuongLienQuanType("diem_danh");
        donTu.setDoiTuongLienQuanId(diemDanh.getId());

        DonTu saved = donTuRepository.save(donTu);

        donTuServiceHelper.ghiLichSu(saved, DonTuHanhDong.duyet, cu, DonTuTrangThai.da_hoan_tat,
                request.getNguoiThucHienId(), request.getYKienXuLy());
        donTuServiceHelper.ghiLichSu(saved, DonTuHanhDong.hoan_tat_nghiep_vu, DonTuTrangThai.da_hoan_tat, DonTuTrangThai.da_hoan_tat,
                request.getNguoiThucHienId(), "Đã cập nhật điểm danh id=" + diemDanh.getId());

        return mapper.toResponse(saved);
    }
}
