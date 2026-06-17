package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusChuongTrinhMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusChuongTrinhMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusChuongTrinhMau;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.SyllabusChuongTrinhMauMapper;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusChuongTrinhMauRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusChuongTrinhRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusChuongTrinhMauService;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SyllabusChuongTrinhMauServiceImpl implements SyllabusChuongTrinhMauService {

    private final SyllabusChuongTrinhMauRepository repository;
    private final SyllabusChuongTrinhMauMapper mapper;
    private final ChuongTrinhNghiepVuValidator validator;
    private final SyllabusChuongTrinhRepository syllabusChuongTrinhRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<SyllabusChuongTrinhMauResponse> findAll(Long chuongTrinhId, String ma, String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.<SyllabusChuongTrinhMau>empty()
                        .and(LocJpa.eq("chuongTrinhId", chuongTrinhId))
                        .and(LocJpa.like("ma", ma))
                        .and(LocJpa.keyword(
                                keyword,
                                "ma",
                                "ten",
                                "mucTieu",
                                "doiTuongTuyenSinh",
                                "thoiGianDaoTao",
                                "khoiLuongKienThuc",
                                "dieuKienTotNghiep",
                                "phuongPhapDaoTao",
                                "phuongPhapDanhGia",
                                "huongDanThucHien",
                                "ghiChu"
                        )),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SyllabusChuongTrinhMauResponse findById(Long id) {
        SyllabusChuongTrinhMau entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Syllabus chương trình gốc không tồn tại: " + id));

        return mapper.toResponse(entity);
    }

    @Override
    public SyllabusChuongTrinhMauResponse create(SyllabusChuongTrinhMauRequest request) {
        validator.validateSyllabusChuongTrinhMau(request, null);

        SyllabusChuongTrinhMau entity = mapper.toEntity(request);
        SyllabusChuongTrinhMau saved = repository.save(entity);

        return mapper.toResponse(saved);
    }

    @Override
    public SyllabusChuongTrinhMauResponse update(Long id, SyllabusChuongTrinhMauRequest request) {
        SyllabusChuongTrinhMau entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Syllabus chương trình gốc không tồn tại: " + id));

        validator.validateSyllabusChuongTrinhMau(request, id);

        mapper.updateEntity(entity, request);
        SyllabusChuongTrinhMau saved = repository.save(entity);

        return mapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Syllabus chương trình gốc không tồn tại: " + id);
        }

        if (syllabusChuongTrinhRepository.existsBySyllabusChuongTrinhMauId(id)) {
            throw new BadRequestException("Không thể xóa syllabus chương trình mẫu đang được copy/gán vào Version. Hãy xóa bản đã lưu trong Version trước, dữ liệu mẫu sẽ được giữ lại để tái sử dụng.");
        }

        repository.deleteById(id);
    }
}