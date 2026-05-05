package org.example.trungcapphuongnam.module.giangDay.service.impl;

import org.example.trungcapphuongnam.module.giangDay.dto.request.SinhVienLopHocPhanRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.SinhVienLopHocPhanResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.SinhVienLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.exception.GiangDayNotFoundException;
import org.example.trungcapphuongnam.module.giangDay.mapper.SinhVienLopHocPhanMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.SinhVienLopHocPhanRepository;
import org.example.trungcapphuongnam.module.giangDay.service.SinhVienLopHocPhanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SinhVienLopHocPhanServiceImpl implements SinhVienLopHocPhanService {

    private final SinhVienLopHocPhanRepository repository;
    private final SinhVienLopHocPhanMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<SinhVienLopHocPhanResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SinhVienLopHocPhanResponse getById(Long id) {
        return mapper.toResponse(findEntity(id));
    }

    @Override
    public SinhVienLopHocPhanResponse create(SinhVienLopHocPhanRequest request) {
        validate(request);
        SinhVienLopHocPhan entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public SinhVienLopHocPhanResponse update(Long id, SinhVienLopHocPhanRequest request) {
        validate(request);
        SinhVienLopHocPhan entity = findEntity(id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        SinhVienLopHocPhan entity = findEntity(id);
        repository.delete(entity);
    }

    private SinhVienLopHocPhan findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GiangDayNotFoundException("SinhVienLopHocPhan không tồn tại với id = " + id));
    }

    private void validate(SinhVienLopHocPhanRequest request) {
        // Không có rule validate đặc biệt.
    }
}
