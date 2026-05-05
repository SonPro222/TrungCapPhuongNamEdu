package org.example.trungcapphuongnam.module.sinhVien.service.impl;

import org.example.trungcapphuongnam.module.sinhVien.dto.request.SinhVienRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.SinhVienResponse;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVien;
import org.example.trungcapphuongnam.common.exception.SinhVienException;
import org.example.trungcapphuongnam.common.exception.SinhVienNotFoundException;
import org.example.trungcapphuongnam.module.sinhVien.mapper.SinhVienMapper;
import org.example.trungcapphuongnam.module.sinhVien.repository.SinhVienRepository;
import org.example.trungcapphuongnam.module.sinhVien.service.SinhVienService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SinhVienServiceImpl implements SinhVienService {

    private final SinhVienRepository repository;
    private final SinhVienMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<SinhVienResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SinhVienResponse findById(Long id) {
        return mapper.toResponse(getById(id));
    }

    @Override
    public SinhVienResponse create(SinhVienRequest request) {
        validateUniqueForCreate(request);
        SinhVien saved = repository.save(mapper.toEntity(request));
        return mapper.toResponse(saved);
    }

    @Override
    public SinhVienResponse update(Long id, SinhVienRequest request) {
        SinhVien entity = getById(id);
        validateUniqueForUpdate(id, request);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        SinhVien entity = getById(id);
        repository.delete(entity);
    }

    private SinhVien getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new SinhVienNotFoundException("Sinh viên", id));
    }

    private void validateUniqueForCreate(SinhVienRequest request) {
        if (repository.existsByMaSinhVien(request.getMaSinhVien())) {
            throw new SinhVienException("Mã sinh viên đã tồn tại");
        }
        if (repository.existsByEmail(request.getEmail())) {
            throw new SinhVienException("Email sinh viên đã tồn tại");
        }
        if (request.getTaiKhoanId() != null && repository.existsByTaiKhoanId(request.getTaiKhoanId())) {
            throw new SinhVienException("Tài khoản đã được gắn với sinh viên khác");
        }
    }

    private void validateUniqueForUpdate(Long id, SinhVienRequest request) {
        repository.findAll().stream()
                .filter(item -> !item.getId().equals(id))
                .filter(item -> item.getMaSinhVien().equals(request.getMaSinhVien()))
                .findFirst()
                .ifPresent(item -> { throw new SinhVienException("Mã sinh viên đã tồn tại"); });

        repository.findAll().stream()
                .filter(item -> !item.getId().equals(id))
                .filter(item -> item.getEmail().equals(request.getEmail()))
                .findFirst()
                .ifPresent(item -> { throw new SinhVienException("Email sinh viên đã tồn tại"); });

        if (request.getTaiKhoanId() != null) {
            repository.findAll().stream()
                    .filter(item -> !item.getId().equals(id))
                    .filter(item -> request.getTaiKhoanId().equals(item.getTaiKhoanId()))
                    .findFirst()
                    .ifPresent(item -> { throw new SinhVienException("Tài khoản đã được gắn với sinh viên khác"); });
        }
    }
}
