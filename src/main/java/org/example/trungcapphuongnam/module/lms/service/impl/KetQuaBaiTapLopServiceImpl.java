package org.example.trungcapphuongnam.module.lms.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.lms.dto.request.KetQuaBaiTapLopRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.KetQuaBaiTapLopResponse;
import org.example.trungcapphuongnam.module.lms.entity.KetQuaBaiTapLop;
import org.example.trungcapphuongnam.module.lms.mapper.KetQuaBaiTapLopMapper;
import org.example.trungcapphuongnam.module.lms.repository.KetQuaBaiTapLopRepository;
import org.example.trungcapphuongnam.module.lms.service.KetQuaBaiTapLopService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class KetQuaBaiTapLopServiceImpl implements KetQuaBaiTapLopService {

    private final KetQuaBaiTapLopRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<KetQuaBaiTapLopResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(KetQuaBaiTapLopMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public KetQuaBaiTapLopResponse findById(Long id) {
        return KetQuaBaiTapLopMapper.toResponse(getEntity(id));
    }

    @Override
    public KetQuaBaiTapLopResponse create(KetQuaBaiTapLopRequest request) {
        KetQuaBaiTapLop entity = KetQuaBaiTapLopMapper.toEntity(request);
        return KetQuaBaiTapLopMapper.toResponse(repository.save(entity));
    }

    @Override
    public KetQuaBaiTapLopResponse update(Long id, KetQuaBaiTapLopRequest request) {
        KetQuaBaiTapLop entity = getEntity(id);
        KetQuaBaiTapLopMapper.updateEntity(entity, request);
        return KetQuaBaiTapLopMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        KetQuaBaiTapLop entity = getEntity(id);
        repository.delete(entity);
    }

    private KetQuaBaiTapLop getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy KetQuaBaiTapLop với id = " + id));
    }
}
