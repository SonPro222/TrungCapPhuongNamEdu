package org.example.trungcapphuongnam.module.diem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.DuplicateResourceException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.util.TextUtil;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHoc;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocRepository;
import org.example.trungcapphuongnam.module.diem.dto.request.CauHinhDanhGiaRequest;
import org.example.trungcapphuongnam.module.diem.dto.response.CauHinhDanhGiaResponse;
import org.example.trungcapphuongnam.module.diem.entity.CauHinhDanhGia;
import org.example.trungcapphuongnam.module.diem.mapper.CauHinhDanhGiaMapper;
import org.example.trungcapphuongnam.module.diem.repository.CauHinhDanhGiaRepository;
import org.example.trungcapphuongnam.module.diem.service.CauHinhDanhGiaService;
import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.repository.LopHocPhanChuongTrinhMonRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.LopHocPhanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CauHinhDanhGiaServiceImpl implements CauHinhDanhGiaService {

    private final CauHinhDanhGiaRepository repository;
    private final SyllabusMonHocRepository syllabusMonHocRepository;
    private final LopHocPhanRepository lopHocPhanRepository;
    private final LopHocPhanChuongTrinhMonRepository lopHocPhanChuongTrinhMonRepository;
    private final CauHinhDanhGiaMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CauHinhDanhGiaResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CauHinhDanhGiaResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CauHinhDanhGiaResponse> findBySyllabusMonHocId(Long syllabusMonHocId) {
        if (syllabusMonHocId == null) {
            throw new BadRequestException("syllabusMonHocId không được để trống");
        }
        return repository.findBySyllabusMonHocIdOrderByThuTuAscIdAsc(syllabusMonHocId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CauHinhDanhGiaResponse> findByLopHocPhanId(Long lopHocPhanId) {
        Long syllabusMonHocId = resolveSyllabusMonHocIdTuLopHocPhan(lopHocPhanId);
        return repository.findBySyllabusMonHocIdOrderByThuTuAscIdAsc(syllabusMonHocId)
                .stream()
                .map(item -> mapper.toResponse(item, lopHocPhanId))
                .collect(Collectors.toList());
    }

    @Override
    public CauHinhDanhGiaResponse create(CauHinhDanhGiaRequest request) {
        validate(request);
        validateSyllabusExists(request.getSyllabusMonHocId());
        validateDuplicate(request, null);

        CauHinhDanhGia entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public CauHinhDanhGiaResponse update(Long id, CauHinhDanhGiaRequest request) {
        CauHinhDanhGia entity = getEntity(id);
        validate(request);
        validateSyllabusExists(request.getSyllabusMonHocId());
        validateDuplicate(request, id);

        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("CauHinhDanhGia không tồn tại: " + id);
        }

        repository.deleteById(id);
    }

    private CauHinhDanhGia getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CauHinhDanhGia không tồn tại: " + id));
    }

    private void validate(CauHinhDanhGiaRequest request) {
        if (request == null) {
            throw new BadRequestException("Dữ liệu cấu hình đánh giá không được để trống");
        }

        request.setTenCotDiem(TextUtil.trimRequired(request.getTenCotDiem()));
        request.setLoaiDiem(TextUtil.trimRequired(request.getLoaiDiem()));

        if (request.getSyllabusMonHocId() == null && request.getLopHocPhanId() != null) {
            request.setSyllabusMonHocId(resolveSyllabusMonHocIdTuLopHocPhan(request.getLopHocPhanId()));
        }

        if (request.getSyllabusMonHocId() == null) {
            throw new BadRequestException("Syllabus môn học không được để trống");
        }

        if (request.getTyLe() == null
                || request.getTyLe().compareTo(BigDecimal.ZERO) < 0
                || request.getTyLe().compareTo(new BigDecimal("100")) > 0) {
            throw new BadRequestException("Tỷ lệ phải nằm trong khoảng 0 - 100");
        }

        if (request.getDiemToiDa() != null && request.getDiemToiDa().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Điểm tối đa phải lớn hơn 0");
        }
    }

    private void validateSyllabusExists(Long syllabusMonHocId) {
        if (!syllabusMonHocRepository.existsById(syllabusMonHocId)) {
            throw new ResourceNotFoundException("Syllabus môn học không tồn tại: " + syllabusMonHocId);
        }
    }

    private void validateDuplicate(CauHinhDanhGiaRequest request, Long currentId) {
        Long syllabusMonHocId = request.getSyllabusMonHocId();

        boolean duplicateTenCotDiem = currentId == null
                ? repository.existsBySyllabusMonHocIdAndTenCotDiem(syllabusMonHocId, request.getTenCotDiem())
                : repository.existsBySyllabusMonHocIdAndTenCotDiemAndIdNot(syllabusMonHocId, request.getTenCotDiem(), currentId);

        if (duplicateTenCotDiem) {
            throw new DuplicateResourceException("Cột điểm đã tồn tại trong syllabus môn học này");
        }

        if (request.getThuTu() != null) {
            boolean duplicateThuTu = currentId == null
                    ? repository.existsBySyllabusMonHocIdAndThuTu(syllabusMonHocId, request.getThuTu())
                    : repository.existsBySyllabusMonHocIdAndThuTuAndIdNot(syllabusMonHocId, request.getThuTu(), currentId);

            if (duplicateThuTu) {
                throw new DuplicateResourceException("Thứ tự cột điểm đã tồn tại trong syllabus môn học này");
            }
        }
    }

    private Long resolveSyllabusMonHocIdTuLopHocPhan(Long lopHocPhanId) {
        if (lopHocPhanId == null) {
            throw new BadRequestException("Lớp học phần không được để trống");
        }

        LopHocPhan lopHocPhan = lopHocPhanRepository.findById(lopHocPhanId)
                .orElseThrow(() -> new ResourceNotFoundException("Lớp học phần không tồn tại: " + lopHocPhanId));

        Long chuongTrinhMonId = resolveChuongTrinhMonIdTuLopHocPhan(lopHocPhan);

        SyllabusMonHoc syllabusMonHoc = syllabusMonHocRepository.findFirstByChuongTrinhMonIdOrderByIdAsc(chuongTrinhMonId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Môn trong chương trình chưa có syllabus áp dụng: " + chuongTrinhMonId
                ));

        return syllabusMonHoc.getId();
    }

    private Long resolveChuongTrinhMonIdTuLopHocPhan(LopHocPhan lopHocPhan) {
        if (lopHocPhan.getChuongTrinhMonId() != null) {
            return lopHocPhan.getChuongTrinhMonId();
        }

        return lopHocPhanChuongTrinhMonRepository.findFirstChuongTrinhMonIdByLopHocPhanId(lopHocPhan.getId())
                .orElseThrow(() -> new BadRequestException(
                        "Lớp học phần chưa tham chiếu môn trong chương trình nên không xác định được syllabus"
                ));
    }

}
