package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.XoaChuongTrinhCascadeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NangLucDauRaGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NangLucDauRaGocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.NangLucDauRaGoc;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.NangLucDauRaGocMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.*;
import org.example.trungcapphuongnam.module.chuongTrinh.service.NangLucDauRaGocService;
import org.example.trungcapphuongnam.common.spec.LocJpa;

@Service
@RequiredArgsConstructor
@Transactional
public class NangLucDauRaGocServiceImpl implements NangLucDauRaGocService {
    private final ChuongTrinhNghiepVuValidator validator;
    private final NangLucDauRaGocRepository repository;
    private final NangLucDauRaGocMapper mapper;
    private final XoaChuongTrinhCascadeService xoaChuongTrinhCascadeService;

    @Override
    @Transactional(readOnly = true)
    public Page<NangLucDauRaGocResponse> findAll(Long chuongTrinhId, String ma, String loai, String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.<NangLucDauRaGoc>empty()
                    .and(LocJpa.eq("chuongTrinhId", chuongTrinhId))
                    .and(LocJpa.like("ma", ma))
                    .and(LocJpa.like("loai", loai))
                    .and(LocJpa.keyword(keyword, "ma", "loai", "noiDung", "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public NangLucDauRaGocResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Nang Luc Dau Ra Goc không tồn tại: " + id));
    }

    @Override
    public NangLucDauRaGocResponse create(NangLucDauRaGocRequest request) {
        validator.validateNangLucDauRaGoc(request, null);
        NangLucDauRaGoc entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public NangLucDauRaGocResponse update(Long id, NangLucDauRaGocRequest request) {
        NangLucDauRaGoc entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nang Luc Dau Ra Goc không tồn tại: " + id));
        validator.validateNangLucDauRaGoc(request, id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Nang Luc Dau Ra Goc không tồn tại: " + id);
        }

        xoaChuongTrinhCascadeService.xoaTheoNangLucGocId(id);

        repository.deleteById(id);
    }

}
