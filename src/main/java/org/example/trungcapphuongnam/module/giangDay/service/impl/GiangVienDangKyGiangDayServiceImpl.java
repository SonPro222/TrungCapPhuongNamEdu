package org.example.trungcapphuongnam.module.giangDay.service.impl;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.giangDay.GiangDayNotFoundException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.GiangVienDangKyGiangDayRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.GiangVienDangKyGiangDayResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.GiangVienDangKyGiangDay;
import org.example.trungcapphuongnam.module.giangDay.mapper.GiangVienDangKyGiangDayMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.GiangVienDangKyGiangDayRepository;
import org.example.trungcapphuongnam.module.giangDay.service.GiangVienDangKyGiangDayService;
import org.example.trungcapphuongnam.module.giangDay.validator.GiangVienDangKyGiangDayValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GiangVienDangKyGiangDayServiceImpl implements GiangVienDangKyGiangDayService {

    private final GiangVienDangKyGiangDayRepository repository;
    private final GiangVienDangKyGiangDayMapper mapper;
    private final GiangVienDangKyGiangDayValidator validator;

    @Override
    @Transactional(readOnly = true)
    public Page<GiangVienDangKyGiangDayResponse> getAll(String keyword, Long giaoVienId, Long khungKyId, String trangThai, Pageable pageable) {
        return repository.findAll(buildSpec(keyword, giaoVienId, khungKyId, trangThai), pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public GiangVienDangKyGiangDayResponse getById(Long id) {
        return mapper.toResponse(findEntity(id));
    }

    @Override
    public GiangVienDangKyGiangDayResponse create(GiangVienDangKyGiangDayRequest request) {
        validator.validateCreate(request);
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    public GiangVienDangKyGiangDayResponse update(Long id, GiangVienDangKyGiangDayRequest request) {
        validator.validateUpdate(id, request);
        GiangVienDangKyGiangDay entity = findEntity(id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        validator.validateDelete(id);
        repository.delete(findEntity(id));
    }

    private GiangVienDangKyGiangDay findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GiangDayNotFoundException("Đăng ký giảng dạy không tồn tại với id = " + id));
    }

    private Specification<GiangVienDangKyGiangDay> buildSpec(String keyword, Long giaoVienId, Long khungKyId, String trangThai) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (giaoVienId != null) predicates.add(cb.equal(root.get("giaoVienId"), giaoVienId));
            if (khungKyId != null) predicates.add(cb.equal(root.get("khungKyId"), khungKyId));
            if (trangThai != null && !trangThai.isBlank()) predicates.add(cb.equal(root.get("trangThai"), trangThai));
            if (keyword != null && !keyword.isBlank()) {
                String p = "%" + keyword.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("trangThai")), p),
                        cb.like(cb.lower(root.get("ghiChu")), p)
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
