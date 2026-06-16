package org.example.trungcapphuongnam.module.donTu.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.donTu.DonTuException;
import org.example.trungcapphuongnam.module.donTu.DonTuNotFoundException;
import org.example.trungcapphuongnam.module.donTu.dto.request.DonTuCreateRequest;
import org.example.trungcapphuongnam.module.donTu.dto.request.DonTuXuLyRequest;
import org.example.trungcapphuongnam.module.donTu.dto.response.DonTuDetailResponse;
import org.example.trungcapphuongnam.module.donTu.dto.response.DonTuResponse;
import org.example.trungcapphuongnam.module.donTu.entity.DonTu;
import org.example.trungcapphuongnam.module.donTu.entity.DonTuLichSu;
import org.example.trungcapphuongnam.module.donTu.entity.LoaiDonTu;
import org.example.trungcapphuongnam.module.donTu.enums.DonTuHanhDong;
import org.example.trungcapphuongnam.module.donTu.enums.DonTuTrangThai;
import org.example.trungcapphuongnam.module.donTu.enums.LoaiDonTuMa;
import org.example.trungcapphuongnam.module.donTu.mapper.DonTuMapper;
import org.example.trungcapphuongnam.module.donTu.repository.DonTuLichSuRepository;
import org.example.trungcapphuongnam.module.donTu.repository.DonTuRepository;
import org.example.trungcapphuongnam.module.donTu.repository.LoaiDonTuRepository;
import org.example.trungcapphuongnam.module.donTu.service.DonTuService;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVien;
import org.example.trungcapphuongnam.module.sinhVien.repository.SinhVienRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DonTuServiceImpl implements DonTuService {

    private final DonTuRepository donTuRepository;
    private final DonTuLichSuRepository donTuLichSuRepository;
    private final LoaiDonTuRepository loaiDonTuRepository;
    private final SinhVienRepository sinhVienRepository;
    private final DonTuMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<DonTuResponse> findCuaSinhVien(Long sinhVienId, Pageable pageable) {
        return donTuRepository.findBySinhVienIdOrderByNgayGuiDesc(sinhVienId, pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public DonTuDetailResponse findById(Long id) {
        DonTu donTu = getById(id);
        List<DonTuLichSu> lichSu = donTuLichSuRepository.findByDonTuIdOrderByCreatedAtAsc(donTu.getId());
        return mapper.toDetailResponse(donTu, lichSu);
    }

    @Override
    public DonTuResponse create(DonTuCreateRequest request) {
        if (request.getLoaiDonTuId() == null && (request.getLoaiDonTuMa() == null || request.getLoaiDonTuMa().isBlank())) {
            throw new DonTuException("Phải cung cấp loại đơn từ (loaiDonTuId hoặc loaiDonTuMa)");
        }

        LoaiDonTu loaiDonTu = timLoaiDonTu(request.getLoaiDonTuId(), request.getLoaiDonTuMa());

        SinhVien sinhVien = sinhVienRepository.findById(request.getSinhVienId())
                .orElseThrow(() -> new DonTuNotFoundException("Sinh viên", request.getSinhVienId()));

        // Chặn tạo đơn bảo lưu mới nếu đang có đơn bảo lưu chưa xử lý xong
        if (LoaiDonTuMa.bao_luu.name().equals(loaiDonTu.getMa())) {
            boolean daCo = donTuRepository.existsBySinhVienIdAndLoaiDonTuMaAndTrangThaiIn(
                    sinhVien.getId(),
                    LoaiDonTuMa.bao_luu.name(),
                    List.of(DonTuTrangThai.cho_duyet, DonTuTrangThai.dang_xu_ly,
                            DonTuTrangThai.yeu_cau_bo_sung, DonTuTrangThai.da_duyet)
            );
            if (daCo) {
                throw new DonTuException("Sinh viên đang có đơn bảo lưu chưa xử lý xong");
            }
        }

        DonTu donTu = DonTu.builder()
                .loaiDonTu(loaiDonTu)
                .sinhVien(sinhVien)
                .tieuDe(request.getTieuDe())
                .noiDung(request.getNoiDung())
                .lyDo(request.getLyDo())
                .duLieu(request.getDuLieu())
                .build();

        DonTu saved = donTuRepository.save(donTu);

        ghiLichSu(saved, DonTuHanhDong.tao_don, null, DonTuTrangThai.cho_duyet, null, "Tạo đơn mới");

        return mapper.toResponse(saved);
    }

    @Override
    public DonTuResponse huy(Long id, DonTuXuLyRequest request) {
        DonTu donTu = getById(id);

        if (donTu.getTrangThai() != DonTuTrangThai.cho_duyet
                && donTu.getTrangThai() != DonTuTrangThai.yeu_cau_bo_sung) {
            throw new DonTuException("Chỉ có thể hủy đơn ở trạng thái chờ duyệt hoặc yêu cầu bổ sung");
        }

        DonTuTrangThai cu = donTu.getTrangThai();
        donTu.setTrangThai(DonTuTrangThai.da_huy);
        DonTu saved = donTuRepository.save(donTu);

        ghiLichSu(saved, DonTuHanhDong.huy, cu, DonTuTrangThai.da_huy,
                request != null ? request.getNguoiThucHienId() : null,
                request != null ? request.getGhiChu() : null);

        return mapper.toResponse(saved);
    }

    private LoaiDonTu timLoaiDonTu(Long id, String ma) {
        if (id != null) {
            return loaiDonTuRepository.findById(id)
                    .orElseThrow(() -> new DonTuNotFoundException("Loại đơn từ", id));
        }
        return loaiDonTuRepository.findByMa(ma)
                .orElseThrow(() -> new DonTuException("Loại đơn từ không tồn tại với mã = " + ma));
    }

    DonTu getById(Long id) {
        return donTuRepository.findById(id)
                .orElseThrow(() -> new DonTuNotFoundException("Đơn từ", id));
    }

    void ghiLichSu(DonTu donTu, DonTuHanhDong hanhDong, DonTuTrangThai cu,
                   DonTuTrangThai moi, Long nguoiThucHienId, String ghiChu) {
        DonTuLichSu ls = DonTuLichSu.builder()
                .donTu(donTu)
                .hanhDong(hanhDong)
                .trangThaiCu(cu)
                .trangThaiMoi(moi)
                .nguoiThucHienId(nguoiThucHienId)
                .ghiChu(ghiChu)
                .build();
        donTuLichSuRepository.save(ls);
    }
}
