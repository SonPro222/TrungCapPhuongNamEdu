package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.TaiLieuMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.TaiLieuMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.TaiLieuMau;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.TaiLieuMauMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.TaiLieuMauRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.TaiLieuMauService;
import org.example.trungcapphuongnam.module.chuongTrinh.service.XoaChuongTrinhCascadeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TaiLieuMauServiceImpl implements TaiLieuMauService {
    private final ChuongTrinhNghiepVuValidator validator;
    private final TaiLieuMauRepository repository;
    private final TaiLieuMauMapper mapper;
    private final XoaChuongTrinhCascadeService xoaChuongTrinhCascadeService;

    @Override
    @Transactional(readOnly = true)
    public Page<TaiLieuMauResponse> findAll(String ma, String loai, String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.<TaiLieuMau>empty()
                        .and(LocJpa.like("ma", ma))
                        .and(LocJpa.like("loai", loai))
                        .and(LocJpa.keyword(keyword, "ma", "ten", "loai", "tacGia", "nhaXuatBan", "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public TaiLieuMauResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Tài liệu gốc không tồn tại: " + id));
    }

    @Override
    public TaiLieuMauResponse create(TaiLieuMauRequest request) {
        validator.validateTaiLieuMau(request, null);
        TaiLieuMau entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public TaiLieuMauResponse update(Long id, TaiLieuMauRequest request) {
        TaiLieuMau entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tài liệu gốc không tồn tại: " + id));

        validator.validateTaiLieuMau(request, id);

        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Tài liệu gốc không tồn tại: " + id);
        }

        xoaChuongTrinhCascadeService.xoaTheoTaiLieuMauId(id);

        repository.deleteById(id);
    }

}