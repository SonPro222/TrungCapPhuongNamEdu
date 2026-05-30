package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhVersionNangLucRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhVersionNangLucResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersionNangLuc;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.ChuongTrinhVersionNangLucMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.*;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhVersionNangLucService;
import org.example.trungcapphuongnam.common.spec.LocJpa;

@Service
@RequiredArgsConstructor
@Transactional
public class ChuongTrinhVersionNangLucServiceImpl implements ChuongTrinhVersionNangLucService {
    private final ChuongTrinhNghiepVuValidator validator;
    private final ChuongTrinhVersionNangLucRepository repository;
    private final ChuongTrinhVersionNangLucMapper mapper;
    private final ChuongTrinhVersionRepository chuongTrinhVersionRepository;
    private final NangLucDauRaGocRepository nangLucDauRaGocRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ChuongTrinhVersionNangLucResponse> findAll(Long chuongTrinhVersionId, Long nangLucGocId, String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.<ChuongTrinhVersionNangLuc>empty()
                    .and(LocJpa.eq("chuongTrinhVersionId", chuongTrinhVersionId))
                    .and(LocJpa.eq("nangLucGocId", nangLucGocId))
                    .and(LocJpa.keyword(keyword, "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ChuongTrinhVersionNangLucResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Chuong Trinh Version Nang Luc không tồn tại: " + id));
    }

    @Override
    public ChuongTrinhVersionNangLucResponse create(ChuongTrinhVersionNangLucRequest request) {
        if (!chuongTrinhVersionRepository.existsById(request.getChuongTrinhVersionId())) {
            throw new ResourceNotFoundException("Version chương trình không tồn tại: " + request.getChuongTrinhVersionId());
        }
        if (!nangLucDauRaGocRepository.existsById(request.getNangLucGocId())) {
            throw new ResourceNotFoundException("Năng lực gốc không tồn tại: " + request.getNangLucGocId());
        }
        if (request.getChuongTrinhVersionId() != null && request.getNangLucGocId() != null && repository.existsByChuongTrinhVersionIdAndNangLucGocId(request.getChuongTrinhVersionId(), request.getNangLucGocId())) {
            throw new IllegalArgumentException("Dữ liệu đã tồn tại, không được tạo trùng.");
        }
        validator.validateChuongTrinhVersionNangLuc(request, null);
        ChuongTrinhVersionNangLuc entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public ChuongTrinhVersionNangLucResponse update(Long id, ChuongTrinhVersionNangLucRequest request) {
        ChuongTrinhVersionNangLuc entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chuong Trinh Version Nang Luc không tồn tại: " + id));
        if (!chuongTrinhVersionRepository.existsById(request.getChuongTrinhVersionId())) {
            throw new ResourceNotFoundException("Version chương trình không tồn tại: " + request.getChuongTrinhVersionId());
        }
        if (!nangLucDauRaGocRepository.existsById(request.getNangLucGocId())) {
            throw new ResourceNotFoundException("Năng lực gốc không tồn tại: " + request.getNangLucGocId());
        }
        if (request.getChuongTrinhVersionId() != null && request.getNangLucGocId() != null && repository.existsByChuongTrinhVersionIdAndNangLucGocIdAndIdNot(request.getChuongTrinhVersionId(), request.getNangLucGocId(), id)) {
            throw new IllegalArgumentException("Dữ liệu đã tồn tại, không được cập nhật trùng.");
        }
        validator.validateChuongTrinhVersionNangLuc(request, id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Chuong Trinh Version Nang Luc không tồn tại: " + id);
        }
        repository.deleteById(id);
    }


}
