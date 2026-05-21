package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhNghiepVuValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhVersionDieuKienTotNghiepRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhVersionDieuKienTotNghiepResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersionDieuKienTotNghiep;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.ChuongTrinhVersionDieuKienTotNghiepMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.*;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhVersionDieuKienTotNghiepService;

@Service
@RequiredArgsConstructor
@Transactional
public class ChuongTrinhVersionDieuKienTotNghiepServiceImpl implements ChuongTrinhVersionDieuKienTotNghiepService {
    private final ChuongTrinhNghiepVuValidator validator;
    private final ChuongTrinhVersionDieuKienTotNghiepRepository repository;
    private final ChuongTrinhVersionDieuKienTotNghiepMapper mapper;
    private final ChuongTrinhVersionRepository chuongTrinhVersionRepository;
    private final DieuKienTotNghiepGocRepository dieuKienTotNghiepGocRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ChuongTrinhVersionDieuKienTotNghiepResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ChuongTrinhVersionDieuKienTotNghiepResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Chuong Trinh Version Dieu Kien Tot Nghiep không tồn tại: " + id));
    }

    @Override
    public ChuongTrinhVersionDieuKienTotNghiepResponse create(ChuongTrinhVersionDieuKienTotNghiepRequest request) {
        if (!chuongTrinhVersionRepository.existsById(request.getChuongTrinhVersionId())) {
            throw new ResourceNotFoundException("Version chương trình không tồn tại: " + request.getChuongTrinhVersionId());
        }
        if (!dieuKienTotNghiepGocRepository.existsById(request.getDieuKienGocId())) {
            throw new ResourceNotFoundException("Điều kiện tốt nghiệp gốc không tồn tại: " + request.getDieuKienGocId());
        }
        if (request.getChuongTrinhVersionId() != null && request.getDieuKienGocId() != null && repository.existsByChuongTrinhVersionIdAndDieuKienGocId(request.getChuongTrinhVersionId(), request.getDieuKienGocId())) {
            throw new IllegalArgumentException("Dữ liệu đã tồn tại, không được tạo trùng.");
        }
        validator.validateChuongTrinhVersionDieuKienTotNghiep(request, null);
        ChuongTrinhVersionDieuKienTotNghiep entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public ChuongTrinhVersionDieuKienTotNghiepResponse update(Long id, ChuongTrinhVersionDieuKienTotNghiepRequest request) {
        ChuongTrinhVersionDieuKienTotNghiep entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chuong Trinh Version Dieu Kien Tot Nghiep không tồn tại: " + id));
        if (!chuongTrinhVersionRepository.existsById(request.getChuongTrinhVersionId())) {
            throw new ResourceNotFoundException("Version chương trình không tồn tại: " + request.getChuongTrinhVersionId());
        }
        if (!dieuKienTotNghiepGocRepository.existsById(request.getDieuKienGocId())) {
            throw new ResourceNotFoundException("Điều kiện tốt nghiệp gốc không tồn tại: " + request.getDieuKienGocId());
        }
        if (request.getChuongTrinhVersionId() != null && request.getDieuKienGocId() != null && repository.existsByChuongTrinhVersionIdAndDieuKienGocIdAndIdNot(request.getChuongTrinhVersionId(), request.getDieuKienGocId(), id)) {
            throw new IllegalArgumentException("Dữ liệu đã tồn tại, không được cập nhật trùng.");
        }
        validator.validateChuongTrinhVersionDieuKienTotNghiep(request, id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Chuong Trinh Version Dieu Kien Tot Nghiep không tồn tại: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChuongTrinhVersionDieuKienTotNghiepResponse> findAllByChuongTrinhVersionId(Long chuongTrinhVersionId, Pageable pageable) {
        return repository.findByChuongTrinhVersionId(chuongTrinhVersionId, pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChuongTrinhVersionDieuKienTotNghiepResponse> findAllByDieuKienGocId(Long dieuKienGocId, Pageable pageable) {
        return repository.findByDieuKienGocId(dieuKienGocId, pageable).map(mapper::toResponse);
    }

}
