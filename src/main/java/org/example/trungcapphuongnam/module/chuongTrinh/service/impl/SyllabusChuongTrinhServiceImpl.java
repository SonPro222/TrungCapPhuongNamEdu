package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusChuongTrinhRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusChuongTrinhResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusChuongTrinh;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusChuongTrinhGoc;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.SyllabusChuongTrinhMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusChuongTrinhRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusChuongTrinhService;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SyllabusChuongTrinhServiceImpl implements SyllabusChuongTrinhService {

    private final ChuongTrinhNghiepVuValidator validator;
    private final SyllabusChuongTrinhRepository repository;
    private final SyllabusChuongTrinhMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<SyllabusChuongTrinhResponse> findAll(
            Long chuongTrinhVersionId,
            Long syllabusChuongTrinhGocId,
            String keyword,
            Pageable pageable
    ) {
        return repository.findAll(
                LocJpa.<SyllabusChuongTrinh>empty()
                        .and(LocJpa.eq("chuongTrinhVersionId", chuongTrinhVersionId))
                        .and(LocJpa.eq("syllabusChuongTrinhGocId", syllabusChuongTrinhGocId))
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
    public SyllabusChuongTrinhResponse findById(Long id) {
        SyllabusChuongTrinh entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Syllabus chương trình không tồn tại: " + id));

        return mapper.toResponse(entity);
    }

    @Override
    public SyllabusChuongTrinhResponse create(SyllabusChuongTrinhRequest request) {
        validator.validateSyllabusChuongTrinh(request, null);

        SyllabusChuongTrinh entity = mapper.toEntity(request);
        SyllabusChuongTrinh saved = repository.save(entity);

        return mapper.toResponse(saved);
    }

    @Override
    public SyllabusChuongTrinhResponse update(Long id, SyllabusChuongTrinhRequest request) {
        SyllabusChuongTrinh entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Syllabus chương trình không tồn tại: " + id));

        validator.validateSyllabusChuongTrinh(request, id);

        mapper.updateEntity(entity, request);
        SyllabusChuongTrinh saved = repository.save(entity);

        return mapper.toResponse(saved);
    }

    @Override
    public SyllabusChuongTrinhResponse dongBoTuSyllabusGoc(
            SyllabusChuongTrinhGoc syllabusGoc,
            Long chuongTrinhVersionId
    ) {
        if (syllabusGoc == null || syllabusGoc.getId() == null || chuongTrinhVersionId == null) {
            throw new IllegalArgumentException("Thiếu dữ liệu đồng bộ syllabus chương trình");
        }

        SyllabusChuongTrinh entity = repository
                .findByChuongTrinhVersionIdAndSyllabusChuongTrinhGocId(
                        chuongTrinhVersionId,
                        syllabusGoc.getId()
                )
                .orElseGet(SyllabusChuongTrinh::new);

        entity.setChuongTrinhVersionId(chuongTrinhVersionId);
        entity.setSyllabusChuongTrinhGocId(syllabusGoc.getId());

        entity.setMa(syllabusGoc.getMa());
        entity.setTen(syllabusGoc.getTen());
        entity.setDuongDan(syllabusGoc.getDuongDan());

        entity.setMucTieu(syllabusGoc.getMucTieu());
        entity.setDoiTuongTuyenSinh(syllabusGoc.getDoiTuongTuyenSinh());
        entity.setThoiGianDaoTao(syllabusGoc.getThoiGianDaoTao());
        entity.setKhoiLuongKienThuc(syllabusGoc.getKhoiLuongKienThuc());
        entity.setDieuKienTotNghiep(syllabusGoc.getDieuKienTotNghiep());
        entity.setPhuongPhapDaoTao(syllabusGoc.getPhuongPhapDaoTao());
        entity.setPhuongPhapDanhGia(syllabusGoc.getPhuongPhapDanhGia());
        entity.setHuongDanThucHien(syllabusGoc.getHuongDanThucHien());
        entity.setGhiChu(syllabusGoc.getGhiChu());

        SyllabusChuongTrinh saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Syllabus chương trình không tồn tại: " + id);
        }

        repository.deleteById(id);
    }
}