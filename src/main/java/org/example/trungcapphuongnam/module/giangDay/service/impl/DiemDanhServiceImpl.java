package org.example.trungcapphuongnam.module.giangDay.service.impl;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.giangDay.GiangDayNotFoundException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.DiemDanhRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.DiemDanhResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.DiemDanh;
import org.example.trungcapphuongnam.module.giangDay.entity.LichHoc;
import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiDiemDanh;
import org.example.trungcapphuongnam.module.giangDay.mapper.DiemDanhMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.DiemDanhRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.LichHocRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.LopHocPhanRepository;
import org.example.trungcapphuongnam.module.giangDay.service.DiemDanhService;
import org.example.trungcapphuongnam.module.giangDay.validator.DiemDanhValidator;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVien;
import org.example.trungcapphuongnam.module.sinhVien.repository.SinhVienRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
@Transactional
public class DiemDanhServiceImpl implements DiemDanhService {

    private final DiemDanhRepository repository;
    private final LichHocRepository lichHocRepository;
    private final LopHocPhanRepository lopHocPhanRepository;
    private final SinhVienRepository sinhVienRepository;
    private final DiemDanhMapper mapper;
    private final DiemDanhValidator validator;

    @Override
    @Transactional(readOnly = true)
    public Page<DiemDanhResponse> getAll(
            Long lichHocId,
            Long lopHocPhanId,
            Long sinhVienId,
            String keywordSinhVien,
            TrangThaiDiemDanh trangThai,
            Pageable pageable
    ) {
        Page<DiemDanh> page = repository.findAll(
                buildSpecification(lichHocId, lopHocPhanId, sinhVienId, keywordSinhVien, trangThai),
                pageable
        );

        Map<Long, LichHoc> lichHocMap = lichHocRepository.findAllById(
                        page.getContent()
                                .stream()
                                .map(DiemDanh::getLichHocId)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet())
                )
                .stream()
                .collect(Collectors.toMap(LichHoc::getId, Function.identity()));

        Map<Long, LopHocPhan> lopHocPhanMap = lopHocPhanRepository.findAllById(
                        lichHocMap.values()
                                .stream()
                                .map(LichHoc::getLopHocPhanId)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet())
                )
                .stream()
                .collect(Collectors.toMap(LopHocPhan::getId, Function.identity()));

        Map<Long, SinhVien> sinhVienMap = sinhVienRepository.findAllById(
                        page.getContent()
                                .stream()
                                .map(DiemDanh::getSinhVienId)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet())
                )
                .stream()
                .collect(Collectors.toMap(SinhVien::getId, Function.identity()));

        return page.map(item -> mapper.toResponse(item, lichHocMap, lopHocPhanMap, sinhVienMap));
    }

    @Override
    @Transactional(readOnly = true)
    public DiemDanhResponse getById(Long id) {
        DiemDanh entity = findEntity(id);

        Map<Long, LichHoc> lichHocMap = lichHocRepository.findAllById(List.of(entity.getLichHocId()))
                .stream()
                .collect(Collectors.toMap(LichHoc::getId, Function.identity()));

        Map<Long, LopHocPhan> lopHocPhanMap = lopHocPhanRepository.findAllById(
                        lichHocMap.values()
                                .stream()
                                .map(LichHoc::getLopHocPhanId)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet())
                )
                .stream()
                .collect(Collectors.toMap(LopHocPhan::getId, Function.identity()));

        Map<Long, SinhVien> sinhVienMap = sinhVienRepository.findAllById(List.of(entity.getSinhVienId()))
                .stream()
                .collect(Collectors.toMap(SinhVien::getId, Function.identity()));

        return mapper.toResponse(entity, lichHocMap, lopHocPhanMap, sinhVienMap);
    }

    @Override
    public DiemDanhResponse create(DiemDanhRequest request) {
        validator.validateCreate(request);
        DiemDanh entity = mapper.toEntity(request);
        chuanHoaThoiGianDiemDanh(entity);
        DiemDanh saved = repository.save(entity);
        return getById(saved.getId());
    }
    @Override
    public DiemDanhResponse update(Long id, DiemDanhRequest request) {
        validator.validateUpdate(id, request);
        DiemDanh entity = findEntity(id);
        mapper.updateEntity(entity, request);
        chuanHoaThoiGianDiemDanh(entity);
        DiemDanh saved = repository.save(entity);
        return getById(saved.getId());
    }
    @Override
    public void delete(Long id) {
        DiemDanh entity = findEntity(id);
        repository.delete(entity);
    }

    private DiemDanh findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GiangDayNotFoundException("Điểm danh không tồn tại với id = " + id));
    }

    private Specification<DiemDanh> buildSpecification(
            Long lichHocId,
            Long lopHocPhanId,
            Long sinhVienId,
            String keywordSinhVien,
            TrangThaiDiemDanh trangThai
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (lichHocId != null) {
                predicates.add(cb.equal(root.get("lichHocId"), lichHocId));
            }

            if (sinhVienId != null) {
                predicates.add(cb.equal(root.get("sinhVienId"), sinhVienId));
            }

            if (trangThai != null) {
                predicates.add(cb.equal(root.get("trangThai"), trangThai));
            }

            if (lopHocPhanId != null) {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<LichHoc> lichRoot = subquery.from(LichHoc.class);

                subquery.select(cb.literal(1L));
                subquery.where(cb.and(
                        cb.equal(lichRoot.get("id"), root.get("lichHocId")),
                        cb.equal(lichRoot.get("lopHocPhanId"), lopHocPhanId)
                ));

                predicates.add(cb.exists(subquery));
            }

            if (keywordSinhVien != null && !keywordSinhVien.isBlank()) {
                String pattern = "%" + keywordSinhVien.trim().toLowerCase() + "%";

                Subquery<Long> subquery = query.subquery(Long.class);
                Root<SinhVien> sinhVienRoot = subquery.from(SinhVien.class);

                subquery.select(cb.literal(1L));
                subquery.where(cb.and(
                        cb.equal(sinhVienRoot.get("id"), root.get("sinhVienId")),
                        cb.or(
                                cb.like(cb.lower(sinhVienRoot.get("maSinhVien")), pattern),
                                cb.like(cb.lower(sinhVienRoot.get("hoTen")), pattern)
                        )
                ));

                predicates.add(cb.exists(subquery));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private void chuanHoaThoiGianDiemDanh(DiemDanh entity) {
        if (entity.getTrangThai() == TrangThaiDiemDanh.chua_diem_danh) {
            entity.setThoiGianDiemDanh(null);
            return;
        }

        if (entity.getThoiGianDiemDanh() == null) {
            entity.setThoiGianDiemDanh(LocalDateTime.now());
        }
    }
}