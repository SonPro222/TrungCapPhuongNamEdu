package org.example.trungcapphuongnam.module.heThong.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.heThong.dto.request.VaiTroQuyenRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.VaiTroQuyenResponse;
import org.example.trungcapphuongnam.module.heThong.entity.VaiTroQuyen;
import org.example.trungcapphuongnam.module.heThong.HeThongNotFoundException;
import org.example.trungcapphuongnam.module.heThong.mapper.VaiTroQuyenMapper;
import org.example.trungcapphuongnam.module.heThong.repository.VaiTroQuyenRepository;
import org.example.trungcapphuongnam.module.heThong.service.VaiTroQuyenService;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VaiTroQuyenServiceImpl implements VaiTroQuyenService {

    private final VaiTroQuyenRepository vaiTroQuyenRepository;
    private final VaiTroQuyenMapper vaiTroQuyenMapper;

    @Override
    @Transactional(readOnly = true)
    public List<VaiTroQuyenResponse> getAll() {
        return vaiTroQuyenRepository.findAll()
                .stream()
                .map(vaiTroQuyenMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VaiTroQuyenResponse> search(Long vaiTroId, Long quyenId, Pageable pageable) {
        Specification<VaiTroQuyen> spec = Specification.where(null);

        if (vaiTroId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("vaiTro").get("id"), vaiTroId));
        }

        if (quyenId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("quyen").get("id"), quyenId));
        }

        return vaiTroQuyenRepository.findAll(spec, pageable).map(vaiTroQuyenMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public VaiTroQuyenResponse getById(Long id) {
        return vaiTroQuyenMapper.toResponse(findByIdOrThrow(id));
    }

    @Override
    public VaiTroQuyenResponse create(VaiTroQuyenRequest request) {
        if (request == null) {
            throw new RuntimeException("Dữ liệu gán quyền không hợp lệ");
        }

        if (request.getVaiTroId() == null) {
            throw new RuntimeException("Vai trò không được để trống");
        }

        if (request.getQuyenId() == null) {
            throw new RuntimeException("Quyền không được để trống");
        }

        if (vaiTroQuyenRepository.existsByVaiTro_IdAndQuyen_Id(request.getVaiTroId(), request.getQuyenId())) {
            throw new RuntimeException("Vai trò đã có quyền này");
        }

        VaiTroQuyen entity = vaiTroQuyenMapper.toEntity(request);
        return vaiTroQuyenMapper.toResponse(vaiTroQuyenRepository.save(entity));
    }

    @Override
    public VaiTroQuyenResponse update(Long id, VaiTroQuyenRequest request) {
        VaiTroQuyen entity = findByIdOrThrow(id);
        vaiTroQuyenMapper.updateEntity(entity, request);
        return vaiTroQuyenMapper.toResponse(vaiTroQuyenRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        vaiTroQuyenRepository.delete(findByIdOrThrow(id));
    }

    private VaiTroQuyen findByIdOrThrow(Long id) {
        return vaiTroQuyenRepository.findById(id)
                .orElseThrow(() -> new HeThongNotFoundException("Không tìm thấy vai trò quyền với id = " + id));
    }
}