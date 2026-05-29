package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.XoaChuongTrinhCascadeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.QuyDoiDiemMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.QuyDoiDiemMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.QuyDoiDiemMau;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.QuyDoiDiemMauMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.*;
import org.example.trungcapphuongnam.module.chuongTrinh.service.QuyDoiDiemMauService;
import org.example.trungcapphuongnam.common.spec.LocJpa;

@Service
@RequiredArgsConstructor
@Transactional
public class QuyDoiDiemMauServiceImpl implements QuyDoiDiemMauService {
    private final ChuongTrinhNghiepVuValidator validator;
    private final QuyDoiDiemMauRepository repository;
    private final QuyDoiDiemMauMapper mapper;
    private final XoaChuongTrinhCascadeService xoaChuongTrinhCascadeService;

    @Override
    @Transactional(readOnly = true)
    public Page<QuyDoiDiemMauResponse> findAll(String ma, String ketQua, String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.<QuyDoiDiemMau>empty()
                    .and(LocJpa.like("ma", ma))
                    .and(LocJpa.like("ketQua", ketQua))
                    .and(LocJpa.keyword(keyword, "ma", "ten", "ketQua", "congThuc", "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public QuyDoiDiemMauResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Quy Doi Diem Mau không tồn tại: " + id));
    }

    @Override
    public QuyDoiDiemMauResponse create(QuyDoiDiemMauRequest request) {
        if (request.getMa() != null && repository.existsByMa(request.getMa())) {
            throw new IllegalArgumentException("Dữ liệu đã tồn tại, không được tạo trùng.");
        }
        validator.validateQuyDoiDiemMau(request, null);
        QuyDoiDiemMau entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public QuyDoiDiemMauResponse update(Long id, QuyDoiDiemMauRequest request) {
        QuyDoiDiemMau entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quy Doi Diem Mau không tồn tại: " + id));
        if (request.getMa() != null && repository.existsByMaAndIdNot(request.getMa(), id)) {
            throw new IllegalArgumentException("Dữ liệu đã tồn tại, không được cập nhật trùng.");
        }
        validator.validateQuyDoiDiemMau(request, id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Quy Doi Diem Mau không tồn tại: " + id);
        }

        xoaChuongTrinhCascadeService.xoaTheoQuyDoiDiemMauId(id);

        repository.deleteById(id);
    }

}
