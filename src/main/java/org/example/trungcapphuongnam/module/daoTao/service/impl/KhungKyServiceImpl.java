package org.example.trungcapphuongnam.module.daoTao.service.impl;

import lombok.*;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.DuplicateResourceException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.util.TextUtil;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinh;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMon;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.view.SyllabusMonHocTongHopView;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersion;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhMonRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhVersionRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocTongHopViewRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.XoaChuongTrinhCascadeService;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyCanhBaoTaiHocResponse;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyGoiYItemResponse;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyTaiHocItemResponse;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyGoiYResponse;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.KhungKy;
import org.example.trungcapphuongnam.module.daoTao.entity.NganhHeDaoTao;
import org.example.trungcapphuongnam.module.daoTao.mapper.KhungKyMapper;
import org.example.trungcapphuongnam.module.daoTao.repository.KhungKyRepository;
import org.example.trungcapphuongnam.module.daoTao.repository.NganhHeDaoTaoRepository;
import org.example.trungcapphuongnam.module.daoTao.service.KhungKyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class KhungKyServiceImpl implements KhungKyService {
    private final ChuongTrinhVersionRepository chuongTrinhVersionRepository;
    private final ChuongTrinhMonRepository chuongTrinhMonRepository;
    private final ChuongTrinhRepository chuongTrinhRepository;
    private final SyllabusMonHocTongHopViewRepository syllabusMonHocTongHopViewRepository;
    private final NganhHeDaoTaoRepository nganhHeDaoTaoRepository;
    private final KhungKyRepository repository;
    private final KhungKyMapper mapper;
    private final XoaChuongTrinhCascadeService xoaChuongTrinhCascadeService;
    @Override
    @Transactional(readOnly = true)
    public Page<KhungKyResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public KhungKyResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Override
    public KhungKyResponse create(KhungKyRequest request) {
        validate(request, null);
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    public KhungKyResponse update(Long id, KhungKyRequest request) {
        KhungKy entity = getEntity(id);
        validate(request, id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Khung kỳ không tồn tại: " + id);
        }

        xoaChuongTrinhCascadeService.xoaTheoKhungKyId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<KhungKyResponse> findAllByChuongTrinhVersionId(
            Long chuongTrinhVersionId,
            Pageable pageable
    ) {
        return repository.findByChuongTrinhVersionId(
                        chuongTrinhVersionId,
                        pageable
                )
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public KhungKyGoiYResponse goiYTheoVersion(Long chuongTrinhVersionId) {
        ChuongTrinhVersion version = getVersion(chuongTrinhVersionId);
        ChuongTrinh chuongTrinh = getChuongTrinh(version.getChuongTrinhId());
        NganhHeDaoTao nganhHe = resolveNganhHeDaoTao(chuongTrinh);
        validateCauHinhThoiGian(version, nganhHe);

        List<KhungKy> kyDaTao = repository.findByChuongTrinhVersionIdOrderByThuTuAsc(chuongTrinhVersionId);
        Map<Integer, KhungKy> kyTheoThuTu = kyDaTao.stream()
                .collect(Collectors.toMap(KhungKy::getThuTu, Function.identity(), (a, b) -> a));

        List<KhungKyGoiYItemResponse> danhSachKy = new ArrayList<>();
        KhungKyGoiYItemResponse kyTiepTheo = null;

        for (int thuTu = 1; thuTu <= nganhHe.getSoKy(); thuTu++) {
            KhoangThoiGianKy khoang = tinhKhoangThoiGianKy(version.getNgayApDung(), nganhHe.getSoThang(), nganhHe.getSoKy(), thuTu);
            KhungKy ky = kyTheoThuTu.get(thuTu);
            KhungKyGoiYItemResponse item = KhungKyGoiYItemResponse.builder()
                    .khungKyId(ky != null ? ky.getId() : null)
                    .thuTu(thuTu)
                    .maKy(ky != null ? ky.getMaKy() : defaultMaKy(thuTu))
                    .tenKy(ky != null ? ky.getTenKy() : defaultTenKy(thuTu))
                    .ngayBatDauGoiY(khoang.ngayBatDau())
                    .ngayKetThucGoiY(khoang.ngayKetThuc())
                    .ngayBatDauHienTai(ky != null ? ky.getNgayBatDau() : null)
                    .ngayKetThucHienTai(ky != null ? ky.getNgayKetThuc() : null)
                    .daTonTai(ky != null)
                    .build();
            danhSachKy.add(item);

            if (kyTiepTheo == null && ky == null) {
                kyTiepTheo = item;
            }
        }

        int soKyDaTaoHopLe = (int) kyDaTao.stream()
                .filter(ky -> ky.getThuTu() != null && ky.getThuTu() >= 1 && ky.getThuTu() <= nganhHe.getSoKy())
                .count();
        int soKyConThieu = Math.max(nganhHe.getSoKy() - soKyDaTaoHopLe, 0);

        return KhungKyGoiYResponse.builder()
                .chuongTrinhVersionId(version.getId())
                .chuongTrinhId(version.getChuongTrinhId())
                .nganhHeDaoTaoId(nganhHe.getId())
                .soThang(nganhHe.getSoThang())
                .soKy(nganhHe.getSoKy())
                .ngayApDung(version.getNgayApDung())
                .ngayHetHieuLucGoiY(tinhNgayKetThucVersion(version.getNgayApDung(), nganhHe.getSoThang()))
                .soKyDaTao(soKyDaTaoHopLe)
                .soKyConThieu(soKyConThieu)
                .daTaoDuKy(soKyConThieu == 0)
                .kyTiepTheoGoiY(kyTiepTheo)
                .danhSachKy(danhSachKy)
                .canhBaoTaiHoc(tinhCanhBaoTaiHoc(version, nganhHe, kyDaTao))
                .message(soKyConThieu == 0
                        ? "Version đã tạo đủ " + nganhHe.getSoKy() + " kỳ."
                        : "Version còn thiếu " + soKyConThieu + " kỳ. Nên tạo theo đúng thứ tự từ kỳ tiếp theo.")
                .build();
    }


    @Override
    @Transactional(readOnly = true)
    public KhungKyCanhBaoTaiHocResponse canhBaoTaiHocTheoVersion(Long chuongTrinhVersionId) {
        ChuongTrinhVersion version = getVersion(chuongTrinhVersionId);
        ChuongTrinh chuongTrinh = getChuongTrinh(version.getChuongTrinhId());
        NganhHeDaoTao nganhHe = resolveNganhHeDaoTao(chuongTrinh);
        validateCauHinhThoiGian(version, nganhHe);
        List<KhungKy> kyDaTao = repository.findByChuongTrinhVersionIdOrderByThuTuAsc(chuongTrinhVersionId);
        return tinhCanhBaoTaiHoc(version, nganhHe, kyDaTao);
    }

    @Override
    public List<KhungKyResponse> taoCacKyConThieuTheoVersion(Long chuongTrinhVersionId) {
        ChuongTrinhVersion version = getVersion(chuongTrinhVersionId);
        ChuongTrinh chuongTrinh = getChuongTrinh(version.getChuongTrinhId());
        NganhHeDaoTao nganhHe = resolveNganhHeDaoTao(chuongTrinh);
        validateCauHinhThoiGian(version, nganhHe);

        List<KhungKy> hienTai = repository.findByChuongTrinhVersionIdOrderByThuTuAsc(chuongTrinhVersionId);
        Map<Integer, KhungKy> kyTheoThuTu = hienTai.stream()
                .collect(Collectors.toMap(KhungKy::getThuTu, Function.identity(), (a, b) -> a));

        List<KhungKyResponse> ketQua = new ArrayList<>();
        for (int thuTu = 1; thuTu <= nganhHe.getSoKy(); thuTu++) {
            KhungKy daCo = kyTheoThuTu.get(thuTu);
            if (daCo != null) {
                ketQua.add(mapper.toResponse(daCo));
                continue;
            }

            KhoangThoiGianKy khoang = tinhKhoangThoiGianKy(version.getNgayApDung(), nganhHe.getSoThang(), nganhHe.getSoKy(), thuTu);
            KhungKyRequest request = KhungKyRequest.builder()
                    .chuongTrinhVersionId(chuongTrinhVersionId)
                    .maKy(defaultMaKy(thuTu))
                    .tenKy(defaultTenKy(thuTu))
                    .thuTu(thuTu)
                    .ngayBatDau(khoang.ngayBatDau())
                    .ngayKetThuc(khoang.ngayKetThuc())
                    .build();
            validate(request, null);
            KhungKy saved = repository.save(mapper.toEntity(request));
            ketQua.add(mapper.toResponse(saved));
        }

        return ketQua.stream()
                .sorted(Comparator.comparing(KhungKyResponse::getThuTu))
                .collect(java.util.stream.Collectors.toList());
    }


    private KhungKyCanhBaoTaiHocResponse tinhCanhBaoTaiHoc(
            ChuongTrinhVersion version,
            NganhHeDaoTao nganhHe,
            List<KhungKy> kyDaTao
    ) {
        List<ChuongTrinhMon> danhSachMon = chuongTrinhMonRepository.findByChuongTrinhVersionId(version.getId());
        Map<Integer, KhungKy> kyTheoThuTu = kyDaTao.stream()
                .filter(ky -> ky.getThuTu() != null)
                .collect(Collectors.toMap(KhungKy::getThuTu, Function.identity(), (a, b) -> a));

        Map<Long, TaiHocAccumulator> taiHocTheoKhungKyId = new java.util.HashMap<>();
        TaiHocAccumulator chuaXepKy = new TaiHocAccumulator();
        TaiHocAccumulator tong = new TaiHocAccumulator();

        for (ChuongTrinhMon mon : danhSachMon) {
            TaiHocMon taiHocMon = layTaiHocMon(mon);
            tong.add(taiHocMon);
            if (mon.getKhungKyId() == null) {
                chuaXepKy.add(taiHocMon);
            } else {
                taiHocTheoKhungKyId
                        .computeIfAbsent(mon.getKhungKyId(), id -> new TaiHocAccumulator())
                        .add(taiHocMon);
            }
        }

        int soKy = nganhHe.getSoKy();
        BigDecimal soMonTrungBinh = chia(BigDecimal.valueOf(tong.soMon), soKy);
        BigDecimal tinChiTrungBinh = chia(tong.tongTinChi, soKy);
        BigDecimal gioTrungBinh = chia(tong.tongGio, soKy);
        BigDecimal buoiTrungBinh = chia(BigDecimal.valueOf(tong.soBuoiHoc), soKy);
        BigDecimal nguongQuaTaiGio = gioTrungBinh.multiply(new BigDecimal("1.20")).setScale(1, RoundingMode.HALF_UP);
        BigDecimal nguongQuaTaiTinChi = tinChiTrungBinh.multiply(new BigDecimal("1.20")).setScale(1, RoundingMode.HALF_UP);
        BigDecimal nguongNheTaiGio = gioTrungBinh.multiply(new BigDecimal("0.70")).setScale(1, RoundingMode.HALF_UP);
        int soMonToiDaKhuyenNghi = soMonTrungBinh.setScale(0, RoundingMode.CEILING).intValue() + 1;
        int soMonToiThieuCanhBao = Math.max(soMonTrungBinh.setScale(0, RoundingMode.FLOOR).intValue() - 1, 0);

        List<String> canhBaoChung = new ArrayList<>();
        if (tong.soMon == 0) {
            canhBaoChung.add("Version chưa có môn học để đánh giá tải học theo kỳ.");
        }
        if (chuaXepKy.soMon > 0) {
            canhBaoChung.add("Có " + chuaXepKy.soMon + " môn chưa được xếp vào kỳ, nên tải học theo kỳ chưa phản ánh đầy đủ.");
        }

        List<KhungKyTaiHocItemResponse> danhSachKy = new ArrayList<>();
        for (int thuTu = 1; thuTu <= soKy; thuTu++) {
            KhungKy ky = kyTheoThuTu.get(thuTu);
            TaiHocAccumulator taiHoc = ky != null
                    ? taiHocTheoKhungKyId.getOrDefault(ky.getId(), new TaiHocAccumulator())
                    : new TaiHocAccumulator();
            danhSachKy.add(buildTaiHocItem(
                    ky != null ? ky.getId() : null,
                    thuTu,
                    ky != null ? ky.getMaKy() : defaultMaKy(thuTu),
                    ky != null ? ky.getTenKy() : defaultTenKy(thuTu),
                    ky != null,
                    false,
                    taiHoc,
                    gioTrungBinh,
                    nguongQuaTaiGio,
                    nguongQuaTaiTinChi,
                    nguongNheTaiGio,
                    soMonToiDaKhuyenNghi,
                    soMonToiThieuCanhBao
            ));
        }

        if (chuaXepKy.soMon > 0) {
            danhSachKy.add(buildTaiHocItem(
                    null,
                    null,
                    "CHUA_XEP_KY",
                    "Môn chưa xếp kỳ",
                    false,
                    true,
                    chuaXepKy,
                    gioTrungBinh,
                    nguongQuaTaiGio,
                    nguongQuaTaiTinChi,
                    nguongNheTaiGio,
                    soMonToiDaKhuyenNghi,
                    soMonToiThieuCanhBao
            ));
        }

        danhSachKy.stream()
                .filter(item -> item.getCanhBao() != null)
                .flatMap(item -> item.getCanhBao().stream())
                .forEach(canhBaoChung::add);

        int soMonDaXepKy = Math.max(tong.soMon - chuaXepKy.soMon, 0);
        return KhungKyCanhBaoTaiHocResponse.builder()
                .chuongTrinhVersionId(version.getId())
                .soKy(soKy)
                .tongSoMon(tong.soMon)
                .soMonDaXepKy(soMonDaXepKy)
                .soMonChuaXepKy(chuaXepKy.soMon)
                .tongTinChi(scale1(tong.tongTinChi))
                .tongGio(scale1(tong.tongGio))
                .gioLyThuyet(scale1(tong.gioLyThuyet))
                .gioThucHanh(scale1(tong.gioThucHanh))
                .gioKiemTra(scale1(tong.gioKiemTra))
                .tongSoBuoiHoc(tong.soBuoiHoc)
                .soMonTrungBinhMoiKy(scale1(soMonTrungBinh))
                .tinChiTrungBinhMoiKy(scale1(tinChiTrungBinh))
                .gioTrungBinhMoiKy(scale1(gioTrungBinh))
                .buoiHocTrungBinhMoiKy(scale1(buoiTrungBinh))
                .nguongQuaTaiTheoGio(scale1(nguongQuaTaiGio))
                .nguongQuaTaiTheoTinChi(scale1(nguongQuaTaiTinChi))
                .nguongNheTaiTheoGio(scale1(nguongNheTaiGio))
                .canhBaoChung(canhBaoChung)
                .danhSachKy(danhSachKy)
                .build();
    }

    private KhungKyTaiHocItemResponse buildTaiHocItem(
            Long khungKyId,
            Integer thuTu,
            String maKy,
            String tenKy,
            Boolean daCoKhungKy,
            Boolean chuaXepKy,
            TaiHocAccumulator taiHoc,
            BigDecimal gioTrungBinh,
            BigDecimal nguongQuaTaiGio,
            BigDecimal nguongQuaTaiTinChi,
            BigDecimal nguongNheTaiGio,
            int soMonToiDaKhuyenNghi,
            int soMonToiThieuCanhBao
    ) {
        List<String> canhBao = new ArrayList<>();
        String mucDoTai = "BINH_THUONG";

        if (Boolean.TRUE.equals(chuaXepKy)) {
            mucDoTai = "CHUA_XEP_KY";
            canhBao.add(tenKy + " có " + taiHoc.soMon + " môn chưa được xếp vào kỳ.");
        } else if (Boolean.FALSE.equals(daCoKhungKy)) {
            mucDoTai = "CHUA_TAO_KY";
            canhBao.add(tenKy + " chưa được tạo khung kỳ nên chưa thể phân bổ tải học.");
        } else {
            if (taiHoc.soMon > soMonToiDaKhuyenNghi) {
                mucDoTai = "QUA_TAI";
                canhBao.add(tenKy + " có " + taiHoc.soMon + " môn, vượt mức khuyến nghị khoảng " + soMonToiDaKhuyenNghi + " môn/kỳ.");
            }
            if (taiHoc.tongGio.compareTo(nguongQuaTaiGio) > 0) {
                mucDoTai = "QUA_TAI";
                canhBao.add(tenKy + " có " + scale1(taiHoc.tongGio) + " giờ, cao hơn ngưỡng quá tải " + scale1(nguongQuaTaiGio) + " giờ/kỳ.");
            }
            if (taiHoc.tongTinChi.compareTo(nguongQuaTaiTinChi) > 0) {
                mucDoTai = "QUA_TAI";
                canhBao.add(tenKy + " có " + scale1(taiHoc.tongTinChi) + " tín chỉ, cao hơn ngưỡng quá tải " + scale1(nguongQuaTaiTinChi) + " tín chỉ/kỳ.");
            }
            if (taiHoc.soMon > 0 && taiHoc.tongGio.compareTo(nguongNheTaiGio) < 0 && gioTrungBinh.compareTo(BigDecimal.ZERO) > 0) {
                if (!"QUA_TAI".equals(mucDoTai)) {
                    mucDoTai = "NHE_TAI";
                }
                canhBao.add(tenKy + " chỉ có " + scale1(taiHoc.tongGio) + " giờ, thấp hơn nhiều so với mức trung bình.");
            }
            if (taiHoc.soMon <= soMonToiThieuCanhBao && soMonToiThieuCanhBao > 0) {
                if (!"QUA_TAI".equals(mucDoTai)) {
                    mucDoTai = "NHE_TAI";
                }
                canhBao.add(tenKy + " chỉ có " + taiHoc.soMon + " môn, thấp hơn nhiều so với phân bổ trung bình.");
            }
        }

        BigDecimal tyLeTai = BigDecimal.ZERO;
        if (gioTrungBinh.compareTo(BigDecimal.ZERO) > 0) {
            tyLeTai = taiHoc.tongGio.multiply(BigDecimal.valueOf(100)).divide(gioTrungBinh, 1, RoundingMode.HALF_UP);
        }

        return KhungKyTaiHocItemResponse.builder()
                .khungKyId(khungKyId)
                .thuTu(thuTu)
                .maKy(maKy)
                .tenKy(tenKy)
                .daCoKhungKy(daCoKhungKy)
                .chuaXepKy(chuaXepKy)
                .soMon(taiHoc.soMon)
                .tongTinChi(scale1(taiHoc.tongTinChi))
                .tongGio(scale1(taiHoc.tongGio))
                .gioLyThuyet(scale1(taiHoc.gioLyThuyet))
                .gioThucHanh(scale1(taiHoc.gioThucHanh))
                .gioKiemTra(scale1(taiHoc.gioKiemTra))
                .soBuoiHoc(taiHoc.soBuoiHoc)
                .tyLeTaiTheoGio(scale1(tyLeTai))
                .mucDoTai(mucDoTai)
                .canhBao(canhBao)
                .build();
    }

    private TaiHocMon layTaiHocMon(ChuongTrinhMon mon) {
        return syllabusMonHocTongHopViewRepository.findByChuongTrinhMonId(mon.getId())
                .map(view -> TaiHocMon.builder()
                        .soTinChi(firstNonNull(view.getSoTinChi(), mon.getSoTinChi()))
                        .tongGio(firstNonNull(view.getTongGio(), mon.getTongGio()))
                        .gioLyThuyet(firstNonNull(view.getGioLyThuyet(), mon.getGioLyThuyet()))
                        .gioThucHanh(firstNonNull(view.getGioThucHanh(), mon.getGioThucHanh()))
                        .gioKiemTra(firstNonNull(view.getGioKiemTra(), mon.getGioKiemTra()))
                        .soBuoiHoc(view.getSoBuoiHoc() == null ? 0 : view.getSoBuoiHoc())
                        .build())
                .orElseGet(() -> TaiHocMon.builder()
                        .soTinChi(nullToZero(mon.getSoTinChi()))
                        .tongGio(nullToZero(mon.getTongGio()))
                        .gioLyThuyet(nullToZero(mon.getGioLyThuyet()))
                        .gioThucHanh(nullToZero(mon.getGioThucHanh()))
                        .gioKiemTra(nullToZero(mon.getGioKiemTra()))
                        .soBuoiHoc(0)
                        .build());
    }

    private BigDecimal firstNonNull(BigDecimal primary, BigDecimal fallback) {
        return primary != null ? primary : nullToZero(fallback);
    }

    private BigDecimal nullToZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private BigDecimal chia(BigDecimal value, int divisor) {
        if (divisor <= 0) {
            return BigDecimal.ZERO;
        }
        return nullToZero(value).divide(BigDecimal.valueOf(divisor), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal scale1(BigDecimal value) {
        return nullToZero(value).setScale(1, RoundingMode.HALF_UP);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class TaiHocMon {
        private BigDecimal soTinChi;
        private BigDecimal tongGio;
        private BigDecimal gioLyThuyet;
        private BigDecimal gioThucHanh;
        private BigDecimal gioKiemTra;
        private Integer soBuoiHoc;
    }

    private static class TaiHocAccumulator {
        private int soMon = 0;
        private BigDecimal tongTinChi = BigDecimal.ZERO;
        private BigDecimal tongGio = BigDecimal.ZERO;
        private BigDecimal gioLyThuyet = BigDecimal.ZERO;
        private BigDecimal gioThucHanh = BigDecimal.ZERO;
        private BigDecimal gioKiemTra = BigDecimal.ZERO;
        private int soBuoiHoc = 0;

        private void add(TaiHocMon mon) {
            soMon++;
            tongTinChi = tongTinChi.add(mon.getSoTinChi() == null ? BigDecimal.ZERO : mon.getSoTinChi());
            tongGio = tongGio.add(mon.getTongGio() == null ? BigDecimal.ZERO : mon.getTongGio());
            gioLyThuyet = gioLyThuyet.add(mon.getGioLyThuyet() == null ? BigDecimal.ZERO : mon.getGioLyThuyet());
            gioThucHanh = gioThucHanh.add(mon.getGioThucHanh() == null ? BigDecimal.ZERO : mon.getGioThucHanh());
            gioKiemTra = gioKiemTra.add(mon.getGioKiemTra() == null ? BigDecimal.ZERO : mon.getGioKiemTra());
            soBuoiHoc += mon.getSoBuoiHoc() == null ? 0 : mon.getSoBuoiHoc();
        }
    }

    private void validate(KhungKyRequest request, Long id) {
        if (request == null) {
            throw new BadRequestException("Dữ liệu khung kỳ không hợp lệ");
        }

        if (request.getChuongTrinhVersionId() == null) {
            throw new BadRequestException("chuongTrinhVersionId không được để trống");
        }

        ChuongTrinhVersion version = getVersion(request.getChuongTrinhVersionId());
        ChuongTrinh chuongTrinh = getChuongTrinh(version.getChuongTrinhId());
        NganhHeDaoTao nganhHe = resolveNganhHeDaoTao(chuongTrinh);
        validateCauHinhThoiGian(version, nganhHe);

        if (id == null && repository.countByChuongTrinhVersionId(request.getChuongTrinhVersionId()) >= nganhHe.getSoKy()) {
            throw new BadRequestException("Version đã tạo đủ " + nganhHe.getSoKy() + " kỳ theo cấu hình ngành hệ đào tạo");
        }

        applyDefaultThuTuVaTenKy(request, id, nganhHe.getSoKy());
        applyDefaultNgayKy(request, version, nganhHe);

        if (request.getThuTu() <= 0) {
            throw new BadRequestException("thuTu phải lớn hơn 0");
        }

        if (request.getThuTu() > nganhHe.getSoKy()) {
            throw new BadRequestException("Thứ tự kỳ không được lớn hơn số kỳ của ngành hệ đào tạo (" + nganhHe.getSoKy() + ")");
        }

        validateNgayKyTrongVersion(request, version, nganhHe);
        validateTaoKyTheoThuTu(request, id);
        validateKhongChongNgay(request, id);
        validateUnique(request, id);
    }

    private void applyDefaultThuTuVaTenKy(KhungKyRequest request, Long id, Integer soKy) {
        if (request.getThuTu() == null && id == null) {
            request.setThuTu((int) repository.countByChuongTrinhVersionId(request.getChuongTrinhVersionId()) + 1);
        }

        if (request.getThuTu() == null) {
            throw new BadRequestException("thuTu không được để trống");
        }

        if (request.getThuTu() > soKy) {
            throw new BadRequestException("Không được tạo kỳ thứ " + request.getThuTu() + " vì ngành hệ chỉ có " + soKy + " kỳ");
        }

        if (request.getMaKy() == null || request.getMaKy().trim().isEmpty()) {
            request.setMaKy(defaultMaKy(request.getThuTu()));
        }

        if (request.getTenKy() == null || request.getTenKy().trim().isEmpty()) {
            request.setTenKy(defaultTenKy(request.getThuTu()));
        }

        request.setMaKy(TextUtil.trimRequired(request.getMaKy()));
        request.setTenKy(TextUtil.trimRequired(request.getTenKy()));
    }

    private void applyDefaultNgayKy(KhungKyRequest request, ChuongTrinhVersion version, NganhHeDaoTao nganhHe) {
        if (request.getNgayBatDau() != null && request.getNgayKetThuc() != null) {
            return;
        }

        KhoangThoiGianKy goiY = tinhKhoangThoiGianKy(
                version.getNgayApDung(),
                nganhHe.getSoThang(),
                nganhHe.getSoKy(),
                request.getThuTu()
        );

        if (request.getNgayBatDau() == null) {
            request.setNgayBatDau(goiY.ngayBatDau());
        }

        if (request.getNgayKetThuc() == null) {
            request.setNgayKetThuc(goiY.ngayKetThuc());
        }
    }

    private void validateNgayKyTrongVersion(KhungKyRequest request, ChuongTrinhVersion version, NganhHeDaoTao nganhHe) {
        if (request.getNgayBatDau() == null || request.getNgayKetThuc() == null) {
            throw new BadRequestException("Ngày bắt đầu và ngày kết thúc kỳ không được để trống");
        }

        if (request.getNgayKetThuc().isBefore(request.getNgayBatDau())) {
            throw new BadRequestException("Ngày kết thúc kỳ không được trước ngày bắt đầu kỳ");
        }

        LocalDate ngayHetHieuLuc = version.getNgayHetHieuLuc() != null
                ? version.getNgayHetHieuLuc()
                : tinhNgayKetThucVersion(version.getNgayApDung(), nganhHe.getSoThang());

        if (request.getNgayBatDau().isBefore(version.getNgayApDung())) {
            throw new BadRequestException("Ngày bắt đầu kỳ không được trước ngày áp dụng của version chương trình ("
                    + version.getNgayApDung() + ")");
        }

        if (request.getNgayKetThuc().isAfter(ngayHetHieuLuc)) {
            throw new BadRequestException("Ngày kết thúc kỳ không được sau ngày hết hiệu lực của version chương trình ("
                    + ngayHetHieuLuc + ")");
        }
    }

    private void validateTaoKyTheoThuTu(KhungKyRequest request, Long id) {
        if (id != null) {
            return;
        }

        int expectedThuTu = (int) repository.countByChuongTrinhVersionId(request.getChuongTrinhVersionId()) + 1;
        if (!request.getThuTu().equals(expectedThuTu)) {
            throw new BadRequestException("Phải tạo khung kỳ theo đúng thứ tự. Kỳ tiếp theo cần tạo là kỳ " + expectedThuTu);
        }
    }

    private void validateKhongChongNgay(KhungKyRequest request, Long id) {
        List<KhungKy> dsKy = repository.findByChuongTrinhVersionIdOrderByThuTuAsc(request.getChuongTrinhVersionId());
        for (KhungKy ky : dsKy) {
            if (id != null && ky.getId().equals(id)) {
                continue;
            }

            if (ky.getNgayBatDau() == null || ky.getNgayKetThuc() == null) {
                continue;
            }

            boolean chongNgay = !request.getNgayKetThuc().isBefore(ky.getNgayBatDau())
                    && !request.getNgayBatDau().isAfter(ky.getNgayKetThuc());

            if (chongNgay) {
                throw new BadRequestException("Thời gian kỳ đang nhập bị chồng với " + ky.getTenKy()
                        + " (" + ky.getNgayBatDau() + " -> " + ky.getNgayKetThuc() + ")");
            }

            if (request.getThuTu() > ky.getThuTu()
                    && !request.getNgayBatDau().isAfter(ky.getNgayKetThuc())) {
                throw new BadRequestException("Ngày bắt đầu của kỳ " + request.getThuTu()
                        + " phải sau ngày kết thúc của kỳ " + ky.getThuTu());
            }

            if (request.getThuTu() < ky.getThuTu()
                    && !request.getNgayKetThuc().isBefore(ky.getNgayBatDau())) {
                throw new BadRequestException("Ngày kết thúc của kỳ " + request.getThuTu()
                        + " phải trước ngày bắt đầu của kỳ " + ky.getThuTu());
            }
        }
    }

    private void validateUnique(KhungKyRequest request, Long id) {
        if (id == null) {
            if (repository.existsByChuongTrinhVersionIdAndMaKy(
                    request.getChuongTrinhVersionId(),
                    request.getMaKy()
            )) {
                throw new DuplicateResourceException("Mã kỳ đã tồn tại trong version chương trình");
            }

            if (repository.existsByChuongTrinhVersionIdAndThuTu(
                    request.getChuongTrinhVersionId(),
                    request.getThuTu()
            )) {
                throw new DuplicateResourceException("Thứ tự kỳ đã tồn tại trong version chương trình");
            }

        } else {
            if (repository.existsByChuongTrinhVersionIdAndMaKyAndIdNot(
                    request.getChuongTrinhVersionId(),
                    request.getMaKy(),
                    id
            )) {
                throw new DuplicateResourceException("Mã kỳ đã tồn tại trong version chương trình");
            }

            if (repository.existsByChuongTrinhVersionIdAndThuTuAndIdNot(
                    request.getChuongTrinhVersionId(),
                    request.getThuTu(),
                    id
            )) {
                throw new DuplicateResourceException("Thứ tự kỳ đã tồn tại trong version chương trình");
            }

        }
    }

    private ChuongTrinhVersion getVersion(Long id) {
        return chuongTrinhVersionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Version chương trình không tồn tại: " + id));
    }

    private ChuongTrinh getChuongTrinh(Long id) {
        return chuongTrinhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chương trình đào tạo không tồn tại: " + id));
    }

    private KhungKy getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Khung kỳ không tồn tại: " + id));
    }

    private NganhHeDaoTao resolveNganhHeDaoTao(ChuongTrinh chuongTrinh) {
        if (chuongTrinh.getNganhHeDaoTaoId() == null) {
            throw new BadRequestException("Chương trình chưa gắn ngành hệ đào tạo nên không thể gợi ý khung kỳ");
        }

        return nganhHeDaoTaoRepository.findById(chuongTrinh.getNganhHeDaoTaoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ngành hệ đào tạo không tồn tại: " + chuongTrinh.getNganhHeDaoTaoId()
                ));
    }

    private void validateCauHinhThoiGian(ChuongTrinhVersion version, NganhHeDaoTao nganhHe) {
        if (version.getNgayApDung() == null) {
            throw new BadRequestException("Version chương trình chưa có ngày áp dụng");
        }

        if (nganhHe.getSoThang() == null || nganhHe.getSoThang() <= 0) {
            throw new BadRequestException("Ngành hệ đào tạo chưa cấu hình số tháng đào tạo hợp lệ");
        }

        if (nganhHe.getSoKy() == null || nganhHe.getSoKy() <= 0) {
            throw new BadRequestException("Ngành hệ đào tạo chưa cấu hình số kỳ đào tạo hợp lệ");
        }
    }

    private KhoangThoiGianKy tinhKhoangThoiGianKy(LocalDate ngayApDung, int soThang, int soKy, int thuTu) {
        int baseMonths = soThang / soKy;
        int remainder = soThang % soKy;

        LocalDate start = ngayApDung;
        for (int i = 1; i < thuTu; i++) {
            start = start.plusMonths(baseMonths + (i <= remainder ? 1 : 0));
        }

        int monthsOfTerm = baseMonths + (thuTu <= remainder ? 1 : 0);
        LocalDate end = start.plusMonths(monthsOfTerm).minusDays(1);
        return new KhoangThoiGianKy(start, end);
    }

    private LocalDate tinhNgayKetThucVersion(LocalDate ngayApDung, int soThang) {
        return ngayApDung.plusMonths(soThang).minusDays(1);
    }

    private String defaultMaKy(Integer thuTu) {
        return "HK" + thuTu;
    }

    private String defaultTenKy(Integer thuTu) {
        return "Học kỳ " + thuTu;
    }

    private record KhoangThoiGianKy(LocalDate ngayBatDau, LocalDate ngayKetThuc) {
    }
}
