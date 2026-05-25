package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.module.chuongTrinh.enums.LoaiDieuKienMonHoc;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.DieuKienMonHocGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.DieuKienMonHocGocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.DieuKienMonHocGoc;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.DieuKienMonHocGocMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.DieuKienMonHocGocRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.DieuKienMonHocGocService;
import org.example.trungcapphuongnam.module.chuongTrinh.service.XoaChuongTrinhCascadeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DieuKienMonHocGocServiceImpl implements DieuKienMonHocGocService {
    private final ChuongTrinhNghiepVuValidator validator;

    private final DieuKienMonHocGocRepository repository;
    private final DieuKienMonHocGocMapper mapper;
    private final XoaChuongTrinhCascadeService xoaChuongTrinhCascadeService;

    @Override
    @Transactional(readOnly = true)
    public Page<DieuKienMonHocGocResponse> findAll(String ma, String loai, String keyword, Pageable pageable) {
        LoaiDieuKienMonHoc loaiEnum = isBlank(loai) ? null : LoaiDieuKienMonHoc.fromValue(loai);

        return repository.findAll(
                LocJpa.<DieuKienMonHocGoc>empty()
                        .and(LocJpa.like("ma", ma))
                        .and(LocJpa.eq("loai", loaiEnum))
                        .and(LocJpa.keyword(keyword, "ma", "noiDung", "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public DieuKienMonHocGocResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Điều kiện môn học gốc không tồn tại: " + id));
    }

    @Override
    public DieuKienMonHocGocResponse create(DieuKienMonHocGocRequest request) {
        validator.validateDieuKienMonHocGoc(request, null);
        DieuKienMonHocGoc entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public DieuKienMonHocGocResponse update(Long id, DieuKienMonHocGocRequest request) {
        DieuKienMonHocGoc entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Điều kiện môn học gốc không tồn tại: " + id));

        validator.validateDieuKienMonHocGoc(request, id);

        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Điều kiện môn học gốc không tồn tại: " + id);
        }

        xoaChuongTrinhCascadeService.xoaTheoDieuKienMonHocGocId(id);

        repository.deleteById(id);
    }


    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}