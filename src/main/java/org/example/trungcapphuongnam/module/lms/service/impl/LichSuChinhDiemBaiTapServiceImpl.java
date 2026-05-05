package org.example.trungcapphuongnam.module.lms.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.lms.dto.request.LichSuChinhDiemBaiTapRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.LichSuChinhDiemBaiTapResponse;
import org.example.trungcapphuongnam.module.lms.entity.LichSuChinhDiemBaiTap;
import org.example.trungcapphuongnam.module.lms.mapper.LichSuChinhDiemBaiTapMapper;
import org.example.trungcapphuongnam.module.lms.repository.LichSuChinhDiemBaiTapRepository;
import org.example.trungcapphuongnam.module.lms.service.LichSuChinhDiemBaiTapService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LichSuChinhDiemBaiTapServiceImpl implements LichSuChinhDiemBaiTapService {

    private final LichSuChinhDiemBaiTapRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<LichSuChinhDiemBaiTapResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(LichSuChinhDiemBaiTapMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public LichSuChinhDiemBaiTapResponse findById(Long id) {
        return LichSuChinhDiemBaiTapMapper.toResponse(getEntity(id));
    }

    @Override
    public LichSuChinhDiemBaiTapResponse create(LichSuChinhDiemBaiTapRequest request) {
        LichSuChinhDiemBaiTap entity = LichSuChinhDiemBaiTapMapper.toEntity(request);
        return LichSuChinhDiemBaiTapMapper.toResponse(repository.save(entity));
    }

    @Override
    public LichSuChinhDiemBaiTapResponse update(Long id, LichSuChinhDiemBaiTapRequest request) {
        LichSuChinhDiemBaiTap entity = getEntity(id);
        LichSuChinhDiemBaiTapMapper.updateEntity(entity, request);
        return LichSuChinhDiemBaiTapMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        LichSuChinhDiemBaiTap entity = getEntity(id);
        repository.delete(entity);
    }

    private LichSuChinhDiemBaiTap getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy LichSuChinhDiemBaiTap với id = " + id));
    }
}
