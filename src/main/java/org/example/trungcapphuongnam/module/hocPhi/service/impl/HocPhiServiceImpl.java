package org.example.trungcapphuongnam.module.hocPhi.service.impl;

import lombok.RequiredArgsConstructor;

import org.example.trungcapphuongnam.module.hocPhi.enums.TrangThaiHocPhi;
import org.example.trungcapphuongnam.module.hocPhi.dto.request.HocPhiRequest;
import org.example.trungcapphuongnam.module.hocPhi.dto.response.HocPhiResponse;
import org.example.trungcapphuongnam.module.hocPhi.entity.HocPhi;
import org.example.trungcapphuongnam.module.hocPhi.HocPhiNotFoundException;
import org.example.trungcapphuongnam.module.hocPhi.mapper.HocPhiMapper;
import org.example.trungcapphuongnam.module.hocPhi.repository.HocPhiRepository;
import org.example.trungcapphuongnam.module.hocPhi.service.HocPhiService;
import org.example.trungcapphuongnam.validator.HocPhiValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HocPhiServiceImpl implements HocPhiService {

    private final HocPhiRepository hocPhiRepository;
    private final HocPhiMapper hocPhiMapper;

    @Override
    public List<HocPhiResponse> getAll() {
        return hocPhiRepository.findAll()
                .stream()
                .map(hocPhiMapper::toResponse)
                .toList();
    }

    @Override
    public HocPhiResponse getById(Long id) {
        return hocPhiMapper.toResponse(findEntityById(id));
    }

    @Override
    public List<HocPhiResponse> getBySinhVienChuongTrinhId(Long sinhVienChuongTrinhId) {
        return hocPhiRepository.findBySinhVienChuongTrinhId(sinhVienChuongTrinhId)
                .stream()
                .map(hocPhiMapper::toResponse)
                .toList();
    }

    @Override
    public List<HocPhiResponse> getByTrangThai(TrangThaiHocPhi trangThai) {
        return hocPhiRepository.findByTrangThai(trangThai)
                .stream()
                .map(hocPhiMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public HocPhiResponse create(HocPhiRequest request) {
        HocPhiValidator.validateHocPhi(request);
        HocPhi saved = hocPhiRepository.save(hocPhiMapper.toEntity(request));
        return hocPhiMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public HocPhiResponse update(Long id, HocPhiRequest request) {
        HocPhiValidator.validateHocPhi(request);
        HocPhi entity = findEntityById(id);
        hocPhiMapper.updateEntity(entity, request);
        return hocPhiMapper.toResponse(hocPhiRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        HocPhi entity = findEntityById(id);
        hocPhiRepository.delete(entity);
    }

    private HocPhi findEntityById(Long id) {
        return hocPhiRepository.findById(id)
                .orElseThrow(() -> new HocPhiNotFoundException(id));
    }
}
