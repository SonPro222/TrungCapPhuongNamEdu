package org.example.trungcapphuongnam.module.heThong.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.heThong.dto.request.QuyenRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.QuyenResponse;
import org.example.trungcapphuongnam.module.heThong.entity.Quyen;
import org.example.trungcapphuongnam.module.heThong.exception.HeThongNotFoundException;
import org.example.trungcapphuongnam.module.heThong.mapper.QuyenMapper;
import org.example.trungcapphuongnam.module.heThong.repository.QuyenRepository;
import org.example.trungcapphuongnam.module.heThong.service.QuyenService;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuyenServiceImpl implements QuyenService {
    private final QuyenRepository quyenRepository;
    private final QuyenMapper quyenMapper;

    @Override
    @Transactional(readOnly = true)
    public List<QuyenResponse> getAll() { return quyenRepository.findAll().stream().map(quyenMapper::toResponse).toList(); }

    @Override
    @Transactional(readOnly = true)
    public Page<QuyenResponse> search(String keyword, Pageable pageable) {
        Specification<Quyen> spec = Specification.where(null);
        if (keyword != null && !keyword.isBlank()) {
            String value = "%" + keyword.trim().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(cb.like(cb.lower(root.get("maQuyen")), value), cb.like(cb.lower(root.get("tenQuyen")), value), cb.like(cb.lower(root.get("moTa")), value)));
        }
        return quyenRepository.findAll(spec, pageable).map(quyenMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public QuyenResponse getById(Long id) { return quyenMapper.toResponse(findByIdOrThrow(id)); }
    @Override
    public QuyenResponse create(QuyenRequest request) { return quyenMapper.toResponse(quyenRepository.save(quyenMapper.toEntity(request))); }
    @Override
    public QuyenResponse update(Long id, QuyenRequest request) { Quyen entity = findByIdOrThrow(id); quyenMapper.updateEntity(entity, request); return quyenMapper.toResponse(quyenRepository.save(entity)); }
    @Override
    public void delete(Long id) { quyenRepository.delete(findByIdOrThrow(id)); }
    private Quyen findByIdOrThrow(Long id) { return quyenRepository.findById(id).orElseThrow(() -> new HeThongNotFoundException("Không tìm thấy quyền với id = " + id)); }
}
