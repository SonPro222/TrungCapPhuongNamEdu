package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.XoaChuongTrinhCascadeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.MucTieuChuongTrinhMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.MucTieuChuongTrinhMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.MucTieuChuongTrinhMau;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.MucTieuChuongTrinhMauMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.*;
import org.example.trungcapphuongnam.module.chuongTrinh.service.MucTieuChuongTrinhMauService;
import org.example.trungcapphuongnam.common.spec.LocJpa;

@Service
@RequiredArgsConstructor
@Transactional
public class MucTieuChuongTrinhMauServiceImpl implements MucTieuChuongTrinhMauService {
    private final ChuongTrinhNghiepVuValidator validator;
    private final MucTieuChuongTrinhMauRepository repository;
    private final MucTieuChuongTrinhMauMapper mapper;
    private final XoaChuongTrinhCascadeService xoaChuongTrinhCascadeService;

    @Override
    @Transactional(readOnly = true)
    public Page<MucTieuChuongTrinhMauResponse> findAll(Long syllabusChuongTrinhMauId, String ma, String loai, String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.<MucTieuChuongTrinhMau>empty()
                    .and(LocJpa.eq("syllabusChuongTrinhMauId", syllabusChuongTrinhMauId))
                    .and(LocJpa.like("ma", ma))
                    .and(LocJpa.like("loai", loai))
                    .and(LocJpa.keyword(keyword, "ma", "loai", "noiDung", "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public MucTieuChuongTrinhMauResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Muc Tieu Chuong Trinh Mau không tồn tại: " + id));
    }

    @Override
    public MucTieuChuongTrinhMauResponse create(MucTieuChuongTrinhMauRequest request) {
        if (request.getMa() != null && repository.existsByMa(request.getMa())) {
            throw new IllegalArgumentException("Dữ liệu đã tồn tại, không được tạo trùng.");
        }
        validator.validateMucTieuChuongTrinhMau(request, null);
        MucTieuChuongTrinhMau entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public MucTieuChuongTrinhMauResponse update(Long id, MucTieuChuongTrinhMauRequest request) {
        MucTieuChuongTrinhMau entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Muc Tieu Chuong Trinh Mau không tồn tại: " + id));
        if (request.getMa() != null && repository.existsByMaAndIdNot(request.getMa(), id)) {
            throw new IllegalArgumentException("Dữ liệu đã tồn tại, không được cập nhật trùng.");
        }
        validator.validateMucTieuChuongTrinhMau(request, id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Muc Tieu Chuong Trinh Mau không tồn tại: " + id);
        }

        xoaChuongTrinhCascadeService.xoaTheoMucTieuMauId(id);

        repository.deleteById(id);
    }

}
