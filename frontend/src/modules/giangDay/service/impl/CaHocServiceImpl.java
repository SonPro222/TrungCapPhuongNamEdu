package org.example.trungcapphuongnam.module.giangDay.service.impl;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.giangDay.GiangDayNotFoundException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.CaHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.CaHocResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.CaHoc;
import org.example.trungcapphuongnam.module.giangDay.mapper.CaHocMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.CaHocRepository;
import org.example.trungcapphuongnam.module.giangDay.service.CaHocService;
import org.example.trungcapphuongnam.module.giangDay.validator.CaHocValidator;
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
public class CaHocServiceImpl implements CaHocService {

    private final CaHocRepository repository;
    private final CaHocMapper mapper;
    private final CaHocValidator validator;

    @Override
    @Transactional(readOnly = true)
    public Page<CaHocResponse> getAll(String keyword, Pageable pageable) {
        return repository.findAll(buildSpecification(keyword), pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CaHocResponse getById(Long id) {
        return mapper.toResponse(findEntity(id));
    }

    @Override
    public CaHocResponse create(CaHocRequest request) {
        validator.validateCreate(request);
        CaHoc entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public CaHocResponse update(Long id, CaHocRequest request) {
        validator.validateUpdate(id, request);
        CaHoc entity = findEntity(id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        CaHoc entity = findEntity(id);
        repository.delete(entity);
    }

    private CaHoc findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GiangDayNotFoundException("Ca học không tồn tại với id = " + id));
    }

    private Specification<CaHoc> buildSpecification(String keyword) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.isBlank()) {
                String pattern = "%" + keyword.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("maCa")), pattern),
                        cb.like(cb.lower(root.get("tenCa")), pattern),
                        cb.like(cb.lower(root.get("moTa")), pattern)
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}