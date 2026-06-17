package org.example.trungcapphuongnam.module.diem.service.impl;


import lombok.RequiredArgsConstructor;

import org.example.trungcapphuongnam.module.diem.enums.TrangThaiBaiNop;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.DuplicateResourceException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.diem.dto.request.BaiNopSinhVienRequest;
import org.example.trungcapphuongnam.module.diem.dto.response.BaiNopSinhVienResponse;
import org.example.trungcapphuongnam.module.diem.entity.BaiNopSinhVien;
import org.example.trungcapphuongnam.module.diem.mapper.BaiNopSinhVienMapper;
import org.example.trungcapphuongnam.module.diem.repository.BaiNopSinhVienRepository;
import org.example.trungcapphuongnam.module.diem.service.BaiNopSinhVienService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class BaiNopSinhVienServiceImpl implements BaiNopSinhVienService {

    private final BaiNopSinhVienRepository repository;
    private final BaiNopSinhVienMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<BaiNopSinhVienResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public BaiNopSinhVienResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Override
    public BaiNopSinhVienResponse create(BaiNopSinhVienRequest request) {
        validate(request);
        if (repository.existsByBaiKiemTraIdAndSinhVienId(request.getBaiKiemTraId(), request.getSinhVienId())) {
            throw new DuplicateResourceException("Sinh viên đã có bài nộp cho bài kiểm tra này");
        }
        BaiNopSinhVien entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public BaiNopSinhVienResponse update(Long id, BaiNopSinhVienRequest request) {
        BaiNopSinhVien entity = getEntity(id);
        validate(request);
        if (repository.existsByBaiKiemTraIdAndSinhVienIdAndIdNot(request.getBaiKiemTraId(), request.getSinhVienId(), id)) {
            throw new DuplicateResourceException("Sinh viên đã có bài nộp cho bài kiểm tra này");
        }
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("BaiNopSinhVien không tồn tại: " + id);
        }
        repository.deleteById(id);
    }

    private BaiNopSinhVien getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BaiNopSinhVien không tồn tại: " + id));
    }


    private void validate(BaiNopSinhVienRequest request) {
        if (request.getTrangThai() != null && !request.getTrangThai().isBlank()
                && !isValidEnum(TrangThaiBaiNop.class, request.getTrangThai())) {
            throw new BadRequestException("Trạng thái bài nộp không hợp lệ: " + request.getTrangThai());
        }
    }


    private <E extends Enum<E>> boolean isValidEnum(Class<E> enumClass, String value) {
        if (value == null) return false;
        for (E e : enumClass.getEnumConstants()) {
            if (e.name().equals(value)) return true;
        }
        return false;
    }
}
