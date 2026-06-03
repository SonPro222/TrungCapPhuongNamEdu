package org.example.trungcapphuongnam.module.lms.service.nghiepvu;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.security.CurrentUserService;
import org.example.trungcapphuongnam.module.lms.dto.request.nghiepvu.NopBaiRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.KetQuaBaiTapLopResponse;
import org.example.trungcapphuongnam.module.lms.dto.response.LanLamBaiOnlineResponse;
import org.example.trungcapphuongnam.module.lms.dto.response.nghiepvu.KetQuaChamTuDong;
import org.example.trungcapphuongnam.module.lms.entity.*;
import org.example.trungcapphuongnam.module.lms.enums.*;
import org.example.trungcapphuongnam.module.lms.mapper.KetQuaBaiTapLopMapper;
import org.example.trungcapphuongnam.module.lms.mapper.LanLamBaiOnlineMapper;
import org.example.trungcapphuongnam.module.lms.repository.*;
import org.example.trungcapphuongnam.module.giangDay.repository.SinhVienLopHocPhanRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LmsNghiepVuServiceImpl implements LmsNghiepVuService {

    private static final BigDecimal DIEM_DAT_MAC_DINH = BigDecimal.valueOf(5);
    private static final BigDecimal THANG_DIEM_MAC_DINH = BigDecimal.TEN;

    private final CurrentUserService currentUserService;
    private final BaiTapLopRepository baiTapLopRepository;
    private final BaiTapOnlineRepository baiTapOnlineRepository;
    private final BaiTapOnlineCauHoiRepository baiTapOnlineCauHoiRepository;
    private final LanLamBaiOnlineRepository lanLamBaiOnlineRepository;
    private final LanLamBaiCauHoiRepository lanLamBaiCauHoiRepository;
    private final LanLamBaiDapAnRepository lanLamBaiDapAnRepository;
    private final CauHoiVersionRepository cauHoiVersionRepository;
    private final DapAnCauHoiVersionRepository dapAnCauHoiVersionRepository;
    private final CauTraLoiSinhVienRepository cauTraLoiSinhVienRepository;
    private final CauTraLoiChonDapAnRepository cauTraLoiChonDapAnRepository;
    private final KetQuaBaiTapLopRepository ketQuaBaiTapLopRepository;
    private final SinhVienLopHocPhanRepository sinhVienLopHocPhanRepository;

    @Override
    public LanLamBaiOnlineResponse batDauLamBai(Long baiTapLopId) {
        Long sinhVienId = currentUserService.getSinhVienId();
        BaiTapLop baiTapLop = getBaiTapLop(baiTapLopId);
        validateSinhVienDuocLamBai(sinhVienId, baiTapLop);

        Optional<LanLamBaiOnline> dangLam = lanLamBaiOnlineRepository
                .findByBaiTapLopIdAndSinhVienIdAndTrangThai(baiTapLopId, sinhVienId, TrangThaiLanLamBai.dang_lam);
        if (dangLam.isPresent()) {
            taoSnapshotNeuChuaCo(dangLam.get(), baiTapLop);
            return LanLamBaiOnlineMapper.toResponse(dangLam.get());
        }

        int lanThuMoi = lanLamBaiOnlineRepository
                .findFirstByBaiTapLopIdAndSinhVienIdOrderByLanThuDesc(baiTapLopId, sinhVienId)
                .map(x -> x.getLanThu() == null ? 1 : x.getLanThu() + 1)
                .orElse(1);

        int soLanToiDa = baiTapLop.getSoLanLamToiDa() == null ? 1 : baiTapLop.getSoLanLamToiDa();
        if (lanThuMoi > soLanToiDa) {
            throw new BadRequestException("Sinh viên đã hết số lần làm bài");
        }

        LanLamBaiOnline lanLam = LanLamBaiOnline.builder()
                .baiTapLopId(baiTapLopId)
                .sinhVienId(sinhVienId)
                .lanThu(lanThuMoi)
                .thoiGianBatDau(LocalDateTime.now())
                .trangThai(TrangThaiLanLamBai.dang_lam)
                .build();

        LanLamBaiOnline saved = lanLamBaiOnlineRepository.save(lanLam);
        taoSnapshotNeuChuaCo(saved, baiTapLop);
        return LanLamBaiOnlineMapper.toResponse(saved);
    }

    @Override
    public LanLamBaiOnlineResponse nopBai(Long lanLamBaiId, NopBaiRequest request) {
        Long sinhVienId = currentUserService.getSinhVienId();
        LanLamBaiOnline lanLam = getLanLam(lanLamBaiId);

        if (!Objects.equals(lanLam.getSinhVienId(), sinhVienId)) {
            throw new AccessDeniedException("Không được nộp bài của sinh viên khác");
        }
        if (lanLam.getTrangThai() != TrangThaiLanLamBai.dang_lam) {
            throw new BadRequestException("Lần làm bài không còn ở trạng thái đang làm");
        }

        BaiTapLop baiTapLop = getBaiTapLop(lanLam.getBaiTapLopId());
        validateSinhVienDuocLamBai(sinhVienId, baiTapLop);
        validateHanNop(lanLam, baiTapLop);
        taoSnapshotNeuChuaCo(lanLam, baiTapLop);

        luuCauTraLoi(lanLam, request == null ? List.of() : request.getCauTraLois());
        KetQuaChamTuDong ketQuaCham = chamTuDong(lanLam);

        lanLam.setThoiGianNop(LocalDateTime.now());
        lanLam.setDiemTuDong(ketQuaCham.getDiemTuDong());
        lanLam.setSoCauDung(ketQuaCham.getSoCauDung());
        lanLam.setSoCauSai(ketQuaCham.getSoCauSai());
        lanLam.setTongSoCau(ketQuaCham.getTongSoCau());

        if (ketQuaCham.isCanGiaoVienCham()) {
            lanLam.setTrangThai(TrangThaiLanLamBai.chua_cham);
        } else {
            lanLam.setDiemCuoiCung(ketQuaCham.getDiemTuDong());
            lanLam.setTrangThai(TrangThaiLanLamBai.da_cham);
        }

        LanLamBaiOnline saved = lanLamBaiOnlineRepository.save(lanLam);
        if (saved.getTrangThai() == TrangThaiLanLamBai.da_cham) {
            tinhLaiKetQua(saved.getBaiTapLopId(), saved.getSinhVienId());
        }

        return LanLamBaiOnlineMapper.toResponse(saved);
    }

    @Override
    public KetQuaBaiTapLopResponse tinhLaiKetQua(Long baiTapLopId, Long sinhVienId) {
        BaiTapLop baiTapLop = getBaiTapLop(baiTapLopId);
        List<LanLamBaiOnline> lanLams = lanLamBaiOnlineRepository
                .findByBaiTapLopIdAndSinhVienIdAndTrangThaiInOrderByLanThuDesc(
                        baiTapLopId,
                        sinhVienId,
                        List.of(TrangThaiLanLamBai.da_cham)
                );

        if (lanLams.isEmpty()) {
            throw new BadRequestException("Sinh viên chưa có lần làm bài đã chấm");
        }

        CachTinhDiem cachTinhDiem = baiTapLop.getCachTinhDiem() == null
                ? CachTinhDiem.lay_diem_cao_nhat
                : baiTapLop.getCachTinhDiem();

        LanLamBaiOnline selected;
        BigDecimal diemHeThong;

        switch (cachTinhDiem) {
            case lay_diem_lan_cuoi -> {
                selected = lanLams.stream().max(Comparator.comparing(x -> nvl(x.getLanThu()))).orElseThrow();
                diemHeThong = selected.getDiemCuoiCung();
            }
            case lay_diem_trung_binh -> {
                selected = lanLams.stream().max(Comparator.comparing(x -> nvl(x.getLanThu()))).orElseThrow();
                diemHeThong = average(lanLams);
            }
            case giao_vien_chon -> {
                selected = lanLams.stream().max(Comparator.comparing(x -> nvl(x.getLanThu()))).orElseThrow();
                diemHeThong = selected.getDiemCuoiCung();
            }
            case lay_diem_cao_nhat -> {
                selected = lanLams.stream().max(Comparator.comparing(x -> nvl(x.getDiemCuoiCung()))).orElseThrow();
                diemHeThong = selected.getDiemCuoiCung();
            }
            default -> throw new BadRequestException("Cách tính điểm không hợp lệ");
        }

        KetQuaBaiTapLop ketQua = ketQuaBaiTapLopRepository
                .findByBaiTapLopIdAndSinhVienId(baiTapLopId, sinhVienId)
                .orElseGet(() -> KetQuaBaiTapLop.builder()
                        .baiTapLopId(baiTapLopId)
                        .sinhVienId(sinhVienId)
                        .build());

        if (ketQua.getTrangThai() == TrangThaiKetQuaBaiTap.da_chot
                || ketQua.getTrangThai() == TrangThaiKetQuaBaiTap.da_day_sang_diem) {
            throw new BadRequestException("Kết quả đã chốt hoặc đã đẩy điểm, không được tự tính lại");
        }

        ketQua.setLanLamDuocChonId(selected.getId());
        ketQua.setDiemHeThong(diemHeThong);
        ketQua.setDiemCuoiCung(ketQua.getDiemGiaoVienChinhSua() != null ? ketQua.getDiemGiaoVienChinhSua() : diemHeThong);
        ketQua.setKetQua(nvl(ketQua.getDiemCuoiCung()).compareTo(DIEM_DAT_MAC_DINH) >= 0 ? KetQuaDat.dat : KetQuaDat.khong_dat);
        ketQua.setTrangThai(TrangThaiKetQuaBaiTap.tam_tinh);
        ketQua.setNgayTinhDiem(LocalDateTime.now());

        return KetQuaBaiTapLopMapper.toResponse(ketQuaBaiTapLopRepository.save(ketQua));
    }

    private void taoSnapshotNeuChuaCo(LanLamBaiOnline lanLam, BaiTapLop baiTapLop) {
        if (!lanLamBaiCauHoiRepository.findByLanLamBaiIdOrderByThuTuAsc(lanLam.getId()).isEmpty()) {
            return;
        }

        BaiTapOnline baiTapOnline = baiTapOnlineRepository.findById(baiTapLop.getBaiTapOnlineId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài tập online id=" + baiTapLop.getBaiTapOnlineId()));

        List<BaiTapOnlineCauHoi> cauHoiTrongBai = baiTapOnlineCauHoiRepository
                .findByBaiTapOnlineIdOrderByThuTuAsc(baiTapOnline.getId());
        if (cauHoiTrongBai.isEmpty()) {
            throw new BadRequestException("Bài tập online chưa có câu hỏi");
        }

        boolean tronCauHoi = Boolean.TRUE.equals(baiTapLop.getTronCauHoi())
                || (baiTapLop.getTronCauHoi() == null && Boolean.TRUE.equals(baiTapOnline.getTronCauHoi()));
        if (tronCauHoi) {
            Collections.shuffle(cauHoiTrongBai);
        }

        int thuTuCau = 1;
        for (BaiTapOnlineCauHoi item : cauHoiTrongBai) {
            CauHoiVersion cauHoi = cauHoiVersionRepository.findById(item.getCauHoiVersionId())
                    .orElseThrow(() -> new BadRequestException("Không tìm thấy câu hỏi version id=" + item.getCauHoiVersionId()));

            BigDecimal diemCau = item.getDiem() != null ? item.getDiem()
                    : cauHoi.getDiemMacDinh() != null ? cauHoi.getDiemMacDinh()
                    : BigDecimal.ONE;

            LanLamBaiCauHoi snapshot = lanLamBaiCauHoiRepository.save(LanLamBaiCauHoi.builder()
                    .lanLamBaiId(lanLam.getId())
                    .cauHoiVersionId(cauHoi.getId())
                    .noiDungSnapshot(cauHoi.getNoiDung())
                    .loaiCauHoiSnapshot(cauHoi.getLoaiCauHoi())
                    .diemSnapshot(diemCau)
                    .thuTu(tronCauHoi ? thuTuCau++ : item.getThuTu())
                    .build());

            List<DapAnCauHoiVersion> dapAns = dapAnCauHoiVersionRepository
                    .findByCauHoiVersionIdOrderByThuTuAsc(cauHoi.getId());
            boolean tronDapAn = Boolean.TRUE.equals(baiTapLop.getTronDapAn())
                    || (baiTapLop.getTronDapAn() == null && Boolean.TRUE.equals(baiTapOnline.getTronDapAn()));
            if (tronDapAn) {
                Collections.shuffle(dapAns);
            }

            int thuTuDapAn = 1;
            for (DapAnCauHoiVersion dapAn : dapAns) {
                lanLamBaiDapAnRepository.save(LanLamBaiDapAn.builder()
                        .lanLamBaiCauHoiId(snapshot.getId())
                        .dapAnCauHoiVersionId(dapAn.getId())
                        .noiDungSnapshot(dapAn.getNoiDung())
                        .laDapAnDungSnapshot(dapAn.getLaDapAnDung())
                        .thuTu(tronDapAn ? thuTuDapAn++ : dapAn.getThuTu())
                        .build());
            }
        }
    }

    private void validateSinhVienDuocLamBai(Long sinhVienId, BaiTapLop baiTapLop) {
        if (baiTapLop.getTrangThai() != TrangThaiBaiTapLop.dang_mo) {
            throw new BadRequestException("Bài tập lớp chưa mở hoặc đã đóng");
        }

        LocalDateTime now = LocalDateTime.now();
        if (baiTapLop.getThoiGianMo() != null && now.isBefore(baiTapLop.getThoiGianMo())) {
            throw new BadRequestException("Chưa tới thời gian mở bài");
        }
        if (baiTapLop.getThoiGianDong() != null && now.isAfter(baiTapLop.getThoiGianDong())
                && !Boolean.TRUE.equals(baiTapLop.getChoPhepLamQuaHan())) {
            throw new BadRequestException("Bài tập đã quá hạn");
        }

        boolean trongLop = sinhVienLopHocPhanRepository.existsBySinhVienIdAndLopHocPhanId(
                sinhVienId,
                baiTapLop.getLopHocPhanId()
        );
        if (!trongLop) {
            throw new AccessDeniedException("Sinh viên không thuộc lớp học phần này");
        }
    }

    private void validateHanNop(LanLamBaiOnline lanLam, BaiTapLop baiTapLop) {
        if (Boolean.TRUE.equals(baiTapLop.getChoPhepLamQuaHan())) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        if (baiTapLop.getThoiGianDong() != null && now.isAfter(baiTapLop.getThoiGianDong())) {
            lanLam.setTrangThai(TrangThaiLanLamBai.qua_han);
            lanLamBaiOnlineRepository.save(lanLam);
            throw new BadRequestException("Bài đã quá hạn nộp");
        }

        if (baiTapLop.getThoiLuongLamBaiPhut() != null && lanLam.getThoiGianBatDau() != null) {
            LocalDateTime hanNopTheoThoiLuong = lanLam.getThoiGianBatDau().plusMinutes(baiTapLop.getThoiLuongLamBaiPhut());
            if (now.isAfter(hanNopTheoThoiLuong)) {
                lanLam.setTrangThai(TrangThaiLanLamBai.qua_han);
                lanLamBaiOnlineRepository.save(lanLam);
                throw new BadRequestException("Đã quá thời lượng làm bài");
            }
        }
    }

    private void luuCauTraLoi(LanLamBaiOnline lanLam, List<NopBaiRequest.CauTraLoiRequest> cauTraLois) {
        List<CauTraLoiSinhVien> old = cauTraLoiSinhVienRepository.findByLanLamBaiId(lanLam.getId());
        List<Long> oldIds = old.stream().map(CauTraLoiSinhVien::getId).toList();
        if (!oldIds.isEmpty()) {
            cauTraLoiChonDapAnRepository.findByCauTraLoiIdIn(oldIds).forEach(cauTraLoiChonDapAnRepository::delete);
        }
        cauTraLoiSinhVienRepository.deleteByLanLamBaiId(lanLam.getId());

        List<LanLamBaiCauHoi> snapshots = lanLamBaiCauHoiRepository.findByLanLamBaiIdOrderByThuTuAsc(lanLam.getId());
        Map<Long, LanLamBaiCauHoi> cauHoiSnapshotMap = snapshots.stream()
                .collect(Collectors.toMap(LanLamBaiCauHoi::getId, Function.identity()));
        Map<Long, LanLamBaiCauHoi> cauHoiVersionMap = snapshots.stream()
                .collect(Collectors.toMap(LanLamBaiCauHoi::getCauHoiVersionId, Function.identity(), (a, b) -> a));

        List<LanLamBaiDapAn> allDapAn = lanLamBaiDapAnRepository
                .findByLanLamBaiCauHoiIdIn(snapshots.stream().map(LanLamBaiCauHoi::getId).toList());
        Map<Long, LanLamBaiDapAn> dapAnMap = allDapAn.stream()
                .collect(Collectors.toMap(LanLamBaiDapAn::getId, Function.identity()));

        for (NopBaiRequest.CauTraLoiRequest item : cauTraLois) {
            if (item == null) {
                continue;
            }
            LanLamBaiCauHoi cauHoiSnapshot = null;
            if (item.getLanLamBaiCauHoiId() != null) {
                cauHoiSnapshot = cauHoiSnapshotMap.get(item.getLanLamBaiCauHoiId());
            } else if (item.getCauHoiVersionId() != null) {
                cauHoiSnapshot = cauHoiVersionMap.get(item.getCauHoiVersionId());
            }
            if (cauHoiSnapshot == null) {
                throw new BadRequestException("Câu hỏi trả lời không thuộc lần làm bài này");
            }

            CauTraLoiSinhVien cauTraLoi = cauTraLoiSinhVienRepository.save(CauTraLoiSinhVien.builder()
                    .lanLamBaiId(lanLam.getId())
                    .lanLamBaiCauHoiId(cauHoiSnapshot.getId())
                    .noiDungTraLoi(item.getNoiDungTraLoi())
                    .build());

            if (item.getLanLamBaiDapAnIds() == null) {
                continue;
            }
            for (Long dapAnSnapshotId : new LinkedHashSet<>(item.getLanLamBaiDapAnIds())) {
                LanLamBaiDapAn dapAn = dapAnMap.get(dapAnSnapshotId);
                if (dapAn == null || !Objects.equals(dapAn.getLanLamBaiCauHoiId(), cauHoiSnapshot.getId())) {
                    throw new BadRequestException("Đáp án snapshot id=" + dapAnSnapshotId + " không thuộc câu hỏi snapshot id=" + cauHoiSnapshot.getId());
                }
                cauTraLoiChonDapAnRepository.save(CauTraLoiChonDapAn.builder()
                        .cauTraLoiId(cauTraLoi.getId())
                        .lanLamBaiDapAnId(dapAnSnapshotId)
                        .build());
            }
        }
    }

    private KetQuaChamTuDong chamTuDong(LanLamBaiOnline lanLam) {
        List<LanLamBaiCauHoi> cauHoiSnapshots = lanLamBaiCauHoiRepository.findByLanLamBaiIdOrderByThuTuAsc(lanLam.getId());
        if (cauHoiSnapshots.isEmpty()) {
            throw new BadRequestException("Lần làm bài chưa có snapshot câu hỏi");
        }

        Map<Long, CauTraLoiSinhVien> traLoiMap = cauTraLoiSinhVienRepository.findByLanLamBaiId(lanLam.getId())
                .stream()
                .collect(Collectors.toMap(CauTraLoiSinhVien::getLanLamBaiCauHoiId, Function.identity(), (a, b) -> a));

        List<Long> snapshotIds = cauHoiSnapshots.stream().map(LanLamBaiCauHoi::getId).toList();
        Map<Long, List<LanLamBaiDapAn>> dapAnsByCauHoi = lanLamBaiDapAnRepository.findByLanLamBaiCauHoiIdIn(snapshotIds)
                .stream()
                .collect(Collectors.groupingBy(LanLamBaiDapAn::getLanLamBaiCauHoiId));

        BigDecimal tongDiemCau = BigDecimal.ZERO;
        BigDecimal diemDatDuoc = BigDecimal.ZERO;
        int soCauDung = 0;
        int soCauSai = 0;
        boolean canGiaoVienCham = false;

        for (LanLamBaiCauHoi cauHoi : cauHoiSnapshots) {
            BigDecimal diemCau = cauHoi.getDiemSnapshot() != null ? cauHoi.getDiemSnapshot() : BigDecimal.ONE;
            tongDiemCau = tongDiemCau.add(diemCau);

            CauTraLoiSinhVien traLoi = traLoiMap.get(cauHoi.getId());
            boolean dung = false;

            if (cauHoi.getLoaiCauHoiSnapshot() == LoaiCauHoi.tu_luan) {
                canGiaoVienCham = true;
                if (traLoi != null) {
                    traLoi.setLaDung(null);
                    traLoi.setDiem(null);
                    cauTraLoiSinhVienRepository.save(traLoi);
                }
                continue;
            }

            if (traLoi != null) {
                dung = isTraLoiDung(cauHoi, traLoi, dapAnsByCauHoi.getOrDefault(cauHoi.getId(), List.of()));
                traLoi.setLaDung(dung);
                traLoi.setDiem(dung ? diemCau : BigDecimal.ZERO);
                cauTraLoiSinhVienRepository.save(traLoi);
            }

            if (dung) {
                soCauDung++;
                diemDatDuoc = diemDatDuoc.add(diemCau);
            } else {
                soCauSai++;
            }
        }

        BigDecimal diemTuDong = tongDiemCau.compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO
                : diemDatDuoc.multiply(THANG_DIEM_MAC_DINH).divide(tongDiemCau, 2, RoundingMode.HALF_UP);

        return KetQuaChamTuDong.builder()
                .diemTuDong(diemTuDong)
                .soCauDung(soCauDung)
                .soCauSai(soCauSai)
                .tongSoCau(cauHoiSnapshots.size())
                .canGiaoVienCham(canGiaoVienCham)
                .build();
    }

    private boolean isTraLoiDung(LanLamBaiCauHoi cauHoi, CauTraLoiSinhVien traLoi, List<LanLamBaiDapAn> dapAns) {
        Set<Long> dapAnDungIds = dapAns.stream()
                .filter(x -> Boolean.TRUE.equals(x.getLaDapAnDungSnapshot()))
                .map(LanLamBaiDapAn::getId)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        if (cauHoi.getLoaiCauHoiSnapshot() == LoaiCauHoi.dien_khuyet) {
            String text = normalize(traLoi.getNoiDungTraLoi());
            return dapAns.stream()
                    .filter(x -> Boolean.TRUE.equals(x.getLaDapAnDungSnapshot()))
                    .map(LanLamBaiDapAn::getNoiDungSnapshot)
                    .map(this::normalize)
                    .anyMatch(text::equals);
        }

        List<CauTraLoiChonDapAn> selected = cauTraLoiChonDapAnRepository.findByCauTraLoiId(traLoi.getId());
        Set<Long> selectedIds = selected.stream()
                .map(CauTraLoiChonDapAn::getLanLamBaiDapAnId)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        if (cauHoi.getLoaiCauHoiSnapshot() == LoaiCauHoi.trac_nghiem_1_dap_an
                || cauHoi.getLoaiCauHoiSnapshot() == LoaiCauHoi.dung_sai) {
            return selectedIds.size() == 1 && dapAnDungIds.size() == 1 && selectedIds.equals(dapAnDungIds);
        }

        if (cauHoi.getLoaiCauHoiSnapshot() == LoaiCauHoi.trac_nghiem_nhieu_dap_an) {
            return !dapAnDungIds.isEmpty() && selectedIds.equals(dapAnDungIds);
        }

        return false;
    }

    private BigDecimal average(List<LanLamBaiOnline> lanLams) {
        BigDecimal sum = lanLams.stream()
                .map(LanLamBaiOnline::getDiemCuoiCung)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(BigDecimal.valueOf(lanLams.size()), 2, RoundingMode.HALF_UP);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase().replaceAll("\s+", " ");
    }

    private int nvl(Integer value) {
        return value == null ? 0 : value;
    }

    private BigDecimal nvl(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private BaiTapLop getBaiTapLop(Long id) {
        return baiTapLopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài tập lớp id=" + id));
    }

    private LanLamBaiOnline getLanLam(Long id) {
        return lanLamBaiOnlineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lần làm bài id=" + id));
    }
}
