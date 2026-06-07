package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.XoaChuongTrinhCascadeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.DuplicateResourceException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.QuyDoiDiemMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.QuyDoiDiemMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.QuyDoiDiemMau;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.QuyDoiDiemMauMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.*;
import org.example.trungcapphuongnam.module.chuongTrinh.service.QuyDoiDiemMauService;
import org.example.trungcapphuongnam.common.spec.LocJpa;

@Service
@RequiredArgsConstructor
@Transactional
public class QuyDoiDiemMauServiceImpl implements QuyDoiDiemMauService {
    private final ChuongTrinhNghiepVuValidator validator;
    private final QuyDoiDiemMauRepository repository;
    private final QuyDoiDiemMauMapper mapper;
    private final XoaChuongTrinhCascadeService xoaChuongTrinhCascadeService;

    @Override
    @Transactional(readOnly = true)
    public Page<QuyDoiDiemMauResponse> findAll(Long syllabusMonHocGocId, String ma, String ketQua, String keyword, Pageable pageable) {
        // Mẫu quy đổi kết quả ở tầng syllabus gốc bắt buộc phải được truy vấn theo syllabusMonHocGocId.
        // Không trả toàn bộ bảng khi thiếu tham số, tránh FE bảng 3 hiển thị dữ liệu toàn ngành.
        if (syllabusMonHocGocId == null) {
            return Page.empty(pageable);
        }

        return repository.findAll(
                LocJpa.<QuyDoiDiemMau>empty()
                    .and(LocJpa.eq("syllabusMonHocGocId", syllabusMonHocGocId))
                    .and(LocJpa.eq("loaiMau", "QUY_DOI_KET_QUA"))
                    .and(LocJpa.like("ma", ma))
                    .and(LocJpa.like("ketQua", ketQua))
                    .and(LocJpa.keyword(keyword, "ma", "ten", "ketQua", "congThuc", "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public QuyDoiDiemMauResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Quy Doi Diem Mau không tồn tại: " + id));
    }

    @Override
    public QuyDoiDiemMauResponse create(QuyDoiDiemMauRequest request) {
        chuanHoaMauQuyDoiKetQua(request);
        validator.validateQuyDoiDiemMau(request, null);
        validateTrungMaTrongSyllabusGoc(request, null);
        QuyDoiDiemMau entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public QuyDoiDiemMauResponse update(Long id, QuyDoiDiemMauRequest request) {
        QuyDoiDiemMau entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quy Doi Diem Mau không tồn tại: " + id));
        chuanHoaMauQuyDoiKetQua(request);
        validator.validateQuyDoiDiemMau(request, id);
        validateTrungMaTrongSyllabusGoc(request, id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    private void chuanHoaMauQuyDoiKetQua(QuyDoiDiemMauRequest request) {
        if (request == null) {
            throw new BadRequestException("Dữ liệu mẫu quy đổi kết quả không được để trống");
        }

        if (request.getSyllabusMonHocGocId() == null) {
            throw new BadRequestException("Cần chọn syllabus môn học gốc trước khi tạo quy đổi điểm mẫu");
        }

        if (request.getLoaiMau() != null && !request.getLoaiMau().isBlank()
                && !"QUY_DOI_KET_QUA".equalsIgnoreCase(request.getLoaiMau())) {
            throw new BadRequestException("Quy đổi điểm mẫu của syllabus gốc chỉ nhận loại QUY_DOI_KET_QUA.");
        }

        request.setLoaiMau("QUY_DOI_KET_QUA");
        if (request.getBatBuoc() == null) {
            request.setBatBuoc(Boolean.TRUE);
        }
    }

    private void validateTrungMaTrongSyllabusGoc(QuyDoiDiemMauRequest request, Long id) {
        if (request.getMa() == null || request.getMa().isBlank()) {
            return;
        }

        if (id == null && repository.existsBySyllabusMonHocGocIdAndMa(request.getSyllabusMonHocGocId(), request.getMa().trim())) {
            throw new DuplicateResourceException("Mã quy đổi điểm mẫu đã tồn tại trong syllabus gốc: " + request.getMa());
        }

        if (id != null && repository.existsBySyllabusMonHocGocIdAndMaAndIdNot(request.getSyllabusMonHocGocId(), request.getMa().trim(), id)) {
            throw new DuplicateResourceException("Mã quy đổi điểm mẫu đã tồn tại trong syllabus gốc: " + request.getMa());
        }
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Quy Doi Diem Mau không tồn tại: " + id);
        }

        xoaChuongTrinhCascadeService.xoaTheoQuyDoiDiemMauId(id);
        repository.deleteById(id);
    }
}
