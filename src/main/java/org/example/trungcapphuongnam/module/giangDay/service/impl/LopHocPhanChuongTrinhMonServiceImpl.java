package org.example.trungcapphuongnam.module.giangDay.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.giangDay.GiangDayNotFoundException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.LopHocPhanChuongTrinhMonRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LopHocPhanChuongTrinhMonResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhanChuongTrinhMon;
import org.example.trungcapphuongnam.module.giangDay.mapper.LopHocPhanChuongTrinhMonMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.LopHocPhanChuongTrinhMonRepository;
import org.example.trungcapphuongnam.module.giangDay.service.LopHocPhanChuongTrinhMonService;
import org.example.trungcapphuongnam.module.giangDay.validator.LopHocPhanChuongTrinhMonValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LopHocPhanChuongTrinhMonServiceImpl implements LopHocPhanChuongTrinhMonService {

    private final LopHocPhanChuongTrinhMonRepository repository;
    private final LopHocPhanChuongTrinhMonMapper mapper;
    private final LopHocPhanChuongTrinhMonValidator validator;

    @Override
    @Transactional(readOnly = true)
    public Page<LopHocPhanChuongTrinhMonResponse> getAll(
            Long lopHocPhanId,
            Long chuongTrinhMonId,
            Pageable pageable
    ) {
        if (lopHocPhanId != null && chuongTrinhMonId != null) {
            return repository.findByLopHocPhanIdAndChuongTrinhMonId(lopHocPhanId, chuongTrinhMonId, pageable)
                    .map(mapper::toResponse);
        }

        if (lopHocPhanId != null) {
            return repository.findByLopHocPhanId(lopHocPhanId, pageable)
                    .map(mapper::toResponse);
        }

        if (chuongTrinhMonId != null) {
            return repository.findByChuongTrinhMonId(chuongTrinhMonId, pageable)
                    .map(mapper::toResponse);
        }

        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public LopHocPhanChuongTrinhMonResponse getById(Long id) {
        return mapper.toResponse(findEntity(id));
    }

    @Override
    public LopHocPhanChuongTrinhMonResponse create(LopHocPhanChuongTrinhMonRequest request) {
        validator.validateCreate(request);

        LopHocPhanChuongTrinhMon entity = mapper.toEntity(request);

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public LopHocPhanChuongTrinhMonResponse update(Long id, LopHocPhanChuongTrinhMonRequest request) {
        validator.validateUpdate(id, request);

        LopHocPhanChuongTrinhMon entity = findEntity(id);
        mapper.updateEntity(entity, request);

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.delete(findEntity(id));
    }

    private LopHocPhanChuongTrinhMon findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GiangDayNotFoundException("Không tìm thấy bản ghi gắn chương trình môn với lớp học phần id = " + id));
    }
}