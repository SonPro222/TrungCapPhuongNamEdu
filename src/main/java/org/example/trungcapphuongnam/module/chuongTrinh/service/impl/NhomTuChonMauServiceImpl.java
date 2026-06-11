package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NhomTuChonGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NhomTuChonGocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.NhomTuChonMau;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.NhomTuChonMauMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.NhomTuChonMauRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.NhomTuChonGocService;
import org.example.trungcapphuongnam.module.chuongTrinh.service.XoaChuongTrinhCascadeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NhomTuChonGocServiceImpl implements NhomTuChonGocService {

    private final NhomTuChonMauRepository repository;
    private final NhomTuChonMauMapper mapper;
    private final ChuongTrinhNghiepVuValidator validator;
    private final XoaChuongTrinhCascadeService xoaChuongTrinhCascadeService;

    @Override
    @Transactional(readOnly = true)
    public Page<NhomTuChonGocResponse> findAll(String ma, String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.<NhomTuChonMau>empty()
                        .and(LocJpa.like("ma", ma))
                        .and(LocJpa.keyword(keyword, "ma", "ten", "moTa")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public NhomTuChonGocResponse findById(Long id) {
        NhomTuChonMau entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nhóm tự chọn gốc không tồn tại: " + id));

        return mapper.toResponse(entity);
    }

    @Override
    public NhomTuChonGocResponse create(NhomTuChonGocRequest request) {
        validator.validateNhomTuChonGoc(request, null);

        NhomTuChonMau entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public NhomTuChonGocResponse update(Long id, NhomTuChonGocRequest request) {
        NhomTuChonMau entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nhóm tự chọn gốc không tồn tại: " + id));

        validator.validateNhomTuChonGoc(request, id);

        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Nhóm tự chọn gốc không tồn tại: " + id);
        }

        xoaChuongTrinhCascadeService.xoaTheoNhomTuChonGocId(id);

        repository.deleteById(id);
    }
}