package org.example.trungcapphuongnam.module.heThong.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.heThong.dto.request.TaiKhoanVaiTroRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.TaiKhoanVaiTroResponse;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoanVaiTro;
import org.example.trungcapphuongnam.module.heThong.exception.HeThongNotFoundException;
import org.example.trungcapphuongnam.module.heThong.mapper.TaiKhoanVaiTroMapper;
import org.example.trungcapphuongnam.module.heThong.repository.TaiKhoanVaiTroRepository;
import org.example.trungcapphuongnam.module.heThong.service.TaiKhoanVaiTroService;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaiKhoanVaiTroServiceImpl implements TaiKhoanVaiTroService {
    private final TaiKhoanVaiTroRepository taiKhoanVaiTroRepository;
    private final TaiKhoanVaiTroMapper taiKhoanVaiTroMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TaiKhoanVaiTroResponse> getAll() { return taiKhoanVaiTroRepository.findAll().stream().map(taiKhoanVaiTroMapper::toResponse).toList(); }

    @Override
    @Transactional(readOnly = true)
    public Page<TaiKhoanVaiTroResponse> search(Long taiKhoanId, Long vaiTroId, Pageable pageable) {
        Specification<TaiKhoanVaiTro> spec = Specification.where(null);
        if (taiKhoanId != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("taiKhoan").get("id"), taiKhoanId));
        if (vaiTroId != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("vaiTro").get("id"), vaiTroId));
        return taiKhoanVaiTroRepository.findAll(spec, pageable).map(taiKhoanVaiTroMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public TaiKhoanVaiTroResponse getById(Long id) { return taiKhoanVaiTroMapper.toResponse(findByIdOrThrow(id)); }
    @Override
    public TaiKhoanVaiTroResponse create(TaiKhoanVaiTroRequest request) { return taiKhoanVaiTroMapper.toResponse(taiKhoanVaiTroRepository.save(taiKhoanVaiTroMapper.toEntity(request))); }
    @Override
    public TaiKhoanVaiTroResponse update(Long id, TaiKhoanVaiTroRequest request) { TaiKhoanVaiTro entity = findByIdOrThrow(id); taiKhoanVaiTroMapper.updateEntity(entity, request); return taiKhoanVaiTroMapper.toResponse(taiKhoanVaiTroRepository.save(entity)); }
    @Override
    public void delete(Long id) { taiKhoanVaiTroRepository.delete(findByIdOrThrow(id)); }
    private TaiKhoanVaiTro findByIdOrThrow(Long id) { return taiKhoanVaiTroRepository.findById(id).orElseThrow(() -> new HeThongNotFoundException("Không tìm thấy tài khoản vai trò với id = " + id)); }
}
