package org.example.trungcapphuongnam.module.lms.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.lms.dto.request.CauTraLoiSinhVienRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.CauTraLoiSinhVienResponse;
import org.example.trungcapphuongnam.module.lms.entity.CauTraLoiSinhVien;
import org.example.trungcapphuongnam.module.lms.mapper.CauTraLoiSinhVienMapper;
import org.example.trungcapphuongnam.module.lms.repository.CauTraLoiSinhVienRepository;
import org.example.trungcapphuongnam.module.lms.service.CauTraLoiSinhVienService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CauTraLoiSinhVienServiceImpl implements CauTraLoiSinhVienService {

    private final CauTraLoiSinhVienRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<CauTraLoiSinhVienResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(CauTraLoiSinhVienMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CauTraLoiSinhVienResponse findById(Long id) {
        return CauTraLoiSinhVienMapper.toResponse(getEntity(id));
    }

    @Override
    public CauTraLoiSinhVienResponse create(CauTraLoiSinhVienRequest request) {
        CauTraLoiSinhVien entity = CauTraLoiSinhVienMapper.toEntity(request);
        return CauTraLoiSinhVienMapper.toResponse(repository.save(entity));
    }

    @Override
    public CauTraLoiSinhVienResponse update(Long id, CauTraLoiSinhVienRequest request) {
        CauTraLoiSinhVien entity = getEntity(id);
        CauTraLoiSinhVienMapper.updateEntity(entity, request);
        return CauTraLoiSinhVienMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        CauTraLoiSinhVien entity = getEntity(id);
        repository.delete(entity);
    }

    private CauTraLoiSinhVien getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy CauTraLoiSinhVien với id = " + id));
    }
}
