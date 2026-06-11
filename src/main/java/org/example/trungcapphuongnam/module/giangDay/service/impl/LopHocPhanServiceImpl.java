package org.example.trungcapphuongnam.module.giangDay.service.impl;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMon;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhMonRepository;
import org.example.trungcapphuongnam.module.giangDay.GiangDayException;
import org.example.trungcapphuongnam.module.giangDay.GiangDayNotFoundException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.LopHocPhanRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LopHocPhanResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.enums.LoaiLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.mapper.LopHocPhanMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.LopHocPhanRepository;
import org.example.trungcapphuongnam.module.giangDay.service.LopHocPhanService;
import org.example.trungcapphuongnam.module.giangDay.service.SaoChepCauHinhDanhGiaService;
import org.example.trungcapphuongnam.module.giangDay.validator.LopHocPhanValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LopHocPhanServiceImpl implements LopHocPhanService {

    private final LopHocPhanValidator validator;
    private final LopHocPhanRepository repository;
    private final ChuongTrinhMonRepository chuongTrinhMonRepository;
    private final LopHocPhanMapper mapper;
    private final SaoChepCauHinhDanhGiaService saoChepCauHinhDanhGiaService;

    @Override
    @Transactional(readOnly = true)
    public Page<LopHocPhanResponse> getAll(
            String keyword,
            Long chuongTrinhMonId,
            Long monHocId,
            LoaiLopHocPhan loaiLopHocPhan,
            TrangThaiLopHocPhan trangThai,
            LocalDate tuNgay,
            LocalDate denNgay,
            Pageable pageable
    ) {
        return repository.findAll(
                        buildSpecification(keyword, chuongTrinhMonId, monHocId, loaiLopHocPhan, trangThai, tuNgay, denNgay),
                        pageable
                )
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public LopHocPhanResponse getById(Long id) {
        return mapper.toResponse(findEntity(id));
    }

    @Override
    public LopHocPhanResponse create(LopHocPhanRequest request) {
        validator.validateCreate(request);

        LopHocPhan entity = mapper.toEntity(request);
        chuanHoaLopHocPhan(entity);

        LopHocPhan saved = repository.save(entity);

        if (saved.getLoaiLopHocPhan() == LoaiLopHocPhan.CHUYEN_NGANH && saved.getChuongTrinhMonId() != null) {
            saoChepCauHinhDanhGiaService.saoChepTuChuongTrinhMonSangLopHocPhan(
                    saved.getId(),
                    saved.getChuongTrinhMonId()
            );
        }

        return mapper.toResponse(saved);
    }

    @Override
    public LopHocPhanResponse update(Long id, LopHocPhanRequest request) {
        validator.validateUpdate(id, request);

        LopHocPhan entity = findEntity(id);
        mapper.updateEntity(entity, request);
        chuanHoaLopHocPhan(entity);

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        LopHocPhan entity = findEntity(id);
        repository.delete(entity);
    }

    private LopHocPhan findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GiangDayNotFoundException("Lớp học phần không tồn tại với id = " + id));
    }

    private void chuanHoaLopHocPhan(LopHocPhan entity) {
        if (entity.getLoaiLopHocPhan() == null) {
            entity.setLoaiLopHocPhan(LoaiLopHocPhan.CHUYEN_NGANH);
        }

        if (entity.getSiSoToiThieu() == null) {
            entity.setSiSoToiThieu(1);
        }

        if (entity.getSoLuongHienTai() == null) {
            entity.setSoLuongHienTai(0);
        }

        if (entity.getLoaiLopHocPhan() == LoaiLopHocPhan.CHUYEN_NGANH) {
            ChuongTrinhMon chuongTrinhMon = chuongTrinhMonRepository.findById(entity.getChuongTrinhMonId())
                    .orElseThrow(() -> new GiangDayNotFoundException("Chương trình môn của lớp học phần không tồn tại"));

            entity.setMonHocId(chuongTrinhMon.getMonHocId());
            validateSoBuoiHocCuaLopHocPhan(entity);
        }

        if (entity.getLoaiLopHocPhan() == LoaiLopHocPhan.HOC_CHUNG) {
            entity.setChuongTrinhMonId(null);
            if (entity.getSoBuoiHoc() == null || entity.getSoBuoiHoc() < 1) {
                throw new GiangDayException("Lớp học chung phải cấu hình số buổi học");
            }
        }
    }


    private void validateSoBuoiHocCuaLopHocPhan(LopHocPhan entity) {
        if (entity.getSoBuoiHoc() == null || entity.getSoBuoiHoc() < 1) {
            throw new GiangDayException("Lớp học phần phải có số buổi học hợp lệ để phân bố lịch học");
        }
    }

    private Specification<LopHocPhan> buildSpecification(
            String keyword,
            Long chuongTrinhMonId,
            Long monHocId,
            LoaiLopHocPhan loaiLopHocPhan,
            TrangThaiLopHocPhan trangThai,
            LocalDate tuNgay,
            LocalDate denNgay
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.isBlank()) {
                String pattern = "%" + keyword.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("maLop")), pattern),
                        cb.like(cb.lower(root.get("tenLop")), pattern)
                ));
            }

            if (chuongTrinhMonId != null) {
                predicates.add(cb.equal(root.get("chuongTrinhMonId"), chuongTrinhMonId));
            }

            if (monHocId != null) {
                predicates.add(cb.equal(root.get("monHocId"), monHocId));
            }

            if (loaiLopHocPhan != null) {
                predicates.add(cb.equal(root.get("loaiLopHocPhan"), loaiLopHocPhan));
            }

            if (trangThai != null) {
                predicates.add(cb.equal(root.get("trangThai"), trangThai));
            }

            if (tuNgay != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("ngayBatDau"), tuNgay));
            }

            if (denNgay != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("ngayKetThuc"), denNgay));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}