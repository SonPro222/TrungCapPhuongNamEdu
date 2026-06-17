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
import org.example.trungcapphuongnam.module.daoTao.dto.ChuongTrinhVersionNghiChuyenKyBulkRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.ChuongTrinhVersionNghiChuyenKyRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.ChuongTrinhVersionNghiChuyenKyResponse;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyCanhBaoTaiHocResponse;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyGoiYItemResponse;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyTaiHocItemResponse;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyGoiYResponse;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyResponse;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKySinhTuDongRequest;
import org.example.trungcapphuongnam.module.daoTao.entity.ChuongTrinhVersionNghiChuyenKy;
import org.example.trungcapphuongnam.module.daoTao.entity.KhungKy;
import org.example.trungcapphuongnam.module.daoTao.entity.NganhHeDaoTao;
import org.example.trungcapphuongnam.module.daoTao.mapper.KhungKyMapper;
import org.example.trungcapphuongnam.module.daoTao.repository.ChuongTrinhVersionNghiChuyenKyRepository;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private final ChuongTrinhVersionNghiChuyenKyRepository nghiChuyenKyRepository;
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
            throw new ResourceNotFoundException("Khung ky khong ton tai: " + id);
        }
        xoaChuongTrinhCascadeService.xoaTheoKhungKyId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<KhungKyResponse> findAllByChuongTrinhVersionId(
            Long chuongTrinhVersionId,
            Pageable pageable
    ) {
        return repository.findByChuongTrinhVersionId(chuongTrinhVersionId, pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public KhungKyGoiYResponse goiYTheoVersion(Long chuongTrinhVersionId) {
        return buildGoiYTheoVersion(chuongTrinhVersionId, null);
    }

    @Override
    @Transactional(readOnly = true)
    public KhungKyGoiYResponse goiYTheoVersion(Long chuongTrinhVersionId, KhungKySinhTuDongRequest request) {
        return buildGoiYTheoVersion(chuongTrinhVersionId, request);
    }

    private KhungKyGoiYResponse buildGoiYTheoVersion(Long chuongTrinhVersionId, KhungKySinhTuDongRequest request) {
        ChuongTrinhVersion version = getVersion(chuongTrinhVersionId);
        ChuongTrinh chuongTrinh = getChuongTrinh(version.getChuongTrinhId());
        NganhHeDaoTao nganhHe = resolveNganhHeDaoTao(chuongTrinh);
        validateCauHinhThoiGian(version, nganhHe);

        List<Integer> soNgayNghiChuyenKy = resolveSoNgayNghiChuyenKy(
                chuongTrinhVersionId, nganhHe, request, false);
        List<KhoangThoiGianKy> danhSachKhoang = tinhDanhSachKhoangThoiGianKy(
                version.getNgayApDung(), nganhHe.getSoThang(), nganhHe.getSoKy(), soNgayNghiChuyenKy);

        List<KhungKy> kyDaTao = repository.findByChuongTrinhVersionIdOrderByThuTuAsc(chuongTrinhVersionId);
        Map<Integer, KhungKy> kyTheoThuTu = kyDaTao.stream()
                .collect(Collectors.toMap(KhungKy::getThuTu, Function.identity(), (a, b) -> a));

        List<KhungKyGoiYItemResponse> danhSachKy = new ArrayList<>();
        KhungKyGoiYItemResponse kyTiepTheo = null;

        for (int thuTu = 1; thuTu <= nganhHe.getSoKy(); thuTu++) {
            KhoangThoiGianKy khoang = danhSachKhoang.get(thuTu - 1);
            KhungKy ky = kyTheoThuTu.get(thuTu);
            KhungKyGoiYItemResponse item = KhungKyGoiYItemResponse.builder()
                    .khungKyId(ky != null ? ky.getId() : null)
                    .thuTu(thuTu)
                    .maKy(ky != null ? ky.getMaKy() : defaultMaKy(thuTu))
                    .tenKy(ky != null ? ky.getTenKy() : defaultTenKy(thuTu))
                    .ngayBatDauGoiY(khoang.ngayBatDau())
                    .ngayKetThucGoiY(khoang.ngayKetThuc())
                    .soNgayNghiChuyenKySau(khoang.soNgayNghiChuyenKySau())
                    .ngayBatDauNghiChuyenKySauGoiY(khoang.ngayBatDauNghiChuyenKySau())
                    .ngayKetThucNghiChuyenKySauGoiY(khoang.ngayKetThucNghiChuyenKySau())
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
                .tongSoNgayNghiChuyenKy(tongSoNgay(soNgayNghiChuyenKy))
                .ngayApDung(version.getNgayApDung())
                .ngayHetHieuLucGoiY(tinhNgayKetThucVersion(version.getNgayApDung(), nganhHe.getSoThang()))
                .soKyDaTao(soKyDaTaoHopLe)
                .soKyConThieu(soKyConThieu)
                .daTaoDuKy(soKyConThieu == 0)
                .kyTiepTheoGoiY(kyTiepTheo)
                .danhSachKy(danhSachKy)
                .danhSachNghiChuyenKy(buildNghiChuyenKyResponses(version, nganhHe, soNgayNghiChuyenKy, danhSachKhoang))
                .canhBaoTaiHoc(tinhCanhBaoTaiHoc(version, nganhHe, kyDaTao))
                .message(soKyConThieu == 0
                        ? "Version da tao du " + nganhHe.getSoKy() + " ky."
                        : "Version con thieu " + soKyConThieu + " ky. Nen nhap nghi chuyen ky truoc roi tao ky theo goi y.")
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
        return taoCacKyConThieuTheoVersion(chuongTrinhVersionId, null);
    }

    @Override
    public List<KhungKyResponse> taoCacKyConThieuTheoVersion(
            Long chuongTrinhVersionId,
            KhungKySinhTuDongRequest request
    ) {
        ChuongTrinhVersion version = getVersion(chuongTrinhVersionId);
        ChuongTrinh chuongTrinh = getChuongTrinh(version.getChuongTrinhId());
        NganhHeDaoTao nganhHe = resolveNganhHeDaoTao(chuongTrinh);
        validateCauHinhThoiGian(version, nganhHe);

        List<Integer> soNgayNghiChuyenKy = resolveSoNgayNghiChuyenKy(
                chuongTrinhVersionId, nganhHe, request, false);
        List<KhoangThoiGianKy> danhSachKhoang = tinhDanhSachKhoangThoiGianKy(
                version.getNgayApDung(), nganhHe.getSoThang(), nganhHe.getSoKy(), soNgayNghiChuyenKy);

        boolean ghiDeKyDaCo = request != null && Boolean.TRUE.equals(request.getGhiDeKyDaCo());

        List<KhungKy> hienTai = repository.findByChuongTrinhVersionIdOrderByThuTuAsc(chuongTrinhVersionId);
        Map<Integer, KhungKy> kyTheoThuTu = hienTai.stream()
                .filter(row -> row.getThuTu() != null)
                .collect(Collectors.toMap(KhungKy::getThuTu, Function.identity(), (a, b) -> a));
        Map<String, KhungKy> kyTheoMa = hienTai.stream()
                .filter(row -> row.getMaKy() != null)
                .collect(Collectors.toMap(row -> row.getMaKy().trim().toUpperCase(), Function.identity(), (a, b) -> a));

        List<KhungKyResponse> ketQua = new ArrayList<>();
        for (int thuTu = 1; thuTu <= nganhHe.getSoKy(); thuTu++) {
            KhoangThoiGianKy khoang = danhSachKhoang.get(thuTu - 1);
            String maKy = defaultMaKy(thuTu);

            KhungKy daCo = kyTheoThuTu.get(thuTu);
            if (daCo == null) {
                daCo = kyTheoMa.get(maKy.trim().toUpperCase());
            }

            if (daCo != null && !ghiDeKyDaCo) {
                ketQua.add(mapper.toResponse(daCo));
                continue;
            }

            if (daCo != null) {
                // ghiDeKyDaCo = true: cap nhat truc tiep, khong qua validate() de tranh loi chong ngay
                daCo.setMaKy(maKy);
                daCo.setTenKy(defaultTenKy(thuTu));
                daCo.setThuTu(thuTu);
                daCo.setNgayBatDau(khoang.ngayBatDau());
                daCo.setNgayKetThuc(khoang.ngayKetThuc());
                ketQua.add(mapper.toResponse(repository.save(daCo)));
            } else {
                KhungKy newKy = KhungKy.builder()
                        .chuongTrinhVersionId(chuongTrinhVersionId)
                        .maKy(maKy)
                        .tenKy(defaultTenKy(thuTu))
                        .thuTu(thuTu)
                        .ngayBatDau(khoang.ngayBatDau())
                        .ngayKetThuc(khoang.ngayKetThuc())
                        .build();
                ketQua.add(mapper.toResponse(repository.save(newKy)));
            }
        }

        return ketQua.stream()
                .sorted(Comparator.comparing(KhungKyResponse::getThuTu))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChuongTrinhVersionNghiChuyenKyResponse> findNghiChuyenKyTheoVersion(Long chuongTrinhVersionId) {
        ChuongTrinhVersion version = getVersion(chuongTrinhVersionId);
        ChuongTrinh chuongTrinh = getChuongTrinh(version.getChuongTrinhId());
        NganhHeDaoTao nganhHe = resolveNganhHeDaoTao(chuongTrinh);
        validateCauHinhThoiGian(version, nganhHe);

        List<Integer> soNgayNghiChuyenKy = resolveSoNgayNghiChuyenKy(chuongTrinhVersionId, nganhHe, null, false);
        List<KhoangThoiGianKy> danhSachKhoang = tinhDanhSachKhoangThoiGianKy(
                version.getNgayApDung(), nganhHe.getSoThang(), nganhHe.getSoKy(), soNgayNghiChuyenKy);
        return buildNghiChuyenKyResponses(version, nganhHe, soNgayNghiChuyenKy, danhSachKhoang);
    }

    @Override
    public List<ChuongTrinhVersionNghiChuyenKyResponse> saveNghiChuyenKyTheoVersion(
            Long chuongTrinhVersionId,
            ChuongTrinhVersionNghiChuyenKyBulkRequest request
    ) {
        ChuongTrinhVersion version = getVersion(chuongTrinhVersionId);
        ChuongTrinh chuongTrinh = getChuongTrinh(version.getChuongTrinhId());
        NganhHeDaoTao nganhHe = resolveNganhHeDaoTao(chuongTrinh);
        validateCauHinhThoiGian(version, nganhHe);

        KhungKySinhTuDongRequest sinhRequest = KhungKySinhTuDongRequest.builder()
                .danhSachNghiChuyenKy(request != null ? request.getDanhSachNghiChuyenKy() : null)
                .build();
        // luu=true: save to DB using update/create/delete (no delete-all+saveAll to avoid constraint violation)
        List<Integer> soNgayNghiChuyenKy = resolveSoNgayNghiChuyenKy(
                chuongTrinhVersionId, nganhHe, sinhRequest, true);
        List<KhoangThoiGianKy> danhSachKhoang = tinhDanhSachKhoangThoiGianKy(
                version.getNgayApDung(), nganhHe.getSoThang(), nganhHe.getSoKy(), soNgayNghiChuyenKy);
        return buildNghiChuyenKyResponses(version, nganhHe, soNgayNghiChuyenKy, danhSachKhoang);
    }

    // ============================================================
    // NGHỈ CHUYỂN KỲ HELPERS
    // ============================================================

    /**
     * Resolve danh sach so ngay nghi chuyen ky (size = soKy).
     * Index i = so ngay nghi sau ky (i+1). Phan tu cuoi = 0.
     * Neu luu=true va request co gia tri moi: thuc hien update/create/delete (KHONG delete-all+saveAll).
     */
    private List<Integer> resolveSoNgayNghiChuyenKy(
            Long chuongTrinhVersionId,
            NganhHeDaoTao nganhHe,
            KhungKySinhTuDongRequest request,
            boolean luu
    ) {
        int soKy = nganhHe.getSoKy();

        List<ChuongTrinhVersionNghiChuyenKy> hienTai =
                nghiChuyenKyRepository.findByChuongTrinhVersionIdOrderByTuKyThuAsc(chuongTrinhVersionId);
        Map<String, ChuongTrinhVersionNghiChuyenKy> hienTaiTheoKhoa = hienTai.stream()
                .collect(Collectors.toMap(
                        row -> row.getTuKyThu() + "_" + row.getDenKyThu(),
                        Function.identity(),
                        (a, b) -> a
                ));

        // Build request items from various input formats
        List<ChuongTrinhVersionNghiChuyenKyRequest> items = null;
        if (request != null && request.getDanhSachNghiChuyenKy() != null && !request.getDanhSachNghiChuyenKy().isEmpty()) {
            items = request.getDanhSachNghiChuyenKy();
        } else if (request != null && request.getSoNgayNghiChuyenKySau() != null && !request.getSoNgayNghiChuyenKySau().isEmpty()) {
            List<Integer> soNgay = request.getSoNgayNghiChuyenKySau();
            items = new ArrayList<>();
            for (int i = 0; i < soKy - 1; i++) {
                int soNgayNghi = i < soNgay.size() ? (soNgay.get(i) != null ? soNgay.get(i) : 0) : 0;
                items.add(ChuongTrinhVersionNghiChuyenKyRequest.builder()
                        .tuKyThu(i + 1).denKyThu(i + 2)
                        .soNgayNghi(soNgayNghi).ghiChu("").build());
            }
        }

        if (luu && items != null) {
            // Update/create/delete - KHONG dung delete-all+saveAll (tranh unique constraint violation)
            Set<String> khoaCanGiu = new HashSet<>();
            for (ChuongTrinhVersionNghiChuyenKyRequest item : items) {
                if (item.getTuKyThu() == null || item.getDenKyThu() == null) continue;
                String khoa = item.getTuKyThu() + "_" + item.getDenKyThu();
                khoaCanGiu.add(khoa);
                int soNgayNghi = item.getSoNgayNghi() != null ? item.getSoNgayNghi() : 0;
                String ghiChu = item.getGhiChu() != null ? item.getGhiChu() : "";
                ChuongTrinhVersionNghiChuyenKy existing = hienTaiTheoKhoa.get(khoa);
                if (existing != null) {
                    // Update existing row
                    existing.setSoNgayNghi(soNgayNghi);
                    existing.setGhiChu(ghiChu);
                    nghiChuyenKyRepository.save(existing);
                } else {
                    // Create new row
                    nghiChuyenKyRepository.save(ChuongTrinhVersionNghiChuyenKy.builder()
                            .chuongTrinhVersionId(chuongTrinhVersionId)
                            .tuKyThu(item.getTuKyThu())
                            .denKyThu(item.getDenKyThu())
                            .soNgayNghi(soNgayNghi)
                            .ghiChu(ghiChu)
                            .build());
                }
            }
            // Delete orphan rows
            for (ChuongTrinhVersionNghiChuyenKy row : hienTai) {
                String khoa = row.getTuKyThu() + "_" + row.getDenKyThu();
                if (!khoaCanGiu.contains(khoa)) {
                    nghiChuyenKyRepository.delete(row);
                }
            }
            // Reload after save
            hienTai = nghiChuyenKyRepository.findByChuongTrinhVersionIdOrderByTuKyThuAsc(chuongTrinhVersionId);
        }

        // Build result: soNgayNghi for each consecutive pair (i, i+1)
        Map<String, Integer> soNgayTheoKhoa;
        if (!luu && items != null) {
            // Use request values without touching DB
            soNgayTheoKhoa = new HashMap<>();
            for (ChuongTrinhVersionNghiChuyenKyRequest item : items) {
                if (item.getTuKyThu() != null && item.getDenKyThu() != null) {
                    soNgayTheoKhoa.put(item.getTuKyThu() + "_" + item.getDenKyThu(),
                            item.getSoNgayNghi() != null ? item.getSoNgayNghi() : 0);
                }
            }
        } else {
            soNgayTheoKhoa = hienTai.stream()
                    .collect(Collectors.toMap(
                            row -> row.getTuKyThu() + "_" + row.getDenKyThu(),
                            row -> row.getSoNgayNghi() != null ? row.getSoNgayNghi() : 0,
                            (a, b) -> a
                    ));
        }

        List<Integer> result = new ArrayList<>();
        for (int i = 1; i <= soKy; i++) {
            if (i < soKy) {
                result.add(soNgayTheoKhoa.getOrDefault(i + "_" + (i + 1), 0));
            } else {
                result.add(0);
            }
        }
        return result;
    }

    private List<ChuongTrinhVersionNghiChuyenKyResponse> buildNghiChuyenKyResponses(
            ChuongTrinhVersion version,
            NganhHeDaoTao nganhHe,
            List<Integer> soNgayNghiChuyenKy,
            List<KhoangThoiGianKy> danhSachKhoang
    ) {
        List<ChuongTrinhVersionNghiChuyenKy> hienTai =
                nghiChuyenKyRepository.findByChuongTrinhVersionIdOrderByTuKyThuAsc(version.getId());
        Map<String, ChuongTrinhVersionNghiChuyenKy> hienTaiTheoKhoa = hienTai.stream()
                .collect(Collectors.toMap(
                        row -> row.getTuKyThu() + "_" + row.getDenKyThu(),
                        Function.identity(),
                        (a, b) -> a
                ));

        List<ChuongTrinhVersionNghiChuyenKyResponse> result = new ArrayList<>();
        int soKy = nganhHe.getSoKy();
        for (int i = 1; i < soKy; i++) {
            int tuKyThu = i;
            int denKyThu = i + 1;
            KhoangThoiGianKy khoang = danhSachKhoang.get(i - 1);
            ChuongTrinhVersionNghiChuyenKy existing = hienTaiTheoKhoa.get(tuKyThu + "_" + denKyThu);

            result.add(ChuongTrinhVersionNghiChuyenKyResponse.builder()
                    .id(existing != null ? existing.getId() : null)
                    .chuongTrinhVersionId(version.getId())
                    .tuKyThu(tuKyThu)
                    .denKyThu(denKyThu)
                    .soNgayNghi(existing != null ? existing.getSoNgayNghi() : 0)
                    .ngayBatDauNghiGoiY(khoang.ngayBatDauNghiChuyenKySau())
                    .ngayKetThucNghiGoiY(khoang.ngayKetThucNghiChuyenKySau())
                    .ghiChu(existing != null ? existing.getGhiChu() : "")
                    .createdAt(existing != null ? existing.getCreatedAt() : null)
                    .updatedAt(existing != null ? existing.getUpdatedAt() : null)
                    .build());
        }
        return result;
    }

    private List<KhoangThoiGianKy> tinhDanhSachKhoangThoiGianKy(
            LocalDate ngayApDung,
            int soThang,
            int soKy,
            List<Integer> soNgayNghiChuyenKy
    ) {
        int baseMonths = soThang / soKy;
        int remainder = soThang % soKy;

        List<KhoangThoiGianKy> result = new ArrayList<>();
        LocalDate start = ngayApDung;

        for (int i = 1; i <= soKy; i++) {
            int monthsOfTerm = baseMonths + (i <= remainder ? 1 : 0);
            LocalDate end = start.plusMonths(monthsOfTerm).minusDays(1);

            int soNgayNghi = (i < soKy && soNgayNghiChuyenKy != null && (i - 1) < soNgayNghiChuyenKy.size())
                    ? (soNgayNghiChuyenKy.get(i - 1) != null ? soNgayNghiChuyenKy.get(i - 1) : 0)
                    : 0;

            LocalDate startNghi = soNgayNghi > 0 ? end.plusDays(1) : null;
            LocalDate endNghi = soNgayNghi > 0 ? end.plusDays(soNgayNghi) : null;

            result.add(new KhoangThoiGianKy(start, end, soNgayNghi, startNghi, endNghi));
            start = end.plusDays(soNgayNghi + 1);
        }
        return result;
    }

    private int tongSoNgay(List<Integer> list) {
        if (list == null) return 0;
        return list.stream().filter(n -> n != null).mapToInt(Integer::intValue).sum();
    }

    // ============================================================
    // CANH BAO TAI HOC
    // ============================================================

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
            canhBaoChung.add("Version chua co mon hoc de danh gia tai hoc theo ky.");
        }
        if (chuaXepKy.soMon > 0) {
            canhBaoChung.add("Co " + chuaXepKy.soMon + " mon chua duoc xep vao ky, nen tai hoc theo ky chua phan anh day du.");
        }

        List<KhungKyTaiHocItemResponse> danhSachKyTaiHoc = new ArrayList<>();
        for (int thuTu = 1; thuTu <= soKy; thuTu++) {
            KhungKy ky = kyTheoThuTu.get(thuTu);
            TaiHocAccumulator taiHoc = ky != null
                    ? taiHocTheoKhungKyId.getOrDefault(ky.getId(), new TaiHocAccumulator())
                    : new TaiHocAccumulator();
            danhSachKyTaiHoc.add(buildTaiHocItem(
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
            danhSachKyTaiHoc.add(buildTaiHocItem(
                    null, null, "CHUA_XEP_KY", "Mon chua xep ky",
                    false, true, chuaXepKy,
                    gioTrungBinh, nguongQuaTaiGio, nguongQuaTaiTinChi, nguongNheTaiGio,
                    soMonToiDaKhuyenNghi, soMonToiThieuCanhBao
            ));
        }

        danhSachKyTaiHoc.stream()
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
                .danhSachKy(danhSachKyTaiHoc)
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
            canhBao.add(tenKy + " co " + taiHoc.soMon + " mon chua duoc xep vao ky.");
        } else if (Boolean.FALSE.equals(daCoKhungKy)) {
            mucDoTai = "CHUA_TAO_KY";
            canhBao.add(tenKy + " chua duoc tao khung ky nen chua the phan bo tai hoc.");
        } else {
            if (taiHoc.soMon > soMonToiDaKhuyenNghi) {
                mucDoTai = "QUA_TAI";
                canhBao.add(tenKy + " co " + taiHoc.soMon + " mon, vuot muc khuyen nghi khoang " + soMonToiDaKhuyenNghi + " mon/ky.");
            }
            if (taiHoc.tongGio.compareTo(nguongQuaTaiGio) > 0) {
                mucDoTai = "QUA_TAI";
                canhBao.add(tenKy + " co " + scale1(taiHoc.tongGio) + " gio, cao hon nguong qua tai " + scale1(nguongQuaTaiGio) + " gio/ky.");
            }
            if (taiHoc.tongTinChi.compareTo(nguongQuaTaiTinChi) > 0) {
                mucDoTai = "QUA_TAI";
                canhBao.add(tenKy + " co " + scale1(taiHoc.tongTinChi) + " tin chi, cao hon nguong qua tai " + scale1(nguongQuaTaiTinChi) + " tin chi/ky.");
            }
            if (taiHoc.soMon > 0 && taiHoc.tongGio.compareTo(nguongNheTaiGio) < 0 && gioTrungBinh.compareTo(BigDecimal.ZERO) > 0) {
                if (!"QUA_TAI".equals(mucDoTai)) {
                    mucDoTai = "NHE_TAI";
                }
                canhBao.add(tenKy + " chi co " + scale1(taiHoc.tongGio) + " gio, thap hon nhieu so voi muc trung binh.");
            }
            if (taiHoc.soMon <= soMonToiThieuCanhBao && soMonToiThieuCanhBao > 0) {
                if (!"QUA_TAI".equals(mucDoTai)) {
                    mucDoTai = "NHE_TAI";
                }
                canhBao.add(tenKy + " chi co " + taiHoc.soMon + " mon, thap hon nhieu so voi phan bo trung binh.");
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
        if (divisor <= 0) return BigDecimal.ZERO;
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

    // ============================================================
    // VALIDATION HELPERS (dung cho create/update don le)
    // ============================================================

    private void validate(KhungKyRequest request, Long id) {
        if (request == null) {
            throw new BadRequestException("Du lieu khung ky khong hop le");
        }
        if (request.getChuongTrinhVersionId() == null) {
            throw new BadRequestException("chuongTrinhVersionId khong duoc de trong");
        }

        ChuongTrinhVersion version = getVersion(request.getChuongTrinhVersionId());
        ChuongTrinh chuongTrinh = getChuongTrinh(version.getChuongTrinhId());
        NganhHeDaoTao nganhHe = resolveNganhHeDaoTao(chuongTrinh);
        validateCauHinhThoiGian(version, nganhHe);

        if (id == null && repository.countByChuongTrinhVersionId(request.getChuongTrinhVersionId()) >= nganhHe.getSoKy()) {
            throw new BadRequestException("Version da tao du " + nganhHe.getSoKy() + " ky theo cau hinh nganh he dao tao");
        }

        applyDefaultThuTuVaTenKy(request, id, nganhHe.getSoKy());
        applyDefaultNgayKy(request, version, nganhHe);

        if (request.getThuTu() <= 0) {
            throw new BadRequestException("thuTu phai lon hon 0");
        }
        if (request.getThuTu() > nganhHe.getSoKy()) {
            throw new BadRequestException("Thu tu ky khong duoc lon hon so ky cua nganh he dao tao (" + nganhHe.getSoKy() + ")");
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
            throw new BadRequestException("thuTu khong duoc de trong");
        }
        if (request.getThuTu() > soKy) {
            throw new BadRequestException("Khong duoc tao ky thu " + request.getThuTu() + " vi nganh he chi co " + soKy + " ky");
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
        // Use basic calculation without nghiChuyenKy for single-ky validation
        List<Integer> zeroList = new ArrayList<>();
        for (int i = 0; i < nganhHe.getSoKy(); i++) zeroList.add(0);
        List<KhoangThoiGianKy> danhSachKhoang = tinhDanhSachKhoangThoiGianKy(
                version.getNgayApDung(), nganhHe.getSoThang(), nganhHe.getSoKy(), zeroList);
        KhoangThoiGianKy goiY = danhSachKhoang.get(request.getThuTu() - 1);
        if (request.getNgayBatDau() == null) {
            request.setNgayBatDau(goiY.ngayBatDau());
        }
        if (request.getNgayKetThuc() == null) {
            request.setNgayKetThuc(goiY.ngayKetThuc());
        }
    }

    private void validateNgayKyTrongVersion(KhungKyRequest request, ChuongTrinhVersion version, NganhHeDaoTao nganhHe) {
        if (request.getNgayBatDau() == null || request.getNgayKetThuc() == null) {
            throw new BadRequestException("Ngay bat dau va ngay ket thuc ky khong duoc de trong");
        }
        if (request.getNgayKetThuc().isBefore(request.getNgayBatDau())) {
            throw new BadRequestException("Ngay ket thuc ky khong duoc truoc ngay bat dau ky");
        }
        LocalDate ngayHetHieuLuc = version.getNgayHetHieuLuc() != null
                ? version.getNgayHetHieuLuc()
                : tinhNgayKetThucVersion(version.getNgayApDung(), nganhHe.getSoThang());
        if (request.getNgayBatDau().isBefore(version.getNgayApDung())) {
            throw new BadRequestException("Ngay bat dau ky khong duoc truoc ngay ap dung cua version chuong trinh ("
                    + version.getNgayApDung() + ")");
        }
        if (request.getNgayKetThuc().isAfter(ngayHetHieuLuc)) {
            throw new BadRequestException("Ngay ket thuc ky khong duoc sau ngay het hieu luc cua version chuong trinh ("
                    + ngayHetHieuLuc + ")");
        }
    }

    private void validateTaoKyTheoThuTu(KhungKyRequest request, Long id) {
        if (id != null) return;
        int expectedThuTu = (int) repository.countByChuongTrinhVersionId(request.getChuongTrinhVersionId()) + 1;
        if (!request.getThuTu().equals(expectedThuTu)) {
            throw new BadRequestException("Phai tao khung ky theo dung thu tu. Ky tiep theo can tao la ky " + expectedThuTu);
        }
    }

    private void validateKhongChongNgay(KhungKyRequest request, Long id) {
        List<KhungKy> dsKy = repository.findByChuongTrinhVersionIdOrderByThuTuAsc(request.getChuongTrinhVersionId());
        for (KhungKy ky : dsKy) {
            if (id != null && ky.getId().equals(id)) continue;
            if (ky.getNgayBatDau() == null || ky.getNgayKetThuc() == null) continue;
            boolean chongNgay = !request.getNgayKetThuc().isBefore(ky.getNgayBatDau())
                    && !request.getNgayBatDau().isAfter(ky.getNgayKetThuc());
            if (chongNgay) {
                throw new BadRequestException("Thoi gian ky dang nhap bi chong voi " + ky.getTenKy()
                        + " (" + ky.getNgayBatDau() + " -> " + ky.getNgayKetThuc() + ")");
            }
            if (request.getThuTu() > ky.getThuTu() && !request.getNgayBatDau().isAfter(ky.getNgayKetThuc())) {
                throw new BadRequestException("Ngay bat dau cua ky " + request.getThuTu()
                        + " phai sau ngay ket thuc cua ky " + ky.getThuTu());
            }
            if (request.getThuTu() < ky.getThuTu() && !request.getNgayKetThuc().isBefore(ky.getNgayBatDau())) {
                throw new BadRequestException("Ngay ket thuc cua ky " + request.getThuTu()
                        + " phai truoc ngay bat dau cua ky " + ky.getThuTu());
            }
        }
    }

    private void validateUnique(KhungKyRequest request, Long id) {
        if (id == null) {
            if (repository.existsByChuongTrinhVersionIdAndMaKy(request.getChuongTrinhVersionId(), request.getMaKy())) {
                throw new DuplicateResourceException("Ma ky da ton tai trong version chuong trinh");
            }
            if (repository.existsByChuongTrinhVersionIdAndThuTu(request.getChuongTrinhVersionId(), request.getThuTu())) {
                throw new DuplicateResourceException("Thu tu ky da ton tai trong version chuong trinh");
            }
        } else {
            if (repository.existsByChuongTrinhVersionIdAndMaKyAndIdNot(request.getChuongTrinhVersionId(), request.getMaKy(), id)) {
                throw new DuplicateResourceException("Ma ky da ton tai trong version chuong trinh");
            }
            if (repository.existsByChuongTrinhVersionIdAndThuTuAndIdNot(request.getChuongTrinhVersionId(), request.getThuTu(), id)) {
                throw new DuplicateResourceException("Thu tu ky da ton tai trong version chuong trinh");
            }
        }
    }

    // ============================================================
    // UTILITY
    // ============================================================

    private ChuongTrinhVersion getVersion(Long id) {
        return chuongTrinhVersionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Version chuong trinh khong ton tai: " + id));
    }

    private ChuongTrinh getChuongTrinh(Long id) {
        return chuongTrinhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chuong trinh dao tao khong ton tai: " + id));
    }

    private KhungKy getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Khung ky khong ton tai: " + id));
    }

    private NganhHeDaoTao resolveNganhHeDaoTao(ChuongTrinh chuongTrinh) {
        if (chuongTrinh.getNganhHeDaoTaoId() == null) {
            throw new BadRequestException("Chuong trinh chua gan nganh he dao tao nen khong the goi y khung ky");
        }
        return nganhHeDaoTaoRepository.findById(chuongTrinh.getNganhHeDaoTaoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Nganh he dao tao khong ton tai: " + chuongTrinh.getNganhHeDaoTaoId()
                ));
    }

    private void validateCauHinhThoiGian(ChuongTrinhVersion version, NganhHeDaoTao nganhHe) {
        if (version.getNgayApDung() == null) {
            throw new BadRequestException("Version chuong trinh chua co ngay ap dung");
        }
        if (nganhHe.getSoThang() == null || nganhHe.getSoThang() <= 0) {
            throw new BadRequestException("Nganh he dao tao chua cau hinh so thang dao tao hop le");
        }
        if (nganhHe.getSoKy() == null || nganhHe.getSoKy() <= 0) {
            throw new BadRequestException("Nganh he dao tao chua cau hinh so ky dao tao hop le");
        }
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

    private record KhoangThoiGianKy(
            LocalDate ngayBatDau,
            LocalDate ngayKetThuc,
            Integer soNgayNghiChuyenKySau,
            LocalDate ngayBatDauNghiChuyenKySau,
            LocalDate ngayKetThucNghiChuyenKySau
    ) {
    }
}
