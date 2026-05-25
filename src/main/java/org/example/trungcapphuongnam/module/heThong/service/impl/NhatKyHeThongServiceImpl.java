package org.example.trungcapphuongnam.module.heThong.service.impl;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.heThong.HeThongNotFoundException;
import org.example.trungcapphuongnam.module.heThong.dto.request.NhatKyHeThongRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.NhatKyHeThongResponse;
import org.example.trungcapphuongnam.module.heThong.entity.NhatKyHeThong;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoan;
import org.example.trungcapphuongnam.module.heThong.mapper.NhatKyHeThongMapper;
import org.example.trungcapphuongnam.module.heThong.repository.NhatKyHeThongRepository;
import org.example.trungcapphuongnam.module.heThong.repository.TaiKhoanRepository;
import org.example.trungcapphuongnam.module.heThong.service.NhatKyHeThongService;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class NhatKyHeThongServiceImpl implements NhatKyHeThongService {

    private static final int DEFAULT_LIMIT = 100;
    private static final int MAX_LIMIT = 100;

    private final NhatKyHeThongRepository nhatKyHeThongRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final NhatKyHeThongMapper nhatKyHeThongMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<NhatKyHeThongResponse> getAll(Map<String, String> filters) {
        Specification<NhatKyHeThong> specification = taoDieuKienLoc(filters);

        int page = docSo(filters, "page", 0, Integer.MAX_VALUE);
        int size = docSo(filters, "size", DEFAULT_LIMIT, MAX_LIMIT);

        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt", "id")
        );

        return nhatKyHeThongRepository
                .findAll(specification, pageRequest)
                .map(nhatKyHeThongMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public NhatKyHeThongResponse getById(Long id) {
        return nhatKyHeThongMapper.toResponse(findByIdOrThrow(id));
    }

    @Override
    public NhatKyHeThongResponse create(NhatKyHeThongRequest request) {
        NhatKyHeThong entity = nhatKyHeThongMapper.toEntity(request);
        entity.setTaiKhoan(resolveTaiKhoan(request == null ? null : request.getTaiKhoanId()));
        return nhatKyHeThongMapper.toResponse(nhatKyHeThongRepository.save(entity));
    }

    @Override
    public NhatKyHeThongResponse update(Long id, NhatKyHeThongRequest request) {
        NhatKyHeThong entity = findByIdOrThrow(id);
        nhatKyHeThongMapper.updateEntity(entity, request);
        entity.setTaiKhoan(resolveTaiKhoan(request == null ? null : request.getTaiKhoanId()));
        return nhatKyHeThongMapper.toResponse(nhatKyHeThongRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        nhatKyHeThongRepository.delete(findByIdOrThrow(id));
    }

    private Specification<NhatKyHeThong> taoDieuKienLoc(Map<String, String> filters) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            themDieuKienBang(predicates, cb, root.get("moduleNghiepVu"), lay(filters, "moduleNghiepVu"));
            themDieuKienBang(predicates, cb, root.get("chucNang"), lay(filters, "chucNang"));
            themDieuKienBang(predicates, cb, root.get("hanhDong"), lay(filters, "hanhDong"));
            themDieuKienBang(predicates, cb, root.get("ketQua"), lay(filters, "ketQua"));
            themDieuKienBang(predicates, cb, root.get("taiKhoanEmail"), lay(filters, "taiKhoanEmail"));
            themDieuKienBang(predicates, cb, root.get("doiTuongLoai"), lay(filters, "doiTuongLoai"));

            themDieuKienLong(predicates, cb, root.get("taiKhoan").get("id"), lay(filters, "taiKhoanId"));
            themDieuKienLong(predicates, cb, root.get("doiTuongId"), lay(filters, "doiTuongId"));
            themDieuKienLong(predicates, cb, root.get("banGhiId"), lay(filters, "banGhiId"));

            LocalDateTime tuNgay = docTuNgay(lay(filters, "tuNgay"));
            if (tuNgay != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), tuNgay));
            }

            LocalDateTime denNgay = docDenNgay(lay(filters, "denNgay"));
            if (denNgay != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), denNgay));
            }

            String keyword = lay(filters, "keyword");
            if (keyword != null && !keyword.isBlank()) {
                String like = "%" + keyword.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("taiKhoanEmail")), like),
                        cb.like(cb.lower(root.get("tenTaiKhoan")), like),
                        cb.like(cb.lower(root.get("doiTuongMa")), like),
                        cb.like(cb.lower(root.get("doiTuongTen")), like),
                        cb.like(cb.lower(root.get("doiTuongEmail")), like),
                        cb.like(cb.lower(root.get("moduleNghiepVu")), like),
                        cb.like(cb.lower(root.get("chucNang")), like),
                        cb.like(cb.lower(root.get("hanhDong")), like),
                        cb.like(cb.lower(root.get("bangTacDong")), like),
                        cb.like(cb.lower(root.get("banGhiMa")), like),
                        cb.like(cb.lower(root.get("banGhiTen")), like),
                        cb.like(cb.lower(root.get("moTa")), like),
                        cb.like(cb.lower(root.get("duongDan")), like),
                        cb.like(cb.lower(root.get("ipAddress")), like),
                        cb.like(cb.lower(root.get("duLieuTimKiem")), like)
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private void themDieuKienBang(List<Predicate> predicates, jakarta.persistence.criteria.CriteriaBuilder cb,
                                  jakarta.persistence.criteria.Expression<String> field, String value) {
        if (value != null && !value.isBlank()) {
            predicates.add(cb.equal(cb.lower(field), value.trim().toLowerCase()));
        }
    }

    private void themDieuKienLong(List<Predicate> predicates, jakarta.persistence.criteria.CriteriaBuilder cb,
                                  jakarta.persistence.criteria.Expression<Long> field, String value) {
        if (value == null || value.isBlank()) {
            return;
        }

        try {
            predicates.add(cb.equal(field, Long.parseLong(value.trim())));
        } catch (NumberFormatException ignored) {
        }
    }

    private String lay(Map<String, String> filters, String key) {
        if (filters == null) {
            return null;
        }
        return filters.get(key);
    }

    private LocalDateTime docTuNgay(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return LocalDate.parse(value.trim()).atStartOfDay();
    }

    private LocalDateTime docDenNgay(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return LocalDate.parse(value.trim()).atTime(LocalTime.MAX);
    }

    private int docSo(Map<String, String> filters, String key, int defaultValue, int maxValue) {
        String value = lay(filters, key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        try {
            int parsed = Integer.parseInt(value.trim());
            if (parsed <= 0) {
                return defaultValue;
            }
            return Math.min(parsed, maxValue);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    private NhatKyHeThong findByIdOrThrow(Long id) {
        return nhatKyHeThongRepository.findById(id)
                .orElseThrow(() -> new HeThongNotFoundException("Không tìm thấy nhật ký hệ thống với id = " + id));
    }

    private TaiKhoan resolveTaiKhoan(Long taiKhoanId) {
        if (taiKhoanId == null) {
            return null;
        }
        return taiKhoanRepository.findById(taiKhoanId)
                .orElseThrow(() -> new HeThongNotFoundException("Không tìm thấy tài khoản với id = " + taiKhoanId));
    }
}