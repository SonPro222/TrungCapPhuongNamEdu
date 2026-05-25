package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusChuongTrinhGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusChuongTrinhGocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusChuongTrinhGoc;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.SyllabusChuongTrinhGocMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusChuongTrinhGocRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusChuongTrinhGocService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SyllabusChuongTrinhGocServiceImpl implements SyllabusChuongTrinhGocService {

    private final SyllabusChuongTrinhGocRepository repository;
    private final SyllabusChuongTrinhGocMapper mapper;
    private final ChuongTrinhNghiepVuValidator validator;

    @Override
    @Transactional(readOnly = true)
    public Page<SyllabusChuongTrinhGocResponse> findAll(String ma, String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.<SyllabusChuongTrinhGoc>empty()
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
    public SyllabusChuongTrinhGocResponse findById(Long id) {
        SyllabusChuongTrinhGoc entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Syllabus chương trình gốc không tồn tại: " + id));

        return mapper.toResponse(entity);
    }

    @Override
    public SyllabusChuongTrinhGocResponse create(SyllabusChuongTrinhGocRequest request) {
        validator.validateSyllabusChuongTrinhGoc(request, null);

        SyllabusChuongTrinhGoc entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public SyllabusChuongTrinhGocResponse update(Long id, SyllabusChuongTrinhGocRequest request) {
        SyllabusChuongTrinhGoc entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Syllabus chương trình gốc không tồn tại: " + id));

        validator.validateSyllabusChuongTrinhGoc(request, id);

        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Syllabus chương trình gốc không tồn tại: " + id);
        }

        repository.deleteById(id);
    }
}