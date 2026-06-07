package org.example.trungcapphuongnam.module.daoTao.service.impl;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.DuplicateResourceException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.util.TextUtil;
import org.example.trungcapphuongnam.module.daoTao.dto.DotHocRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.DotHocResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.DotHoc;
import org.example.trungcapphuongnam.module.daoTao.mapper.DotHocMapper;
import org.example.trungcapphuongnam.module.daoTao.repository.DotHocRepository;
import org.example.trungcapphuongnam.module.daoTao.repository.KhoaDaoTaoRepository;
import org.example.trungcapphuongnam.module.daoTao.repository.LopHanhChinhRepository;
import org.example.trungcapphuongnam.module.daoTao.service.DotHocService;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhVersionRepository;
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
public class DotHocServiceImpl implements DotHocService {

    private final DotHocRepository repository;
    private final KhoaDaoTaoRepository khoaDaoTaoRepository;
    private final LopHanhChinhRepository lopHanhChinhRepository;
    private final ChuongTrinhVersionRepository chuongTrinhVersionRepository;
    private final DotHocMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<DotHocResponse> findAll(String keyword, Long khoaDaoTaoId, Long lopHanhChinhId, String trangThai, Pageable pageable) {
        return repository.findAll(buildSpec(keyword, khoaDaoTaoId, lopHanhChinhId, trangThai), pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public DotHocResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Override
    public DotHocResponse create(DotHocRequest request) {
        validate(request, null);
        if (repository.existsByMaDotIgnoreCase(request.getMaDot().trim())) {
            throw new DuplicateResourceException("Mã đợt học đã tồn tại: " + request.getMaDot());
        }
        if (request.getTrangThai() == null || request.getTrangThai().isBlank()) {
            request.setTrangThai("du_kien");
        }
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    public DotHocResponse update(Long id, DotHocRequest request) {
        DotHoc entity = getEntity(id);
        validate(request, id);
        if (repository.existsByMaDotIgnoreCaseAndIdNot(request.getMaDot().trim(), id)) {
            throw new DuplicateResourceException("Mã đợt học đã tồn tại: " + request.getMaDot());
        }
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Đợt học không tồn tại: " + id);
        }
        repository.deleteById(id);
    }

    private void validate(DotHocRequest request, Long id) {
        if (request == null) {
            throw new BadRequestException("Dữ liệu đợt học không hợp lệ");
        }
        request.setMaDot(TextUtil.trimRequired(request.getMaDot()));
        request.setTenDot(TextUtil.trimRequired(request.getTenDot()));
        if (request.getTuNgay() == null || request.getDenNgay() == null) {
            throw new BadRequestException("Từ ngày và đến ngày không được để trống");
        }
        if (request.getDenNgay().isBefore(request.getTuNgay())) {
            throw new BadRequestException("Đến ngày không được trước từ ngày");
        }
        if (request.getKhoaDaoTaoId() != null && !khoaDaoTaoRepository.existsById(request.getKhoaDaoTaoId())) {
            throw new ResourceNotFoundException("Khóa đào tạo không tồn tại: " + request.getKhoaDaoTaoId());
        }
        if (request.getLopHanhChinhId() != null && !lopHanhChinhRepository.existsById(request.getLopHanhChinhId())) {
            throw new ResourceNotFoundException("Lớp hành chính không tồn tại: " + request.getLopHanhChinhId());
        }
        if (request.getChuongTrinhVersionId() != null && !chuongTrinhVersionRepository.existsById(request.getChuongTrinhVersionId())) {
            throw new ResourceNotFoundException("Phiên bản chương trình không tồn tại: " + request.getChuongTrinhVersionId());
        }
    }

    private DotHoc getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Đợt học không tồn tại: " + id));
    }

    private Specification<DotHoc> buildSpec(String keyword, Long khoaDaoTaoId, Long lopHanhChinhId, String trangThai) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (keyword != null && !keyword.isBlank()) {
                String pattern = "%" + keyword.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("maDot")), pattern),
                        cb.like(cb.lower(root.get("tenDot")), pattern)
                ));
            }
            if (khoaDaoTaoId != null) predicates.add(cb.equal(root.get("khoaDaoTaoId"), khoaDaoTaoId));
            if (lopHanhChinhId != null) predicates.add(cb.equal(root.get("lopHanhChinhId"), lopHanhChinhId));
            if (trangThai != null && !trangThai.isBlank()) predicates.add(cb.equal(root.get("trangThai"), trangThai));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
