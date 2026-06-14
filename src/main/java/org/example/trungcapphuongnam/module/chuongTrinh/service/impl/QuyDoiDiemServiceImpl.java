package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.common.util.TextUtil;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.QuyDoiDiemRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.QuyDoiDiemResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.QuyDoiDiem;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHoc;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.QuyDoiDiemMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.QuyDoiDiemRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.QuyDoiDiemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class QuyDoiDiemServiceImpl implements QuyDoiDiemService {

    private final QuyDoiDiemRepository repository;
    private final QuyDoiDiemMapper mapper;
    private final SyllabusMonHocRepository syllabusMonHocRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<QuyDoiDiemResponse> findAll(
            Long syllabusMonHocId,
            String ketQua,
            String keyword,
            Pageable pageable
    ) {
        return repository.findAll(
                LocJpa.<QuyDoiDiem>empty()
                        .and(LocJpa.eq("syllabusMonHocId", syllabusMonHocId))
                        .and(LocJpa.like("ketQua", ketQua))
                        .and(LocJpa.keyword(keyword, "ketQua", "congThuc", "ghiChu", "ma", "ten", "loaiMau")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public QuyDoiDiemResponse findById(Long id) {
        QuyDoiDiem entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuyDoiDiem không tồn tại: " + id));

        return mapper.toResponse(entity);
    }

    @Override
    public QuyDoiDiemResponse create(QuyDoiDiemRequest request) {
        SyllabusMonHoc syllabusMonHoc = validateRequest(request, null);

        QuyDoiDiem entity = mapper.toEntity(request);
        ganScopeTheoSyllabus(entity, syllabusMonHoc);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public QuyDoiDiemResponse update(Long id, QuyDoiDiemRequest request) {
        QuyDoiDiem entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuyDoiDiem không tồn tại: " + id));

        SyllabusMonHoc syllabusMonHoc = validateRequest(request, id);

        mapper.updateEntity(entity, request);
        ganScopeTheoSyllabus(entity, syllabusMonHoc);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("QuyDoiDiem không tồn tại: " + id);
        }

        repository.deleteById(id);
    }

    private SyllabusMonHoc validateRequest(QuyDoiDiemRequest request, Long currentId) {
        if (request == null) {
            throw new BadRequestException("Dữ liệu quy đổi điểm không được để trống");
        }

        request.setKetQua(TextUtil.trimToNull(request.getKetQua()));
        request.setCongThuc(TextUtil.trimToNull(request.getCongThuc()));
        request.setGhiChu(TextUtil.trimToNull(request.getGhiChu()));
        request.setMa(TextUtil.trimToNull(request.getMa()));
        request.setTen(TextUtil.trimToNull(request.getTen()));
        request.setLoaiMau(TextUtil.trimToNull(request.getLoaiMau()));

        if (request.getSyllabusMonHocId() == null) {
            throw new BadRequestException("syllabusMonHocId không được để trống");
        }

        SyllabusMonHoc syllabusMonHoc = syllabusMonHocRepository.findById(request.getSyllabusMonHocId())
                .orElseThrow(() -> new ResourceNotFoundException("Syllabus môn học không tồn tại: " + request.getSyllabusMonHocId()));

        validateNguongDiem(request);
        validateOverlapInSyllabus(request, currentId);
        validateTenCotDiemInSyllabus(request, currentId);

        return syllabusMonHoc;
    }

    private void ganScopeTheoSyllabus(QuyDoiDiem entity, SyllabusMonHoc syllabusMonHoc) {
        entity.setSyllabusMonHocId(syllabusMonHoc.getId());
    }

    private void validateNguongDiem(QuyDoiDiemRequest request) {
        BigDecimal nguongTu = request.getNguongTu();
        BigDecimal nguongDen = request.getNguongDen();

        if (nguongTu != null && nguongDen != null && nguongTu.compareTo(nguongDen) > 0) {
            throw new BadRequestException("Ngưỡng từ không được lớn hơn ngưỡng đến");
        }

        if (request.getDiemQuyDoi() != null && request.getDiemQuyDoi().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Điểm quy đổi không được âm");
        }


        if (request.getDiemToiDa() != null && request.getDiemToiDa().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Điểm tối đa phải lớn hơn 0");
        }
    }

    private void validateOverlapInSyllabus(QuyDoiDiemRequest request, Long currentId) {
        if (request.getNguongTu() == null || request.getNguongDen() == null) {
            return;
        }

        boolean duplicated = repository.existsOverlapBySyllabusMonHocId(
                request.getSyllabusMonHocId(),
                request.getNguongTu(),
                request.getNguongDen(),
                currentId
        );

        if (duplicated) {
            throw new BadRequestException("Khoảng ngưỡng điểm bị trùng trong syllabus môn học này");
        }
    }

    private void validateTenCotDiemInSyllabus(QuyDoiDiemRequest request, Long currentId) {
        String tenCotDiem = TextUtil.trimToNull(request.getTen());

        if (tenCotDiem == null) {
            tenCotDiem = TextUtil.trimToNull(request.getGhiChu());
        }

        if (tenCotDiem == null) {
            return;
        }

        boolean duplicated = repository.existsTenCotDiemTrongSyllabus(
                request.getSyllabusMonHocId(),
                tenCotDiem,
                currentId
        );

        if (duplicated) {
            throw new BadRequestException("Tên cột điểm/quy đổi đã tồn tại trong syllabus môn học này");
        }
    }


}
