package org.example.trungcapphuongnam.module.giangDay.service.impl;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.giangDay.GiangDayNotFoundException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.YeuCauDoiLichRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.YeuCauDoiLichResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.YeuCauDoiLich;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiYeuCauDoiLich;
import org.example.trungcapphuongnam.module.giangDay.mapper.YeuCauDoiLichMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.YeuCauDoiLichRepository;
import org.example.trungcapphuongnam.module.giangDay.service.YeuCauDoiLichService;
import org.example.trungcapphuongnam.module.giangDay.validator.YeuCauDoiLichValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class YeuCauDoiLichServiceImpl implements YeuCauDoiLichService {

    private final YeuCauDoiLichRepository repository;
    private final YeuCauDoiLichMapper mapper;
    private final YeuCauDoiLichValidator validator;

    @Override
    @Transactional(readOnly = true)
    public Page<YeuCauDoiLichResponse> getAll(
            Long lichHocCuId,
            Long giaoVienId,
            Long nguoiDuyetId,
            TrangThaiYeuCauDoiLich trangThai,
            LocalDate tuNgayMoi,
            LocalDate denNgayMoi,
            Pageable pageable
    ) {
        return repository.findAll(
                        buildSpecification(lichHocCuId, giaoVienId, nguoiDuyetId, trangThai, tuNgayMoi, denNgayMoi),
                        pageable
                )
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public YeuCauDoiLichResponse getById(Long id) {
        return mapper.toResponse(findEntity(id));
    }

    @Override
    public YeuCauDoiLichResponse create(YeuCauDoiLichRequest request) {
        validator.validateCreate(request);
        YeuCauDoiLich entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public YeuCauDoiLichResponse update(Long id, YeuCauDoiLichRequest request) {
        validator.validateUpdate(id, request);
        YeuCauDoiLich entity = findEntity(id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        YeuCauDoiLich entity = findEntity(id);
        repository.delete(entity);
    }

    private YeuCauDoiLich findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GiangDayNotFoundException("Yêu cầu đổi lịch không tồn tại với id = " + id));
    }

    private Specification<YeuCauDoiLich> buildSpecification(
            Long lichHocCuId,
            Long giaoVienId,
            Long nguoiDuyetId,
            TrangThaiYeuCauDoiLich trangThai,
            LocalDate tuNgayMoi,
            LocalDate denNgayMoi
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (lichHocCuId != null) {
                predicates.add(cb.equal(root.get("lichHocCuId"), lichHocCuId));
            }

            if (giaoVienId != null) {
                predicates.add(cb.equal(root.get("giaoVienId"), giaoVienId));
            }

            if (nguoiDuyetId != null) {
                predicates.add(cb.equal(root.get("nguoiDuyetId"), nguoiDuyetId));
            }

            if (trangThai != null) {
                predicates.add(cb.equal(root.get("trangThai"), trangThai));
            }

            if (tuNgayMoi != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("ngayMoi"), tuNgayMoi));
            }

            if (denNgayMoi != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("ngayMoi"), denNgayMoi));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}