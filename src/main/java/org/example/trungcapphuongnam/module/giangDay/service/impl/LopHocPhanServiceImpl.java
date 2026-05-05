package org.example.trungcapphuongnam.module.giangDay.service.impl;

import org.example.trungcapphuongnam.module.giangDay.dto.request.LopHocPhanRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LopHocPhanResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.exception.GiangDayNotFoundException;
import org.example.trungcapphuongnam.module.giangDay.mapper.LopHocPhanMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.LopHocPhanRepository;
import org.example.trungcapphuongnam.module.giangDay.service.LopHocPhanService;
import org.example.trungcapphuongnam.common.util.GiangDayValidateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LopHocPhanServiceImpl implements LopHocPhanService {

    private final LopHocPhanRepository repository;
    private final LopHocPhanMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<LopHocPhanResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LopHocPhanResponse getById(Long id) {
        return mapper.toResponse(findEntity(id));
    }

    @Override
    public LopHocPhanResponse create(LopHocPhanRequest request) {
        validate(request);
        LopHocPhan entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public LopHocPhanResponse update(Long id, LopHocPhanRequest request) {
        validate(request);
        LopHocPhan entity = findEntity(id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        LopHocPhan entity = findEntity(id);
        repository.delete(entity);
    }

    private LopHocPhan findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GiangDayNotFoundException("LopHocPhan không tồn tại với id = " + id));
    }

    private void validate(LopHocPhanRequest request) {
        GiangDayValidateUtil.requireEndDateNotBeforeStart(request.getNgayBatDau(), request.getNgayKetThuc(), "Ngày kết thúc không được trước ngày bắt đầu");
        GiangDayValidateUtil.requireNonNegative(request.getSoLuongToiDa(), "Số lượng tối đa");
        GiangDayValidateUtil.requireNonNegative(request.getSoLuongHienTai(), "Số lượng hiện tại");
    }
}
