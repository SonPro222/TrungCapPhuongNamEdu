package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.QuyDoiDiem;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.QuyDoiDiemMau;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHoc;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.SyllabusMonHocMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.QuyDoiDiemMauRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.QuyDoiDiemRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusChuongBaiRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocService;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.service.XoaChuongTrinhCascadeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.common.spec.LocJpa;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SyllabusMonHocServiceImpl implements SyllabusMonHocService {

    private final XoaChuongTrinhCascadeService xoaChuongTrinhCascadeService;
    private final SyllabusMonHocRepository repository;
    private final SyllabusChuongBaiRepository syllabusChuongBaiRepository;
    private final QuyDoiDiemMauRepository quyDoiDiemMauRepository;
    private final QuyDoiDiemRepository quyDoiDiemRepository;
    private final SyllabusMonHocMapper mapper;
    private final ChuongTrinhNghiepVuValidator validator;

    @Override
    @Transactional(readOnly = true)
    public Page<SyllabusMonHocResponse> findAll(
            Long chuongTrinhMonId,
            Long syllabusMonHocGocId,
            Boolean batBuocDuThi,
            String keyword,
            Pageable pageable
    ) {
        return repository.findAll(
                LocJpa.<SyllabusMonHoc>empty()
                        .and(LocJpa.eq("chuongTrinhMonId", chuongTrinhMonId))
                        .and(LocJpa.eq("syllabusMonHocGocId", syllabusMonHocGocId))
                        .and(LocJpa.eq("batBuocDuThi", batBuocDuThi))
                        .and(LocJpa.keyword(
                                keyword,
                                "viTri",
                                "tinhChat",
                                "mucTieu",
                                "phuongPhapDanhGia",
                                "dieuKienHoanThanh",
                                "huongDan",
                                "donViDiem",
                                "congThucQuyDoi"
                        )),
                pageable
        ).map(this::toResponseCoTongGio);
    }

    @Override
    @Transactional(readOnly = true)
    public SyllabusMonHocResponse findById(Long id) {
        SyllabusMonHoc entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SyllabusMonHoc không tồn tại: " + id));
        return toResponseCoTongGio(entity);
    }

    @Override
    public SyllabusMonHocResponse create(SyllabusMonHocRequest request) {
        validator.validateSyllabusMonHoc(request, null);

        SyllabusMonHoc entity = repository.save(mapper.toEntity(request));
        saoChepQuyDoiDiemMauTuSyllabusGoc(entity);
        return toResponseCoTongGio(entity);
    }

    @Override
    public SyllabusMonHocResponse update(Long id, SyllabusMonHocRequest request) {
        validator.validateSyllabusMonHoc(request, id);

        SyllabusMonHoc entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SyllabusMonHoc không tồn tại: " + id));

        Long oldSyllabusMonHocGocId = entity.getSyllabusMonHocGocId();
        mapper.updateEntity(entity, request);
        SyllabusMonHoc saved = repository.save(entity);

        if (saved.getSyllabusMonHocGocId() != null && !saved.getSyllabusMonHocGocId().equals(oldSyllabusMonHocGocId)) {
            saoChepQuyDoiDiemMauTuSyllabusGoc(saved);
        }

        return toResponseCoTongGio(saved);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("SyllabusMonHoc không tồn tại: " + id);
        }

        xoaChuongTrinhCascadeService.xoaTheoSyllabusMonHocId(id);
    }

    private void saoChepQuyDoiDiemMauTuSyllabusGoc(SyllabusMonHoc syllabusMonHoc) {
        if (syllabusMonHoc == null || syllabusMonHoc.getId() == null || syllabusMonHoc.getSyllabusMonHocGocId() == null) {
            return;
        }

        List<QuyDoiDiemMau> danhSachMau = quyDoiDiemMauRepository
                .findBySyllabusMonHocGocIdOrderByThuTuAscIdAsc(syllabusMonHoc.getSyllabusMonHocGocId());

        for (QuyDoiDiemMau mau : danhSachMau) {
            String ten = mau.getTen() == null ? mau.getMa() : mau.getTen();
            if (ten == null || ten.isBlank()) {
                continue;
            }

            if (quyDoiDiemRepository.existsTenCotDiemTrongSyllabus(syllabusMonHoc.getId(), ten, null)) {
                continue;
            }

            QuyDoiDiem quyDoiDiem = QuyDoiDiem.builder()
                    .chuongTrinhMonId(syllabusMonHoc.getChuongTrinhMonId())
                    .syllabusMonHocId(syllabusMonHoc.getId())
                    .ma(mau.getMa())
                    .ten(mau.getTen())
                    .nguongTu(mau.getNguongTu())
                    .nguongDen(mau.getNguongDen())
                    .diemQuyDoi(mau.getDiemQuyDoi())
                    .ketQua(mau.getKetQua() == null ? null : mau.getKetQua().getValue())
                    .congThuc(mau.getCongThuc())
                    .ghiChu(mau.getGhiChu())
                    .loaiMau(mau.getLoaiMau())
                    .tyLe(mau.getTyLe())
                    .diemToiDa(mau.getDiemToiDa())
                    .thuTu(mau.getThuTu())
                    .batBuoc(mau.getBatBuoc())
                    .build();

            quyDoiDiemRepository.save(quyDoiDiem);
        }
    }

    private SyllabusMonHocResponse toResponseCoTongGio(SyllabusMonHoc entity) {
        SyllabusMonHocResponse response = mapper.toResponse(entity);
        if (entity == null || entity.getId() == null) {
            return response;
        }

        Long syllabusMonId = entity.getId();
        response.setTongGio(syllabusChuongBaiRepository.tongGioTheoSyllabusMonId(syllabusMonId));
        response.setGioLyThuyet(syllabusChuongBaiRepository.tongGioLyThuyetTheoSyllabusMonId(syllabusMonId));
        response.setGioThucHanh(syllabusChuongBaiRepository.tongGioThucHanhTheoSyllabusMonId(syllabusMonId));
        response.setGioKiemTra(syllabusChuongBaiRepository.tongGioKiemTraTheoSyllabusMonId(syllabusMonId));
        return response;
    }
}
