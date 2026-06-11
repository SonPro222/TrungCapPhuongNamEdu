package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhVersionNangLucRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhVersionNangLucResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersionNangLuc;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.NangLucDauRaMau;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.ChuongTrinhVersionNangLucMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.*;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhVersionNangLucService;
import org.example.trungcapphuongnam.common.spec.LocJpa;

@Service
@RequiredArgsConstructor
@Transactional
public class ChuongTrinhVersionNangLucServiceImpl implements ChuongTrinhVersionNangLucService {
    private final ChuongTrinhNghiepVuValidator validator;
    private final ChuongTrinhVersionNangLucRepository repository;
    private final ChuongTrinhVersionNangLucMapper mapper;
    private final ChuongTrinhVersionRepository chuongTrinhVersionRepository;
    private final NangLucDauRaMauRepository nangLucDauRaMauRepository;

    private boolean rong(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void dienThongTinNangLucTuMauNeuCan(ChuongTrinhVersionNangLuc entity, NangLucDauRaMau Mau) {
        if (entity == null || Mau == null) {
            return;
        }
        if (rong(entity.getMa())) {
            entity.setMa(Mau.getMa());
        }
        if (entity.getLoai() == null) {
            entity.setLoai(Mau.getLoai());
        }
        if (rong(entity.getNoiDung())) {
            entity.setNoiDung(Mau.getNoiDung());
        }
        if (rong(entity.getGhiChu())) {
            entity.setGhiChu(Mau.getGhiChu());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChuongTrinhVersionNangLucResponse> findAll(Long chuongTrinhVersionId, Long nangLucMauId, String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.<ChuongTrinhVersionNangLuc>empty()
                    .and(LocJpa.eq("chuongTrinhVersionId", chuongTrinhVersionId))
                    .and(LocJpa.eq("nangLucMauId", nangLucMauId))
                    .and(LocJpa.keyword(keyword, "ma", "loai", "noiDung", "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ChuongTrinhVersionNangLucResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Chuong Trinh Version Nang Luc không tồn tại: " + id));
    }

    @Override
    public ChuongTrinhVersionNangLucResponse create(ChuongTrinhVersionNangLucRequest request) {
        if (!chuongTrinhVersionRepository.existsById(request.getChuongTrinhVersionId())) {
            throw new ResourceNotFoundException("Version chương trình không tồn tại: " + request.getChuongTrinhVersionId());
        }
        NangLucDauRaMau nangLucMau = nangLucDauRaMauRepository.findById(request.getNangLucMauId())
                .orElseThrow(() -> new ResourceNotFoundException("Năng lực gốc không tồn tại: " + request.getNangLucMauId()));
        if (request.getChuongTrinhVersionId() != null && request.getNangLucMauId() != null && repository.existsByChuongTrinhVersionIdAndNangLucMauId(request.getChuongTrinhVersionId(), request.getNangLucMauId())) {
            throw new IllegalArgumentException("Dữ liệu đã tồn tại, không được tạo trùng.");
        }
        validator.validateChuongTrinhVersionNangLuc(request, null);
        ChuongTrinhVersionNangLuc entity = mapper.toEntity(request);
        dienThongTinNangLucTuMauNeuCan(entity, nangLucMau);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public ChuongTrinhVersionNangLucResponse update(Long id, ChuongTrinhVersionNangLucRequest request) {
        ChuongTrinhVersionNangLuc entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chuong Trinh Version Nang Luc không tồn tại: " + id));
        if (!chuongTrinhVersionRepository.existsById(request.getChuongTrinhVersionId())) {
            throw new ResourceNotFoundException("Version chương trình không tồn tại: " + request.getChuongTrinhVersionId());
        }
        NangLucDauRaMau nangLucMau = nangLucDauRaMauRepository.findById(request.getNangLucMauId())
                .orElseThrow(() -> new ResourceNotFoundException("Năng lực gốc không tồn tại: " + request.getNangLucMauId()));
        if (request.getChuongTrinhVersionId() != null && request.getNangLucMauId() != null && repository.existsByChuongTrinhVersionIdAndNangLucMauIdAndIdNot(request.getChuongTrinhVersionId(), request.getNangLucMauId(), id)) {
            throw new IllegalArgumentException("Dữ liệu đã tồn tại, không được cập nhật trùng.");
        }
        validator.validateChuongTrinhVersionNangLuc(request, id);
        mapper.updateEntity(entity, request);
        dienThongTinNangLucTuMauNeuCan(entity, nangLucMau);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Chuong Trinh Version Nang Luc không tồn tại: " + id);
        }
        repository.deleteById(id);
    }
}
