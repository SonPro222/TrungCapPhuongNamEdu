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
    private final ViTriViecLamGocRepository viTriViecLamGocRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ChuongTrinhVersionViTriViecLamResponse> findAll(Long chuongTrinhVersionId, Long viTriGocId, String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.<ChuongTrinhVersionViTriViecLam>empty()
                    .and(LocJpa.eq("chuongTrinhVersionId", chuongTrinhVersionId))
                    .and(LocJpa.eq("viTriGocId", viTriGocId))
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
        if (!viTriViecLamGocRepository.existsById(request.getViTriGocId())) {
            throw new ResourceNotFoundException("Vị trí việc làm gốc không tồn tại: " + request.getViTriGocId());
        }
        if (request.getChuongTrinhVersionId() != null && request.getViTriGocId() != null && repository.existsByChuongTrinhVersionIdAndViTriGocId(request.getChuongTrinhVersionId(), request.getViTriGocId())) {
            throw new IllegalArgumentException("Dữ liệu đã tồn tại, không được tạo trùng.");
        }
        validator.validateChuongTrinhVersionViTriViecLam(request, null);
        ChuongTrinhVersionViTriViecLam entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public ChuongTrinhVersionViTriViecLamResponse update(Long id, ChuongTrinhVersionViTriViecLamRequest request) {
        ChuongTrinhVersionViTriViecLam entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chuong Trinh Version Vi Tri Viec Lam không tồn tại: " + id));
        if (!chuongTrinhVersionRepository.existsById(request.getChuongTrinhVersionId())) {
            throw new ResourceNotFoundException("Version chương trình không tồn tại: " + request.getChuongTrinhVersionId());
        }
        if (!viTriViecLamGocRepository.existsById(request.getViTriGocId())) {
            throw new ResourceNotFoundException("Vị trí việc làm gốc không tồn tại: " + request.getViTriGocId());
        }
        if (request.getChuongTrinhVersionId() != null && request.getViTriGocId() != null && repository.existsByChuongTrinhVersionIdAndViTriGocIdAndIdNot(request.getChuongTrinhVersionId(), request.getViTriGocId(), id)) {
            throw new IllegalArgumentException("Dữ liệu đã tồn tại, không được cập nhật trùng.");
        }
        validator.validateChuongTrinhVersionViTriViecLam(request, id);
        mapper.updateEntity(entity, request);
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
