package org.example.trungcapphuongnam.module.giangDay.service.impl;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.giangDay.GiangDayNotFoundException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.PhongHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.PhongHocResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.PhongHoc;
import org.example.trungcapphuongnam.module.giangDay.enums.LoaiPhong;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiPhongHoc;
import org.example.trungcapphuongnam.module.giangDay.mapper.PhongHocMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.PhongHocRepository;
import org.example.trungcapphuongnam.module.giangDay.service.PhongHocService;
import org.example.trungcapphuongnam.module.giangDay.validator.PhongHocValidator;
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
public class PhongHocServiceImpl implements PhongHocService {

    private final PhongHocRepository repository;
    private final PhongHocMapper mapper;
    private final PhongHocValidator validator;

    @Override
    @Transactional(readOnly = true)
    public Page<PhongHocResponse> getAll(
            String keyword,
            LoaiPhong loaiPhong,
            TrangThaiPhongHoc trangThai,
            Pageable pageable
    ) {
        return repository.findAll(buildSpecification(keyword, loaiPhong, trangThai), pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public PhongHocResponse getById(Long id) {
        return mapper.toResponse(findEntity(id));
    }

    @Override
    public PhongHocResponse create(PhongHocRequest request) {
        validator.validateCreate(request);
        PhongHoc entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public PhongHocResponse update(Long id, PhongHocRequest request) {
        validator.validateUpdate(id, request);
        PhongHoc entity = findEntity(id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        PhongHoc entity = findEntity(id);
        repository.delete(entity);
    }

    private PhongHoc findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GiangDayNotFoundException("Phòng học không tồn tại với id = " + id));
    }

    private Specification<PhongHoc> buildSpecification(
            String keyword,
            LoaiPhong loaiPhong,
            TrangThaiPhongHoc trangThai
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.isBlank()) {
                String pattern = "%" + keyword.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("maPhong")), pattern),
                        cb.like(cb.lower(root.get("tenPhong")), pattern),
                        cb.like(cb.lower(root.get("diaDiem")), pattern)
                ));
            }

            if (loaiPhong != null) {
                predicates.add(cb.equal(root.get("loaiPhong"), loaiPhong));
            }

            if (trangThai != null) {
                predicates.add(cb.equal(root.get("trangThai"), trangThai));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}