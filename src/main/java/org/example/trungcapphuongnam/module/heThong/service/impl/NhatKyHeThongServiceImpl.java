package org.example.trungcapphuongnam.module.heThong.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.heThong.dto.request.NhatKyHeThongRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.NhatKyHeThongResponse;
import org.example.trungcapphuongnam.module.heThong.entity.NhatKyHeThong;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoan;
import org.example.trungcapphuongnam.module.heThong.exception.HeThongNotFoundException;
import org.example.trungcapphuongnam.module.heThong.mapper.NhatKyHeThongMapper;
import org.example.trungcapphuongnam.module.heThong.repository.NhatKyHeThongRepository;
import org.example.trungcapphuongnam.module.heThong.repository.TaiKhoanRepository;
import org.example.trungcapphuongnam.module.heThong.service.NhatKyHeThongService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NhatKyHeThongServiceImpl implements NhatKyHeThongService {

    private final NhatKyHeThongRepository nhatKyHeThongRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final NhatKyHeThongMapper nhatKyHeThongMapper;

    @Override
    @Transactional(readOnly = true)
    public List<NhatKyHeThongResponse> getAll() {
        return nhatKyHeThongRepository.findAll().stream().map(nhatKyHeThongMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public NhatKyHeThongResponse getById(Long id) {
        return nhatKyHeThongMapper.toResponse(findByIdOrThrow(id));
    }

    @Override
    public NhatKyHeThongResponse create(NhatKyHeThongRequest request) {
        NhatKyHeThong entity = nhatKyHeThongMapper.toEntity(request);
        entity.setTaiKhoan(resolveTaiKhoan(request == null ? null : request.getTaiKhoanId()));
        return nhatKyHeThongMapper.toResponse(nhatKyHeThongRepository.save(entity));
    }

    @Override
    public NhatKyHeThongResponse update(Long id, NhatKyHeThongRequest request) {
        NhatKyHeThong entity = findByIdOrThrow(id);
        nhatKyHeThongMapper.updateEntity(entity, request);
        entity.setTaiKhoan(resolveTaiKhoan(request == null ? null : request.getTaiKhoanId()));
        return nhatKyHeThongMapper.toResponse(nhatKyHeThongRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        nhatKyHeThongRepository.delete(findByIdOrThrow(id));
    }

    private NhatKyHeThong findByIdOrThrow(Long id) {
        return nhatKyHeThongRepository.findById(id)
                .orElseThrow(() -> new HeThongNotFoundException("Không tìm thấy nhật ký hệ thống với id = " + id));
    }

    private TaiKhoan resolveTaiKhoan(Long taiKhoanId) {
        if (taiKhoanId == null) {
            return null;
        }
        return taiKhoanRepository.findById(taiKhoanId)
                .orElseThrow(() -> new HeThongNotFoundException("Không tìm thấy tài khoản với id = " + taiKhoanId));
    }
}
