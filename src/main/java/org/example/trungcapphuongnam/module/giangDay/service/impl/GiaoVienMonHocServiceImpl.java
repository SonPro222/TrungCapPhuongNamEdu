package org.example.trungcapphuongnam.module.giangDay.service.impl;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.giangDay.GiangDayNotFoundException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.GiaoVienMonHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.GiaoVienMonHocResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.GiaoVienMonHoc;
import org.example.trungcapphuongnam.module.giangDay.mapper.GiaoVienMonHocMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienMonHocRepository;
import org.example.trungcapphuongnam.module.giangDay.service.GiaoVienMonHocService;
import org.example.trungcapphuongnam.module.giangDay.validator.GiaoVienMonHocValidator;
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
public class GiaoVienMonHocServiceImpl implements GiaoVienMonHocService {

    private final GiaoVienMonHocRepository repository;
    private final GiaoVienMonHocMapper mapper;
    private final GiaoVienMonHocValidator validator;

    @Override
    @Transactional(readOnly = true)
    public Page<GiaoVienMonHocResponse> getAll(String keyword, Long giaoVienId, Long monHocId, String trangThai, Pageable pageable) {
        return repository.findAll(buildSpec(keyword, giaoVienId, monHocId, trangThai), pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public GiaoVienMonHocResponse getById(Long id) {
        return mapper.toResponse(findEntity(id));
    }

    @Override
    public GiaoVienMonHocResponse create(GiaoVienMonHocRequest request) {
        validator.validateCreate(request);
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    public GiaoVienMonHocResponse update(Long id, GiaoVienMonHocRequest request) {
        validator.validateUpdate(id, request);
        GiaoVienMonHoc entity = findEntity(id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.delete(findEntity(id));
    }

    private GiaoVienMonHoc findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GiangDayNotFoundException("Cấu hình giảng viên dạy môn không tồn tại với id = " + id));
    }

    private Specification<GiaoVienMonHoc> buildSpec(String keyword, Long giaoVienId, Long monHocId, String trangThai) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (giaoVienId != null) predicates.add(cb.equal(root.get("giaoVienId"), giaoVienId));
            if (monHocId != null) predicates.add(cb.equal(root.get("monHocId"), monHocId));
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
