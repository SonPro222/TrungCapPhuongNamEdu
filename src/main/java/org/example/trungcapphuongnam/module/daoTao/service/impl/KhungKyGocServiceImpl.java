package org.example.trungcapphuongnam.module.daoTao.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.DuplicateResourceException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.util.TextUtil;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyGocRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyGocResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.KhungKyGoc;
import org.example.trungcapphuongnam.module.daoTao.mapper.KhungKyGocMapper;
import org.example.trungcapphuongnam.module.daoTao.repository.KhungKyGocRepository;
import org.example.trungcapphuongnam.module.daoTao.service.KhungKyGocService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class KhungKyGocServiceImpl implements KhungKyGocService {

    private final KhungKyGocRepository repository;
    private final KhungKyGocMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<KhungKyGocResponse> findAll(String keyword, Pageable pageable) {
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
    public KhungKyGocResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Override
    public KhungKyGocResponse create(KhungKyGocRequest request) {
        validate(request, null);

        KhungKyGoc entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public KhungKyGocResponse update(Long id, KhungKyGocRequest request) {
        KhungKyGoc entity = getEntity(id);

        validate(request, id);

        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Khung kỳ gốc không tồn tại: " + id);
        }

        repository.deleteById(id);
    }

    private void validate(KhungKyGocRequest request, Long id) {
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

    private KhungKyGoc getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Khung kỳ gốc không tồn tại: " + id));
    }
}