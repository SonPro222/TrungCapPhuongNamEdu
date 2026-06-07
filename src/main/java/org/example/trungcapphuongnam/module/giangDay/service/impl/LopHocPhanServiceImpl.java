package org.example.trungcapphuongnam.module.giangDay.service.impl;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMon;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHoc;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhMonRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocRepository;
import org.example.trungcapphuongnam.module.giangDay.GiangDayNotFoundException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.LopHocPhanRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LopHocPhanResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhanChuongTrinhMon;
import org.example.trungcapphuongnam.module.giangDay.enums.LoaiLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.mapper.LopHocPhanMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.LopHocPhanChuongTrinhMonRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.LopHocPhanRepository;
import org.example.trungcapphuongnam.module.giangDay.service.LopHocPhanService;
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
    private final LopHocPhanChuongTrinhMonRepository lopHocPhanChuongTrinhMonRepository;
    private final ChuongTrinhMonRepository chuongTrinhMonRepository;
    private final SyllabusMonHocRepository syllabusMonHocRepository;
    private final LopHocPhanMapper mapper;

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

        Long chuongTrinhMonIdGoc = request.getChuongTrinhMonId();

        LopHocPhan entity = mapper.toEntity(request);
        chuanHoaLopHocPhan(entity, chuongTrinhMonIdGoc);

        LopHocPhan saved = repository.save(entity);

        if (saved.getLoaiLopHocPhan() == LoaiLopHocPhan.HOC_CHUNG && chuongTrinhMonIdGoc != null) {
            ganChuongTrinhMonChoLopHocChung(saved.getId(), chuongTrinhMonIdGoc);
        }

        return mapper.toResponse(saved);
    }

    @Override
    public LopHocPhanResponse update(Long id, LopHocPhanRequest request) {
        validator.validateUpdate(id, request);

        Long chuongTrinhMonIdGoc = request.getChuongTrinhMonId();

        LopHocPhan entity = findEntity(id);
        mapper.updateEntity(entity, request);
        chuanHoaLopHocPhan(entity, chuongTrinhMonIdGoc);

        LopHocPhan saved = repository.save(entity);

        if (saved.getLoaiLopHocPhan() == LoaiLopHocPhan.HOC_CHUNG && chuongTrinhMonIdGoc != null) {
            ganChuongTrinhMonChoLopHocChung(saved.getId(), chuongTrinhMonIdGoc);
        }

        return mapper.toResponse(saved);
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

    private void chuanHoaLopHocPhan(LopHocPhan entity, Long chuongTrinhMonIdGoc) {
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
            if (entity.getChuongTrinhMonId() == null) {
                throw new GiangDayNotFoundException("Lớp học phần chuyên ngành phải có chương trình môn");
            }

            ChuongTrinhMon chuongTrinhMon = chuongTrinhMonRepository.findById(entity.getChuongTrinhMonId())
                    .orElseThrow(() -> new GiangDayNotFoundException("Chương trình môn của lớp học phần không tồn tại"));

            entity.setMonHocId(chuongTrinhMon.getMonHocId());
            entity.setSoBuoiHoc(laySoBuoiHocTuSyllabus(entity.getChuongTrinhMonId()));
        }

        if (entity.getLoaiLopHocPhan() == LoaiLopHocPhan.HOC_CHUNG) {
            if (chuongTrinhMonIdGoc != null) {
                ChuongTrinhMon chuongTrinhMon = chuongTrinhMonRepository.findById(chuongTrinhMonIdGoc)
                        .orElseThrow(() -> new GiangDayNotFoundException("Chương trình môn của lớp học phần không tồn tại"));

                entity.setMonHocId(chuongTrinhMon.getMonHocId());
                entity.setSoBuoiHoc(laySoBuoiHocTuSyllabus(chuongTrinhMonIdGoc));
            }

            if (entity.getMonHocId() == null) {
                throw new GiangDayNotFoundException("Lớp học chung phải có môn học");
            }

            if (entity.getSoBuoiHoc() == null || entity.getSoBuoiHoc() < 1) {
                throw new GiangDayNotFoundException("Lớp học chung phải có số buổi học từ syllabus");
            }

            entity.setChuongTrinhMonId(null);
        }
    }

    private void ganChuongTrinhMonChoLopHocChung(Long lopHocPhanId, Long chuongTrinhMonId) {
        if (lopHocPhanId == null || chuongTrinhMonId == null) {
            return;
        }

        boolean daGan = lopHocPhanChuongTrinhMonRepository.existsByLopHocPhanIdAndChuongTrinhMonId(
                lopHocPhanId,
                chuongTrinhMonId
        );

        if (daGan) {
            return;
        }

        LopHocPhanChuongTrinhMon entity = LopHocPhanChuongTrinhMon.builder()
                .lopHocPhanId(lopHocPhanId)
                .chuongTrinhMonId(chuongTrinhMonId)
                .build();

        lopHocPhanChuongTrinhMonRepository.save(entity);
    }

    private Integer laySoBuoiHocTuSyllabus(Long chuongTrinhMonId) {
        SyllabusMonHoc syllabus = syllabusMonHocRepository.findFirstByChuongTrinhMonId(chuongTrinhMonId)
                .orElseThrow(() -> new GiangDayNotFoundException(
                        "Môn này chưa có syllabus đã lưu vào version, không thể mở/cập nhật lớp học phần"
                ));

        Integer soBuoiHoc = syllabus.getSoBuoiHoc();

        if (soBuoiHoc == null || soBuoiHoc < 1) {
            throw new GiangDayNotFoundException("Syllabus của môn chưa có số buổi học hợp lệ");
        }

        return soBuoiHoc;
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