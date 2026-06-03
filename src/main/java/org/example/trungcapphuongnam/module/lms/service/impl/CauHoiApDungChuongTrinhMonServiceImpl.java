package org.example.trungcapphuongnam.module.lms.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.lms.dto.request.CauHoiApDungChuongTrinhMonRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.CauHoiApDungChuongTrinhMonResponse;
import org.example.trungcapphuongnam.module.lms.entity.CauHoiApDungChuongTrinhMon;
import org.example.trungcapphuongnam.module.lms.mapper.CauHoiApDungChuongTrinhMonMapper;
import org.example.trungcapphuongnam.module.lms.repository.CauHoiApDungChuongTrinhMonRepository;
import org.example.trungcapphuongnam.module.lms.service.CauHoiApDungChuongTrinhMonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CauHoiApDungChuongTrinhMonServiceImpl implements CauHoiApDungChuongTrinhMonService {

    private final CauHoiApDungChuongTrinhMonRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<CauHoiApDungChuongTrinhMonResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(CauHoiApDungChuongTrinhMonMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CauHoiApDungChuongTrinhMonResponse findById(Long id) {
        return CauHoiApDungChuongTrinhMonMapper.toResponse(getEntity(id));
    }

    @Override
    public CauHoiApDungChuongTrinhMonResponse create(CauHoiApDungChuongTrinhMonRequest request) {
        CauHoiApDungChuongTrinhMon entity = CauHoiApDungChuongTrinhMonMapper.toEntity(request);
        return CauHoiApDungChuongTrinhMonMapper.toResponse(repository.save(entity));
    }

    @Override
    public CauHoiApDungChuongTrinhMonResponse update(Long id, CauHoiApDungChuongTrinhMonRequest request) {
        CauHoiApDungChuongTrinhMon entity = getEntity(id);
        CauHoiApDungChuongTrinhMonMapper.updateEntity(entity, request);
        return CauHoiApDungChuongTrinhMonMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        CauHoiApDungChuongTrinhMon entity = getEntity(id);
        repository.delete(entity);
    }

    private CauHoiApDungChuongTrinhMon getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy CauHoiApDungChuongTrinhMon với id = " + id));
    }
}
