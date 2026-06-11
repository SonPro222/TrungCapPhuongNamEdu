package org.example.trungcapphuongnam.module.daoTao.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.DuplicateResourceException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.util.TextUtil;
import org.example.trungcapphuongnam.module.chuongTrinh.service.XoaChuongTrinhCascadeService;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyMauRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyMauResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.KhungKyMau;
import org.example.trungcapphuongnam.module.daoTao.mapper.KhungKyMauMapper;
import org.example.trungcapphuongnam.module.daoTao.repository.KhungKyMauRepository;
import org.example.trungcapphuongnam.module.daoTao.service.KhungKyMauService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class KhungKyMauServiceImpl implements KhungKyMauService {

    private final KhungKyMauRepository repository;
    private final KhungKyMauMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<KhungKyMauResponse> findAll(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return repository.findAll(pageable).map(mapper::toResponse);
        }

        String value = keyword.trim();

        return repository.findByMaKyContainingIgnoreCaseOrTenKyContainingIgnoreCaseOrMoTaContainingIgnoreCase(
                value,
                value,
                value,
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public KhungKyMauResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Override
    public KhungKyMauResponse create(KhungKyMauRequest request) {
        validate(request, null);

        KhungKyMau entity = mapper.toEntity(request);
        KhungKyMau saved = repository.save(entity);
        // Fetch lại để lấy createdAt/updatedAt mà DB đã set (DEFAULT NOW())
        return mapper.toResponse(repository.findById(saved.getId()).orElse(saved));
    }

    @Override
    public KhungKyMauResponse update(Long id, KhungKyMauRequest request) {
        KhungKyMau entity = getEntity(id);

        validate(request, id);

        mapper.updateEntity(entity, request);
        KhungKyMau saved = repository.save(entity);
        return mapper.toResponse(repository.findById(saved.getId()).orElse(saved));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Khung kỳ mẫu không tồn tại: " + id);
        }

        repository.deleteById(id);
    }

    private void validate(KhungKyMauRequest request, Long id) {
        if (request == null) {
            throw new BadRequestException("Dữ liệu khung kỳ gốc không hợp lệ");
        }

        request.setMaKy(TextUtil.trimRequired(request.getMaKy()));
        request.setTenKy(TextUtil.trimRequired(request.getTenKy()));

        if (request.getThuTu() == null) {
            throw new BadRequestException("thuTu không được để trống");
        }

        if (request.getThuTu() <= 0) {
            throw new BadRequestException("thuTu phải lớn hơn 0");
        }

        if (id == null) {
            if (repository.existsByMaKy(request.getMaKy())) {
                throw new DuplicateResourceException("Mã khung kỳ gốc đã tồn tại: " + request.getMaKy());
            }

            if (repository.existsByThuTu(request.getThuTu())) {
                throw new DuplicateResourceException("Thứ tự khung kỳ gốc đã tồn tại: " + request.getThuTu());
            }
        } else {
            if (repository.existsByMaKyAndIdNot(request.getMaKy(), id)) {
                throw new DuplicateResourceException("Mã khung kỳ gốc đã tồn tại: " + request.getMaKy());
            }

            if (repository.existsByThuTuAndIdNot(request.getThuTu(), id)) {
                throw new DuplicateResourceException("Thứ tự khung kỳ gốc đã tồn tại: " + request.getThuTu());
            }
        }
    }

    private KhungKyMau getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Khung kỳ gốc không tồn tại: " + id));
    }
}