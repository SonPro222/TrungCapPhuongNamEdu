package org.example.trungcapphuongnam.module.heThong.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.heThong.dto.request.NhatKyDangNhapRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.NhatKyDangNhapResponse;
import org.example.trungcapphuongnam.module.heThong.entity.NhatKyDangNhap;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoan;
import org.example.trungcapphuongnam.module.heThong.exception.HeThongNotFoundException;
import org.example.trungcapphuongnam.module.heThong.mapper.NhatKyDangNhapMapper;
import org.example.trungcapphuongnam.module.heThong.repository.NhatKyDangNhapRepository;
import org.example.trungcapphuongnam.module.heThong.repository.TaiKhoanRepository;
import org.example.trungcapphuongnam.module.heThong.service.NhatKyDangNhapService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NhatKyDangNhapServiceImpl implements NhatKyDangNhapService {

    private final NhatKyDangNhapRepository nhatKyDangNhapRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final NhatKyDangNhapMapper nhatKyDangNhapMapper;

    @Override
    @Transactional(readOnly = true)
    public List<NhatKyDangNhapResponse> getAll() {
        return nhatKyDangNhapRepository.findAll().stream().map(nhatKyDangNhapMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public NhatKyDangNhapResponse getById(Long id) {
        return nhatKyDangNhapMapper.toResponse(findByIdOrThrow(id));
    }

    @Override
    public NhatKyDangNhapResponse create(NhatKyDangNhapRequest request) {
        NhatKyDangNhap entity = nhatKyDangNhapMapper.toEntity(request);
        entity.setTaiKhoan(resolveTaiKhoan(request == null ? null : request.getTaiKhoanId()));
        return nhatKyDangNhapMapper.toResponse(nhatKyDangNhapRepository.save(entity));
    }

    @Override
    public NhatKyDangNhapResponse update(Long id, NhatKyDangNhapRequest request) {
        NhatKyDangNhap entity = findByIdOrThrow(id);
        nhatKyDangNhapMapper.updateEntity(entity, request);
        entity.setTaiKhoan(resolveTaiKhoan(request == null ? null : request.getTaiKhoanId()));
        return nhatKyDangNhapMapper.toResponse(nhatKyDangNhapRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        nhatKyDangNhapRepository.delete(findByIdOrThrow(id));
    }

    private NhatKyDangNhap findByIdOrThrow(Long id) {
        return nhatKyDangNhapRepository.findById(id)
                .orElseThrow(() -> new HeThongNotFoundException("Không tìm thấy nhật ký đăng nhập với id = " + id));
    }

    private TaiKhoan resolveTaiKhoan(Long taiKhoanId) {
        if (taiKhoanId == null) {
            return null;
        }
        return taiKhoanRepository.findById(taiKhoanId)
                .orElseThrow(() -> new HeThongNotFoundException("Không tìm thấy tài khoản với id = " + taiKhoanId));
    }
}
