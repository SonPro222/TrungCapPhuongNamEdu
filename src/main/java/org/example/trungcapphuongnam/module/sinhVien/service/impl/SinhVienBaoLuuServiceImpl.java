package org.example.trungcapphuongnam.module.sinhVien.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMon;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhMonRepository;
import org.example.trungcapphuongnam.module.diem.entity.KetQuaMonHoc;
import org.example.trungcapphuongnam.module.diem.repository.KetQuaMonHocRepository;
import org.example.trungcapphuongnam.module.sinhVien.SinhVienException;
import org.example.trungcapphuongnam.module.sinhVien.SinhVienNotFoundException;
import org.example.trungcapphuongnam.module.sinhVien.dto.request.SinhVienBaoLuuRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.request.SinhVienDiHocLaiRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.SinhVienBaoLuuResponse;
import org.example.trungcapphuongnam.module.sinhVien.entity.MienTruBaoLuu;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVien;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVienBaoLuu;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVienChuongTrinh;
import org.example.trungcapphuongnam.module.sinhVien.enums.MienTruBaoLuuLoai;
import org.example.trungcapphuongnam.module.sinhVien.enums.PheDuyetTrangThai;
import org.example.trungcapphuongnam.module.sinhVien.enums.SinhVienBaoLuuTrangThai;
import org.example.trungcapphuongnam.module.sinhVien.enums.SinhVienChuongTrinhTrangThai;
import org.example.trungcapphuongnam.module.sinhVien.enums.SinhVienTrangThai;
import org.example.trungcapphuongnam.module.sinhVien.mapper.SinhVienBaoLuuMapper;
import org.example.trungcapphuongnam.module.sinhVien.repository.MienTruBaoLuuRepository;
import org.example.trungcapphuongnam.module.sinhVien.repository.SinhVienBaoLuuRepository;
import org.example.trungcapphuongnam.module.sinhVien.repository.SinhVienChuongTrinhRepository;
import org.example.trungcapphuongnam.module.sinhVien.repository.SinhVienRepository;
import org.example.trungcapphuongnam.module.sinhVien.service.SinhVienBaoLuuService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SinhVienBaoLuuServiceImpl implements SinhVienBaoLuuService {

    private static final int SO_THANG_MOT_KY = 6;
    private static final int SO_KY_BAO_LUU_TOI_DA = 2;

    private final SinhVienBaoLuuRepository repository;
    private final SinhVienRepository sinhVienRepository;
    private final SinhVienChuongTrinhRepository sinhVienChuongTrinhRepository;
    private final ChuongTrinhMonRepository chuongTrinhMonRepository;
    private final KetQuaMonHocRepository ketQuaMonHocRepository;
    private final MienTruBaoLuuRepository mienTruBaoLuuRepository;
    private final SinhVienBaoLuuMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<SinhVienBaoLuuResponse> findAll(Long sinhVienId, SinhVienBaoLuuTrangThai trangThai, Pageable pageable) {
        if (sinhVienId != null && trangThai != null) {
            return repository.findBySinhVienIdAndTrangThaiOrderByCreatedAtDesc(sinhVienId, trangThai, pageable)
                    .map(mapper::toResponse);
        }

        if (sinhVienId != null) {
            return repository.findBySinhVienIdOrderByCreatedAtDesc(sinhVienId, pageable)
                    .map(mapper::toResponse);
        }

        if (trangThai != null) {
            return repository.findByTrangThaiOrderByCreatedAtDesc(trangThai, pageable)
                    .map(mapper::toResponse);
        }

        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SinhVienBaoLuuResponse findById(Long id) {
        return mapper.toResponse(getById(id));
    }

    @Override
    public SinhVienBaoLuuResponse baoLuu(SinhVienBaoLuuRequest request) {
        validateBaoLuuRequest(request);

        SinhVien sinhVien = getSinhVien(request.getSinhVienId());
        SinhVienChuongTrinh sinhVienChuongTrinh = getSinhVienChuongTrinh(request.getSinhVienChuongTrinhId());

        if (sinhVienChuongTrinh.getSinhVien() == null
                || !sinhVienChuongTrinh.getSinhVien().getId().equals(sinhVien.getId())) {
            throw new SinhVienException("Sinh viên chương trình không thuộc sinh viên đã chọn");
        }

        if (repository.existsBySinhVienIdAndTrangThai(sinhVien.getId(), SinhVienBaoLuuTrangThai.dang_bao_luu)) {
            throw new SinhVienException("Sinh viên đang có hồ sơ bảo lưu chưa đi học lại");
        }

        SinhVienBaoLuu entity = mapper.toEntity(request, sinhVien, sinhVienChuongTrinh);
        SinhVienBaoLuu saved = repository.save(entity);

        sinhVien.setTrangThai(SinhVienTrangThai.bao_luu);
        sinhVienRepository.save(sinhVien);

        sinhVienChuongTrinh.setTrangThai(SinhVienChuongTrinhTrangThai.tam_dung);
        sinhVienChuongTrinhRepository.save(sinhVienChuongTrinh);

        return mapper.toResponse(saved);
    }

    @Override
    public SinhVienBaoLuuResponse diHocLai(Long id, SinhVienDiHocLaiRequest request) {
        validateDiHocLaiRequest(request);

        SinhVienBaoLuu entity = getById(id);

        if (entity.getTrangThai() != SinhVienBaoLuuTrangThai.dang_bao_luu) {
            throw new SinhVienException("Hồ sơ bảo lưu này không còn trạng thái đang bảo lưu");
        }

        SinhVien sinhVien = entity.getSinhVien();
        SinhVienChuongTrinh sinhVienChuongTrinh = entity.getSinhVienChuongTrinh();

        if (sinhVien == null || sinhVienChuongTrinh == null) {
            throw new SinhVienException("Hồ sơ bảo lưu thiếu thông tin sinh viên hoặc chương trình");
        }

        kiemTraTrungDangKyVersionKhacDong(sinhVien.getId(), sinhVienChuongTrinh.getId(), request.getChuongTrinhVersionIdHocLai());

        int soMonDuocCongNhan = congNhanMonDaHoanThanh(
                sinhVien.getId(),
                sinhVienChuongTrinh.getChuongTrinhVersionId(),
                request.getChuongTrinhVersionIdHocLai()
        );

        entity.setNgayDiHocLai(request.getNgayDiHocLai());
        entity.setChuongTrinhVersionIdHocLai(request.getChuongTrinhVersionIdHocLai());
        entity.setKhungKyIdHocLai(request.getKhungKyIdHocLai());
        entity.setLopHanhChinhIdHocLai(request.getLopHanhChinhIdHocLai());
        entity.setTrangThai(SinhVienBaoLuuTrangThai.da_di_hoc_lai);
        entity.setGhiChu(request.getGhiChu());

        sinhVien.setTrangThai(SinhVienTrangThai.dang_hoc);
        sinhVienRepository.save(sinhVien);

        sinhVienChuongTrinh.setChuongTrinhVersionId(request.getChuongTrinhVersionIdHocLai());
        sinhVienChuongTrinh.setLopHanhChinhId(request.getLopHanhChinhIdHocLai());
        sinhVienChuongTrinh.setTrangThai(SinhVienChuongTrinhTrangThai.dang_hoc);
        sinhVienChuongTrinhRepository.save(sinhVienChuongTrinh);

        SinhVienBaoLuu saved = repository.save(entity);
        return mapper.toResponse(saved, soMonDuocCongNhan);
    }

    @Override
    public void huy(Long id) {
        SinhVienBaoLuu entity = getById(id);

        if (entity.getTrangThai() == SinhVienBaoLuuTrangThai.da_di_hoc_lai) {
            throw new SinhVienException("Không được hủy hồ sơ bảo lưu đã đi học lại");
        }

        entity.setTrangThai(SinhVienBaoLuuTrangThai.da_huy);
        repository.save(entity);

        SinhVien sinhVien = entity.getSinhVien();
        SinhVienChuongTrinh sinhVienChuongTrinh = entity.getSinhVienChuongTrinh();

        if (sinhVien != null) {
            sinhVien.setTrangThai(SinhVienTrangThai.dang_hoc);
            sinhVienRepository.save(sinhVien);
        }

        if (sinhVienChuongTrinh != null) {
            sinhVienChuongTrinh.setTrangThai(SinhVienChuongTrinhTrangThai.dang_hoc);
            sinhVienChuongTrinhRepository.save(sinhVienChuongTrinh);
        }
    }

    private void validateBaoLuuRequest(SinhVienBaoLuuRequest request) {
        if (request == null) {
            throw new SinhVienException("Dữ liệu bảo lưu không hợp lệ");
        }

        LocalDate ngayBatDau = request.getNgayBatDau() != null ? request.getNgayBatDau() : LocalDate.now();
        Integer soKyBaoLuu = request.getSoKyBaoLuu();

        if (soKyBaoLuu == null || soKyBaoLuu < 1 || soKyBaoLuu > SO_KY_BAO_LUU_TOI_DA) {
            throw new SinhVienException("Thời gian bảo lưu chỉ được từ 1 đến 2 kỳ");
        }

        LocalDate ngayKetThucDuKien = request.getNgayKetThucDuKien() != null
                ? request.getNgayKetThucDuKien()
                : ngayBatDau.plusMonths(soKyBaoLuu * SO_THANG_MOT_KY);

        if (ngayKetThucDuKien.isBefore(ngayBatDau)) {
            throw new SinhVienException("Ngày kết thúc dự kiến không được trước ngày bắt đầu bảo lưu");
        }

        LocalDate gioiHan = ngayBatDau.plusMonths(SO_KY_BAO_LUU_TOI_DA * SO_THANG_MOT_KY);

        if (ngayKetThucDuKien.isAfter(gioiHan)) {
            throw new SinhVienException("Thời gian bảo lưu không được quá 2 kỳ");
        }
    }

    private void validateDiHocLaiRequest(SinhVienDiHocLaiRequest request) {
        if (request == null) {
            throw new SinhVienException("Dữ liệu đi học lại không hợp lệ");
        }

        if (request.getNgayDiHocLai() == null) {
            throw new SinhVienException("Ngày đi học lại không được để trống");
        }

        if (request.getChuongTrinhVersionIdHocLai() == null) {
            throw new SinhVienException("Version học lại không được để trống");
        }

        if (request.getKhungKyIdHocLai() == null) {
            throw new SinhVienException("Kỳ học lại không được để trống");
        }

        if (request.getLopHanhChinhIdHocLai() == null) {
            throw new SinhVienException("Lớp hành chính học lại không được để trống");
        }
    }

    private int congNhanMonDaHoanThanh(Long sinhVienId, Long versionCuId, Long versionHocLaiId) {
        if (versionCuId == null || versionHocLaiId == null || versionCuId.equals(versionHocLaiId)) {
            return 0;
        }

        List<KetQuaMonHoc> ketQuaDat = new ArrayList<>();
        ketQuaDat.addAll(ketQuaMonHocRepository.findBySinhVienIdAndKetQua(sinhVienId, "dat"));
        ketQuaDat.addAll(ketQuaMonHocRepository.findBySinhVienIdAndKetQua(sinhVienId, "mien_hoc"));

        if (ketQuaDat.isEmpty()) {
            return 0;
        }

        Map<Long, ChuongTrinhMon> monCuTheoId = chuongTrinhMonRepository.findByChuongTrinhVersionId(versionCuId)
                .stream()
                .collect(Collectors.toMap(ChuongTrinhMon::getId, Function.identity(), (a, b) -> a));

        Map<Long, ChuongTrinhMon> monMoiTheoMonHocId = chuongTrinhMonRepository.findByChuongTrinhVersionId(versionHocLaiId)
                .stream()
                .filter(item -> item.getMonHocId() != null)
                .collect(Collectors.toMap(ChuongTrinhMon::getMonHocId, Function.identity(), (a, b) -> a));

        int soMonDuocCongNhan = 0;

        for (KetQuaMonHoc ketQua : ketQuaDat) {
            ChuongTrinhMon monCu = monCuTheoId.get(ketQua.getChuongTrinhMonId());

            if (monCu == null || monCu.getMonHocId() == null) {
                continue;
            }

            ChuongTrinhMon monMoi = monMoiTheoMonHocId.get(monCu.getMonHocId());

            if (monMoi == null) {
                continue;
            }

            boolean daCoCongNhan = mienTruBaoLuuRepository.existsBySinhVienIdAndChuongTrinhMonIdAndLoai(
                    sinhVienId,
                    monMoi.getId(),
                    MienTruBaoLuuLoai.cong_nhan
            );

            if (daCoCongNhan) {
                continue;
            }

            MienTruBaoLuu congNhan = MienTruBaoLuu.builder()
                    .sinhVien(getSinhVien(sinhVienId))
                    .chuongTrinhMonId(monMoi.getId())
                    .loai(MienTruBaoLuuLoai.cong_nhan)
                    .lyDo("Công nhận môn đã hoàn thành ở version cũ khi sinh viên đi học lại")
                    .trangThai(PheDuyetTrangThai.da_duyet)
                    .ngayDuyet(LocalDateTime.now())
                    .ghiChu("Tự động công nhận khi chuyển từ version " + versionCuId + " sang version " + versionHocLaiId)
                    .build();

            mienTruBaoLuuRepository.save(congNhan);
            soMonDuocCongNhan++;
        }

        return soMonDuocCongNhan;
    }

    private void kiemTraTrungDangKyVersionKhacDong(Long sinhVienId, Long sinhVienChuongTrinhId, Long versionHocLaiId) {
        sinhVienChuongTrinhRepository.findFirstBySinhVienIdAndChuongTrinhVersionId(sinhVienId, versionHocLaiId)
                .ifPresent(item -> {
                    if (!item.getId().equals(sinhVienChuongTrinhId)) {
                        throw new SinhVienException("Sinh viên đã có hồ sơ chương trình ở version học lại này");
                    }
                });
    }

    private SinhVienBaoLuu getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new SinhVienNotFoundException("Sinh viên bảo lưu", id));
    }

    private SinhVien getSinhVien(Long id) {
        return sinhVienRepository.findById(id)
                .orElseThrow(() -> new SinhVienNotFoundException("Sinh viên", id));
    }

    private SinhVienChuongTrinh getSinhVienChuongTrinh(Long id) {
        return sinhVienChuongTrinhRepository.findById(id)
                .orElseThrow(() -> new SinhVienNotFoundException("Sinh viên chương trình", id));
    }
}