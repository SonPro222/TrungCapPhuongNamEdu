package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhNghiepVuValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhVersionMucTieuRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhVersionMucTieuResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersionMucTieu;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.ChuongTrinhVersionMucTieuMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.*;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhVersionMucTieuService;
import org.example.trungcapphuongnam.common.spec.LocJpa;

@Service
@RequiredArgsConstructor
@Transactional
public class ChuongTrinhVersionMucTieuServiceImpl implements ChuongTrinhVersionMucTieuService {
    private final ChuongTrinhNghiepVuValidator validator;
    private final ChuongTrinhVersionMucTieuRepository repository;
    private final ChuongTrinhVersionMucTieuMapper mapper;
    private final ChuongTrinhVersionRepository chuongTrinhVersionRepository;
    private final MucTieuChuongTrinhGocRepository mucTieuChuongTrinhGocRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ChuongTrinhVersionMucTieuResponse> findAll(Long chuongTrinhVersionId, Long mucTieuGocId, String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.<ChuongTrinhVersionMucTieu>empty()
                    .and(LocJpa.eq("chuongTrinhVersionId", chuongTrinhVersionId))
                    .and(LocJpa.eq("mucTieuGocId", mucTieuGocId))
                    .and(LocJpa.keyword(keyword, "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ChuongTrinhVersionMucTieuResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Chuong Trinh Version Muc Tieu không tồn tại: " + id));
    }

    @Override
    public ChuongTrinhVersionMucTieuResponse create(ChuongTrinhVersionMucTieuRequest request) {
        if (!chuongTrinhVersionRepository.existsById(request.getChuongTrinhVersionId())) {
            throw new ResourceNotFoundException("Version chương trình không tồn tại: " + request.getChuongTrinhVersionId());
        }
        if (!mucTieuChuongTrinhGocRepository.existsById(request.getMucTieuGocId())) {
            throw new ResourceNotFoundException("Mục tiêu gốc không tồn tại: " + request.getMucTieuGocId());
        }
        if (request.getChuongTrinhVersionId() != null && request.getMucTieuGocId() != null && repository.existsByChuongTrinhVersionIdAndMucTieuGocId(request.getChuongTrinhVersionId(), request.getMucTieuGocId())) {
            throw new IllegalArgumentException("Dữ liệu đã tồn tại, không được tạo trùng.");
        }
        validator.validateChuongTrinhVersionMucTieu(request, null);
        ChuongTrinhVersionMucTieu entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public ChuongTrinhVersionMucTieuResponse update(Long id, ChuongTrinhVersionMucTieuRequest request) {
        ChuongTrinhVersionMucTieu entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chuong Trinh Version Muc Tieu không tồn tại: " + id));
        if (!chuongTrinhVersionRepository.existsById(request.getChuongTrinhVersionId())) {
            throw new ResourceNotFoundException("Version chương trình không tồn tại: " + request.getChuongTrinhVersionId());
        }
        if (!mucTieuChuongTrinhGocRepository.existsById(request.getMucTieuGocId())) {
            throw new ResourceNotFoundException("Mục tiêu gốc không tồn tại: " + request.getMucTieuGocId());
        }
        if (request.getChuongTrinhVersionId() != null && request.getMucTieuGocId() != null && repository.existsByChuongTrinhVersionIdAndMucTieuGocIdAndIdNot(request.getChuongTrinhVersionId(), request.getMucTieuGocId(), id)) {
            throw new IllegalArgumentException("Dữ liệu đã tồn tại, không được cập nhật trùng.");
        }
        validator.validateChuongTrinhVersionMucTieu(request, id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Chuong Trinh Version Muc Tieu không tồn tại: " + id);
        }
        repository.deleteById(id);
    }


}
