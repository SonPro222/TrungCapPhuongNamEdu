package org.example.trungcapphuongnam.module.hocPhi.service.impl;

import lombok.RequiredArgsConstructor;

import org.example.trungcapphuongnam.common.enums.TrangThaiHocPhi;
import org.example.trungcapphuongnam.module.hocPhi.dto.request.GiaoDichHocPhiRequest;
import org.example.trungcapphuongnam.module.hocPhi.dto.response.GiaoDichHocPhiResponse;
import org.example.trungcapphuongnam.module.hocPhi.entity.GiaoDichHocPhi;
import org.example.trungcapphuongnam.module.hocPhi.entity.HocPhi;
import org.example.trungcapphuongnam.module.hocPhi.exception.GiaoDichHocPhiNotFoundException;
import org.example.trungcapphuongnam.module.hocPhi.exception.HocPhiBusinessException;
import org.example.trungcapphuongnam.module.hocPhi.exception.HocPhiNotFoundException;
import org.example.trungcapphuongnam.module.hocPhi.mapper.GiaoDichHocPhiMapper;
import org.example.trungcapphuongnam.module.hocPhi.repository.GiaoDichHocPhiRepository;
import org.example.trungcapphuongnam.module.hocPhi.repository.HocPhiRepository;
import org.example.trungcapphuongnam.module.hocPhi.service.GiaoDichHocPhiService;
import org.example.trungcapphuongnam.common.util.HocPhiUtil;
import org.example.trungcapphuongnam.validator.HocPhiValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GiaoDichHocPhiServiceImpl implements GiaoDichHocPhiService {

    private final GiaoDichHocPhiRepository giaoDichHocPhiRepository;
    private final HocPhiRepository hocPhiRepository;
    private final GiaoDichHocPhiMapper giaoDichHocPhiMapper;

    @Override
    public List<GiaoDichHocPhiResponse> getAll() {
        return giaoDichHocPhiRepository.findAll()
                .stream()
                .map(giaoDichHocPhiMapper::toResponse)
                .toList();
    }

    @Override
    public GiaoDichHocPhiResponse getById(Long id) {
        return giaoDichHocPhiMapper.toResponse(findEntityById(id));
    }

    @Override
    public List<GiaoDichHocPhiResponse> getByHocPhiId(Long hocPhiId) {
        return giaoDichHocPhiRepository.findByHocPhiId(hocPhiId)
                .stream()
                .map(giaoDichHocPhiMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public GiaoDichHocPhiResponse create(GiaoDichHocPhiRequest request) {
        HocPhi hocPhi = hocPhiRepository.findById(request.getHocPhiId())
                .orElseThrow(() -> new HocPhiNotFoundException(request.getHocPhiId()));
        HocPhiValidator.validateGiaoDich(hocPhi, request);

        if (request.getMaGiaoDich() != null && !request.getMaGiaoDich().isBlank()
                && giaoDichHocPhiRepository.existsByMaGiaoDich(request.getMaGiaoDich())) {
            throw new HocPhiBusinessException("Mã giao dịch đã tồn tại: " + request.getMaGiaoDich());
        }

        GiaoDichHocPhi saved = giaoDichHocPhiRepository.save(giaoDichHocPhiMapper.toEntity(request));
        capNhatTienDaDong(hocPhi, request.getSoTien());
        return giaoDichHocPhiMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public GiaoDichHocPhiResponse update(Long id, GiaoDichHocPhiRequest request) {
        GiaoDichHocPhi entity = findEntityById(id);
        HocPhi oldHocPhi = hocPhiRepository.findById(entity.getHocPhiId())
                .orElseThrow(() -> new HocPhiNotFoundException(entity.getHocPhiId()));
        HocPhi newHocPhi = hocPhiRepository.findById(request.getHocPhiId())
                .orElseThrow(() -> new HocPhiNotFoundException(request.getHocPhiId()));
        HocPhiValidator.validateGiaoDich(newHocPhi, request);

        if (request.getMaGiaoDich() != null && !request.getMaGiaoDich().isBlank()
                && !request.getMaGiaoDich().equals(entity.getMaGiaoDich())
                && giaoDichHocPhiRepository.existsByMaGiaoDich(request.getMaGiaoDich())) {
            throw new HocPhiBusinessException("Mã giao dịch đã tồn tại: " + request.getMaGiaoDich());
        }

        capNhatTienDaDong(oldHocPhi, entity.getSoTien().negate());
        giaoDichHocPhiMapper.updateEntity(entity, request);
        GiaoDichHocPhi saved = giaoDichHocPhiRepository.save(entity);
        capNhatTienDaDong(newHocPhi, request.getSoTien());
        return giaoDichHocPhiMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        GiaoDichHocPhi entity = findEntityById(id);
        HocPhi hocPhi = hocPhiRepository.findById(entity.getHocPhiId())
                .orElseThrow(() -> new HocPhiNotFoundException(entity.getHocPhiId()));
        capNhatTienDaDong(hocPhi, entity.getSoTien().negate());
        giaoDichHocPhiRepository.delete(entity);
    }

    private GiaoDichHocPhi findEntityById(Long id) {
        return giaoDichHocPhiRepository.findById(id)
                .orElseThrow(() -> new GiaoDichHocPhiNotFoundException(id));
    }

    private void capNhatTienDaDong(HocPhi hocPhi, BigDecimal soTienCongThem) {
        BigDecimal soTienMoi = HocPhiUtil.safe(hocPhi.getSoTienDaDong()).add(HocPhiUtil.safe(soTienCongThem));
        if (soTienMoi.compareTo(BigDecimal.ZERO) < 0) {
            soTienMoi = BigDecimal.ZERO;
        }
        if (soTienMoi.compareTo(HocPhiUtil.safe(hocPhi.getSoTienPhaiDong())) > 0) {
            throw new HocPhiBusinessException("Tổng số tiền đã đóng không được lớn hơn số tiền phải đóng");
        }

        hocPhi.setSoTienDaDong(soTienMoi);
        hocPhi.setTrangThai(HocPhiUtil.tinhTrangThai(hocPhi.getSoTienPhaiDong(), soTienMoi, hocPhi.getHanDong()));
        if (hocPhi.getTrangThai() == TrangThaiHocPhi.da_dong && hocPhi.getNgayDong() == null) {
            hocPhi.setNgayDong(LocalDate.now());
        }
        if (hocPhi.getTrangThai() != TrangThaiHocPhi.da_dong) {
            hocPhi.setNgayDong(null);
        }
        hocPhiRepository.save(hocPhi);
    }
}
