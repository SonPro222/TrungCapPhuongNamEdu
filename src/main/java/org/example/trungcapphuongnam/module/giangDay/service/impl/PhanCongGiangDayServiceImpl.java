package org.example.trungcapphuongnam.module.giangDay.service.impl;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.giangDay.GiangDayNotFoundException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.PhanCongGiangDayRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.PhanCongGiangDayResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.GiaoVien;
import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.entity.PhanCongGiangDay;
import org.example.trungcapphuongnam.module.giangDay.enums.VaiTroGiangDay;
import org.example.trungcapphuongnam.module.giangDay.mapper.PhanCongGiangDayMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.LopHocPhanRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.PhanCongGiangDayRepository;
import org.example.trungcapphuongnam.module.giangDay.service.PhanCongGiangDayService;
import org.example.trungcapphuongnam.module.giangDay.validator.PhanCongGiangDayValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PhanCongGiangDayServiceImpl implements PhanCongGiangDayService {

    private final PhanCongGiangDayRepository repository;
    private final LopHocPhanRepository lopHocPhanRepository;
    private final GiaoVienRepository giaoVienRepository;
    private final PhanCongGiangDayMapper mapper;
    private final PhanCongGiangDayValidator validator;

    @Override
    @Transactional(readOnly = true)
    public Page<PhanCongGiangDayResponse> getAll(
            String keywordLop,
            String keywordGiaoVien,
            VaiTroGiangDay vaiTro,
            Pageable pageable
    ) {
        Page<PhanCongGiangDay> page = repository.findAll(
                buildSpecification(keywordLop, keywordGiaoVien, vaiTro),
                pageable
        );

        Map<Long, LopHocPhan> lopHocPhanMap = lopHocPhanRepository.findAllById(
                        page.getContent()
                                .stream()
                                .map(PhanCongGiangDay::getLopHocPhanId)
                                .filter(id -> id != null)
                                .collect(Collectors.toSet())
                )
                .stream()
                .collect(Collectors.toMap(LopHocPhan::getId, Function.identity()));

        Map<Long, GiaoVien> giaoVienMap = giaoVienRepository.findAllById(
                        page.getContent()
                                .stream()
                                .map(PhanCongGiangDay::getGiaoVienId)
                                .filter(id -> id != null)
                                .collect(Collectors.toSet())
                )
                .stream()
                .collect(Collectors.toMap(GiaoVien::getId, Function.identity()));

        return page.map(item -> toResponse(item, lopHocPhanMap, giaoVienMap));
    }

    @Override
    @Transactional(readOnly = true)
    public PhanCongGiangDayResponse getById(Long id) {
        PhanCongGiangDay entity = findEntity(id);

        Map<Long, LopHocPhan> lopHocPhanMap = lopHocPhanRepository.findAllById(
                        List.of(entity.getLopHocPhanId())
                )
                .stream()
                .collect(Collectors.toMap(LopHocPhan::getId, Function.identity()));

        Map<Long, GiaoVien> giaoVienMap = giaoVienRepository.findAllById(
                        List.of(entity.getGiaoVienId())
                )
                .stream()
                .collect(Collectors.toMap(GiaoVien::getId, Function.identity()));

        return toResponse(entity, lopHocPhanMap, giaoVienMap);
    }

    @Override
    public PhanCongGiangDayResponse create(PhanCongGiangDayRequest request) {
        validator.validateCreate(request);
        PhanCongGiangDay entity = mapper.toEntity(request);
        PhanCongGiangDay saved = repository.save(entity);
        return getById(saved.getId());
    }

    @Override
    public PhanCongGiangDayResponse update(Long id, PhanCongGiangDayRequest request) {
        validator.validateUpdate(id, request);
        PhanCongGiangDay entity = findEntity(id);
        mapper.updateEntity(entity, request);
        PhanCongGiangDay saved = repository.save(entity);
        return getById(saved.getId());
    }

    @Override
    public void delete(Long id) {
        PhanCongGiangDay entity = findEntity(id);
        repository.delete(entity);
    }

    private PhanCongGiangDay findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GiangDayNotFoundException("Phân công giảng dạy không tồn tại với id = " + id));
    }

    private Specification<PhanCongGiangDay> buildSpecification(
            String keywordLop,
            String keywordGiaoVien,
            VaiTroGiangDay vaiTro
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (vaiTro != null) {
                predicates.add(cb.equal(root.get("vaiTro"), vaiTro));
            }

            if (keywordLop != null && !keywordLop.isBlank()) {
                String pattern = "%" + keywordLop.trim().toLowerCase() + "%";

                Subquery<Long> subquery = query.subquery(Long.class);
                Root<LopHocPhan> lopHocPhanRoot = subquery.from(LopHocPhan.class);

                subquery.select(lopHocPhanRoot.get("id"));
                subquery.where(
                        cb.and(
                                cb.equal(lopHocPhanRoot.get("id"), root.get("lopHocPhanId")),
                                cb.or(
                                        cb.like(cb.lower(lopHocPhanRoot.get("maLop")), pattern),
                                        cb.like(cb.lower(lopHocPhanRoot.get("tenLop")), pattern)
                                )
                        )
                );

                predicates.add(cb.exists(subquery));
            }

            if (keywordGiaoVien != null && !keywordGiaoVien.isBlank()) {
                String pattern = "%" + keywordGiaoVien.trim().toLowerCase() + "%";

                Subquery<Long> subquery = query.subquery(Long.class);
                Root<GiaoVien> giaoVienRoot = subquery.from(GiaoVien.class);

                subquery.select(giaoVienRoot.get("id"));
                subquery.where(
                        cb.and(
                                cb.equal(giaoVienRoot.get("id"), root.get("giaoVienId")),
                                cb.or(
                                        cb.like(cb.lower(giaoVienRoot.get("maGiaoVien")), pattern),
                                        cb.like(cb.lower(giaoVienRoot.get("hoTen")), pattern)
                                )
                        )
                );

                predicates.add(cb.exists(subquery));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private PhanCongGiangDayResponse toResponse(
            PhanCongGiangDay entity,
            Map<Long, LopHocPhan> lopHocPhanMap,
            Map<Long, GiaoVien> giaoVienMap
    ) {
        PhanCongGiangDayResponse response = mapper.toResponse(entity);

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

        return response;
    }
}