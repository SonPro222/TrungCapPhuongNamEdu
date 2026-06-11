package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.XoaChuongTrinhCascadeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.DieuKienTotNghiepGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.DieuKienTotNghiepGocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.DieuKienTotNghiepMau;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.DieuKienTotNghiepMauMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.*;
import org.example.trungcapphuongnam.module.chuongTrinh.service.DieuKienTotNghiepGocService;
import org.example.trungcapphuongnam.common.spec.LocJpa;

@Service
@RequiredArgsConstructor
@Transactional
public class DieuKienTotNghiepGocServiceImpl implements DieuKienTotNghiepGocService {
    private final ChuongTrinhNghiepVuValidator validator;
    private final DieuKienTotNghiepMauRepository repository;
    private final DieuKienTotNghiepMauMapper mapper;
    private final XoaChuongTrinhCascadeService xoaChuongTrinhCascadeService;

    @Override
    @Transactional(readOnly = true)
    public Page<DieuKienTotNghiepGocResponse> findAll(String ma, String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.<DieuKienTotNghiepMau>empty()
                    .and(LocJpa.like("ma", ma))
                    .and(LocJpa.keyword(keyword, "ma", "noiDung", "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public DieuKienTotNghiepGocResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Dieu Kien Tot Nghiep Goc không tồn tại: " + id));
    }

    @Override
    public DieuKienTotNghiepGocResponse create(DieuKienTotNghiepGocRequest request) {
        if (request.getMa() != null && repository.existsByMa(request.getMa())) {
            throw new IllegalArgumentException("Dữ liệu đã tồn tại, không được tạo trùng.");
        }
        validator.validateDieuKienTotNghiepGoc(request, null);
        DieuKienTotNghiepMau entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public DieuKienTotNghiepGocResponse update(Long id, DieuKienTotNghiepGocRequest request) {
        DieuKienTotNghiepMau entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dieu Kien Tot Nghiep Goc không tồn tại: " + id));
        if (request.getMa() != null && repository.existsByMaAndIdNot(request.getMa(), id)) {
            throw new IllegalArgumentException("Dữ liệu đã tồn tại, không được cập nhật trùng.");
        }
        validator.validateDieuKienTotNghiepGoc(request, id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Dieu Kien Tot Nghiep Goc không tồn tại: " + id);
        }

        xoaChuongTrinhCascadeService.xoaTheoDieuKienTotNghiepGocId(id);

        repository.deleteById(id);
    }

}
