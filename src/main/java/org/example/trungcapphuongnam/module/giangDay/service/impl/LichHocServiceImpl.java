package org.example.trungcapphuongnam.module.giangDay.service.impl;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.giangDay.GiangDayNotFoundException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.LichHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LichHocResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.CaHoc;
import org.example.trungcapphuongnam.module.giangDay.entity.GiaoVien;
import org.example.trungcapphuongnam.module.giangDay.entity.LichHoc;
import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.entity.PhongHoc;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLichHoc;
import org.example.trungcapphuongnam.module.giangDay.mapper.LichHocMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.CaHocRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.LichHocRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.LopHocPhanRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.PhongHocRepository;
import org.example.trungcapphuongnam.module.giangDay.service.LichHocService;
import org.example.trungcapphuongnam.module.giangDay.validator.LichHocValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.module.giangDay.entity.DiemDanh;
import org.example.trungcapphuongnam.module.giangDay.entity.SinhVienLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiDiemDanh;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiSinhVienLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.repository.DiemDanhRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.SinhVienLopHocPhanRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LichHocServiceImpl implements LichHocService {

    private final LichHocRepository repository;
    private final LopHocPhanRepository lopHocPhanRepository;
    private final GiaoVienRepository giaoVienRepository;
    private final PhongHocRepository phongHocRepository;
    private final CaHocRepository caHocRepository;
    private final LichHocMapper mapper;
    private final LichHocValidator validator;
    private final DiemDanhRepository diemDanhRepository;
    private final SinhVienLopHocPhanRepository sinhVienLopHocPhanRepository;
    @Override
    @Transactional(readOnly = true)
    public Page<LichHocResponse> getAll(
            Long lopHocPhanId,
            String keywordLop,
            String keywordGiaoVien,
            String keywordPhong,
            String keywordCa,
            TrangThaiLichHoc trangThai,
            LocalDate tuNgay,
            LocalDate denNgay,
            Pageable pageable
    ) {
        Page<LichHoc> page = repository.findAll(
                buildSpecification(
                        lopHocPhanId,
                        keywordLop,
                        keywordGiaoVien,
                        keywordPhong,
                        keywordCa,
                        trangThai,
                        tuNgay,
                        denNgay
                ),
                pageable
        );

        Map<Long, LopHocPhan> lopHocPhanMap = lopHocPhanRepository.findAllById(
                        page.getContent()
                                .stream()
                                .map(LichHoc::getLopHocPhanId)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet())
                )
                .stream()
                .collect(Collectors.toMap(LopHocPhan::getId, Function.identity()));

        Map<Long, GiaoVien> giaoVienMap = giaoVienRepository.findAllById(
                        page.getContent()
                                .stream()
                                .map(LichHoc::getGiaoVienId)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet())
                )
                .stream()
                .collect(Collectors.toMap(GiaoVien::getId, Function.identity()));

        Map<Long, PhongHoc> phongHocMap = phongHocRepository.findAllById(
                        page.getContent()
                                .stream()
                                .map(LichHoc::getPhongHocId)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet())
                )
                .stream()
                .collect(Collectors.toMap(PhongHoc::getId, Function.identity()));

        Map<Long, CaHoc> caHocMap = caHocRepository.findAllById(
                        page.getContent()
                                .stream()
                                .map(LichHoc::getCaHocId)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet())
                )
                .stream()
                .collect(Collectors.toMap(CaHoc::getId, Function.identity()));

        return page.map(item -> toResponse(item, lopHocPhanMap, giaoVienMap, phongHocMap, caHocMap));
    }

    @Override
    @Transactional(readOnly = true)
    public LichHocResponse getById(Long id) {
        LichHoc entity = findEntity(id);

        Map<Long, LopHocPhan> lopHocPhanMap = entity.getLopHocPhanId() == null
                ? Map.of()
                : lopHocPhanRepository.findAllById(List.of(entity.getLopHocPhanId()))
                .stream()
                .collect(Collectors.toMap(LopHocPhan::getId, Function.identity()));

        Map<Long, GiaoVien> giaoVienMap = entity.getGiaoVienId() == null
                ? Map.of()
                : giaoVienRepository.findAllById(List.of(entity.getGiaoVienId()))
                .stream()
                .collect(Collectors.toMap(GiaoVien::getId, Function.identity()));

        Map<Long, PhongHoc> phongHocMap = entity.getPhongHocId() == null
                ? Map.of()
                : phongHocRepository.findAllById(List.of(entity.getPhongHocId()))
                .stream()
                .collect(Collectors.toMap(PhongHoc::getId, Function.identity()));

        Map<Long, CaHoc> caHocMap = entity.getCaHocId() == null
                ? Map.of()
                : caHocRepository.findAllById(List.of(entity.getCaHocId()))
                .stream()
                .collect(Collectors.toMap(CaHoc::getId, Function.identity()));

        return toResponse(entity, lopHocPhanMap, giaoVienMap, phongHocMap, caHocMap);
    }

    @Override
    public LichHocResponse create(LichHocRequest request) {
        validator.validateCreate(request);
        LichHoc entity = mapper.toEntity(request);
        LichHoc saved = repository.save(entity);
        taoDiemDanhChoBuoiHoc(saved);
        return getById(saved.getId());
    }

    @Override
    public LichHocResponse update(Long id, LichHocRequest request) {
        validator.validateUpdate(id, request);
        LichHoc entity = findEntity(id);
        mapper.updateEntity(entity, request);
        LichHoc saved = repository.save(entity);
        return getById(saved.getId());
    }

    @Override
    public void delete(Long id) {
        LichHoc entity = findEntity(id);
        repository.delete(entity);
    }

    private LichHoc findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GiangDayNotFoundException("Lịch học không tồn tại với id = " + id));
    }

    private Specification<LichHoc> buildSpecification(
            Long lopHocPhanId,
            String keywordLop,
            String keywordGiaoVien,
            String keywordPhong,
            String keywordCa,
            TrangThaiLichHoc trangThai,
            LocalDate tuNgay,
            LocalDate denNgay
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (lopHocPhanId != null) {
                predicates.add(cb.equal(root.get("lopHocPhanId"), lopHocPhanId));
            }

            if (trangThai != null) {
                predicates.add(cb.equal(root.get("trangThai"), trangThai));
            }

            if (tuNgay != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("ngayHoc"), tuNgay));
            }

            if (denNgay != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("ngayHoc"), denNgay));
            }

            if (keywordLop != null && !keywordLop.isBlank()) {
                String pattern = "%" + keywordLop.trim().toLowerCase() + "%";

                Subquery<Long> subquery = query.subquery(Long.class);
                Root<LopHocPhan> lopRoot = subquery.from(LopHocPhan.class);

                subquery.select(lopRoot.get("id"));
                subquery.where(cb.and(
                        cb.equal(lopRoot.get("id"), root.get("lopHocPhanId")),
                        cb.or(
                                cb.like(cb.lower(lopRoot.get("maLop")), pattern),
                                cb.like(cb.lower(lopRoot.get("tenLop")), pattern)
                        )
                ));

                predicates.add(cb.exists(subquery));
            }

            if (keywordGiaoVien != null && !keywordGiaoVien.isBlank()) {
                String pattern = "%" + keywordGiaoVien.trim().toLowerCase() + "%";

                Subquery<Long> subquery = query.subquery(Long.class);
                Root<GiaoVien> giaoVienRoot = subquery.from(GiaoVien.class);

                subquery.select(giaoVienRoot.get("id"));
                subquery.where(cb.and(
                        cb.equal(giaoVienRoot.get("id"), root.get("giaoVienId")),
                        cb.or(
                                cb.like(cb.lower(giaoVienRoot.get("maGiaoVien")), pattern),
                                cb.like(cb.lower(giaoVienRoot.get("hoTen")), pattern)
                        )
                ));

                predicates.add(cb.exists(subquery));
            }

            if (keywordPhong != null && !keywordPhong.isBlank()) {
                String pattern = "%" + keywordPhong.trim().toLowerCase() + "%";

                Subquery<Long> subquery = query.subquery(Long.class);
                Root<PhongHoc> phongRoot = subquery.from(PhongHoc.class);

                subquery.select(phongRoot.get("id"));
                subquery.where(cb.and(
                        cb.equal(phongRoot.get("id"), root.get("phongHocId")),
                        cb.or(
                                cb.like(cb.lower(phongRoot.get("maPhong")), pattern),
                                cb.like(cb.lower(phongRoot.get("tenPhong")), pattern)
                        )
                ));

                predicates.add(cb.exists(subquery));
            }

            if (keywordCa != null && !keywordCa.isBlank()) {
                String pattern = "%" + keywordCa.trim().toLowerCase() + "%";

                Subquery<Long> subquery = query.subquery(Long.class);
                Root<CaHoc> caRoot = subquery.from(CaHoc.class);

                subquery.select(caRoot.get("id"));
                subquery.where(cb.and(
                        cb.equal(caRoot.get("id"), root.get("caHocId")),
                        cb.or(
                                cb.like(cb.lower(caRoot.get("maCa")), pattern),
                                cb.like(cb.lower(caRoot.get("tenCa")), pattern)
                        )
                ));

                predicates.add(cb.exists(subquery));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private LichHocResponse toResponse(
            LichHoc entity,
            Map<Long, LopHocPhan> lopHocPhanMap,
            Map<Long, GiaoVien> giaoVienMap,
            Map<Long, PhongHoc> phongHocMap,
            Map<Long, CaHoc> caHocMap
    ) {
        LichHocResponse response = mapper.toResponse(entity);

        LopHocPhan lopHocPhan = lopHocPhanMap.get(entity.getLopHocPhanId());
        if (lopHocPhan != null) {
            response.setMaLop(lopHocPhan.getMaLop());
            response.setTenLop(lopHocPhan.getTenLop());
        }

        GiaoVien giaoVien = giaoVienMap.get(entity.getGiaoVienId());
        if (giaoVien != null) {
            response.setMaGiaoVien(giaoVien.getMaGiaoVien());
            response.setTenGiaoVien(giaoVien.getHoTen());
        }

        PhongHoc phongHoc = phongHocMap.get(entity.getPhongHocId());
        if (phongHoc != null) {
            response.setMaPhong(phongHoc.getMaPhong());
            response.setTenPhong(phongHoc.getTenPhong());
        }

        CaHoc caHoc = caHocMap.get(entity.getCaHocId());
        if (caHoc != null) {
            response.setMaCa(caHoc.getMaCa());
            response.setTenCa(caHoc.getTenCa());
            response.setGioBatDau(caHoc.getGioBatDau());
            response.setGioKetThuc(caHoc.getGioKetThuc());
        }

        return response;
    }
    private void taoDiemDanhChoBuoiHoc(LichHoc lichHoc) {
        if (lichHoc == null || lichHoc.getId() == null || lichHoc.getLopHocPhanId() == null) {
            return;
        }

        if (lichHoc.getTrangThai() == TrangThaiLichHoc.nghi) {
            return;
        }

        List<SinhVienLopHocPhan> danhSachSinhVien = sinhVienLopHocPhanRepository
                .findByLopHocPhanIdAndTrangThaiInOrderByIdDesc(
                        lichHoc.getLopHocPhanId(),
                        List.of(
                                TrangThaiSinhVienLopHocPhan.da_dang_ky,
                                TrangThaiSinhVienLopHocPhan.dang_hoc,
                                TrangThaiSinhVienLopHocPhan.hoc_lai
                        )
                );

        List<DiemDanh> danhSachDiemDanh = danhSachSinhVien.stream()
                .filter(item -> item.getSinhVienId() != null)
                .filter(item -> !diemDanhRepository.existsByLichHocIdAndSinhVienId(
                        lichHoc.getId(),
                        item.getSinhVienId()
                ))
                .map(item -> DiemDanh.builder()
                        .lichHocId(lichHoc.getId())
                        .sinhVienId(item.getSinhVienId())
                        .trangThai(TrangThaiDiemDanh.chua_diem_danh)
                        .thoiGianDiemDanh(null)
                        .ghiChu(null)
                        .build())
                .toList();

        if (!danhSachDiemDanh.isEmpty()) {
            diemDanhRepository.saveAll(danhSachDiemDanh);
        }
    }
}