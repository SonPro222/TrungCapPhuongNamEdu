package org.example.trungcapphuongnam.module.diem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.DuplicateResourceException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.util.TextUtil;
import org.example.trungcapphuongnam.module.diem.dto.request.CauHinhDanhGiaRequest;
import org.example.trungcapphuongnam.module.diem.dto.response.CauHinhDanhGiaResponse;
import org.example.trungcapphuongnam.module.diem.entity.CauHinhDanhGia;
import org.example.trungcapphuongnam.module.diem.mapper.CauHinhDanhGiaMapper;
import org.example.trungcapphuongnam.module.diem.repository.CauHinhDanhGiaRepository;
import org.example.trungcapphuongnam.module.diem.service.CauHinhDanhGiaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CauHinhDanhGiaServiceImpl implements CauHinhDanhGiaService {

    private final CauHinhDanhGiaRepository repository;
    private final CauHinhDanhGiaMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CauHinhDanhGiaResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CauHinhDanhGiaResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CauHinhDanhGiaResponse> findByLopHocPhanId(Long lopHocPhanId) {
        return repository.findByLopHocPhanIdOrderByThuTuAsc(lopHocPhanId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CauHinhDanhGiaResponse create(CauHinhDanhGiaRequest request) {
        validate(request);

        if (repository.existsByLopHocPhanIdAndTenCotDiem(request.getLopHocPhanId(), request.getTenCotDiem())) {
            throw new DuplicateResourceException("Cột điểm đã tồn tại trong lớp học phần này");
        }

        CauHinhDanhGia entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public CauHinhDanhGiaResponse update(Long id, CauHinhDanhGiaRequest request) {
        CauHinhDanhGia entity = getEntity(id);
        validate(request);

        if (repository.existsByLopHocPhanIdAndTenCotDiemAndIdNot(request.getLopHocPhanId(), request.getTenCotDiem(), id)) {
            throw new DuplicateResourceException("Cột điểm đã tồn tại trong lớp học phần này");
        }

        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("CauHinhDanhGia không tồn tại: " + id);
        }

        repository.deleteById(id);
    }

    private CauHinhDanhGia getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CauHinhDanhGia không tồn tại: " + id));
    }

    private void validate(CauHinhDanhGiaRequest request) {
        request.setTenCotDiem(TextUtil.trimRequired(request.getTenCotDiem()));
        request.setLoaiDiem(TextUtil.trimToNull(request.getLoaiDiem()));
        request.setNguonDiem(TextUtil.trimToNull(request.getNguonDiem()));
        if (request.getNguonDiem() == null) {
            String loai = request.getLoaiDiem() == null ? "" : request.getLoaiDiem().toLowerCase();
            String ten = request.getTenCotDiem() == null ? "" : request.getTenCotDiem().toLowerCase();
            if (loai.contains("online") || ten.contains("online") || ten.contains("lms")) {
                request.setNguonDiem("LMS");
            } else {
                request.setNguonDiem("GIANG_VIEN_NHAP");
            }
        }

        if (request.getLopHocPhanId() == null) {
            throw new BadRequestException("Lớp học phần không được để trống");
        }

        if (!request.getNguonDiem().equals("LMS")
                && !request.getNguonDiem().equals("GIANG_VIEN_NHAP")
                && !request.getNguonDiem().equals("IMPORT")
                && !request.getNguonDiem().equals("THI")) {
            throw new BadRequestException("Nguồn điểm không hợp lệ: " + request.getNguonDiem());
        }

        if (request.getTyLe() == null
                || request.getTyLe().compareTo(BigDecimal.ZERO) < 0
                || request.getTyLe().compareTo(new BigDecimal("100")) > 0) {
            throw new BadRequestException("Tỷ lệ phải nằm trong khoảng 0 - 100");
        }

        if (request.getDiemToiDa() != null && request.getDiemToiDa().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Điểm tối đa phải lớn hơn 0");
        }
    }
}