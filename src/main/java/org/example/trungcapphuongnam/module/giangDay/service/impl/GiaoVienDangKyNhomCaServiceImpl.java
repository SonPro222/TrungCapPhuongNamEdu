package org.example.trungcapphuongnam.module.giangDay.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.giangDay.GiangDayException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.GiaoVienDangKyNhomCaRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.GiaoVienDangKyNhomCaResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.CaHoc;
import org.example.trungcapphuongnam.module.giangDay.entity.GiaoVienDangKyNhomCa;
import org.example.trungcapphuongnam.module.giangDay.mapper.GiaoVienDangKyNhomCaMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.CaHocRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienDangKyNhomCaRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienRepository;
import org.example.trungcapphuongnam.module.giangDay.service.GiaoVienDangKyNhomCaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GiaoVienDangKyNhomCaServiceImpl implements GiaoVienDangKyNhomCaService {

    private final GiaoVienDangKyNhomCaRepository repository;
    private final GiaoVienRepository giaoVienRepository;
    private final CaHocRepository caHocRepository;
    private final GiaoVienDangKyNhomCaMapper mapper;

    @Override
    public Page<GiaoVienDangKyNhomCaResponse> getAll(Long giaoVienId, Long khungKyId, Pageable pageable) {
        // Hiện tại dùng findAll, có thể thêm filter sau
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    public List<GiaoVienDangKyNhomCaResponse> getByGiaoVien(Long giaoVienId) {
        return repository.findByGiaoVienId(giaoVienId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public GiaoVienDangKyNhomCaResponse getById(Long id) {
        return mapper.toResponse(findEntityById(id));
    }

    @Override
    public GiaoVienDangKyNhomCaResponse create(GiaoVienDangKyNhomCaRequest request) {
        validate(request, null);
        GiaoVienDangKyNhomCa entity = GiaoVienDangKyNhomCa.builder()
                .giaoVienId(request.getGiaoVienId())
                .khungKyId(request.getKhungKyId())
                .thuTrongTuan(request.getThuTrongTuan())
                .caBatDauId(request.getCaBatDauId())
                .caKetThucId(request.getCaKetThucId())
                .mucDoUuTien(request.getMucDoUuTien() != null ? request.getMucDoUuTien() : 1)
                .ghiChu(request.getGhiChu())
                .build();
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public GiaoVienDangKyNhomCaResponse update(Long id, GiaoVienDangKyNhomCaRequest request) {
        validate(request, id);
        GiaoVienDangKyNhomCa entity = findEntityById(id);
        entity.setGiaoVienId(request.getGiaoVienId());
        entity.setKhungKyId(request.getKhungKyId());
        entity.setThuTrongTuan(request.getThuTrongTuan());
        entity.setCaBatDauId(request.getCaBatDauId());
        entity.setCaKetThucId(request.getCaKetThucId());
        entity.setMucDoUuTien(request.getMucDoUuTien() != null ? request.getMucDoUuTien() : 1);
        entity.setGhiChu(request.getGhiChu());
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.delete(findEntityById(id));
    }

    // ===== Private helpers =====

    private GiaoVienDangKyNhomCa findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GiangDayException("Đăng ký nhóm ca không tồn tại: " + id));
    }

    private void validate(GiaoVienDangKyNhomCaRequest request, Long excludeId) {
        // giaoVienId bắt buộc
        if (request.getGiaoVienId() == null) {
            throw new GiangDayException("Giáo viên không được để trống");
        }
        if (!giaoVienRepository.existsById(request.getGiaoVienId())) {
            throw new GiangDayException("Giáo viên không tồn tại: " + request.getGiaoVienId());
        }

        // thuTrongTuan bắt buộc
        if (request.getThuTrongTuan() == null) {
            throw new GiangDayException("Thứ trong tuần không được để trống");
        }

        // caBatDauId + caKetThucId bắt buộc
        if (request.getCaBatDauId() == null) {
            throw new GiangDayException("Ca bắt đầu không được để trống");
        }
        if (request.getCaKetThucId() == null) {
            throw new GiangDayException("Ca kết thúc không được để trống");
        }

        // Kiểm tra ca tồn tại và lấy thuTu
        CaHoc caBatDau = caHocRepository.findById(request.getCaBatDauId())
                .orElseThrow(() -> new GiangDayException("Ca bắt đầu không tồn tại: " + request.getCaBatDauId()));
        CaHoc caKetThuc = caHocRepository.findById(request.getCaKetThucId())
                .orElseThrow(() -> new GiangDayException("Ca kết thúc không tồn tại: " + request.getCaKetThucId()));

        Integer thuTuBatDau = caBatDau.getThuTu();
        Integer thuTuKetThuc = caKetThuc.getThuTu();

        if (thuTuBatDau == null || thuTuKetThuc == null) {
            throw new GiangDayException("Ca học chưa có thứ tự, không thể cấu hình nhóm ca liên tục.");
        }
        if (thuTuKetThuc < thuTuBatDau) {
            throw new GiangDayException("Ca kết thúc phải có thứ tự >= ca bắt đầu");
        }
        if (thuTuKetThuc - thuTuBatDau > 1) {
            throw new GiangDayException("Chỉ cho phép 1 ca hoặc 2 ca liền kề");
        }

        // mucDoUuTien 1-3
        if (request.getMucDoUuTien() != null && (request.getMucDoUuTien() < 1 || request.getMucDoUuTien() > 3)) {
            throw new GiangDayException("Mức độ ưu tiên phải từ 1 đến 3");
        }

        // Kiểm tra trùng (bỏ qua bản ghi đang update)
        boolean trung = repository.existsByGiaoVienIdAndKhungKyIdAndThuTrongTuanAndCaBatDauIdAndCaKetThucId(
                request.getGiaoVienId(),
                request.getKhungKyId(),
                request.getThuTrongTuan(),
                request.getCaBatDauId(),
                request.getCaKetThucId()
        );
        if (trung) {
            // Nếu đang update thì kiểm tra có phải chính bản ghi đó không
            if (excludeId == null) {
                throw new GiangDayException("Cấu hình nhóm ca này đã tồn tại");
            }
            // Cho phép update nếu trùng chính nó
            GiaoVienDangKyNhomCa existing = findEntityById(excludeId);
            boolean laSameRecord = existing.getGiaoVienId().equals(request.getGiaoVienId())
                    && java.util.Objects.equals(existing.getKhungKyId(), request.getKhungKyId())
                    && java.util.Objects.equals(existing.getThuTrongTuan(), request.getThuTrongTuan())
                    && existing.getCaBatDauId().equals(request.getCaBatDauId())
                    && existing.getCaKetThucId().equals(request.getCaKetThucId());
            if (!laSameRecord) {
                throw new GiangDayException("Cấu hình nhóm ca này đã tồn tại");
            }
        }
    }
}
