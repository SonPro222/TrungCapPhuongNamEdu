package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhNghiepVuValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.MucTieuChuongTrinhGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.MucTieuChuongTrinhGocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.MucTieuChuongTrinhGoc;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.MucTieuChuongTrinhGocMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.*;
import org.example.trungcapphuongnam.module.chuongTrinh.service.MucTieuChuongTrinhGocService;
import org.example.trungcapphuongnam.common.spec.LocJpa;

@Service
@RequiredArgsConstructor
@Transactional
public class MucTieuChuongTrinhGocServiceImpl implements MucTieuChuongTrinhGocService {
    private final ChuongTrinhNghiepVuValidator validator;
    private final MucTieuChuongTrinhGocRepository repository;
    private final MucTieuChuongTrinhGocMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<MucTieuChuongTrinhGocResponse> findAll(String ma, String loai, String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.<MucTieuChuongTrinhGoc>empty()
                    .and(LocJpa.like("ma", ma))
                    .and(LocJpa.like("loai", loai))
                    .and(LocJpa.keyword(keyword, "ma", "loai", "noiDung", "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public MucTieuChuongTrinhGocResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Muc Tieu Chuong Trinh Goc không tồn tại: " + id));
    }

    @Override
    public MucTieuChuongTrinhGocResponse create(MucTieuChuongTrinhGocRequest request) {
        if (request.getMa() != null && repository.existsByMa(request.getMa())) {
            throw new IllegalArgumentException("Dữ liệu đã tồn tại, không được tạo trùng.");
        }
        validator.validateMucTieuChuongTrinhGoc(request, null);
        MucTieuChuongTrinhGoc entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public MucTieuChuongTrinhGocResponse update(Long id, MucTieuChuongTrinhGocRequest request) {
        MucTieuChuongTrinhGoc entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Muc Tieu Chuong Trinh Goc không tồn tại: " + id));
        if (request.getMa() != null && repository.existsByMaAndIdNot(request.getMa(), id)) {
            throw new IllegalArgumentException("Dữ liệu đã tồn tại, không được cập nhật trùng.");
        }
        validator.validateMucTieuChuongTrinhGoc(request, id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Muc Tieu Chuong Trinh Goc không tồn tại: " + id);
        }
        repository.deleteById(id);
    }

}
