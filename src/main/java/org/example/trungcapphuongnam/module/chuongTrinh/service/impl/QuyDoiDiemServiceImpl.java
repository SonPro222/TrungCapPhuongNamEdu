package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.QuyDoiDiemRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.QuyDoiDiemResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.QuyDoiDiem;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.QuyDoiDiemMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.QuyDoiDiemRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.QuyDoiDiemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.common.spec.LocJpa;

@Service
@RequiredArgsConstructor
@Transactional
public class QuyDoiDiemServiceImpl implements QuyDoiDiemService {
    private final ChuongTrinhNghiepVuValidator validator;
    private final QuyDoiDiemRepository repository;
    private final QuyDoiDiemMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<QuyDoiDiemResponse> findAll(Long chuongTrinhMonId, String ketQua, String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.<QuyDoiDiem>empty()
                    .and(LocJpa.eq("chuongTrinhMonId", chuongTrinhMonId))
                    .and(LocJpa.like("ketQua", ketQua))
                    .and(LocJpa.keyword(keyword, "ketQua", "congThuc", "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public QuyDoiDiemResponse findById(Long id) {
        QuyDoiDiem entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuyDoiDiem không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public QuyDoiDiemResponse create(QuyDoiDiemRequest request) {
        validator.validateQuyDoiDiem(request, null);
        QuyDoiDiem entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public QuyDoiDiemResponse update(Long id, QuyDoiDiemRequest request) {
        QuyDoiDiem entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuyDoiDiem không tồn tại: " + id));
        validator.validateQuyDoiDiem(request, id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("QuyDoiDiem không tồn tại: " + id);
        }
        repository.deleteById(id);
    }
}
