package org.example.trungcapphuongnam.module.giangDay.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.giangDay.GiangDayNotFoundException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.GiaoVienRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.GiaoVienResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.GiaoVien;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiGiaoVien;
import org.example.trungcapphuongnam.module.giangDay.mapper.GiaoVienMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienRepository;
import org.example.trungcapphuongnam.module.giangDay.service.GiaoVienService;
import org.example.trungcapphuongnam.module.giangDay.validator.GiaoVienValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GiaoVienServiceImpl implements GiaoVienService {

    private final GiaoVienRepository repository;
    private final GiaoVienMapper mapper;
    private final GiaoVienValidator validator;

    @Override
    @Transactional(readOnly = true)
    public Page<GiaoVienResponse> getAll(String keyword, TrangThaiGiaoVien trangThai, Pageable pageable) {
        String tuKhoa = keyword == null ? "" : keyword.trim();

        if (!tuKhoa.isBlank() && trangThai != null) {
            return repository
                    .findByTrangThaiAndMaGiaoVienContainingIgnoreCaseOrTrangThaiAndHoTenContainingIgnoreCaseOrTrangThaiAndEmailContainingIgnoreCase(
                            trangThai,
                            tuKhoa,
                            trangThai,
                            tuKhoa,
                            trangThai,
                            tuKhoa,
                            pageable
                    )
                    .map(mapper::toResponse);
        }

        if (!tuKhoa.isBlank()) {
            return repository
                    .findByMaGiaoVienContainingIgnoreCaseOrHoTenContainingIgnoreCaseOrEmailContainingIgnoreCase(
                            tuKhoa,
                            tuKhoa,
                            tuKhoa,
                            pageable
                    )
                    .map(mapper::toResponse);
        }

        if (trangThai != null) {
            return repository.findByTrangThai(trangThai, pageable)
                    .map(mapper::toResponse);
        }

        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public GiaoVienResponse getById(Long id) {
        return mapper.toResponse(findEntity(id));
    }

    @Override
    public GiaoVienResponse create(GiaoVienRequest request) {
        validator.validateCreate(request);
        GiaoVien entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public GiaoVienResponse update(Long id, GiaoVienRequest request) {
        validator.validateUpdate(id, request);
        GiaoVien entity = findEntity(id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        GiaoVien entity = findEntity(id);
        repository.delete(entity);
    }

    private GiaoVien findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GiangDayNotFoundException("Giáo viên không tồn tại với id = " + id));
    }
}