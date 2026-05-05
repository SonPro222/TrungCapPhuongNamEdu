package org.example.trungcapphuongnam.module.sinhVien.service.impl;

import org.example.trungcapphuongnam.module.sinhVien.dto.request.MienTruBaoLuuRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.MienTruBaoLuuResponse;
import org.example.trungcapphuongnam.module.sinhVien.entity.MienTruBaoLuu;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVien;
import org.example.trungcapphuongnam.common.exception.SinhVienNotFoundException;
import org.example.trungcapphuongnam.module.sinhVien.mapper.MienTruBaoLuuMapper;
import org.example.trungcapphuongnam.module.sinhVien.repository.MienTruBaoLuuRepository;
import org.example.trungcapphuongnam.module.sinhVien.repository.SinhVienRepository;
import org.example.trungcapphuongnam.module.sinhVien.service.MienTruBaoLuuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MienTruBaoLuuServiceImpl implements MienTruBaoLuuService {

    private final MienTruBaoLuuRepository repository;
    private final SinhVienRepository sinhVienRepository;
    private final MienTruBaoLuuMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<MienTruBaoLuuResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public MienTruBaoLuuResponse findById(Long id) {
        return mapper.toResponse(getById(id));
    }

    @Override
    public MienTruBaoLuuResponse create(MienTruBaoLuuRequest request) {
        SinhVien sinhVien = getSinhVien(request.getSinhVienId());
        MienTruBaoLuu saved = repository.save(mapper.toEntity(request, sinhVien));
        return mapper.toResponse(saved);
    }

    @Override
    public MienTruBaoLuuResponse update(Long id, MienTruBaoLuuRequest request) {
        MienTruBaoLuu entity = getById(id);
        SinhVien sinhVien = getSinhVien(request.getSinhVienId());
        mapper.updateEntity(entity, request, sinhVien);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        MienTruBaoLuu entity = getById(id);
        repository.delete(entity);
    }

    private MienTruBaoLuu getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new SinhVienNotFoundException("Miễn trừ bảo lưu", id));
    }

    private SinhVien getSinhVien(Long id) {
        return sinhVienRepository.findById(id)
                .orElseThrow(() -> new SinhVienNotFoundException("Sinh viên", id));
    }
}
