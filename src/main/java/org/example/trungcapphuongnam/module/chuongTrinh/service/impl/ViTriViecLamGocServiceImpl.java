package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.XoaChuongTrinhCascadeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ViTriViecLamGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ViTriViecLamGocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ViTriViecLamGoc;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.ViTriViecLamGocMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.*;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ViTriViecLamGocService;
import org.example.trungcapphuongnam.common.spec.LocJpa;

@Service
@RequiredArgsConstructor
@Transactional
public class ViTriViecLamGocServiceImpl implements ViTriViecLamGocService {
    private final ChuongTrinhNghiepVuValidator validator;
    private final ViTriViecLamGocRepository repository;
    private final ViTriViecLamGocMapper mapper;
    private final XoaChuongTrinhCascadeService xoaChuongTrinhCascadeService;

    @Override
    @Transactional(readOnly = true)
    public Page<ViTriViecLamGocResponse> findAll(String ma, String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.<ViTriViecLamGoc>empty()
                    .and(LocJpa.like("ma", ma))
                    .and(LocJpa.keyword(keyword, "ma", "ten", "moTa", "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ViTriViecLamGocResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Vi Tri Viec Lam Goc không tồn tại: " + id));
    }

    @Override
    public ViTriViecLamGocResponse create(ViTriViecLamGocRequest request) {
        if (request.getMa() != null && repository.existsByMa(request.getMa())) {
            throw new IllegalArgumentException("Dữ liệu đã tồn tại, không được tạo trùng.");
        }
        validator.validateViTriViecLamGoc(request, null);
        ViTriViecLamGoc entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public ViTriViecLamGocResponse update(Long id, ViTriViecLamGocRequest request) {
        ViTriViecLamGoc entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vi Tri Viec Lam Goc không tồn tại: " + id));
        if (request.getMa() != null && repository.existsByMaAndIdNot(request.getMa(), id)) {
            throw new IllegalArgumentException("Dữ liệu đã tồn tại, không được cập nhật trùng.");
        }
        validator.validateViTriViecLamGoc(request, id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Vi Tri Viec Lam Goc không tồn tại: " + id);
        }

        xoaChuongTrinhCascadeService.xoaTheoViTriGocId(id);

        repository.deleteById(id);
    }

}
