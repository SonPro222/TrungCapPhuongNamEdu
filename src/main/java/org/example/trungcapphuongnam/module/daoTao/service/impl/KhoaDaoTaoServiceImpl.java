package org.example.trungcapphuongnam.module.daoTao.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.DuplicateResourceException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.util.TextUtil;
import org.example.trungcapphuongnam.module.daoTao.dto.KhoaDaoTaoRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.KhoaDaoTaoResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.KhoaDaoTao;
import org.example.trungcapphuongnam.module.daoTao.mapper.KhoaDaoTaoMapper;
import org.example.trungcapphuongnam.module.daoTao.repository.KhoaDaoTaoRepository;
import org.example.trungcapphuongnam.module.daoTao.service.KhoaDaoTaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class KhoaDaoTaoServiceImpl implements KhoaDaoTaoService {

    private final KhoaDaoTaoRepository repository;
    private final KhoaDaoTaoMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<KhoaDaoTaoResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public KhoaDaoTaoResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Override
    public KhoaDaoTaoResponse create(KhoaDaoTaoRequest request) {
        validate(request);
        if (repository.existsByMaKhoa(request.getMaKhoa())) {
            throw new DuplicateResourceException("Mã khóa đã tồn tại: " + request.getMaKhoa());
        }
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    public KhoaDaoTaoResponse update(Long id, KhoaDaoTaoRequest request) {
        KhoaDaoTao entity = getEntity(id);
        validate(request);
        if (repository.existsByMaKhoaAndIdNot(request.getMaKhoa(), id)) {
            throw new DuplicateResourceException("Mã khóa đã tồn tại: " + request.getMaKhoa());
        }
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Khóa đào tạo không tồn tại: " + id);
        repository.deleteById(id);
    }

    private void validate(KhoaDaoTaoRequest request) {
        request.setMaKhoa(TextUtil.trimRequired(request.getMaKhoa()));
        if (request.getNamBatDau() != null && request.getNamBatDau() <= 1900) throw new BadRequestException("Năm bắt đầu không hợp lệ");
        if (request.getNamBatDau() != null && request.getNamKetThuc() != null && request.getNamKetThuc() < request.getNamBatDau()) {
            throw new BadRequestException("Năm kết thúc phải lớn hơn hoặc bằng năm bắt đầu");
        }
    }

    private KhoaDaoTao getEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Khóa đào tạo không tồn tại: " + id));
    }
}
