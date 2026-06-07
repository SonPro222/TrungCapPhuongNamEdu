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
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocGocRepository;
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
    private final SyllabusMonHocGocRepository syllabusMonHocGocRepository;
    private final CauHinhDanhGiaMauMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CauHinhDanhGiaMauResponse> findAll(
            Long syllabusMonHocGocId,
            String keyword,
            Pageable pageable
    ) {
        // Cột điểm mẫu thuộc syllabus môn học gốc. Không trả toàn bộ bảng khi thiếu syllabusMonHocGocId.
        if (syllabusMonHocGocId == null) {
            return Page.empty(pageable);
        }

        return repository.findAll(buildSpecification(syllabusMonHocGocId, keyword), pageable)
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
        validateSyllabusGocExists(request.getSyllabusMonHocGocId());
        validateDuplicate(request, null);

        CauHinhDanhGiaMau entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public CauHinhDanhGiaMauResponse update(Long id, CauHinhDanhGiaMauRequest request) {
        CauHinhDanhGiaMau entity = getEntity(id);
        validate(request);
        validateSyllabusGocExists(request.getSyllabusMonHocGocId());
        validateDuplicate(request, id);

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
        if (request == null) {
            throw new BadRequestException("Dữ liệu cấu hình đánh giá mẫu không được để trống");
        }

        if (request.getChuongTrinhMonId() != null) {
            throw new BadRequestException("Cột điểm mẫu không thuộc môn trong chương trình. Hãy truyền syllabusMonHocGocId.");
        }

        if (request.getSyllabusMonHocId() != null) {
            throw new BadRequestException("Cột điểm mẫu không thuộc syllabus áp dụng. Hãy truyền syllabusMonHocGocId.");
        }

        if (request.getSyllabusMonHocGocId() == null) {
            throw new BadRequestException("syllabusMonHocGocId không được để trống");
        }

        request.setTenCotDiem(TextUtil.trimRequired(request.getTenCotDiem()));
        request.setLoaiDiem(TextUtil.trimToNull(request.getLoaiDiem()));
        request.setGhiChu(TextUtil.trimToNull(request.getGhiChu()));

        if (request.getTyLe() == null ||
                request.getTyLe().compareTo(BigDecimal.ZERO) < 0 ||
                request.getTyLe().compareTo(new BigDecimal("100")) > 0) {
            throw new BadRequestException("Tỷ lệ phải nằm trong khoảng 0 - 100");
        }

        if (request.getDiemToiDa() != null && request.getDiemToiDa().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Điểm tối đa phải lớn hơn 0");
        }
    }

    private void validateSyllabusGocExists(Long syllabusMonHocGocId) {
        if (!syllabusMonHocGocRepository.existsById(syllabusMonHocGocId)) {
            throw new ResourceNotFoundException("Syllabus môn học gốc không tồn tại: " + syllabusMonHocGocId);
        }
    }

    private void validateDuplicate(CauHinhDanhGiaMauRequest request, Long currentId) {
        Long syllabusMonHocGocId = request.getSyllabusMonHocGocId();

        boolean duplicateTenCotDiem = currentId == null
                ? repository.existsBySyllabusMonHocGocIdAndTenCotDiem(syllabusMonHocGocId, request.getTenCotDiem())
                : repository.existsBySyllabusMonHocGocIdAndTenCotDiemAndIdNot(syllabusMonHocGocId, request.getTenCotDiem(), currentId);

        if (duplicateTenCotDiem) {
            throw new DuplicateResourceException("Tên cột điểm mẫu đã tồn tại trong syllabus gốc này");
        }

        if (request.getThuTu() != null) {
            boolean duplicateThuTu = currentId == null
                    ? repository.existsBySyllabusMonHocGocIdAndThuTu(syllabusMonHocGocId, request.getThuTu())
                    : repository.existsBySyllabusMonHocGocIdAndThuTuAndIdNot(syllabusMonHocGocId, request.getThuTu(), currentId);

            if (duplicateThuTu) {
                throw new DuplicateResourceException("Thứ tự cột điểm mẫu đã tồn tại trong syllabus gốc này");
            }
        }
    }

    private Specification<CauHinhDanhGiaMau> buildSpecification(Long syllabusMonHocGocId, String keyword) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (syllabusMonHocGocId != null) {
                predicates.add(cb.equal(root.get("syllabusMonHocGocId"), syllabusMonHocGocId));
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
