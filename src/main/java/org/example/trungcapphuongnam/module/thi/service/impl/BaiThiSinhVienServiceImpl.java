package org.example.trungcapphuongnam.module.thi.service.impl;

import org.example.trungcapphuongnam.module.thi.dto.request.BaiThiSinhVienRequest;
import org.example.trungcapphuongnam.module.thi.dto.response.BaiThiSinhVienResponse;
import org.example.trungcapphuongnam.module.thi.entity.BaiThiSinhVien;
import org.example.trungcapphuongnam.common.exception.ThiException;
import org.example.trungcapphuongnam.common.exception.ThiNotFoundException;
import org.example.trungcapphuongnam.module.thi.mapper.BaiThiSinhVienMapper;
import org.example.trungcapphuongnam.module.thi.repository.BaiThiSinhVienRepository;
import org.example.trungcapphuongnam.module.thi.service.BaiThiSinhVienService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BaiThiSinhVienServiceImpl implements BaiThiSinhVienService {
    private final BaiThiSinhVienRepository repository;
    private final BaiThiSinhVienMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<BaiThiSinhVienResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public BaiThiSinhVienResponse findById(Long id) {
        return mapper.toResponse(getById(id));
    }

    @Override
    public BaiThiSinhVienResponse create(BaiThiSinhVienRequest request) {
        if (repository.existsByDeThiIdAndSinhVienId(request.getDeThiId(), request.getSinhVienId())) {
            throw new ThiException("Sinh viên đã có bài thi cho đề thi này");
        }
        if (request.getThoiGianNop() != null && request.getThoiGianBatDau() != null && request.getThoiGianNop().isBefore(request.getThoiGianBatDau())) {
            throw new ThiException("Thời gian nộp không được nhỏ hơn thời gian bắt đầu");
        }
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    public BaiThiSinhVienResponse update(Long id, BaiThiSinhVienRequest request) {
        if (request.getThoiGianNop() != null && request.getThoiGianBatDau() != null && request.getThoiGianNop().isBefore(request.getThoiGianBatDau())) {
            throw new ThiException("Thời gian nộp không được nhỏ hơn thời gian bắt đầu");
        }
        BaiThiSinhVien entity = getById(id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.delete(getById(id));
    }

    private BaiThiSinhVien getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ThiNotFoundException("BaiThiSinhVien", id));
    }
}
