package org.example.trungcapphuongnam.module.lms.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.lms.dto.request.NganHangCauHoiRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.NganHangCauHoiResponse;
import org.example.trungcapphuongnam.module.lms.entity.NganHangCauHoi;
import org.example.trungcapphuongnam.module.lms.mapper.NganHangCauHoiMapper;
import org.example.trungcapphuongnam.module.lms.repository.NganHangCauHoiRepository;
import org.example.trungcapphuongnam.module.lms.service.NganHangCauHoiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NganHangCauHoiServiceImpl implements NganHangCauHoiService {

    private final NganHangCauHoiRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<NganHangCauHoiResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(NganHangCauHoiMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public NganHangCauHoiResponse findById(Long id) {
        return NganHangCauHoiMapper.toResponse(getEntity(id));
    }

    @Override
    public NganHangCauHoiResponse create(NganHangCauHoiRequest request) {
        NganHangCauHoi entity = NganHangCauHoiMapper.toEntity(request);
        return NganHangCauHoiMapper.toResponse(repository.save(entity));
    }

    @Override
    public NganHangCauHoiResponse update(Long id, NganHangCauHoiRequest request) {
        NganHangCauHoi entity = getEntity(id);
        NganHangCauHoiMapper.updateEntity(entity, request);
        return NganHangCauHoiMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        NganHangCauHoi entity = getEntity(id);
        repository.delete(entity);
    }

    private NganHangCauHoi getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy NganHangCauHoi với id = " + id));
    }
}
