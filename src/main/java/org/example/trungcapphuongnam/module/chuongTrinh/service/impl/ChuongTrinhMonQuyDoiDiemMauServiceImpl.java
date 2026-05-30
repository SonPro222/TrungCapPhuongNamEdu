package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhMonQuyDoiDiemMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhMonQuyDoiDiemMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMonQuyDoiDiemMau;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.ChuongTrinhMonQuyDoiDiemMauMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.*;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhMonQuyDoiDiemMauService;
import org.example.trungcapphuongnam.common.spec.LocJpa;

@Service
@RequiredArgsConstructor
@Transactional
public class ChuongTrinhMonQuyDoiDiemMauServiceImpl implements ChuongTrinhMonQuyDoiDiemMauService {
    private final ChuongTrinhNghiepVuValidator validator;
    private final ChuongTrinhMonQuyDoiDiemMauRepository repository;
    private final ChuongTrinhMonQuyDoiDiemMauMapper mapper;
    private final ChuongTrinhMonRepository chuongTrinhMonRepository;
    private final QuyDoiDiemMauRepository quyDoiDiemMauRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ChuongTrinhMonQuyDoiDiemMauResponse> findAll(Long chuongTrinhMonId, Long quyDoiDiemMauId, String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.<ChuongTrinhMonQuyDoiDiemMau>empty()
                    .and(LocJpa.eq("chuongTrinhMonId", chuongTrinhMonId))
                    .and(LocJpa.eq("quyDoiDiemMauId", quyDoiDiemMauId))
                    .and(LocJpa.keyword(keyword, "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ChuongTrinhMonQuyDoiDiemMauResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Chuong Trinh Mon Quy Doi Diem Mau không tồn tại: " + id));
    }

    @Override
    public ChuongTrinhMonQuyDoiDiemMauResponse create(ChuongTrinhMonQuyDoiDiemMauRequest request) {

        if (!chuongTrinhMonRepository.existsById(request.getChuongTrinhMonId())) {
            throw new ResourceNotFoundException("Môn trong chương trình không tồn tại: " + request.getChuongTrinhMonId());
        }
        if (!quyDoiDiemMauRepository.existsById(request.getQuyDoiDiemMauId())) {
            throw new ResourceNotFoundException("Quy đổi điểm mẫu không tồn tại: " + request.getQuyDoiDiemMauId());
        }
        if (request.getChuongTrinhMonId() != null && request.getQuyDoiDiemMauId() != null && repository.existsByChuongTrinhMonIdAndQuyDoiDiemMauId(request.getChuongTrinhMonId(), request.getQuyDoiDiemMauId())) {
            throw new IllegalArgumentException("Dữ liệu đã tồn tại, không được tạo trùng.");
        }
        validator.validateChuongTrinhMonQuyDoiDiemMau(request, null);
        ChuongTrinhMonQuyDoiDiemMau entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public ChuongTrinhMonQuyDoiDiemMauResponse update(Long id, ChuongTrinhMonQuyDoiDiemMauRequest request) {
        ChuongTrinhMonQuyDoiDiemMau entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chuong Trinh Mon Quy Doi Diem Mau không tồn tại: " + id));
        if (!chuongTrinhMonRepository.existsById(request.getChuongTrinhMonId())) {
            throw new ResourceNotFoundException("Môn trong chương trình không tồn tại: " + request.getChuongTrinhMonId());
        }
        if (!quyDoiDiemMauRepository.existsById(request.getQuyDoiDiemMauId())) {
            throw new ResourceNotFoundException("Quy đổi điểm mẫu không tồn tại: " + request.getQuyDoiDiemMauId());
        }
        if (request.getChuongTrinhMonId() != null && request.getQuyDoiDiemMauId() != null && repository.existsByChuongTrinhMonIdAndQuyDoiDiemMauIdAndIdNot(request.getChuongTrinhMonId(), request.getQuyDoiDiemMauId(), id)) {
            throw new IllegalArgumentException("Dữ liệu đã tồn tại, không được cập nhật trùng.");
        }
        validator.validateChuongTrinhMonQuyDoiDiemMau(request, id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Chuong Trinh Mon Quy Doi Diem Mau không tồn tại: " + id);
        }
        repository.deleteById(id);
    }


}
