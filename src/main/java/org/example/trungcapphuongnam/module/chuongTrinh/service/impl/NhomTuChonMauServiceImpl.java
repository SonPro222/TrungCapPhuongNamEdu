package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NhomTuChonMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NhomTuChonMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.NhomTuChonMau;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.NhomTuChonMauMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.NhomTuChonMauRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.NhomTuChonMauService;
import org.example.trungcapphuongnam.module.chuongTrinh.service.XoaChuongTrinhCascadeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NhomTuChonMauServiceImpl implements NhomTuChonMauService {

    private final NhomTuChonMauRepository repository;
    private final NhomTuChonMauMapper mapper;
    private final ChuongTrinhNghiepVuValidator validator;
    private final XoaChuongTrinhCascadeService xoaChuongTrinhCascadeService;

    @Override
    @Transactional(readOnly = true)
    public Page<NhomTuChonMauResponse> findAll(String ma, String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.<NhomTuChonMau>empty()
                        .and(LocJpa.like("ma", ma))
                        .and(LocJpa.keyword(keyword, "ma", "ten", "moTa")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public NhomTuChonMauResponse findById(Long id) {
        NhomTuChonMau entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nhóm tự chọn gốc không tồn tại: " + id));

        return mapper.toResponse(entity);
    }

    @Override
    public NhomTuChonMauResponse create(NhomTuChonMauRequest request) {
        validator.validateNhomTuChonMau(request, null);

        NhomTuChonMau entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public NhomTuChonMauResponse update(Long id, NhomTuChonMauRequest request) {
        NhomTuChonMau entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nhóm tự chọn gốc không tồn tại: " + id));

        validator.validateNhomTuChonMau(request, id);

        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Nhóm tự chọn gốc không tồn tại: " + id);
        }

        xoaChuongTrinhCascadeService.xoaTheoNhomTuChonMauId(id);

        repository.deleteById(id);
    }
}