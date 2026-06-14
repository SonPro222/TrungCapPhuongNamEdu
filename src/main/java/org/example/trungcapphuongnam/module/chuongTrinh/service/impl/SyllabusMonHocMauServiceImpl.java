package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.*;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.*;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.*;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.*;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.*;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocMauFileService;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocMauService;
import org.example.trungcapphuongnam.module.chuongTrinh.service.XoaChuongTrinhCascadeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SyllabusMonHocMauServiceImpl implements SyllabusMonHocMauService {

    private final SyllabusMonHocMauRepository repository;
    private final SyllabusMonHocMauChuongBaiRepository syllabusMonHocMauChuongBaiRepository;
    private final SyllabusMonHocMauDieuKienRepository syllabusMonHocMauDieuKienRepository;
    private final SyllabusMonHocMauTaiLieuRepository syllabusMonHocMauTaiLieuRepository;
    private final CauHinhDanhGiaMauRepository cauHinhDanhGiaMauRepository;
    private final QuyDoiDiemMauRepository quyDoiDiemMauRepository;
    private final SyllabusMonHocMauFileRepository syllabusMonHocMauFileRepository;

    private final SyllabusMonHocMauMapper mapper;
    private final SyllabusMonHocMauChuongBaiMapper chuongBaiMapper;
    private final SyllabusMonHocMauDieuKienMapper dieuKienMapper;
    private final SyllabusMonHocMauTaiLieuMapper taiLieuMapper;
    private final CauHinhDanhGiaMauMapper cauHinhDanhGiaMauMapper;
    private final QuyDoiDiemMauMapper quyDoiDiemMauMapper;
    private final SyllabusMonHocMauFileService syllabusMonHocMauFileService;

    private final ChuongTrinhNghiepVuValidator validator;
    private final XoaChuongTrinhCascadeService xoaChuongTrinhCascadeService;

    @Override
    @Transactional(readOnly = true)
    public Page<SyllabusMonHocMauResponse> findAll(Long monHocId, String ma, String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.<SyllabusMonHocMau>empty()
                        .and(LocJpa.eq("monHocId", monHocId))
                        .and(LocJpa.like("ma", ma))
                        .and(LocJpa.keyword(keyword, "ma", "ten", "mucTieu", "phuongPhapDanhGia", "ghiChu")),
                pageable
        ).map(this::toResponseCoTongGio);
    }

    @Override
    @Transactional(readOnly = true)
    public SyllabusMonHocMauResponse findById(Long id) {
        SyllabusMonHocMau entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Syllabus môn học gốc không tồn tại: " + id));
        return toResponseCoTongGio(entity);
    }

    @Override
    public SyllabusMonHocMauResponse create(SyllabusMonHocMauRequest request) {
        validator.validateSyllabusMonHocMau(request, null);

        SyllabusMonHocMau entity = mapper.toEntity(request);

        if (entity.getBatBuocDuThi() == null) {
            entity.setBatBuocDuThi(false);
        }

        return toResponseCoTongGio(repository.save(entity));
    }

    @Override
    public SyllabusMonHocMauResponse update(Long id, SyllabusMonHocMauRequest request) {
        SyllabusMonHocMau entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Syllabus môn học gốc không tồn tại: " + id));

        validator.validateSyllabusMonHocMau(request, id);

        mapper.updateEntity(entity, request);

        if (entity.getBatBuocDuThi() == null) {
            entity.setBatBuocDuThi(false);
        }

        return toResponseCoTongGio(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Syllabus môn học gốc không tồn tại: " + id);
        }

        xoaChuongTrinhCascadeService.xoaTheoSyllabusMonHocMauId(id);

        repository.deleteById(id);
    }

    // ==================== Full CRUD ====================

    @Override
    public SyllabusMonHocMauFullResponse createFull(SyllabusMonHocMauFullRequest request) {
        if (request == null || request.getSyllabusMonHocMau() == null) {
            throw new BadRequestException("syllabusMonHocMau không được null");
        }

        // 1. Tạo syllabus chính
        validator.validateSyllabusMonHocMau(request.getSyllabusMonHocMau(), null);
        SyllabusMonHocMau entity = mapper.toEntity(request.getSyllabusMonHocMau());
        if (entity.getBatBuocDuThi() == null) entity.setBatBuocDuThi(false);
        SyllabusMonHocMau saved = repository.save(entity);
        Long syllabusId = saved.getId();

        // 2. Chương bài — validate giờ, tính tổng từ danh sách
        List<SyllabusMonHocMauChuongBaiResponse> chuongBaiResponses = luuChuongBai(syllabusId, request.getChuongBaiList());

        // 3. Điều kiện
        List<SyllabusMonHocMauDieuKienResponse> dieuKienResponses = luuDieuKien(syllabusId, request.getDieuKienList());

        // 4. Tài liệu
        List<SyllabusMonHocMauTaiLieuResponse> taiLieuResponses = luuTaiLieu(syllabusId, request.getTaiLieuList());

        // 5. Cấu hình đánh giá
        List<CauHinhDanhGiaMauResponse> cauHinhResponses = luuCauHinhDanhGia(syllabusId, request.getCauHinhDanhGiaList());

        // 6. Quy đổi điểm
        List<QuyDoiDiemMauResponse> quyDoiResponses = luuQuyDoiDiem(syllabusId, request.getQuyDoiDiemList());

        return buildFullResponse(saved, chuongBaiResponses, dieuKienResponses, taiLieuResponses,
                cauHinhResponses, quyDoiResponses);
    }

    @Override
    public SyllabusMonHocMauFullResponse updateFull(Long id, SyllabusMonHocMauFullRequest request) {
        SyllabusMonHocMau entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Syllabus môn học gốc không tồn tại: " + id));

        if (request == null || request.getSyllabusMonHocMau() == null) {
            throw new BadRequestException("syllabusMonHocMau không được null");
        }

        // 1. Cập nhật thông tin chính
        validator.validateSyllabusMonHocMau(request.getSyllabusMonHocMau(), id);
        mapper.updateEntity(entity, request.getSyllabusMonHocMau());
        if (entity.getBatBuocDuThi() == null) entity.setBatBuocDuThi(false);
        SyllabusMonHocMau saved = repository.save(entity);

        // 2. Chương bài — xóa cũ, tạo lại
        syllabusMonHocMauChuongBaiRepository.deleteBySyllabusMonHocMauId(id);
        List<SyllabusMonHocMauChuongBaiResponse> chuongBaiResponses = luuChuongBai(id, request.getChuongBaiList());

        // 3. Điều kiện — xóa cũ, tạo lại
        syllabusMonHocMauDieuKienRepository.deleteBySyllabusMonHocMauId(id);
        List<SyllabusMonHocMauDieuKienResponse> dieuKienResponses = luuDieuKien(id, request.getDieuKienList());

        // 4. Tài liệu — xóa cũ, tạo lại
        syllabusMonHocMauTaiLieuRepository.deleteBySyllabusMonHocMauId(id);
        List<SyllabusMonHocMauTaiLieuResponse> taiLieuResponses = luuTaiLieu(id, request.getTaiLieuList());

        // 5. Cấu hình đánh giá — xóa cũ, tạo lại
        cauHinhDanhGiaMauRepository.findBySyllabusMonHocMauIdOrderByThuTuAscIdAsc(id)
                .forEach(c -> cauHinhDanhGiaMauRepository.deleteById(c.getId()));
        List<CauHinhDanhGiaMauResponse> cauHinhResponses = luuCauHinhDanhGia(id, request.getCauHinhDanhGiaList());

        // 6. Quy đổi điểm — xóa cũ, tạo lại
        quyDoiDiemMauRepository.findBySyllabusMonHocMauIdOrderByThuTuAscIdAsc(id)
                .forEach(q -> quyDoiDiemMauRepository.deleteById(q.getId()));
        List<QuyDoiDiemMauResponse> quyDoiResponses = luuQuyDoiDiem(id, request.getQuyDoiDiemList());

        // File gốc không xóa khi updateFull
        return buildFullResponse(saved, chuongBaiResponses, dieuKienResponses, taiLieuResponses,
                cauHinhResponses, quyDoiResponses);
    }

    @Override
    @Transactional(readOnly = true)
    public SyllabusMonHocMauFullResponse getFull(Long id) {
        SyllabusMonHocMau entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Syllabus môn học gốc không tồn tại: " + id));

        List<SyllabusMonHocMauChuongBaiResponse> chuongBai = syllabusMonHocMauChuongBaiRepository
                .findBySyllabusMonHocMauIdOrderByThuTuAscIdAsc(id)
                .stream().map(chuongBaiMapper::toResponse).collect(Collectors.toList());

        List<SyllabusMonHocMauDieuKienResponse> dieuKien = syllabusMonHocMauDieuKienRepository
                .findBySyllabusMonHocMauIdOrderByThuTuAscIdAsc(id)
                .stream().map(dieuKienMapper::toResponse).collect(Collectors.toList());

        List<SyllabusMonHocMauTaiLieuResponse> taiLieu = syllabusMonHocMauTaiLieuRepository
                .findBySyllabusMonHocMauIdOrderByThuTuAscIdAsc(id)
                .stream().map(taiLieuMapper::toResponse).collect(Collectors.toList());

        List<CauHinhDanhGiaMauResponse> cauHinh = cauHinhDanhGiaMauRepository
                .findBySyllabusMonHocMauIdOrderByThuTuAscIdAsc(id)
                .stream().map(cauHinhDanhGiaMauMapper::toResponse).collect(Collectors.toList());

        List<QuyDoiDiemMauResponse> quyDoi = quyDoiDiemMauRepository
                .findBySyllabusMonHocMauIdOrderByThuTuAscIdAsc(id)
                .stream().map(quyDoiDiemMauMapper::toResponse).collect(Collectors.toList());

        List<SyllabusMonHocMauFileResponse> files = syllabusMonHocMauFileService.findBySyllabusMonHocMauId(id);

        SyllabusMonHocMauResponse syllabusResponse = toResponseCoTongGioTuDanh(entity, chuongBai);

        return SyllabusMonHocMauFullResponse.builder()
                .syllabusMonHocMau(syllabusResponse)
                .chuongBaiList(chuongBai)
                .dieuKienList(dieuKien)
                .taiLieuList(taiLieu)
                .cauHinhDanhGiaList(cauHinh)
                .quyDoiDiemList(quyDoi)
                .fileList(files)
                .build();
    }

    // ==================== Private helpers ====================

    private List<SyllabusMonHocMauChuongBaiResponse> luuChuongBai(Long syllabusId,
                                                                   List<SyllabusMonHocMauChuongBaiRequest> list) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        List<SyllabusMonHocMauChuongBaiResponse> result = new ArrayList<>();
        for (SyllabusMonHocMauChuongBaiRequest req : list) {
            validateGioChuongBai(req);
            SyllabusMonHocMauChuongBai cb = chuongBaiMapper.toEntity(req);
            cb.setSyllabusMonHocMauId(syllabusId);
            result.add(chuongBaiMapper.toResponse(syllabusMonHocMauChuongBaiRepository.save(cb)));
        }
        return result;
    }

    private List<SyllabusMonHocMauDieuKienResponse> luuDieuKien(Long syllabusId,
                                                                  List<SyllabusMonHocMauDieuKienRequest> list) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        List<SyllabusMonHocMauDieuKienResponse> result = new ArrayList<>();
        for (SyllabusMonHocMauDieuKienRequest req : list) {
            SyllabusMonHocMauDieuKien dk = dieuKienMapper.toEntity(req);
            dk.setSyllabusMonHocMauId(syllabusId);
            result.add(dieuKienMapper.toResponse(syllabusMonHocMauDieuKienRepository.save(dk)));
        }
        return result;
    }

    private List<SyllabusMonHocMauTaiLieuResponse> luuTaiLieu(Long syllabusId,
                                                               List<SyllabusMonHocMauTaiLieuRequest> list) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        List<SyllabusMonHocMauTaiLieuResponse> result = new ArrayList<>();
        for (SyllabusMonHocMauTaiLieuRequest req : list) {
            SyllabusMonHocMauTaiLieu tl = taiLieuMapper.toEntity(req);
            tl.setSyllabusMonHocMauId(syllabusId);
            result.add(taiLieuMapper.toResponse(syllabusMonHocMauTaiLieuRepository.save(tl)));
        }
        return result;
    }

    private List<CauHinhDanhGiaMauResponse> luuCauHinhDanhGia(Long syllabusId,
                                                               List<CauHinhDanhGiaMauRequest> list) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        List<CauHinhDanhGiaMauResponse> result = new ArrayList<>();
        for (CauHinhDanhGiaMauRequest req : list) {
            if (req.getTyLe() != null && req.getTyLe().compareTo(BigDecimal.ZERO) < 0) {
                throw new BadRequestException("tyLe cấu hình đánh giá không được âm");
            }
            CauHinhDanhGiaMau ch = cauHinhDanhGiaMauMapper.toEntity(req);
            ch.setSyllabusMonHocMauId(syllabusId);
            result.add(cauHinhDanhGiaMauMapper.toResponse(cauHinhDanhGiaMauRepository.save(ch)));
        }
        return result;
    }

    private List<QuyDoiDiemMauResponse> luuQuyDoiDiem(Long syllabusId,
                                                       List<QuyDoiDiemMauRequest> list) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        List<QuyDoiDiemMauResponse> result = new ArrayList<>();
        for (QuyDoiDiemMauRequest req : list) {
            QuyDoiDiemMau qd = quyDoiDiemMauMapper.toEntity(req);
            qd.setSyllabusMonHocMauId(syllabusId);
            result.add(quyDoiDiemMauMapper.toResponse(quyDoiDiemMauRepository.save(qd)));
        }
        return result;
    }

    private void validateGioChuongBai(SyllabusMonHocMauChuongBaiRequest req) {
        if (req.getTongGio() != null && req.getTongGio().compareTo(BigDecimal.ZERO) < 0)
            throw new BadRequestException("tongGio chương/bài không được âm: " + req.getTen());
        if (req.getGioLyThuyet() != null && req.getGioLyThuyet().compareTo(BigDecimal.ZERO) < 0)
            throw new BadRequestException("gioLyThuyet không được âm: " + req.getTen());
        if (req.getGioThucHanh() != null && req.getGioThucHanh().compareTo(BigDecimal.ZERO) < 0)
            throw new BadRequestException("gioThucHanh không được âm: " + req.getTen());
        if (req.getGioKiemTra() != null && req.getGioKiemTra().compareTo(BigDecimal.ZERO) < 0)
            throw new BadRequestException("gioKiemTra không được âm: " + req.getTen());
    }

    private SyllabusMonHocMauFullResponse buildFullResponse(SyllabusMonHocMau saved,
                                                            List<SyllabusMonHocMauChuongBaiResponse> chuongBai,
                                                            List<SyllabusMonHocMauDieuKienResponse> dieuKien,
                                                            List<SyllabusMonHocMauTaiLieuResponse> taiLieu,
                                                            List<CauHinhDanhGiaMauResponse> cauHinh,
                                                            List<QuyDoiDiemMauResponse> quyDoi) {
        List<SyllabusMonHocMauFileResponse> files = syllabusMonHocMauFileService.findBySyllabusMonHocMauId(saved.getId());
        SyllabusMonHocMauResponse syllabusResponse = toResponseCoTongGioTuDanh(saved, chuongBai);
        return SyllabusMonHocMauFullResponse.builder()
                .syllabusMonHocMau(syllabusResponse)
                .chuongBaiList(chuongBai)
                .dieuKienList(dieuKien)
                .taiLieuList(taiLieu)
                .cauHinhDanhGiaList(cauHinh)
                .quyDoiDiemList(quyDoi)
                .fileList(files)
                .build();
    }

    /** Tính tổng giờ từ danh sách chương/bài đã có (không truy vấn thêm DB) */
    private SyllabusMonHocMauResponse toResponseCoTongGioTuDanh(SyllabusMonHocMau entity,
                                                                  List<SyllabusMonHocMauChuongBaiResponse> chuongBai) {
        SyllabusMonHocMauResponse response = mapper.toResponse(entity);
        if (chuongBai == null || chuongBai.isEmpty()) {
            response.setTongGio(BigDecimal.ZERO);
            response.setGioLyThuyet(BigDecimal.ZERO);
            response.setGioThucHanh(BigDecimal.ZERO);
            response.setGioKiemTra(BigDecimal.ZERO);
            return response;
        }
        response.setTongGio(chuongBai.stream()
                .map(c -> c.getTongGio() != null ? c.getTongGio() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        response.setGioLyThuyet(chuongBai.stream()
                .map(c -> c.getGioLyThuyet() != null ? c.getGioLyThuyet() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        response.setGioThucHanh(chuongBai.stream()
                .map(c -> c.getGioThucHanh() != null ? c.getGioThucHanh() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        response.setGioKiemTra(chuongBai.stream()
                .map(c -> c.getGioKiemTra() != null ? c.getGioKiemTra() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        return response;
    }

    private SyllabusMonHocMauResponse toResponseCoTongGio(SyllabusMonHocMau entity) {
        SyllabusMonHocMauResponse response = mapper.toResponse(entity);
        if (entity == null || entity.getId() == null) {
            return response;
        }

        Long syllabusMonHocMauId = entity.getId();
        response.setTongGio(syllabusMonHocMauChuongBaiRepository.tongGioTheoSyllabusMonHocMauId(syllabusMonHocMauId));
        response.setGioLyThuyet(syllabusMonHocMauChuongBaiRepository.tongGioLyThuyetTheoSyllabusMonHocMauId(syllabusMonHocMauId));
        response.setGioThucHanh(syllabusMonHocMauChuongBaiRepository.tongGioThucHanhTheoSyllabusMonHocMauId(syllabusMonHocMauId));
        response.setGioKiemTra(syllabusMonHocMauChuongBaiRepository.tongGioKiemTraTheoSyllabusMonHocMauId(syllabusMonHocMauId));
        return response;
    }
}
