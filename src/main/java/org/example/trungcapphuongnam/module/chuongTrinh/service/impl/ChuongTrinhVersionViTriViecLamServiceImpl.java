package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhVersionViTriViecLamRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhVersionViTriViecLamResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ViTriViecLamMau;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersionViTriViecLam;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.ChuongTrinhVersionViTriViecLamMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.*;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhVersionViTriViecLamService;
import org.example.trungcapphuongnam.common.spec.LocJpa;

@Service
@RequiredArgsConstructor
@Transactional
public class ChuongTrinhVersionViTriViecLamServiceImpl implements ChuongTrinhVersionViTriViecLamService {
    private final ChuongTrinhNghiepVuValidator validator;
    private final ChuongTrinhVersionViTriViecLamRepository repository;
    private final ChuongTrinhVersionViTriViecLamMapper mapper;
    private final ChuongTrinhVersionRepository chuongTrinhVersionRepository;
    private final ViTriViecLamMauRepository viTriViecLamMauRepository;

    private boolean rong(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void dienThongTinViTriTuMauNeuCan(ChuongTrinhVersionViTriViecLam entity, ViTriViecLamMau Mau) {
        if (entity == null || Mau == null) {
            return;
        }
        if (rong(entity.getMa())) {
            entity.setMa(Mau.getMa());
        }
        if (rong(entity.getTen())) {
            entity.setTen(Mau.getTen());
        }
        if (rong(entity.getMoTa())) {
            entity.setMoTa(Mau.getMoTa());
        }
        if (rong(entity.getGhiChu())) {
            entity.setGhiChu(Mau.getGhiChu());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChuongTrinhVersionViTriViecLamResponse> findAll(Long chuongTrinhVersionId, Long viTriMauId, String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.<ChuongTrinhVersionViTriViecLam>empty()
                    .and(LocJpa.eq("chuongTrinhVersionId", chuongTrinhVersionId))
                    .and(LocJpa.eq("viTriMauId", viTriMauId))
                    .and(LocJpa.keyword(keyword, "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ChuongTrinhVersionViTriViecLamResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Chuong Trinh Version Vi Tri Viec Lam không tồn tại: " + id));
    }

    @Override
    public ChuongTrinhVersionViTriViecLamResponse create(ChuongTrinhVersionViTriViecLamRequest request) {
        if (!chuongTrinhVersionRepository.existsById(request.getChuongTrinhVersionId())) {
            throw new ResourceNotFoundException("Version chương trình không tồn tại: " + request.getChuongTrinhVersionId());
        }
        ViTriViecLamMau viTriMau = viTriViecLamMauRepository.findById(request.getViTriMauId())
                .orElseThrow(() -> new ResourceNotFoundException("Vị trí việc làm gốc không tồn tại: " + request.getViTriMauId()));
        if (request.getChuongTrinhVersionId() != null && request.getViTriMauId() != null && repository.existsByChuongTrinhVersionIdAndViTriMauId(request.getChuongTrinhVersionId(), request.getViTriMauId())) {
            throw new IllegalArgumentException("Dữ liệu đã tồn tại, không được tạo trùng.");
        }
        validator.validateChuongTrinhVersionViTriViecLam(request, null);
        ChuongTrinhVersionViTriViecLam entity = mapper.toEntity(request);
        dienThongTinViTriTuMauNeuCan(entity, viTriMau);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public ChuongTrinhVersionViTriViecLamResponse update(Long id, ChuongTrinhVersionViTriViecLamRequest request) {
        ChuongTrinhVersionViTriViecLam entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chuong Trinh Version Vi Tri Viec Lam không tồn tại: " + id));
        if (!chuongTrinhVersionRepository.existsById(request.getChuongTrinhVersionId())) {
            throw new ResourceNotFoundException("Version chương trình không tồn tại: " + request.getChuongTrinhVersionId());
        }
        ViTriViecLamMau viTriMau = viTriViecLamMauRepository.findById(request.getViTriMauId())
                .orElseThrow(() -> new ResourceNotFoundException("Vị trí việc làm gốc không tồn tại: " + request.getViTriMauId()));
        if (request.getChuongTrinhVersionId() != null && request.getViTriMauId() != null && repository.existsByChuongTrinhVersionIdAndViTriMauIdAndIdNot(request.getChuongTrinhVersionId(), request.getViTriMauId(), id)) {
            throw new IllegalArgumentException("Dữ liệu đã tồn tại, không được cập nhật trùng.");
        }
        validator.validateChuongTrinhVersionViTriViecLam(request, id);
        mapper.updateEntity(entity, request);
        dienThongTinViTriTuMauNeuCan(entity, viTriMau);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Chuong Trinh Version Vi Tri Viec Lam không tồn tại: " + id);
        }
        repository.deleteById(id);
    }


}
