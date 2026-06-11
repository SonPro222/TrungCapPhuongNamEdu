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
            Long syllabusMonHocMauId,
            Boolean batBuocDuThi,
            String keyword,
            Pageable pageable
    ) {
        return repository.findAll(
                LocJpa.<SyllabusMonHoc>empty()
                        .and(LocJpa.eq("chuongTrinhMonId", chuongTrinhMonId))
                        .and(LocJpa.eq("syllabusMonHocMauId", syllabusMonHocMauId))
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
        saoChepQuyDoiDiemMauTuSyllabusMau(entity);
        return toResponseCoTongGio(entity);
    }

    @Override
    public SyllabusMonHocResponse update(Long id, SyllabusMonHocRequest request) {
        validator.validateSyllabusMonHoc(request, id);

        SyllabusMonHoc entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SyllabusMonHoc không tồn tại: " + id));

        Long oldSyllabusMonHocMauId = entity.getSyllabusMonHocMauId();
        mapper.updateEntity(entity, request);
        SyllabusMonHoc saved = repository.save(entity);

        if (saved.getSyllabusMonHocMauId() != null && !saved.getSyllabusMonHocMauId().equals(oldSyllabusMonHocMauId)) {
            saoChepQuyDoiDiemMauTuSyllabusMau(saved);
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

    private void saoChepQuyDoiDiemMauTuSyllabusMau(SyllabusMonHoc syllabusMonHoc) {
        if (syllabusMonHoc == null || syllabusMonHoc.getId() == null || syllabusMonHoc.getSyllabusMonHocMauId() == null) {
            return;
        }

        List<QuyDoiDiemMau> danhSachMau = quyDoiDiemMauRepository
                .findBySyllabusMonHocMauIdOrderByThuTuAscIdAsc(syllabusMonHoc.getSyllabusMonHocMauId());

        for (QuyDoiDiemMau Mau : danhSachMau) {
            String ten = Mau.getTen() == null ? Mau.getMa() : Mau.getTen();
            if (ten == null || ten.isBlank()) {
                continue;
            }

            if (quyDoiDiemRepository.existsTenCotDiemTrongSyllabus(syllabusMonHoc.getId(), ten, null)) {
                continue;
            }

            QuyDoiDiem quyDoiDiem = QuyDoiDiem.builder()
                    .chuongTrinhMonId(syllabusMonHoc.getChuongTrinhMonId())
                    .syllabusMonHocId(syllabusMonHoc.getId())
                    .ma(Mau.getMa())
                    .ten(Mau.getTen())
                    .nguongTu(Mau.getNguongTu())
                    .nguongDen(Mau.getNguongDen())
                    .diemQuyDoi(Mau.getDiemQuyDoi())
                    .ketQua(Mau.getKetQua() == null ? null : Mau.getKetQua().getValue())
                    .congThuc(Mau.getCongThuc())
                    .ghiChu(Mau.getGhiChu())
                    .loaiMau(Mau.getLoaiMau())
                    .tyLe(Mau.getTyLe())
                    .diemToiDa(Mau.getDiemToiDa())
                    .thuTu(Mau.getThuTu())
                    .batBuoc(Mau.getBatBuoc())
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
