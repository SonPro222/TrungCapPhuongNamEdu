package org.example.trungcapphuongnam.module.daoTao.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.daoTao.enums.TrangThaiLopHanhChinh;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.DuplicateResourceException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.util.TextUtil;
import org.example.trungcapphuongnam.module.daoTao.dto.LopHanhChinhRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.LopHanhChinhResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.LopHanhChinh;
import org.example.trungcapphuongnam.module.giangDay.repository.SinhVienLopHocPhanRepository;
import org.example.trungcapphuongnam.module.sinhVien.repository.SinhVienChuongTrinhRepository;

import java.time.LocalDateTime;
import java.util.List;

import org.example.trungcapphuongnam.module.daoTao.mapper.LopHanhChinhMapper;
import org.example.trungcapphuongnam.module.daoTao.repository.KhoaDaoTaoRepository;
import org.example.trungcapphuongnam.module.daoTao.repository.LopHanhChinhRepository;
import org.example.trungcapphuongnam.module.daoTao.service.LopHanhChinhService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Transactional
public class LopHanhChinhServiceImpl implements LopHanhChinhService {

    private final LopHanhChinhRepository repository;
    private final KhoaDaoTaoRepository khoaDaoTaoRepository;
    private final LopHanhChinhMapper mapper;
    private final SinhVienChuongTrinhRepository sinhVienChuongTrinhRepository;
    private final SinhVienLopHocPhanRepository sinhVienLopHocPhanRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<LopHanhChinhResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<LopHanhChinhResponse> findAllByChuongTrinhVersionId(
            Long chuongTrinhVersionId,
            Pageable pageable
    ) {
        return repository.findByChuongTrinhVersionId(
                chuongTrinhVersionId,
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LopHanhChinhResponse> findAllByKhoaDaoTaoId(
            Long khoaDaoTaoId,
            Pageable pageable
    ) {
        return repository.findByKhoaDaoTaoId(
                khoaDaoTaoId,
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LopHanhChinhResponse> findAllByChuongTrinhVersionIdAndKhoaDaoTaoId(
            Long chuongTrinhVersionId,
            Long khoaDaoTaoId,
            Pageable pageable
    ) {
        return repository.findByChuongTrinhVersionIdAndKhoaDaoTaoId(
                chuongTrinhVersionId,
                khoaDaoTaoId,
                pageable
        ).map(mapper::toResponse);
    }
    @Override
    @Transactional(readOnly = true)
    public LopHanhChinhResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Override
    public LopHanhChinhResponse create(LopHanhChinhRequest request) {
        validate(request, null);
        if (repository.existsByMaLop(request.getMaLop())) {
            throw new DuplicateResourceException("Mã lớp đã tồn tại: " + request.getMaLop());
        }
        if (request.getTrangThai() == null || request.getTrangThai().isBlank()) {
            request.setTrangThai(TrangThaiLopHanhChinh.dang_hoc.name());
        }
        if (request.getSiSo() == null) request.setSiSo(0);
        validate75PhanTram(request);
        LopHanhChinh entity = mapper.toEntity(request);
        setNgayChotNeuCanThiet(entity, false);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public LopHanhChinhResponse update(Long id, LopHanhChinhRequest request) {
        LopHanhChinh entity = getEntity(id);
        validate(request, id);
        if (repository.existsByMaLopAndIdNot(request.getMaLop(), id)) {
            throw new DuplicateResourceException("Mã lớp đã tồn tại: " + request.getMaLop());
        }
        mapper.updateEntity(entity, request);
        setNgayChotNeuCanThiet(entity, true);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Lớp hành chính không tồn tại: " + id);
        repository.deleteById(id);
    }

    @Override
    public LopHanhChinhResponse chotTuyenSinh(Long id) {
        LopHanhChinh entity = getEntity(id);
        entity.setDaChotTuyenSinh(true);
        if (entity.getNgayChotTuyenSinh() == null) {
            entity.setNgayChotTuyenSinh(LocalDateTime.now());
        }
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public LopHanhChinhResponse huyChoTuyenSinh(Long id) {
        LopHanhChinh entity = getEntity(id);
        if (!Boolean.TRUE.equals(entity.getDaChotTuyenSinh())) {
            throw new BadRequestException("Lớp hành chính chưa được chốt tuyển sinh.");
        }
        List<Long> svIds = sinhVienChuongTrinhRepository.findSinhVienIdsByLopHanhChinhId(id);
        if (!svIds.isEmpty() && sinhVienLopHocPhanRepository.existsBySinhVienIdIn(svIds)) {
            throw new BadRequestException(
                    "Không thể hủy chốt tuyển sinh vì lớp hành chính đã phát sinh lớp học phần.");
        }
        entity.setDaChotTuyenSinh(false);
        entity.setNgayChotTuyenSinh(null);
        return mapper.toResponse(repository.save(entity));
    }

    // ---------------------------------------------------------------

    private void validate(LopHanhChinhRequest request, Long id) {
        request.setMaLop(TextUtil.trimRequired(request.getMaLop()));
        request.setTenLop(TextUtil.trimRequired(request.getTenLop()));
        if (request.getChuongTrinhVersionId() == null)
            throw new BadRequestException("chuongTrinhVersionId không được để trống");
        if (request.getKhoaDaoTaoId() != null && !khoaDaoTaoRepository.existsById(request.getKhoaDaoTaoId())) {
            throw new ResourceNotFoundException("Khóa đào tạo không tồn tại: " + request.getKhoaDaoTaoId());
        }
        if (request.getSiSo() != null && request.getSiSo() < 0)
            throw new BadRequestException("Sĩ số không được âm");
        if (request.getTrangThai() != null && !request.getTrangThai().isBlank()) {
            boolean valid = Arrays.stream(TrangThaiLopHanhChinh.values())
                    .anyMatch(e -> e.name().equals(request.getTrangThai()));
            if (!valid) throw new BadRequestException("Trạng thái lớp hành chính không hợp lệ: " + request.getTrangThai());
        }
        // Validate khoảng ngày nhận sinh viên
        if (request.getNgayBatDauNhanSinhVien() != null && request.getNgayKetThucNhanSinhVien() != null
                && request.getNgayKetThucNhanSinhVien().isBefore(request.getNgayBatDauNhanSinhVien())) {
            throw new BadRequestException("ngayKetThucNhanSinhVien không được trước ngayBatDauNhanSinhVien");
        }
    }

    /**
     * Validator 75%: khi tạo LHC mới, LHC gần nhất cùng nhóm (chuongTrinhVersionId + khoaDaoTaoId)
     * phải đã đạt >= 75% sĩ số tối đa. Nếu chưa có LHC nào thì bỏ qua.
     */
    private void validate75PhanTram(LopHanhChinhRequest request) {
        if (request.getChuongTrinhVersionId() == null) return;

        List<LopHanhChinh> existing;
        if (request.getKhoaDaoTaoId() != null) {
            existing = repository.findByChuongTrinhVersionIdAndKhoaDaoTaoIdOrderByIdDesc(
                    request.getChuongTrinhVersionId(), request.getKhoaDaoTaoId());
        } else {
            existing = repository.findByChuongTrinhVersionIdOrderByIdDesc(
                    request.getChuongTrinhVersionId());
        }

        if (existing.isEmpty()) return; // Lớp đầu tiên → không cần check

        LopHanhChinh latest = existing.get(0);
        Integer siSo = latest.getSiSo();
        if (siSo == null || siSo <= 0) return; // Không có sĩ số → bỏ qua

        long svCount = sinhVienChuongTrinhRepository.countByLopHanhChinhId(latest.getId());
        long nguong75 = (long) Math.ceil(siSo * 0.75);

        if (svCount < nguong75) {
            throw new BadRequestException(
                    "Không thể tạo lớp hành chính mới. Lớp hành chính gần nhất ("
                    + latest.getMaLop() + ") chỉ có " + svCount + "/" + siSo
                    + " sinh viên (chưa đạt 75% = " + nguong75 + " SV).");
        }
    }

    /**
     * Nếu daChotTuyenSinh = true và ngayChotTuyenSinh đang null thì tự set now().
     * keepExisting=true khi update (không ghi đè ngayChotTuyenSinh đã có sẵn).
     */
    private void setNgayChotNeuCanThiet(LopHanhChinh entity, boolean keepExisting) {
        if (Boolean.TRUE.equals(entity.getDaChotTuyenSinh())) {
            if (!keepExisting || entity.getNgayChotTuyenSinh() == null) {
                if (entity.getNgayChotTuyenSinh() == null) {
                    entity.setNgayChotTuyenSinh(LocalDateTime.now());
                }
            }
        }
    }

    private LopHanhChinh getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lớp hành chính không tồn tại: " + id));
    }
}
