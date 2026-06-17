package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusChuongTrinhRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusChuongTrinhResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.*;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.SyllabusChuongTrinhMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.*;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusChuongTrinhService;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Transactional
public class SyllabusChuongTrinhServiceImpl implements SyllabusChuongTrinhService {

    private final ChuongTrinhNghiepVuValidator validator;
    private final SyllabusChuongTrinhRepository repository;
    private final SyllabusChuongTrinhMauRepository syllabusChuongTrinhMauRepository;
    private final SyllabusChuongTrinhMapper mapper;
    private final MucTieuChuongTrinhRepository mucTieuChuongTrinhRepository;
    private final NangLucDauRaRepository nangLucDauRaRepository;
    private final ViTriViecLamRepository viTriViecLamRepository;
    private final DieuKienTotNghiepRepository dieuKienTotNghiepRepository;
    private final MucTieuChuongTrinhMauRepository mucTieuChuongTrinhMauRepository;
    private final NangLucDauRaMauRepository nangLucDauRaMauRepository;
    private final ViTriViecLamMauRepository viTriViecLamMauRepository;
    private final DieuKienTotNghiepMauRepository dieuKienTotNghiepMauRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<SyllabusChuongTrinhResponse> findAll(
            Long chuongTrinhVersionId,
            Long syllabusChuongTrinhMauId,
            String keyword,
            Pageable pageable
    ) {
        return repository.findAll(
                LocJpa.<SyllabusChuongTrinh>empty()
                        .and(LocJpa.eq("chuongTrinhVersionId", chuongTrinhVersionId))
                        .and(LocJpa.eq("syllabusChuongTrinhMauId", syllabusChuongTrinhMauId))
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
    public SyllabusChuongTrinhResponse dongBoTuSyllabusMau(
            Long syllabusChuongTrinhMauId,
            Long chuongTrinhVersionId
    ) {
        if (syllabusChuongTrinhMauId == null || chuongTrinhVersionId == null) {
            throw new IllegalArgumentException("Thiếu syllabusChuongTrinhMauId hoặc chuongTrinhVersionId để đồng bộ syllabus chương trình");
        }

        SyllabusChuongTrinhMau syllabusMau = syllabusChuongTrinhMauRepository.findById(syllabusChuongTrinhMauId)
                .orElseThrow(() -> new ResourceNotFoundException("Syllabus chương trình mẫu không tồn tại: " + syllabusChuongTrinhMauId));

        return dongBoTuSyllabusMau(syllabusMau, chuongTrinhVersionId);
    }

    @Override
    public SyllabusChuongTrinhResponse dongBoTuSyllabusMau(
            SyllabusChuongTrinhMau syllabusMau,
            Long chuongTrinhVersionId
    ) {
        if (syllabusMau == null || syllabusMau.getId() == null || chuongTrinhVersionId == null) {
            throw new IllegalArgumentException("Thiếu dữ liệu đồng bộ syllabus chương trình");
        }

        SyllabusChuongTrinh entity = layHoacTaoSyllabusApDungDuyNhat(chuongTrinhVersionId);

        entity.setChuongTrinhVersionId(chuongTrinhVersionId);
        entity.setSyllabusChuongTrinhMauId(syllabusMau.getId());

        entity.setMa(syllabusMau.getMa());
        entity.setTen(syllabusMau.getTen());
        entity.setDuongDan(syllabusMau.getDuongDan());

        entity.setMucTieu(syllabusMau.getMucTieu());
        entity.setDoiTuongTuyenSinh(syllabusMau.getDoiTuongTuyenSinh());
        entity.setThoiGianDaoTao(syllabusMau.getThoiGianDaoTao());
        entity.setKhoiLuongKienThuc(syllabusMau.getKhoiLuongKienThuc());
        entity.setDieuKienTotNghiep(syllabusMau.getDieuKienTotNghiep());
        entity.setPhuongPhapDaoTao(syllabusMau.getPhuongPhapDaoTao());
        entity.setPhuongPhapDanhGia(syllabusMau.getPhuongPhapDanhGia());
        entity.setHuongDanThucHien(syllabusMau.getHuongDanThucHien());
        entity.setGhiChu(syllabusMau.getGhiChu());

        SyllabusChuongTrinh saved = repository.save(entity);
        copyBangConTuSyllabusMau(saved.getId(), syllabusMau.getId());
        return mapper.toResponse(saved);
    }

    private SyllabusChuongTrinh layHoacTaoSyllabusApDungDuyNhat(Long chuongTrinhVersionId) {
        List<SyllabusChuongTrinh> list = repository.findByChuongTrinhVersionIdOrderByIdDesc(chuongTrinhVersionId);
        if (list.isEmpty()) {
            return new SyllabusChuongTrinh();
        }

        SyllabusChuongTrinh entity = list.get(0);

        if (list.size() > 1) {
            list.stream()
                    .skip(1)
                    .map(SyllabusChuongTrinh::getId)
                    .forEach(this::xoaBangConVaSyllabusApDung);
        }

        return entity;
    }

    private void xoaBangConVaSyllabusApDung(Long syllabusChuongTrinhId) {
        if (syllabusChuongTrinhId == null) return;

        mucTieuChuongTrinhRepository.deleteBySyllabusChuongTrinhId(syllabusChuongTrinhId);
        nangLucDauRaRepository.deleteBySyllabusChuongTrinhId(syllabusChuongTrinhId);
        viTriViecLamRepository.deleteBySyllabusChuongTrinhId(syllabusChuongTrinhId);
        dieuKienTotNghiepRepository.deleteBySyllabusChuongTrinhId(syllabusChuongTrinhId);
        repository.deleteById(syllabusChuongTrinhId);
    }

    private void copyBangConTuSyllabusMau(Long syllabusChuongTrinhId, Long syllabusChuongTrinhMauId) {
        mucTieuChuongTrinhRepository.deleteBySyllabusChuongTrinhId(syllabusChuongTrinhId);
        nangLucDauRaRepository.deleteBySyllabusChuongTrinhId(syllabusChuongTrinhId);
        viTriViecLamRepository.deleteBySyllabusChuongTrinhId(syllabusChuongTrinhId);
        dieuKienTotNghiepRepository.deleteBySyllabusChuongTrinhId(syllabusChuongTrinhId);

        AtomicInteger thuTuMucTieu = new AtomicInteger(1);
        List<MucTieuChuongTrinh> mucTieu = mucTieuChuongTrinhMauRepository
                .findBySyllabusChuongTrinhMauId(syllabusChuongTrinhMauId, Pageable.unpaged())
                .getContent()
                .stream()
                .map(Mau -> MucTieuChuongTrinh.builder()
                        .syllabusChuongTrinhId(syllabusChuongTrinhId)
                        .ma(Mau.getMa())
                        .loai(Mau.getLoai() == null ? null : Mau.getLoai().getValue())
                        .noiDung(Mau.getNoiDung())
                        .thuTu(thuTuMucTieu.getAndIncrement())
                        .ghiChu(Mau.getGhiChu())
                        .build())
                .toList();
        mucTieuChuongTrinhRepository.saveAll(mucTieu);

        AtomicInteger thuTuNangLuc = new AtomicInteger(1);
        List<NangLucDauRa> nangLuc = nangLucDauRaMauRepository
                .findBySyllabusChuongTrinhMauId(syllabusChuongTrinhMauId, Pageable.unpaged())
                .getContent()
                .stream()
                .map(Mau -> NangLucDauRa.builder()
                        .syllabusChuongTrinhId(syllabusChuongTrinhId)
                        .ma(Mau.getMa())
                        .loai(Mau.getLoai() == null ? null : Mau.getLoai().getValue())
                        .noiDung(Mau.getNoiDung())
                        .thuTu(thuTuNangLuc.getAndIncrement())
                        .ghiChu(Mau.getGhiChu())
                        .build())
                .toList();
        nangLucDauRaRepository.saveAll(nangLuc);

        AtomicInteger thuTuViTri = new AtomicInteger(1);
        List<ViTriViecLam> viTri = viTriViecLamMauRepository
                .findBySyllabusChuongTrinhMauId(syllabusChuongTrinhMauId, Pageable.unpaged())
                .getContent()
                .stream()
                .map(Mau -> ViTriViecLam.builder()
                        .syllabusChuongTrinhId(syllabusChuongTrinhId)
                        .ma(Mau.getMa())
                        .ten(Mau.getTen())
                        .moTa(Mau.getMoTa())
                        .thuTu(thuTuViTri.getAndIncrement())
                        .ghiChu(Mau.getGhiChu())
                        .build())
                .toList();
        viTriViecLamRepository.saveAll(viTri);

        AtomicInteger thuTuDieuKien = new AtomicInteger(1);
        List<DieuKienTotNghiep> dieuKien = dieuKienTotNghiepMauRepository
                .findBySyllabusChuongTrinhMauId(syllabusChuongTrinhMauId, Pageable.unpaged())
                .getContent()
                .stream()
                .map(Mau -> DieuKienTotNghiep.builder()
                        .syllabusChuongTrinhId(syllabusChuongTrinhId)
                        .ma(Mau.getMa())
                        .noiDung(Mau.getNoiDung())
                        .thuTu(thuTuDieuKien.getAndIncrement())
                        .ghiChu(Mau.getGhiChu())
                        .build())
                .toList();
        dieuKienTotNghiepRepository.saveAll(dieuKien);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Syllabus chương trình không tồn tại: " + id);
        }

        xoaBangConVaSyllabusApDung(id);
    }
}
