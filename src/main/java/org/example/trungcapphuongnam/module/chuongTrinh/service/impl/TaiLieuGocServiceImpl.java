package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.DuplicateResourceException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.TaiLieuGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.TaiLieuGocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.TaiLieuGoc;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.TaiLieuGocMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.TaiLieuGocRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.TaiLieuGocService;
import org.example.trungcapphuongnam.module.chuongTrinh.service.XoaChuongTrinhCascadeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class TaiLieuGocServiceImpl implements TaiLieuGocService {
    private final ChuongTrinhNghiepVuValidator validator;
    private final TaiLieuGocRepository repository;
    private final TaiLieuGocMapper mapper;
    private final XoaChuongTrinhCascadeService xoaChuongTrinhCascadeService;

    @Override
    @Transactional(readOnly = true)
    public Page<TaiLieuGocResponse> findAll(String ma, String loai, String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.<TaiLieuGoc>empty()
                        .and(LocJpa.like("ma", ma))
                        .and(LocJpa.like("loai", loai))
                        .and(LocJpa.keyword(keyword, "ma", "ten", "loai", "tacGia", "nhaXuatBan", "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public TaiLieuGocResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Tài liệu gốc không tồn tại: " + id));
    }

    @Override
    public TaiLieuGocResponse create(TaiLieuGocRequest request) {
        validator.validateTaiLieuGoc(request, null);
        TaiLieuGoc entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public TaiLieuGocResponse update(Long id, TaiLieuGocRequest request) {
        TaiLieuGoc entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tài liệu gốc không tồn tại: " + id));

        validator.validateTaiLieuGoc(request, id);

        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Tài liệu gốc không tồn tại: " + id);
        }

        xoaChuongTrinhCascadeService.xoaTheoTaiLieuGocId(id);

        repository.deleteById(id);
    }

}