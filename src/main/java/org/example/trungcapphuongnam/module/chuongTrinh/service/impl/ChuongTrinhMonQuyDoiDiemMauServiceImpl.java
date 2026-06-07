package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhMonQuyDoiDiemMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhMonQuyDoiDiemMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMonQuyDoiDiemMau;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHoc;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.QuyDoiDiemMau;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.ChuongTrinhMonQuyDoiDiemMauMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhMonQuyDoiDiemMauRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.QuyDoiDiemMauRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhMonQuyDoiDiemMauService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChuongTrinhMonQuyDoiDiemMauServiceImpl implements ChuongTrinhMonQuyDoiDiemMauService {

    private final ChuongTrinhMonQuyDoiDiemMauRepository repository;
    private final ChuongTrinhMonQuyDoiDiemMauMapper mapper;
    private final SyllabusMonHocRepository syllabusMonHocRepository;
    private final QuyDoiDiemMauRepository quyDoiDiemMauRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ChuongTrinhMonQuyDoiDiemMauResponse> findAll(
            Long chuongTrinhMonId,
            Long syllabusMonHocId,
            Long quyDoiDiemMauId,
            String keyword,
            Pageable pageable
    ) {
        return repository.findAll(
                LocJpa.<ChuongTrinhMonQuyDoiDiemMau>empty()
                        .and(LocJpa.eq("chuongTrinhMonId", chuongTrinhMonId))
                        .and(LocJpa.eq("syllabusMonHocId", syllabusMonHocId))
                        .and(LocJpa.eq("quyDoiDiemMauId", quyDoiDiemMauId))
                        .and(LocJpa.keyword(keyword, "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ChuongTrinhMonQuyDoiDiemMauResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Chuong Trinh Mon Quy Doi Diem Mau không tồn tại: " + id));
    }

    @Override
    public ChuongTrinhMonQuyDoiDiemMauResponse create(ChuongTrinhMonQuyDoiDiemMauRequest request) {
        SyllabusMonHoc syllabusMonHoc = validateRequest(request);
        validateDuplicate(request, null);

        ChuongTrinhMonQuyDoiDiemMau entity = mapper.toEntity(request);
        ganScopeTheoSyllabus(entity, syllabusMonHoc);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public ChuongTrinhMonQuyDoiDiemMauResponse update(Long id, ChuongTrinhMonQuyDoiDiemMauRequest request) {
        ChuongTrinhMonQuyDoiDiemMau entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chuong Trinh Mon Quy Doi Diem Mau không tồn tại: " + id));

        SyllabusMonHoc syllabusMonHoc = validateRequest(request);
        validateDuplicate(request, id);

        mapper.updateEntity(entity, request);
        ganScopeTheoSyllabus(entity, syllabusMonHoc);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Chuong Trinh Mon Quy Doi Diem Mau không tồn tại: " + id);
        }

        repository.deleteById(id);
    }

    private SyllabusMonHoc validateRequest(ChuongTrinhMonQuyDoiDiemMauRequest request) {
        if (request == null) {
            throw new BadRequestException("Dữ liệu gán mẫu quy đổi điểm không được để trống");
        }

        if (request.getChuongTrinhMonId() != null) {
            throw new BadRequestException("Mẫu quy đổi kết quả không được gán trực tiếp vào môn trong chương trình. Hãy truyền syllabusMonHocId.");
        }

        if (request.getSyllabusMonHocId() == null) {
            throw new BadRequestException("syllabusMonHocId không được để trống");
        }

        if (request.getQuyDoiDiemMauId() == null) {
            throw new BadRequestException("quyDoiDiemMauId không được để trống");
        }

        SyllabusMonHoc syllabusMonHoc = syllabusMonHocRepository.findById(request.getSyllabusMonHocId())
                .orElseThrow(() -> new ResourceNotFoundException("Syllabus môn học không tồn tại: " + request.getSyllabusMonHocId()));

        QuyDoiDiemMau mau = quyDoiDiemMauRepository.findById(request.getQuyDoiDiemMauId())
                .orElseThrow(() -> new ResourceNotFoundException("Quy đổi điểm mẫu không tồn tại: " + request.getQuyDoiDiemMauId()));

        if (!"QUY_DOI_KET_QUA".equalsIgnoreCase(String.valueOf(mau.getLoaiMau()))) {
            throw new BadRequestException("Chỉ được gán mẫu loại QUY_DOI_KET_QUA vào syllabus. Cột điểm không gán qua bảng mẫu quy đổi kết quả.");
        }

        return syllabusMonHoc;
    }

    private void ganScopeTheoSyllabus(ChuongTrinhMonQuyDoiDiemMau entity, SyllabusMonHoc syllabusMonHoc) {
        entity.setSyllabusMonHocId(syllabusMonHoc.getId());
        entity.setChuongTrinhMonId(syllabusMonHoc.getChuongTrinhMonId());
    }

    private void validateDuplicate(ChuongTrinhMonQuyDoiDiemMauRequest request, Long currentId) {
        Long syllabusMonHocId = request.getSyllabusMonHocId();
        Long quyDoiDiemMauId = request.getQuyDoiDiemMauId();

        boolean duplicated = currentId == null
                ? repository.existsBySyllabusMonHocIdAndQuyDoiDiemMauId(syllabusMonHocId, quyDoiDiemMauId)
                : repository.existsBySyllabusMonHocIdAndQuyDoiDiemMauIdAndIdNot(syllabusMonHocId, quyDoiDiemMauId, currentId);

        if (duplicated) {
            throw new BadRequestException("Mẫu quy đổi điểm này đã được gán cho syllabus môn học");
        }
    }
}
