package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.DuplicateResourceException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.util.TextUtil;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.CauHinhDanhGiaMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.CauHinhDanhGiaMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.CauHinhDanhGiaMau;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.CauHinhDanhGiaMauMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.CauHinhDanhGiaMauRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.CauHinhDanhGiaMauService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CauHinhDanhGiaMauServiceImpl implements CauHinhDanhGiaMauService {

    private final CauHinhDanhGiaMauRepository repository;
    private final CauHinhDanhGiaMauMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CauHinhDanhGiaMauResponse> findAll(Long chuongTrinhMonId, String keyword, Pageable pageable) {
        return repository.findAll(buildSpecification(chuongTrinhMonId, keyword), pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CauHinhDanhGiaMauResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Override
    public CauHinhDanhGiaMauResponse create(CauHinhDanhGiaMauRequest request) {
        validate(request);

        if (request.getChuongTrinhMonId() == null) {
            if (repository.existsByTenCotDiem(request.getTenCotDiem())) {
                throw new DuplicateResourceException("Tên cột điểm mẫu đã tồn tại");
            }

            if (request.getThuTu() != null && repository.existsByThuTu(request.getThuTu())) {
                throw new DuplicateResourceException("Thứ tự cột điểm mẫu đã tồn tại");
            }
        } else {
            if (repository.existsByChuongTrinhMonIdAndTenCotDiem(request.getChuongTrinhMonId(), request.getTenCotDiem())) {
                throw new DuplicateResourceException("Tên cột điểm mẫu đã tồn tại trong chương trình môn này");
            }

            if (request.getThuTu() != null &&
                    repository.existsByChuongTrinhMonIdAndThuTu(request.getChuongTrinhMonId(), request.getThuTu())) {
                throw new DuplicateResourceException("Thứ tự cột điểm mẫu đã tồn tại trong chương trình môn này");
            }
        }

        CauHinhDanhGiaMau entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public CauHinhDanhGiaMauResponse update(Long id, CauHinhDanhGiaMauRequest request) {
        CauHinhDanhGiaMau entity = getEntity(id);
        validate(request);

        if (request.getChuongTrinhMonId() == null) {
            if (repository.existsByTenCotDiemAndIdNot(request.getTenCotDiem(), id)) {
                throw new DuplicateResourceException("Tên cột điểm mẫu đã tồn tại");
            }

            if (request.getThuTu() != null && repository.existsByThuTuAndIdNot(request.getThuTu(), id)) {
                throw new DuplicateResourceException("Thứ tự cột điểm mẫu đã tồn tại");
            }
        } else {
            if (repository.existsByChuongTrinhMonIdAndTenCotDiemAndIdNot(request.getChuongTrinhMonId(), request.getTenCotDiem(), id)) {
                throw new DuplicateResourceException("Tên cột điểm mẫu đã tồn tại trong chương trình môn này");
            }

            if (request.getThuTu() != null &&
                    repository.existsByChuongTrinhMonIdAndThuTuAndIdNot(request.getChuongTrinhMonId(), request.getThuTu(), id)) {
                throw new DuplicateResourceException("Thứ tự cột điểm mẫu đã tồn tại trong chương trình môn này");
            }
        }

        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Cấu hình đánh giá mẫu không tồn tại: " + id);
        }

        repository.deleteById(id);
    }

    private CauHinhDanhGiaMau getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cấu hình đánh giá mẫu không tồn tại: " + id));
    }

    private void validate(CauHinhDanhGiaMauRequest request) {
        request.setTenCotDiem(TextUtil.trimRequired(request.getTenCotDiem()));
        request.setLoaiDiem(TextUtil.trimToNull(request.getLoaiDiem()));

        if (request.getTyLe() == null ||
                request.getTyLe().compareTo(BigDecimal.ZERO) < 0 ||
                request.getTyLe().compareTo(new BigDecimal("100")) > 0) {
            throw new BadRequestException("Tỷ lệ phải nằm trong khoảng 0 - 100");
        }

        if (request.getDiemToiDa() != null && request.getDiemToiDa().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Điểm tối đa phải lớn hơn 0");
        }
    }

    private Specification<CauHinhDanhGiaMau> buildSpecification(Long chuongTrinhMonId, String keyword) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (chuongTrinhMonId != null) {
                predicates.add(cb.equal(root.get("chuongTrinhMonId"), chuongTrinhMonId));
            }

            if (keyword != null && !keyword.isBlank()) {
                String pattern = "%" + keyword.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("tenCotDiem")), pattern),
                        cb.like(cb.lower(root.get("loaiDiem")), pattern),
                        cb.like(cb.lower(root.get("ghiChu")), pattern)
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}