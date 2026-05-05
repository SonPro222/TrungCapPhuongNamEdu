package org.example.trungcapphuongnam.module.sinhVien.service.impl;

import org.example.trungcapphuongnam.module.sinhVien.dto.request.SinhVienChuongTrinhRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.SinhVienChuongTrinhResponse;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVien;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVienChuongTrinh;
import org.example.trungcapphuongnam.common.exception.SinhVienException;
import org.example.trungcapphuongnam.common.exception.SinhVienNotFoundException;
import org.example.trungcapphuongnam.module.sinhVien.mapper.SinhVienChuongTrinhMapper;
import org.example.trungcapphuongnam.module.sinhVien.repository.SinhVienChuongTrinhRepository;
import org.example.trungcapphuongnam.module.sinhVien.repository.SinhVienRepository;
import org.example.trungcapphuongnam.module.sinhVien.service.SinhVienChuongTrinhService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SinhVienChuongTrinhServiceImpl implements SinhVienChuongTrinhService {

    private final SinhVienChuongTrinhRepository repository;
    private final SinhVienRepository sinhVienRepository;
    private final SinhVienChuongTrinhMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<SinhVienChuongTrinhResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SinhVienChuongTrinhResponse findById(Long id) {
        return mapper.toResponse(getById(id));
    }

    @Override
    public SinhVienChuongTrinhResponse create(SinhVienChuongTrinhRequest request) {
        if (repository.existsBySinhVienIdAndChuongTrinhVersionId(request.getSinhVienId(), request.getChuongTrinhVersionId())) {
            throw new SinhVienException("Sinh viên đã đăng ký chương trình version này");
        }
        SinhVien sinhVien = getSinhVien(request.getSinhVienId());
        SinhVienChuongTrinh saved = repository.save(mapper.toEntity(request, sinhVien));
        return mapper.toResponse(saved);
    }

    @Override
    public SinhVienChuongTrinhResponse update(Long id, SinhVienChuongTrinhRequest request) {
        SinhVienChuongTrinh entity = getById(id);
        boolean duplicated = repository.findAll().stream()
                .filter(item -> !item.getId().equals(id))
                .anyMatch(item -> item.getSinhVien().getId().equals(request.getSinhVienId())
                        && item.getChuongTrinhVersionId().equals(request.getChuongTrinhVersionId()));
        if (duplicated) {
            throw new SinhVienException("Sinh viên đã đăng ký chương trình version này");
        }
        SinhVien sinhVien = getSinhVien(request.getSinhVienId());
        mapper.updateEntity(entity, request, sinhVien);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        SinhVienChuongTrinh entity = getById(id);
        repository.delete(entity);
    }

    private SinhVienChuongTrinh getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new SinhVienNotFoundException("Sinh viên chương trình", id));
    }

    private SinhVien getSinhVien(Long id) {
        return sinhVienRepository.findById(id)
                .orElseThrow(() -> new SinhVienNotFoundException("Sinh viên", id));
    }
}
