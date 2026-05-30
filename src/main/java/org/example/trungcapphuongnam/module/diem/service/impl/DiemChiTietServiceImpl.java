package org.example.trungcapphuongnam.module.diem.service.impl;


import lombok.RequiredArgsConstructor;

import org.example.trungcapphuongnam.module.diem.enums.TrangThaiDiem;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.DuplicateResourceException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.diem.dto.request.DiemChiTietRequest;
import org.example.trungcapphuongnam.module.diem.dto.response.DiemChiTietResponse;
import org.example.trungcapphuongnam.module.diem.entity.DiemChiTiet;
import org.example.trungcapphuongnam.module.diem.mapper.DiemChiTietMapper;
import org.example.trungcapphuongnam.module.diem.repository.DiemChiTietRepository;
import org.example.trungcapphuongnam.module.diem.service.DiemChiTietService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
@Transactional
public class DiemChiTietServiceImpl implements DiemChiTietService {

    private final DiemChiTietRepository repository;
    private final DiemChiTietMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<DiemChiTietResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<DiemChiTietResponse> findTheoBoLoc(
            Long sinhVienId,
            Long lopHocPhanId,
            Long baiKiemTraId,
            Pageable pageable
    ) {
        if (sinhVienId != null && lopHocPhanId != null) {
            return repository.findBySinhVienIdAndLopHocPhanId(sinhVienId, lopHocPhanId, pageable)
                    .map(mapper::toResponse);
        }

        if (sinhVienId != null) {
            return repository.findBySinhVienId(sinhVienId, pageable)
                    .map(mapper::toResponse);
        }

        if (lopHocPhanId != null) {
            return repository.findByLopHocPhanId(lopHocPhanId, pageable)
                    .map(mapper::toResponse);
        }

        if (baiKiemTraId != null) {
            return repository.findByBaiKiemTraId(baiKiemTraId, pageable)
                    .map(mapper::toResponse);
        }

        return findAll(pageable);
    }
    @Override
    @Transactional(readOnly = true)
    public DiemChiTietResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Override
    public DiemChiTietResponse create(DiemChiTietRequest request) {
        validate(request);
        if (request.getCauHinhDanhGiaId() != null && repository.existsBySinhVienIdAndLopHocPhanIdAndCauHinhDanhGiaId(request.getSinhVienId(), request.getLopHocPhanId(), request.getCauHinhDanhGiaId())) {
            throw new DuplicateResourceException("Sinh viên đã có điểm cho cột điểm này");
        }
        DiemChiTiet entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public DiemChiTietResponse update(Long id, DiemChiTietRequest request) {
        DiemChiTiet entity = getEntity(id);
        validate(request);
        if (request.getCauHinhDanhGiaId() != null && repository.existsBySinhVienIdAndLopHocPhanIdAndCauHinhDanhGiaIdAndIdNot(request.getSinhVienId(), request.getLopHocPhanId(), request.getCauHinhDanhGiaId(), id)) {
            throw new DuplicateResourceException("Sinh viên đã có điểm cho cột điểm này");
        }
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("DiemChiTiet không tồn tại: " + id);
        }
        repository.deleteById(id);
    }

    private DiemChiTiet getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DiemChiTiet không tồn tại: " + id));
    }


    private void validate(DiemChiTietRequest request) {
        if (request.getDiem() != null && request.getDiem().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Điểm không được âm");
        }
        if (request.getTrangThai() != null && !request.getTrangThai().isBlank()
                && !isValidEnum(TrangThaiDiem.class, request.getTrangThai())) {
            throw new BadRequestException("Trạng thái điểm không hợp lệ: " + request.getTrangThai());
        }
    }


    private <E extends Enum<E>> boolean isValidEnum(Class<E> enumClass, String value) {
        if (value == null) return false;
        for (E e : enumClass.getEnumConstants()) {
            if (e.name().equals(value)) return true;
        }
        return false;
    }
}
