package org.example.trungcapphuongnam.module.giangDay.service.impl;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMon;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.MonHoc;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHoc;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhMonRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.MonHocRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocRepository;
import org.example.trungcapphuongnam.module.daoTao.entity.LopHanhChinh;
import org.example.trungcapphuongnam.module.daoTao.repository.LopHanhChinhRepository;
import org.example.trungcapphuongnam.module.diem.repository.DiemChiTietRepository;
import org.example.trungcapphuongnam.module.giangDay.GiangDayException;
import org.example.trungcapphuongnam.module.giangDay.GiangDayNotFoundException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.AutoTaoLopHocPhanTheoKyRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.request.LopHocPhanRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.AutoTaoLopHocPhanItemResult;
import org.example.trungcapphuongnam.module.giangDay.dto.response.AutoTaoLopHocPhanMonResult;
import org.example.trungcapphuongnam.module.giangDay.dto.response.AutoTaoLopHocPhanTheoKyResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LopHocPhanResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.entity.SinhVienLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.enums.AutoTaoMode;
import org.example.trungcapphuongnam.module.giangDay.enums.LoaiLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiSinhVienLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.mapper.LopHocPhanMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.LichHocRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.LopHocPhanRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.PhanCongGiangDayRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.SinhVienLopHocPhanRepository;
import org.example.trungcapphuongnam.module.giangDay.service.LopHocPhanService;
import org.example.trungcapphuongnam.module.giangDay.service.SaoChepCauHinhDanhGiaService;
import org.example.trungcapphuongnam.module.giangDay.validator.LopHocPhanValidator;
import org.example.trungcapphuongnam.module.lms.repository.BaiTapLopRepository;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVienChuongTrinh;
import org.example.trungcapphuongnam.module.sinhVien.enums.SinhVienChuongTrinhTrangThai;
import org.example.trungcapphuongnam.module.sinhVien.repository.SinhVienChuongTrinhRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LopHocPhanServiceImpl implements LopHocPhanService {

    private final LopHocPhanValidator validator;
    private final LopHocPhanRepository repository;
    private final ChuongTrinhMonRepository chuongTrinhMonRepository;
    private final LopHocPhanMapper mapper;
    private final SaoChepCauHinhDanhGiaService saoChepCauHinhDanhGiaService;
    private final SyllabusMonHocRepository syllabusMonHocRepository;
    private final SinhVienChuongTrinhRepository sinhVienChuongTrinhRepository;
    private final SinhVienLopHocPhanRepository sinhVienLopHocPhanRepository;
    private final PhanCongGiangDayRepository phanCongGiangDayRepository;
    private final LichHocRepository lichHocRepository;
    private final DiemChiTietRepository diemChiTietRepository;
    private final BaiTapLopRepository baiTapLopRepository;
    private final MonHocRepository monHocRepository;
    private final LopHanhChinhRepository lopHanhChinhRepository;

    // ============================================================
    //  CRUD
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Page<LopHocPhanResponse> getAll(String keyword, Long chuongTrinhVersionId,
            Long chuongTrinhMonId, Long monHocId,
            LoaiLopHocPhan loaiLopHocPhan, TrangThaiLopHocPhan trangThai,
            Pageable pageable) {
        return repository.findAll(
                buildSpecification(keyword, chuongTrinhVersionId, chuongTrinhMonId, monHocId,
                        loaiLopHocPhan, trangThai),
                pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public LopHocPhanResponse getById(Long id) {
        return mapper.toResponse(findEntity(id));
    }

    @Override
    public LopHocPhanResponse create(LopHocPhanRequest request) {
        validator.validateCreate(request);
        LopHocPhan entity = mapper.toEntity(request);
        chuanHoaLopHocPhan(entity);
        LopHocPhan saved = repository.save(entity);
        if (saved.getLoaiLopHocPhan() == LoaiLopHocPhan.CHUYEN_NGANH && saved.getChuongTrinhMonId() != null) {
            saoChepCauHinhDanhGiaService.saoChepTuChuongTrinhMonSangLopHocPhan(saved.getId(), saved.getChuongTrinhMonId());
        }
        return mapper.toResponse(saved);
    }

    @Override
    public LopHocPhanResponse update(Long id, LopHocPhanRequest request) {
        validator.validateUpdate(id, request);
        LopHocPhan entity = findEntity(id);
        mapper.updateEntity(entity, request);
        chuanHoaLopHocPhan(entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.delete(findEntity(id));
    }

    // ============================================================
    //  Auto tao lop hoc phan theo ky
    // ============================================================

    @Override
    @Transactional
    public AutoTaoLopHocPhanTheoKyResponse autoTaoLopHocPhanTheoKy(AutoTaoLopHocPhanTheoKyRequest request) {
        validateAutoTaoRequest(request);
        AutoTaoMode mode = request.getMode();
        if (mode == AutoTaoMode.CHINH_THUC && !Boolean.TRUE.equals(request.getChoPhepMoDuKien())) {
            throw new GiangDayException("Mode CHINH_THUC yeu cau xac nhan choPhepMoDuKien = true");
        }

        List<SinhVienChuongTrinhTrangThai> tsTrangThai = List.of(
                SinhVienChuongTrinhTrangThai.da_dang_ky,
                SinhVienChuongTrinhTrangThai.da_dong_tien,
                SinhVienChuongTrinhTrangThai.dang_hoc);

        List<SinhVienChuongTrinh> danhSachSV;
        if (request.getLopHanhChinhIds() != null && !request.getLopHanhChinhIds().isEmpty()) {
            danhSachSV = sinhVienChuongTrinhRepository.findByLopHanhChinhIdInAndChuongTrinhVersionIdAndTrangThaiIn(
                    request.getLopHanhChinhIds(), request.getChuongTrinhVersionId(), tsTrangThai);
        } else {
            danhSachSV = sinhVienChuongTrinhRepository.findByChuongTrinhVersionId(request.getChuongTrinhVersionId())
                    .stream().filter(sv -> tsTrangThai.contains(sv.getTrangThai())).collect(Collectors.toList());
        }

        List<ChuongTrinhMon> danhSachMon = chuongTrinhMonRepository
                .findByChuongTrinhVersionIdAndKhungKyId(request.getChuongTrinhVersionId(), request.getKhungKyId());

        List<AutoTaoLopHocPhanMonResult> monResults = new ArrayList<>();
        List<String> canhBaoTong = new ArrayList<>();
        List<String> loiTong = new ArrayList<>();

        // Validate tuyen sinh truoc khi xu ly tung mon
        validateTuyenSinh(request, canhBaoTong);

        int tongLopDuKien = 0, tongLopDaTao = 0;
        for (ChuongTrinhMon mon : danhSachMon) {
            AutoTaoLopHocPhanMonResult r = xuLyMotMon(mon, danhSachSV, request);
            monResults.add(r);
            if (r.getLoi() != null) loiTong.add("[" + r.getMaMon() + "] " + r.getLoi());
            if (r.getCanhBao() != null) canhBaoTong.add("[" + r.getMaMon() + "] " + r.getCanhBao());
            if (r.getSoLopCanTao() != null) tongLopDuKien += r.getSoLopCanTao();
            if (r.getLopHocPhanResults() != null)
                tongLopDaTao += (int) r.getLopHocPhanResults().stream()
                        .filter(i -> i.getLopHocPhanId() != null).count();
        }

        return AutoTaoLopHocPhanTheoKyResponse.builder()
                .chuongTrinhVersionId(request.getChuongTrinhVersionId())
                .khungKyId(request.getKhungKyId())
                .mode(mode.name())
                .tongSinhVien(danhSachSV.size())
                .tongMonTrongKy(danhSachMon.size())
                .tongLopHocPhanDuKien(tongLopDuKien)
                .tongLopHocPhanDaTao(tongLopDaTao)
                .monResults(monResults)
                .canhBao(canhBaoTong.isEmpty() ? null : canhBaoTong)
                .loi(loiTong.isEmpty() ? null : loiTong)
                .build();
    }

    // ============================================================
    //  Validate tuyen sinh cho auto tao LHP
    // ============================================================

    /**
     * Kiểm tra trạng thái chốt tuyển sinh của LHC trước khi auto tạo LHP.
     * - CHINH_THUC: ném exception nếu có LHC chưa chốt và chưa qua ngày kết thúc nhận SV.
     * - DU_KIEN / PREVIEW: thêm cảnh báo vào canhBaoTong, không ném exception.
     *
     * Lưu ý: Không còn validate ngayBatDauLHP vs ngayKetThucNhanSinhVien vì
     * ngayBatDau/ngayKetThuc đã bỏ khỏi LHP (thời gian học thuộc lịch học).
     */
    private void validateTuyenSinh(AutoTaoLopHocPhanTheoKyRequest request, List<String> canhBaoTong) {
        List<Long> lhcIds = request.getLopHanhChinhIds();
        if (lhcIds == null || lhcIds.isEmpty()) return;

        List<LopHanhChinh> danhSachLHC = lopHanhChinhRepository.findAllByIdIn(lhcIds);
        AutoTaoMode mode = request.getMode();
        LocalDate ngayHienTai = LocalDate.now();

        List<String> lhcChuaChot = new ArrayList<>();

        for (LopHanhChinh lhc : danhSachLHC) {
            boolean daChot = Boolean.TRUE.equals(lhc.getDaChotTuyenSinh());
            LocalDate ngayKetThuc = lhc.getNgayKetThucNhanSinhVien();

            boolean daQuaNgayKetThuc = (ngayKetThuc != null && ngayHienTai.isAfter(ngayKetThuc));
            if (!daChot && !daQuaNgayKetThuc) {
                lhcChuaChot.add(lhc.getMaLop());
            }
        }

        if (!lhcChuaChot.isEmpty()) {
            String danhSach = String.join(", ", lhcChuaChot);
            if (mode == AutoTaoMode.CHINH_THUC) {
                throw new GiangDayException(
                        "Cac lop hanh chinh chua chot tuyen sinh va chua het han nhan sinh vien: ["
                        + danhSach + "]. Khong the auto xep chinh thuc.");
            }
            canhBaoTong.add("[CANH BAO] Lop hanh chinh chua chot tuyen sinh: [" + danhSach
                    + "]. Ket qua chi la xep du kien.");
        }
    }

    // ============================================================
    //  Xu ly tung mon
    // ============================================================

    private AutoTaoLopHocPhanMonResult xuLyMotMon(ChuongTrinhMon mon,
            List<SinhVienChuongTrinh> danhSachSV, AutoTaoLopHocPhanTheoKyRequest request) {
        String maMon = mon.getMaMonTrongCt();
        String tenMon = "Mon #" + mon.getMonHocId();
        if (mon.getMonHocId() != null) {
            Optional<MonHoc> mhOpt = monHocRepository.findById(mon.getMonHocId());
            if (mhOpt.isPresent()) {
                maMon = mhOpt.get().getMaMon() != null ? mhOpt.get().getMaMon() : maMon;
                tenMon = mhOpt.get().getTenMon() != null ? mhOpt.get().getTenMon() : tenMon;
            }
        }

        Optional<SyllabusMonHoc> syllabusOpt =
                syllabusMonHocRepository.findFirstByChuongTrinhMonIdOrderByIdAsc(mon.getId());
        if (syllabusOpt.isEmpty()) {
            return errMon(mon.getId(), mon.getMonHocId(), maMon, tenMon, null, 0, "Chua co syllabus mon hoc");
        }
        SyllabusMonHoc syllabus = syllabusOpt.get();
        int soBuoiHoc = syllabus.getSoBuoiHoc() != null ? syllabus.getSoBuoiHoc() : 0;
        if (soBuoiHoc < 1) {
            return errMon(mon.getId(), mon.getMonHocId(), maMon, tenMon, syllabus.getId(), soBuoiHoc,
                    "So buoi hoc = 0, khong the tao lop");
        }

        int n = danhSachSV.size();
        int siSoToiDa = request.getSiSoToiDa() != null ? request.getSiSoToiDa() : 1;
        int siSoToiThieu = request.getSiSoToiThieu() != null ? request.getSiSoToiThieu() : 1;
        int k = n == 0 ? 1 : (int) Math.ceil((double) n / siSoToiDa);
        String canhBao = n == 0 ? "Khong co sinh vien - tao 1 lop trong" : null;
        if (n > 0 && k > 1 && (n / k) < siSoToiThieu)
            canhBao = "Si so mot so lop thap hon toi thieu (" + siSoToiThieu + ")";

        String tienTo = request.getTienToMaLop() != null ? request.getTienToMaLop().trim() : "LHP";
        String maMonSafe = maMon != null ? maMon.replaceAll("[^A-Za-z0-9]", "") : "MON" + mon.getId();

        return switch (request.getMode()) {
            case PREVIEW -> xuLyPreview(mon, syllabus, danhSachSV, request, maMon, tenMon,
                    soBuoiHoc, k, siSoToiDa, siSoToiThieu, tienTo, maMonSafe, canhBao);
            case DU_KIEN -> xuLyDuKien(mon, syllabus, danhSachSV, request, maMon, tenMon,
                    soBuoiHoc, k, siSoToiDa, siSoToiThieu, tienTo, maMonSafe, canhBao, n);
            case CHINH_THUC -> xuLyChinhThuc(mon, syllabus, danhSachSV, request, maMon, tenMon,
                    soBuoiHoc, k, siSoToiDa, siSoToiThieu, tienTo, maMonSafe, canhBao, n);
        };
    }

    // ============================================================
    //  PREVIEW - chi tinh toan, khong ghi DB
    // ============================================================

    private AutoTaoLopHocPhanMonResult xuLyPreview(ChuongTrinhMon mon, SyllabusMonHoc syllabus,
            List<SinhVienChuongTrinh> danhSachSV, AutoTaoLopHocPhanTheoKyRequest request,
            String maMon, String tenMon, int soBuoiHoc, int k,
            int siSoToiDa, int siSoToiThieu, String tienTo, String maMonSafe, String canhBao) {
        int n = danhSachSV.size();
        int base = k > 0 ? n / k : 0;
        int rem = k > 0 ? n % k : 0;
        List<AutoTaoLopHocPhanItemResult> items = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            int siso = (i < rem) ? base + 1 : base;
            items.add(AutoTaoLopHocPhanItemResult.builder()
                    .lopHocPhanId(null)
                    .maLop(tienTo + "-" + maMonSafe + "-" + String.format("%02d", i + 1))
                    .tenLop(tenMon + " - Lop " + (i + 1))
                    .siSoDuKien(siso).siSoDaPhanBo(0)
                    .siSoToiThieu(siSoToiThieu).siSoToiDa(siSoToiDa)
                    .chuongTrinhMonId(mon.getId()).syllabusMonHocId(syllabus.getId())
                    .trangThai("du_kien").build());
        }
        return okMon(mon.getId(), mon.getMonHocId(), maMon, tenMon, syllabus.getId(), soBuoiHoc,
                n, k, items, canhBao);
    }

    // ============================================================
    //  DU_KIEN - tao/cap nhat lop du_kien, phan bo SV du kien
    // ============================================================

    private AutoTaoLopHocPhanMonResult xuLyDuKien(ChuongTrinhMon mon, SyllabusMonHoc syllabus,
            List<SinhVienChuongTrinh> danhSachSV, AutoTaoLopHocPhanTheoKyRequest request,
            String maMon, String tenMon, int soBuoiHoc, int k,
            int siSoToiDa, int siSoToiThieu, String tienTo, String maMonSafe, String canhBaoIn, int n) {
        String canhBao = canhBaoIn;

        List<LopHocPhan> existing = repository.findByChuongTrinhMonIdOrderByMaLopAsc(mon.getId())
                .stream().filter(lhp -> lhp.getTrangThai() == TrangThaiLopHocPhan.du_kien)
                .collect(Collectors.toList());

        List<LopHocPhan> coData = existing.stream()
                .filter(lhp -> coDataSau(lhp.getId())).collect(Collectors.toList());
        if (!coData.isEmpty() && coData.size() == existing.size()) {
            return errMon(mon.getId(), mon.getMonHocId(), maMon, tenMon, syllabus.getId(), soBuoiHoc,
                    "Tat ca lop du kien da co du lieu sau - khong the tu dong cap nhat");
        }

        List<LopHocPhan> chuaData = new ArrayList<>(existing.stream()
                .filter(lhp -> !coDataSau(lhp.getId())).collect(Collectors.toList()));
        if (!coData.isEmpty())
            canhBao = (canhBao != null ? canhBao + "; " : "")
                    + coData.size() + " lop co du lieu sau - giu nguyen";

        // Xoa SV khoi cac lop chua co data sau de phan bo lai
        for (LopHocPhan lhp : chuaData)
            sinhVienLopHocPhanRepository.deleteAll(
                    sinhVienLopHocPhanRepository.findByLopHocPhanIdOrderByIdDesc(lhp.getId()));

        int soEditable = Math.max(0, k - coData.size());
        if (chuaData.size() > soEditable) {
            repository.deleteAll(chuaData.subList(soEditable, chuaData.size()));
            chuaData = chuaData.subList(0, soEditable);
        }

        List<LopHocPhan> lopMoi = new ArrayList<>();
        for (int i = chuaData.size(); i < soEditable; i++) {
            LopHocPhan lhp = taoMoiLopHocPhan(mon, syllabus, request, tienTo, maMonSafe, tenMon,
                    soBuoiHoc, siSoToiThieu, siSoToiDa, TrangThaiLopHocPhan.du_kien, i + coData.size());
            lopMoi.add(repository.save(lhp));
        }

        List<LopHocPhan> lopEditable = new ArrayList<>(chuaData);
        lopEditable.addAll(lopMoi);

        Set<Long> svTrongCoData = svIdsTuLops(coData);
        List<AutoTaoLopHocPhanItemResult> items;
        if (Boolean.TRUE.equals(request.getTuDongPhanBoSinhVien()) && !lopEditable.isEmpty()) {
            List<SinhVienChuongTrinh> svCan = danhSachSV.stream()
                    .filter(sv -> !svTrongCoData.contains(sv.getSinhVien().getId()))
                    .collect(Collectors.toList());
            items = phanBoSinhVienVaoLops(lopEditable, svCan, mon.getId(), syllabus.getId(),
                    siSoToiThieu, siSoToiDa);
        } else {
            items = chiBaoGomLops(lopEditable, mon.getId(), syllabus.getId(), siSoToiThieu, siSoToiDa);
        }

        return okMon(mon.getId(), mon.getMonHocId(), maMon, tenMon, syllabus.getId(), soBuoiHoc,
                n, k, items, canhBao);
    }

    // ============================================================
    //  CHINH_THUC - chot lop dang_mo, phan bo SV chinh thuc
    // ============================================================

    private AutoTaoLopHocPhanMonResult xuLyChinhThuc(ChuongTrinhMon mon, SyllabusMonHoc syllabus,
            List<SinhVienChuongTrinh> danhSachSV, AutoTaoLopHocPhanTheoKyRequest request,
            String maMon, String tenMon, int soBuoiHoc, int k,
            int siSoToiDa, int siSoToiThieu, String tienTo, String maMonSafe, String canhBaoIn, int n) {
        String canhBao = canhBaoIn;

        List<LopHocPhan> existing = repository.findByChuongTrinhMonIdOrderByMaLopAsc(mon.getId())
                .stream().filter(lhp -> lhp.getTrangThai() != TrangThaiLopHocPhan.huy
                        && lhp.getTrangThai() != TrangThaiLopHocPhan.da_ket_thuc)
                .collect(Collectors.toList());

        List<LopHocPhan> coData = existing.stream()
                .filter(lhp -> coDataSau(lhp.getId())).collect(Collectors.toList());
        List<LopHocPhan> chuaData = new ArrayList<>(existing.stream()
                .filter(lhp -> !coDataSau(lhp.getId())).collect(Collectors.toList()));

        // Chuyen trang thai du_kien -> dang_mo
        for (LopHocPhan lhp : existing) {
            if (lhp.getTrangThai() == TrangThaiLopHocPhan.du_kien) {
                lhp.setTrangThai(TrangThaiLopHocPhan.dang_mo);
                repository.save(lhp);
            }
        }

        if (!coData.isEmpty())
            canhBao = (canhBao != null ? canhBao + "; " : "")
                    + coData.size() + " lop co du lieu sau - giu nguyen phan bo SV";

        int soEditable = Math.max(0, k - coData.size());
        if (chuaData.size() > soEditable) {
            for (LopHocPhan lhp : chuaData.subList(soEditable, chuaData.size())) {
                sinhVienLopHocPhanRepository.deleteAll(
                        sinhVienLopHocPhanRepository.findByLopHocPhanIdOrderByIdDesc(lhp.getId()));
                lhp.setTrangThai(TrangThaiLopHocPhan.huy);
                repository.save(lhp);
            }
            chuaData = chuaData.subList(0, soEditable);
        }

        // Xoa SV cu khoi lop chua co data sau
        for (LopHocPhan lhp : chuaData)
            sinhVienLopHocPhanRepository.deleteAll(
                    sinhVienLopHocPhanRepository.findByLopHocPhanIdOrderByIdDesc(lhp.getId()));

        List<LopHocPhan> lopMoi = new ArrayList<>();
        for (int i = chuaData.size(); i < soEditable; i++) {
            LopHocPhan lhp = taoMoiLopHocPhan(mon, syllabus, request, tienTo, maMonSafe, tenMon,
                    soBuoiHoc, siSoToiThieu, siSoToiDa, TrangThaiLopHocPhan.dang_mo, i + coData.size());
            lopMoi.add(repository.save(lhp));
        }

        List<LopHocPhan> lopEditable = new ArrayList<>(chuaData);
        lopEditable.addAll(lopMoi);

        Set<Long> svTrongCoData = svIdsTuLops(coData);

        // Build result cho lop co data sau (giu nguyen)
        List<AutoTaoLopHocPhanItemResult> items = new ArrayList<>();
        for (LopHocPhan lhp : coData) {
            long cnt = sinhVienLopHocPhanRepository.countByLopHocPhanId(lhp.getId());
            items.add(AutoTaoLopHocPhanItemResult.builder()
                    .lopHocPhanId(lhp.getId()).maLop(lhp.getMaLop()).tenLop(lhp.getTenLop())
                    .siSoDuKien((int) cnt).siSoDaPhanBo((int) cnt)
                    .siSoToiThieu(lhp.getSiSoToiThieu()).siSoToiDa(lhp.getSoLuongToiDa())
                    .chuongTrinhMonId(mon.getId()).syllabusMonHocId(syllabus.getId())
                    .trangThai(lhp.getTrangThai().name()).build());
        }

        if (Boolean.TRUE.equals(request.getTuDongPhanBoSinhVien()) && !lopEditable.isEmpty()) {
            List<SinhVienChuongTrinh> svCan = danhSachSV.stream()
                    .filter(sv -> !svTrongCoData.contains(sv.getSinhVien().getId()))
                    .collect(Collectors.toList());
            items.addAll(phanBoSinhVienVaoLops(lopEditable, svCan, mon.getId(), syllabus.getId(),
                    siSoToiThieu, siSoToiDa));
        } else {
            items.addAll(chiBaoGomLops(lopEditable, mon.getId(), syllabus.getId(), siSoToiThieu, siSoToiDa));
        }

        return okMon(mon.getId(), mon.getMonHocId(), maMon, tenMon, syllabus.getId(), soBuoiHoc,
                n, k, items, canhBao);
    }

    // ============================================================
    //  Helper: phan bo SV vao danh sach lop
    // ============================================================

    private List<AutoTaoLopHocPhanItemResult> phanBoSinhVienVaoLops(List<LopHocPhan> lops,
            List<SinhVienChuongTrinh> svList, Long chuongTrinhMonId, Long syllabusMonHocId,
            int siSoToiThieu, int siSoToiDa) {
        int n = svList.size();
        int k = lops.size();
        int base = k > 0 ? n / k : 0;
        int rem  = k > 0 ? n % k : 0;
        List<AutoTaoLopHocPhanItemResult> results = new ArrayList<>();
        int svIdx = 0;
        for (int i = 0; i < k; i++) {
            LopHocPhan lhp = lops.get(i);
            int cap = (i < rem) ? base + 1 : base;
            int phanBo = 0;
            for (int j = 0; j < cap && svIdx < svList.size(); j++, svIdx++) {
                Long svId = svList.get(svIdx).getSinhVien().getId();
                if (!sinhVienLopHocPhanRepository
                        .existsBySinhVienIdAndLopCuaChuongTrinhMon(svId, chuongTrinhMonId)) {
                    sinhVienLopHocPhanRepository.save(SinhVienLopHocPhan.builder()
                            .sinhVienId(svId)
                            .lopHocPhanId(lhp.getId())
                            .ngayDangKy(LocalDateTime.now())
                            .trangThai(TrangThaiSinhVienLopHocPhan.da_dang_ky)
                            .laHocLai(false)
                            .build());
                    phanBo++;
                }
            }
            lhp.setSoLuongHienTai(phanBo);
            repository.save(lhp);
            results.add(AutoTaoLopHocPhanItemResult.builder()
                    .lopHocPhanId(lhp.getId()).maLop(lhp.getMaLop()).tenLop(lhp.getTenLop())
                    .siSoDuKien(cap).siSoDaPhanBo(phanBo)
                    .siSoToiThieu(siSoToiThieu).siSoToiDa(siSoToiDa)
                    .chuongTrinhMonId(chuongTrinhMonId).syllabusMonHocId(syllabusMonHocId)
                    .trangThai(lhp.getTrangThai().name()).build());
        }
        return results;
    }

    private List<AutoTaoLopHocPhanItemResult> chiBaoGomLops(List<LopHocPhan> lops,
            Long chuongTrinhMonId, Long syllabusMonHocId, int siSoToiThieu, int siSoToiDa) {
        List<AutoTaoLopHocPhanItemResult> results = new ArrayList<>();
        for (LopHocPhan lhp : lops) {
            results.add(AutoTaoLopHocPhanItemResult.builder()
                    .lopHocPhanId(lhp.getId()).maLop(lhp.getMaLop()).tenLop(lhp.getTenLop())
                    .siSoDuKien(lhp.getSoLuongToiDa()).siSoDaPhanBo(0)
                    .siSoToiThieu(siSoToiThieu).siSoToiDa(siSoToiDa)
                    .chuongTrinhMonId(chuongTrinhMonId).syllabusMonHocId(syllabusMonHocId)
                    .trangThai(lhp.getTrangThai().name()).build());
        }
        return results;
    }

    private Set<Long> svIdsTuLops(List<LopHocPhan> lops) {
        Set<Long> ids = new HashSet<>();
        for (LopHocPhan lhp : lops)
            sinhVienLopHocPhanRepository.findByLopHocPhanIdOrderByIdDesc(lhp.getId())
                    .forEach(sv -> ids.add(sv.getSinhVienId()));
        return ids;
    }

    // ============================================================
    //  Helper: tao entity LopHocPhan moi
    // ============================================================

    private LopHocPhan taoMoiLopHocPhan(ChuongTrinhMon mon, SyllabusMonHoc syllabus,
            AutoTaoLopHocPhanTheoKyRequest request, String tienTo, String maMonSafe, String tenMon,
            int soBuoiHoc, int siSoToiThieu, int siSoToiDa,
            TrangThaiLopHocPhan trangThai, int index) {
        LopHocPhan lhp = new LopHocPhan();
        lhp.setChuongTrinhMonId(mon.getId());
        lhp.setMonHocId(mon.getMonHocId());
        lhp.setLoaiLopHocPhan(LoaiLopHocPhan.CHUYEN_NGANH);
        lhp.setMaLop(taoMaLopDoc(tienTo, maMonSafe, index + 1));
        lhp.setTenLop(tenMon + " - Lop " + (index + 1));
        lhp.setSiSoToiThieu(siSoToiThieu);
        lhp.setSoLuongToiDa(siSoToiDa);
        lhp.setSoLuongHienTai(0);
        lhp.setSoBuoiHoc(soBuoiHoc);
        // ngayBatDau/ngayKetThuc không set: thời gian học phần thuộc lịch học
        lhp.setTrangThai(trangThai);
        return lhp;
    }

    private String taoMaLopDoc(String tienTo, String maMonSafe, int index) {
        String candidate = tienTo + "-" + maMonSafe + "-" + String.format("%02d", index);
        if (!repository.existsByMaLopIgnoreCase(candidate)) return candidate;
        for (char c = 'A'; c <= 'Z'; c++) {
            String alt = candidate + "-" + c;
            if (!repository.existsByMaLopIgnoreCase(alt)) return alt;
        }
        return candidate + "-" + (System.currentTimeMillis() % 10000);
    }

    // ============================================================
    //  Helper: kiem tra du lieu sau cua lop hoc phan
    // ============================================================

    private boolean coDataSau(Long lopHocPhanId) {
        return phanCongGiangDayRepository.existsByLopHocPhanId(lopHocPhanId)
                || lichHocRepository.existsByLopHocPhanId(lopHocPhanId)
                || diemChiTietRepository.existsByLopHocPhanId(lopHocPhanId)
                || baiTapLopRepository.existsByLopHocPhanId(lopHocPhanId);
    }

    // ============================================================
    //  Helper: builder shortcuts cho MonResult
    // ============================================================

    private AutoTaoLopHocPhanMonResult errMon(Long chuongTrinhMonId, Long monHocId,
            String maMon, String tenMon, Long syllabusId, int soBuoiHoc, String loi) {
        return AutoTaoLopHocPhanMonResult.builder()
                .chuongTrinhMonId(chuongTrinhMonId).monHocId(monHocId)
                .maMon(maMon).tenMon(tenMon).syllabusMonHocId(syllabusId)
                .soBuoiHoc(soBuoiHoc).soLopCanTao(0)
                .lopHocPhanResults(Collections.emptyList()).loi(loi).build();
    }

    private AutoTaoLopHocPhanMonResult okMon(Long chuongTrinhMonId, Long monHocId,
            String maMon, String tenMon, Long syllabusId, int soBuoiHoc,
            int tongSV, int soLop, List<AutoTaoLopHocPhanItemResult> items, String canhBao) {
        return AutoTaoLopHocPhanMonResult.builder()
                .chuongTrinhMonId(chuongTrinhMonId).monHocId(monHocId)
                .maMon(maMon).tenMon(tenMon).syllabusMonHocId(syllabusId)
                .soBuoiHoc(soBuoiHoc).tongSinhVien(tongSV).soLopCanTao(soLop)
                .lopHocPhanResults(items).canhBao(canhBao).build();
    }

    // ============================================================
    //  Validate request
    // ============================================================

    private void validateAutoTaoRequest(AutoTaoLopHocPhanTheoKyRequest request) {
        if (request == null) throw new GiangDayException("Request khong duoc null");
        if (request.getMode() == null)
            throw new GiangDayException("Truong mode bat buoc (PREVIEW / DU_KIEN / CHINH_THUC)");
        if (request.getChuongTrinhVersionId() == null)
             throw new GiangDayException("chuongTrinhVersionId bat buoc");
        if (request.getKhungKyId() == null)
            throw new GiangDayException("khungKyId bat buoc");
        if (request.getMode() != AutoTaoMode.PREVIEW) {
            if (request.getSiSoToiThieu() == null || request.getSiSoToiThieu() < 1)
                throw new GiangDayException("siSoToiThieu phai > 0");
            if (request.getSiSoToiDa() == null || request.getSiSoToiDa() < 1)
                throw new GiangDayException("siSoToiDa phai > 0");
            if (request.getSiSoToiDa() < request.getSiSoToiThieu())
                throw new GiangDayException("siSoToiDa phai >= siSoToiThieu");
            // ngayBatDau/ngayKetThuc đã bỏ: thời gian học thuộc lịch học
        }
    }

    // ============================================================
    //  CRUD helpers
    // ============================================================

    private LopHocPhan findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GiangDayNotFoundException(
                        "Lop hoc phan khong ton tai voi id = " + id));
    }

    private void chuanHoaLopHocPhan(LopHocPhan entity) {
        if (entity.getLoaiLopHocPhan() == null) entity.setLoaiLopHocPhan(LoaiLopHocPhan.CHUYEN_NGANH);
        if (entity.getSiSoToiThieu() == null) entity.setSiSoToiThieu(1);
        if (entity.getSoLuongHienTai() == null) entity.setSoLuongHienTai(0);

        if (entity.getLoaiLopHocPhan() == LoaiLopHocPhan.CHUYEN_NGANH) {
            org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMon ctm =
                    chuongTrinhMonRepository.findById(entity.getChuongTrinhMonId())
                    .orElseThrow(() -> new GiangDayNotFoundException(
                            "Chuong trinh mon khong ton tai: id = " + entity.getChuongTrinhMonId()));
            entity.setMonHocId(ctm.getMonHocId());
            SyllabusMonHoc syllabus = syllabusMonHocRepository
                    .findFirstByChuongTrinhMonIdOrderByIdAsc(entity.getChuongTrinhMonId())
                    .orElseThrow(() -> new GiangDayException(
                            "Chuong trinh mon chua co syllabus: id = " + entity.getChuongTrinhMonId()));
            if (syllabus.getSoBuoiHoc() == null || syllabus.getSoBuoiHoc() < 1)
                throw new GiangDayException("Syllabus co so buoi hoc khong hop le (<1)");
            entity.setSoBuoiHoc(syllabus.getSoBuoiHoc());
        }

        if (entity.getLoaiLopHocPhan() == LoaiLopHocPhan.HOC_CHUNG) {
            entity.setChuongTrinhMonId(null);
            if (entity.getSoBuoiHoc() == null || entity.getSoBuoiHoc() < 1)
                throw new GiangDayException("Lop hoc chung phai cau hinh so buoi hoc (>= 1)");
        }
    }

    private Specification<LopHocPhan> buildSpecification(String keyword, Long chuongTrinhVersionId,
            Long chuongTrinhMonId, Long monHocId,
            LoaiLopHocPhan loaiLopHocPhan, TrangThaiLopHocPhan trangThai) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (keyword != null && !keyword.isBlank()) {
                String p = "%" + keyword.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("maLop")), p),
                        cb.like(cb.lower(root.get("tenLop")), p)));
            }
            if (chuongTrinhVersionId != null) {
                List<Long> ctmIds = chuongTrinhMonRepository
                        .findByChuongTrinhVersionId(chuongTrinhVersionId)
                        .stream()
                        .map(ChuongTrinhMon::getId)
                        .toList();
                if (ctmIds.isEmpty()) {
                    predicates.add(cb.disjunction()); // version không có môn → trả rỗng
                } else {
                    predicates.add(root.get("chuongTrinhMonId").in(ctmIds));
                }
            }
            if (chuongTrinhMonId != null)
                predicates.add(cb.equal(root.get("chuongTrinhMonId"), chuongTrinhMonId));
            if (monHocId != null)
                predicates.add(cb.equal(root.get("monHocId"), monHocId));
            if (loaiLopHocPhan != null)
                predicates.add(cb.equal(root.get("loaiLopHocPhan"), loaiLopHocPhan));
            if (trangThai != null)
                predicates.add(cb.equal(root.get("trangThai"), trangThai));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
