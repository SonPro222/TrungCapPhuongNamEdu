package org.example.trungcapphuongnam.module.daoTao.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.daoTao.enums.TrangThaiLopHanhChinh;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.DuplicateResourceException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.util.TextUtil;
import org.example.trungcapphuongnam.module.daoTao.dto.LopHanhChinhRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.LopHanhChinhResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.LopHanhChinh;

import org.example.trungcapphuongnam.module.daoTao.mapper.LopHanhChinhMapper;
import org.example.trungcapphuongnam.module.daoTao.repository.KhoaDaoTaoRepository;
import org.example.trungcapphuongnam.module.daoTao.repository.LopHanhChinhRepository;
import org.example.trungcapphuongnam.module.daoTao.service.LopHanhChinhService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Transactional
public class LopHanhChinhServiceImpl implements LopHanhChinhService {

    private final LopHanhChinhRepository repository;
    private final KhoaDaoTaoRepository khoaDaoTaoRepository;
    private final LopHanhChinhMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<LopHanhChinhResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<LopHanhChinhResponse> findAllByChuongTrinhVersionId(
            Long chuongTrinhVersionId,
            Pageable pageable
    ) {
        return repository.findByChuongTrinhVersionId(
                chuongTrinhVersionId,
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LopHanhChinhResponse> findAllByKhoaDaoTaoId(
            Long khoaDaoTaoId,
            Pageable pageable
    ) {
        return repository.findByKhoaDaoTaoId(
                khoaDaoTaoId,
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LopHanhChinhResponse> findAllByChuongTrinhVersionIdAndKhoaDaoTaoId(
            Long chuongTrinhVersionId,
            Long khoaDaoTaoId,
            Pageable pageable
    ) {
        return repository.findByChuongTrinhVersionIdAndKhoaDaoTaoId(
                chuongTrinhVersionId,
                khoaDaoTaoId,
                pageable
        ).map(mapper::toResponse);
    }
    @Override
    @Transactional(readOnly = true)
    public LopHanhChinhResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Override
    public LopHanhChinhResponse create(LopHanhChinhRequest request) {
        validate(request, null);
        if (repository.existsByMaLop(request.getMaLop())) {
            throw new DuplicateResourceException("Mã lớp đã tồn tại: " + request.getMaLop());
        }
        if (request.getTrangThai() == null || request.getTrangThai().isBlank()) {
            request.setTrangThai(TrangThaiLopHanhChinh.dang_hoc.name());
        }
        if (request.getSiSo() == null) request.setSiSo(0);
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    public LopHanhChinhResponse update(Long id, LopHanhChinhRequest request) {
        LopHanhChinh entity = getEntity(id);
        validate(request, id);
        if (repository.existsByMaLopAndIdNot(request.getMaLop(), id)) {
            throw new DuplicateResourceException("Mã lớp đã tồn tại: " + request.getMaLop());
        }
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Lớp hành chính không tồn tại: " + id);
        repository.deleteById(id);
    }

    private void validate(LopHanhChinhRequest request, Long id) {
        request.setMaLop(TextUtil.trimRequired(request.getMaLop()));
        request.setTenLop(TextUtil.trimRequired(request.getTenLop()));
        if (request.getChuongTrinhVersionId() == null) throw new BadRequestException("chuongTrinhVersionId không được để trống");
        if (request.getKhoaDaoTaoId() != null && !khoaDaoTaoRepository.existsById(request.getKhoaDaoTaoId())) {
            throw new ResourceNotFoundException("Khóa đào tạo không tồn tại: " + request.getKhoaDaoTaoId());
        }
        if (request.getSiSo() != null && request.getSiSo() < 0) throw new BadRequestException("Sĩ số không được âm");
        if (request.getTrangThai() != null && !request.getTrangThai().isBlank()) {
            boolean valid = Arrays.stream(TrangThaiLopHanhChinh.values())
                    .anyMatch(e -> e.name().equals(request.getTrangThai()));
            if (!valid) throw new BadRequestException("Trạng thái lớp hành chính không hợp lệ: " + request.getTrangThai());
        }
    }

    private LopHanhChinh getEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lớp hành chính không tồn tại: " + id));
    }
}
