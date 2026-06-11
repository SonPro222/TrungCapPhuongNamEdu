package org.example.trungcapphuongnam.module.giangDay.service.impl;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.giangDay.GiangDayNotFoundException;
import org.example.trungcapphuongnam.common.security.CurrentUserService;
import org.example.trungcapphuongnam.module.giangDay.GiangDayException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.LichHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LichHocResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.*;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLichHoc;
import org.example.trungcapphuongnam.module.giangDay.mapper.LichHocMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.CaHocRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.LichHocRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.LopHocPhanRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.PhongHocRepository;
import org.example.trungcapphuongnam.module.giangDay.service.LichHocService;
import org.example.trungcapphuongnam.module.giangDay.validator.LichHocValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiDiemDanh;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiSinhVienLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.repository.DiemDanhRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LichHocServiceImpl implements LichHocService {

    private final LichHocRepository repository;
    private final LopHocPhanRepository lopHocPhanRepository;
    private final GiaoVienRepository giaoVienRepository;
    private final PhongHocRepository phongHocRepository;
    private final CaHocRepository caHocRepository;
    private final LichHocMapper mapper;
    private final LichHocValidator validator;
    private final CurrentUserService currentUserService;
    private final DiemDanhRepository diemDanhRepository;
    @Override
    @Transactional(readOnly = true)
    public Page<LichHocResponse> getAll(
            Long lopHocPhanId,
            String keywordLop,
            String keywordGiaoVien,
            String keywordPhong,
            String keywordCa,
            TrangThaiLichHoc trangThai,
            LocalDate tuNgay,
            LocalDate denNgay,
            Pageable pageable
    ) {
        Page<LichHoc> page = repository.findAll(
                buildSpecification(
                        lopHocPhanId,
                        keywordLop,
                        keywordGiaoVien,
                        keywordPhong,
                        keywordCa,
                        trangThai,
                        tuNgay,
                        denNgay,
                        null,
                        null
                ),
                pageable
        );

        Map<Long, LopHocPhan> lopHocPhanMap = lopHocPhanRepository.findAllById(
                        page.getContent()
                                .stream()
                                .map(LichHoc::getLopHocPhanId)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet())
                )
                .stream()
                .collect(Collectors.toMap(LopHocPhan::getId, Function.identity()));

        Map<Long, GiaoVien> giaoVienMap = giaoVienRepository.findAllById(
                        page.getContent()
                                .stream()
                                .map(LichHoc::getGiaoVienId)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet())
                )
                .stream()
                .collect(Collectors.toMap(GiaoVien::getId, Function.identity()));

        Map<Long, PhongHoc> phongHocMap = phongHocRepository.findAllById(
                        page.getContent()
                                .stream()
                                .map(LichHoc::getPhongHocId)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet())
                )
                .stream()
                .collect(Collectors.toMap(PhongHoc::getId, Function.identity()));

        Map<Long, CaHoc> caHocMap = caHocRepository.findAllById(
                        page.getContent()
                                .stream()
                                .map(LichHoc::getCaHocId)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet())
                )
                .stream()
                .collect(Collectors.toMap(CaHoc::getId, Function.identity()));

        return page.map(item -> mapper.toResponse(
                item,
                lopHocPhanMap,
                giaoVienMap,
                phongHocMap,
                caHocMap,
                tinhSoBuoiDaXep(item)
        ));
    }

    @Override
    @Transactional(readOnly = true)
    public LichHocResponse getById(Long id) {
        LichHoc entity = findEntity(id);

        Map<Long, LopHocPhan> lopHocPhanMap = entity.getLopHocPhanId() == null
                ? Map.of()
                : lopHocPhanRepository.findAllById(List.of(entity.getLopHocPhanId()))
                .stream()
                .collect(Collectors.toMap(LopHocPhan::getId, Function.identity()));

        Map<Long, GiaoVien> giaoVienMap = entity.getGiaoVienId() == null
                ? Map.of()
                : giaoVienRepository.findAllById(List.of(entity.getGiaoVienId()))
                .stream()
                .collect(Collectors.toMap(GiaoVien::getId, Function.identity()));

        Map<Long, PhongHoc> phongHocMap = entity.getPhongHocId() == null
                ? Map.of()
                : phongHocRepository.findAllById(List.of(entity.getPhongHocId()))
                .stream()
                .collect(Collectors.toMap(PhongHoc::getId, Function.identity()));

        Map<Long, CaHoc> caHocMap = entity.getCaHocId() == null
                ? Map.of()
                : caHocRepository.findAllById(List.of(entity.getCaHocId()))
                .stream()
                .collect(Collectors.toMap(CaHoc::getId, Function.identity()));

        return mapper.toResponse(
                entity,
                lopHocPhanMap,
                giaoVienMap,
                phongHocMap,
                caHocMap,
                tinhSoBuoiDaXep(entity)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LichHocResponse> getLichHocSinhVienHienTai(
            TrangThaiLichHoc trangThai,
            LocalDate tuNgay,
            LocalDate denNgay,
            Pageable pageable
    ) {
        Long sinhVienId = currentUserService.getSinhVienId();
        return repository.findAll(
                buildSpecification(null, null, null, null, null, trangThai, tuNgay, denNgay, null, sinhVienId),
                pageable
        ).map(this::toResponseDayDu);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LichHocResponse> getLichDayGiangVienHienTai(
            TrangThaiLichHoc trangThai,
            LocalDate tuNgay,
            LocalDate denNgay,
            Pageable pageable
    ) {
        Long giaoVienId = currentUserService.getGiaoVienId();
        return repository.findAll(
                buildSpecification(null, null, null, null, null, trangThai, tuNgay, denNgay, giaoVienId, null),
                pageable
        ).map(this::toResponseDayDu);
    }

    @Override
    public LichHocResponse create(LichHocRequest request) {
        List<Long> caHocIds = layCaHocIdsTuRequest(request);
        validateNgayVaCaHopLe(request, caHocIds);
        validateSoBuoiConLai(request.getLopHocPhanId(), caHocIds.size(), null);

        // Validate toàn bộ các ca trước khi lưu bất kỳ bản ghi nào.
        // Nếu một ca bị trùng lớp/giảng viên/phòng/sinh viên thì trả lỗi rõ ràng
        // và không để transaction bị rollback âm thầm sau khi đã save một phần.
        for (Long caHocId : caHocIds) {
            request.setCaHocId(caHocId);
            validator.validateCreate(request);
        }

        LichHocResponse responseDauTien = null;
        for (Long caHocId : caHocIds) {
            request.setCaHocId(caHocId);
            LichHoc entity = mapper.toEntity(request);
            LichHoc saved = repository.save(entity);
            taoDiemDanhChoBuoiHoc(saved);
            if (responseDauTien == null) {
                responseDauTien = getById(saved.getId());
            }
        }

        return responseDauTien;
    }

    @Override
    public LichHocResponse update(Long id, LichHocRequest request) {
        List<Long> caHocIds = layCaHocIdsTuRequest(request);
        if (caHocIds.size() > 1) {
            throw new GiangDayException("Khi cập nhật một buổi học chỉ được chọn một ca học");
        }
        request.setCaHocId(caHocIds.get(0));
        validateNgayVaCaHopLe(request, caHocIds);
        validateSoBuoiConLai(request.getLopHocPhanId(), 1, id);
        validator.validateUpdate(id, request);
        LichHoc entity = findEntity(id);
        mapper.updateEntity(entity, request);
        LichHoc saved = repository.save(entity);
        return getById(saved.getId());
    }

    @Override
    public void delete(Long id) {
        LichHoc entity = findEntity(id);
        repository.delete(entity);
    }

    private LichHoc findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GiangDayNotFoundException("Lịch học không tồn tại với id = " + id));
    }

    private Specification<LichHoc> buildSpecification(
            Long lopHocPhanId,
            String keywordLop,
            String keywordGiaoVien,
            String keywordPhong,
            String keywordCa,
            TrangThaiLichHoc trangThai,
            LocalDate tuNgay,
            LocalDate denNgay,
            Long giaoVienIdExact,
            Long sinhVienIdExact
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (lopHocPhanId != null) {
                predicates.add(cb.equal(root.get("lopHocPhanId"), lopHocPhanId));
            }

            if (giaoVienIdExact != null) {
                predicates.add(cb.equal(root.get("giaoVienId"), giaoVienIdExact));
            }

            if (sinhVienIdExact != null) {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<SinhVienLopHocPhan> svlhpRoot = subquery.from(SinhVienLopHocPhan.class);

                subquery.select(cb.literal(1L));
                subquery.where(cb.and(
                        cb.equal(svlhpRoot.get("lopHocPhanId"), root.get("lopHocPhanId")),
                        cb.equal(svlhpRoot.get("sinhVienId"), sinhVienIdExact),
                        svlhpRoot.get("trangThai").in(List.of(
                                TrangThaiSinhVienLopHocPhan.da_dang_ky,
                                TrangThaiSinhVienLopHocPhan.dang_hoc,
                                TrangThaiSinhVienLopHocPhan.hoc_lai
                        ))
                ));

                predicates.add(cb.exists(subquery));
            }

            if (trangThai != null) {
                predicates.add(cb.equal(root.get("trangThai"), trangThai));
            }

            if (tuNgay != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("ngayHoc"), tuNgay));
            }

            if (denNgay != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("ngayHoc"), denNgay));
            }

            if (keywordLop != null && !keywordLop.isBlank()) {
                String pattern = "%" + keywordLop.trim().toLowerCase() + "%";

                Subquery<Long> subquery = query.subquery(Long.class);
                Root<LopHocPhan> lopRoot = subquery.from(LopHocPhan.class);

                subquery.select(cb.literal(1L));
                subquery.where(cb.and(
                        cb.equal(lopRoot.get("id"), root.get("lopHocPhanId")),
                        cb.or(
                                cb.like(cb.lower(lopRoot.get("maLop")), pattern),
                                cb.like(cb.lower(lopRoot.get("tenLop")), pattern)
                        )
                ));

                predicates.add(cb.exists(subquery));
            }

            if (keywordGiaoVien != null && !keywordGiaoVien.isBlank()) {
                String pattern = "%" + keywordGiaoVien.trim().toLowerCase() + "%";

                Subquery<Long> subquery = query.subquery(Long.class);
                Root<GiaoVien> giaoVienRoot = subquery.from(GiaoVien.class);

                subquery.select(cb.literal(1L));
                subquery.where(cb.and(
                        cb.equal(giaoVienRoot.get("id"), root.get("giaoVienId")),
                        cb.or(
                                cb.like(cb.lower(giaoVienRoot.get("maGiaoVien")), pattern),
                                cb.like(cb.lower(giaoVienRoot.get("hoTen")), pattern)
                        )
                ));

                predicates.add(cb.exists(subquery));
            }

            if (keywordPhong != null && !keywordPhong.isBlank()) {
                String pattern = "%" + keywordPhong.trim().toLowerCase() + "%";

                Subquery<Long> subquery = query.subquery(Long.class);
                Root<PhongHoc> phongRoot = subquery.from(PhongHoc.class);

                subquery.select(cb.literal(1L));
                subquery.where(cb.and(
                        cb.equal(phongRoot.get("id"), root.get("phongHocId")),
                        cb.or(
                                cb.like(cb.lower(phongRoot.get("maPhong")), pattern),
                                cb.like(cb.lower(phongRoot.get("tenPhong")), pattern)
                        )
                ));

                predicates.add(cb.exists(subquery));
            }

            if (keywordCa != null && !keywordCa.isBlank()) {
                String pattern = "%" + keywordCa.trim().toLowerCase() + "%";

                Subquery<Long> subquery = query.subquery(Long.class);
                Root<CaHoc> caRoot = subquery.from(CaHoc.class);

                subquery.select(cb.literal(1L));
                subquery.where(cb.and(
                        cb.equal(caRoot.get("id"), root.get("caHocId")),
                        cb.or(
                                cb.like(cb.lower(caRoot.get("maCa")), pattern),
                                cb.like(cb.lower(caRoot.get("tenCa")), pattern)
                        )
                ));

                predicates.add(cb.exists(subquery));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Long tinhSoBuoiDaXep(LichHoc entity) {
        if (entity == null || entity.getLopHocPhanId() == null) {
            return null;
        }
        return repository.countSoBuoiDangTinh(entity.getLopHocPhanId(), TrangThaiLichHoc.nghi, null);
    }

    private LichHocResponse toResponseDayDu(LichHoc entity) {
        Map<Long, LopHocPhan> lopHocPhanMap = entity.getLopHocPhanId() == null
                ? Map.of()
                : lopHocPhanRepository.findAllById(List.of(entity.getLopHocPhanId()))
                .stream()
                .collect(Collectors.toMap(LopHocPhan::getId, Function.identity()));

        Map<Long, GiaoVien> giaoVienMap = entity.getGiaoVienId() == null
                ? Map.of()
                : giaoVienRepository.findAllById(List.of(entity.getGiaoVienId()))
                .stream()
                .collect(Collectors.toMap(GiaoVien::getId, Function.identity()));

        Map<Long, PhongHoc> phongHocMap = entity.getPhongHocId() == null
                ? Map.of()
                : phongHocRepository.findAllById(List.of(entity.getPhongHocId()))
                .stream()
                .collect(Collectors.toMap(PhongHoc::getId, Function.identity()));

        Map<Long, CaHoc> caHocMap = entity.getCaHocId() == null
                ? Map.of()
                : caHocRepository.findAllById(List.of(entity.getCaHocId()))
                .stream()
                .collect(Collectors.toMap(CaHoc::getId, Function.identity()));

        return mapper.toResponse(
                entity,
                lopHocPhanMap,
                giaoVienMap,
                phongHocMap,
                caHocMap,
                tinhSoBuoiDaXep(entity)
        );
    }

    private List<Long> layCaHocIdsTuRequest(LichHocRequest request) {
        if (request == null) {
            throw new GiangDayException("Dữ liệu lịch học không hợp lệ");
        }

        LinkedHashSet<Long> ids = new LinkedHashSet<>();
        if (request.getCaHocIds() != null) {
            request.getCaHocIds().stream()
                    .filter(Objects::nonNull)
                    .forEach(ids::add);
        }
        if (ids.isEmpty() && request.getCaHocId() != null) {
            ids.add(request.getCaHocId());
        }

        if (request.getTrangThai() != TrangThaiLichHoc.nghi && ids.isEmpty()) {
            throw new GiangDayException("Vui lòng chọn ca học");
        }


        if (ids.isEmpty()) {
            List<Long> caRong = new ArrayList<>();
            caRong.add(null);
            return caRong;
        }
        return new ArrayList<>(ids);
    }


    private void validateSoBuoiConLai(Long lopHocPhanId, int soBuoiMuonXep, Long idDangCapNhat) {
        if (lopHocPhanId == null || soBuoiMuonXep < 1) {
            return;
        }

        LopHocPhan lopHocPhan = lopHocPhanRepository.findById(lopHocPhanId)
                .orElseThrow(() -> new GiangDayException("Lớp học phần không tồn tại"));

        Integer soBuoiHoc = lopHocPhan.getSoBuoiHoc();
        if (soBuoiHoc == null || soBuoiHoc < 1) {
            throw new GiangDayException("Lớp học phần chưa cấu hình số buổi học để phân bố lịch");
        }

        long soBuoiDaXep = repository.countSoBuoiDangTinh(lopHocPhanId, TrangThaiLichHoc.nghi, idDangCapNhat);
        long soBuoiConLai = soBuoiHoc - soBuoiDaXep;

        if (soBuoiMuonXep > soBuoiConLai) {
            throw new GiangDayException("Lớp học phần chỉ còn " + Math.max(soBuoiConLai, 0) + " buổi chưa xếp");
        }
    }

    private void validateNgayVaCaHopLe(LichHocRequest request, List<Long> caHocIds) {
        if (request == null || request.getTrangThai() == TrangThaiLichHoc.nghi) {
            return;
        }

        if (request.getNgayHoc() != null && request.getThuTrongTuan() != null) {
            Integer thuThucTe = tinhThuTrongTuan(request.getNgayHoc());
            if (!request.getThuTrongTuan().equals(thuThucTe)) {
                throw new GiangDayException("Thứ trong tuần không khớp với ngày học");
            }
        }

        List<CaHoc> caHocs = caHocRepository.findAllById(caHocIds);
        if (caHocs.size() != caHocIds.stream().filter(Objects::nonNull).distinct().count()) {
            throw new GiangDayException("Ca học không tồn tại");
        }
    }

    private Integer tinhThuTrongTuan(LocalDate ngayHoc) {
        if (ngayHoc == null) return null;
        int dayOfWeek = ngayHoc.getDayOfWeek().getValue();
        return dayOfWeek == 7 ? 8 : dayOfWeek + 1;
    }

    private void taoDiemDanhChoBuoiHoc(LichHoc lichHoc) {
        if (lichHoc == null || lichHoc.getId() == null || lichHoc.getLopHocPhanId() == null) {
            return;
        }

        if (lichHoc.getTrangThai() == TrangThaiLichHoc.nghi) {
            return;
        }

        diemDanhRepository.insertMissingDiemDanhForLichHoc(
                lichHoc.getId(),
                lichHoc.getLopHocPhanId(),
                List.of(
                        TrangThaiSinhVienLopHocPhan.da_dang_ky.name(),
                        TrangThaiSinhVienLopHocPhan.dang_hoc.name(),
                        TrangThaiSinhVienLopHocPhan.hoc_lai.name()
                ),
                TrangThaiDiemDanh.chua_diem_danh.name()
        );
    }
}