package org.example.trungcapphuongnam.module.daoTao.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.DuplicateResourceException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.util.TextUtil;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinh;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersion;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhVersionRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.XoaChuongTrinhCascadeService;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyGoiYItemResponse;
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
    private final ChuongTrinhRepository chuongTrinhRepository;
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
                .message(soKyConThieu == 0
                        ? "Version đã tạo đủ " + nganhHe.getSoKy() + " kỳ."
                        : "Version còn thiếu " + soKyConThieu + " kỳ. Nên tạo theo đúng thứ tự từ kỳ tiếp theo.")
                .build();
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
