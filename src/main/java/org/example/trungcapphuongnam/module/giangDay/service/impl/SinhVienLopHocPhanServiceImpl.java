package org.example.trungcapphuongnam.module.giangDay.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMon;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhMonRepository;
import org.example.trungcapphuongnam.module.giangDay.GiangDayNotFoundException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.SinhVienLopHocPhanRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.SinhVienLopHocPhanResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.entity.SinhVienLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiSinhVienLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.mapper.SinhVienLopHocPhanMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.LopHocPhanRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.SinhVienLopHocPhanRepository;
import org.example.trungcapphuongnam.module.giangDay.service.SinhVienLopHocPhanService;
import org.example.trungcapphuongnam.module.giangDay.validator.SinhVienLopHocPhanValidator;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.SinhVienResponse;
import org.example.trungcapphuongnam.module.sinhVien.enums.SinhVienTrangThai;
import org.example.trungcapphuongnam.module.sinhVien.mapper.SinhVienMapper;
import org.example.trungcapphuongnam.module.sinhVien.repository.SinhVienChuongTrinhRepository;
import org.example.trungcapphuongnam.module.sinhVien.repository.SinhVienRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.module.giangDay.GiangDayException;
import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhanChuongTrinhMon;
import org.example.trungcapphuongnam.module.giangDay.enums.LoaiLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.repository.LopHocPhanChuongTrinhMonRepository;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
@Service
@RequiredArgsConstructor
@Transactional
public class SinhVienLopHocPhanServiceImpl implements SinhVienLopHocPhanService {
    private final LopHocPhanChuongTrinhMonRepository lopHocPhanChuongTrinhMonRepository;
    private final SinhVienLopHocPhanRepository repository;
    private final LopHocPhanRepository lopHocPhanRepository;
    private final ChuongTrinhMonRepository chuongTrinhMonRepository;
    private final SinhVienRepository sinhVienRepository;
    private final SinhVienChuongTrinhRepository sinhVienChuongTrinhRepository;
    private final SinhVienLopHocPhanMapper mapper;
    private final SinhVienMapper sinhVienMapper;
    private final SinhVienLopHocPhanValidator validator;

    @Override
    @Transactional(readOnly = true)
    public Page<SinhVienLopHocPhanResponse> getAll(
            Long lopHocPhanId,
            Long sinhVienId,
            TrangThaiSinhVienLopHocPhan trangThai,
            Pageable pageable
    ) {
        if (lopHocPhanId != null && sinhVienId != null && trangThai != null) {
            return repository.findByLopHocPhanIdAndSinhVienIdAndTrangThai(
                    lopHocPhanId,
                    sinhVienId,
                    trangThai,
                    pageable
            ).map(mapper::toResponse);
        }

        if (lopHocPhanId != null && sinhVienId != null) {
            return repository.findByLopHocPhanIdAndSinhVienId(lopHocPhanId, sinhVienId, pageable)
                    .map(mapper::toResponse);
        }

        if (lopHocPhanId != null && trangThai != null) {
            return repository.findByLopHocPhanIdAndTrangThai(lopHocPhanId, trangThai, pageable)
                    .map(mapper::toResponse);
        }

        if (sinhVienId != null && trangThai != null) {
            return repository.findBySinhVienIdAndTrangThai(sinhVienId, trangThai, pageable)
                    .map(mapper::toResponse);
        }

        if (lopHocPhanId != null) {
            return repository.findByLopHocPhanId(lopHocPhanId, pageable)
                    .map(mapper::toResponse);
        }

        if (sinhVienId != null) {
            return repository.findBySinhVienId(sinhVienId, pageable)
                    .map(mapper::toResponse);
        }

        if (trangThai != null) {
            return repository.findByTrangThai(trangThai, pageable)
                    .map(mapper::toResponse);
        }

        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SinhVienLopHocPhanResponse getById(Long id) {
        return mapper.toResponse(findEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SinhVienLopHocPhanResponse> getByLopHocPhan(Long lopHocPhanId, Pageable pageable) {
        if (lopHocPhanId == null) {
            throw new GiangDayException("Lớp học phần không được để trống");
        }

        return repository.findByLopHocPhanId(lopHocPhanId, pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SinhVienResponse> getSinhVienTrongLopHocPhan(Long lopHocPhanId, Pageable pageable) {
        if (lopHocPhanId == null) {
            throw new GiangDayException("Lớp học phần không được để trống");
        }

        Page<SinhVienLopHocPhan> pageDangHoc = repository.findByLopHocPhanIdAndTrangThai(
                lopHocPhanId,
                TrangThaiSinhVienLopHocPhan.dang_hoc,
                pageable
        );

        List<Long> sinhVienIds = pageDangHoc.getContent()
                .stream()
                .map(SinhVienLopHocPhan::getSinhVienId)
                .toList();

        if (sinhVienIds.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, pageDangHoc.getTotalElements());
        }

        List<SinhVienResponse> content = sinhVienRepository.findByIdIn(sinhVienIds)
                .stream()
                .map(sinhVienMapper::toResponse)
                .toList();

        return new PageImpl<>(content, pageable, pageDangHoc.getTotalElements());
    }
    @Override
    @Transactional(readOnly = true)
    public Page<SinhVienResponse> getSinhVienChuaVaoLopHocPhan(
            Long chuongTrinhVersionId,
            Long lopHocPhanId,
            Pageable pageable
    ) {
        if (lopHocPhanId == null) {
            throw new GiangDayException("Lớp học phần không được để trống");
        }

        LopHocPhan lopHocPhan = lopHocPhanRepository.findById(lopHocPhanId)
                .orElseThrow(() -> new GiangDayException("Lớp học phần không tồn tại"));

        Set<Long> versionIds = layVersionDuocHocLop(lopHocPhan, chuongTrinhVersionId);

        if (versionIds.isEmpty()) {
            return Page.empty(pageable);
        }

        return sinhVienRepository.findSinhVienDuDieuKienChuaVaoLopHocPhan(
                        versionIds,
                        lopHocPhanId,
                        validator.trangThaiChuongTrinhDuocPhanLop(),
                        List.of(SinhVienTrangThai.dang_hoc, SinhVienTrangThai.bao_luu),
                        validator.trangThaiDangTinhSiSo(),
                        pageable
                )
                .map(sinhVienMapper::toResponse);
    }
    @Override
    public SinhVienLopHocPhanResponse create(SinhVienLopHocPhanRequest request) {
        validateRequestCoBan(request);
        validator.validateThemSinhVienVaoLop(request.getSinhVienId(), request.getLopHocPhanId());

        SinhVienLopHocPhan entity = mapper.toEntity(request);

        if (entity.getNgayDangKy() == null) {
            entity.setNgayDangKy(LocalDateTime.now());
        }

        if (entity.getTrangThai() == null) {
            entity.setTrangThai(TrangThaiSinhVienLopHocPhan.da_dang_ky);
        }

        if (entity.getLaHocLai() == null) {
            entity.setLaHocLai(false);
        }

        SinhVienLopHocPhan saved = repository.save(entity);
        capNhatSiSoLopHocPhan(saved.getLopHocPhanId());

        return mapper.toResponse(saved);
    }

    @Override
    public SinhVienLopHocPhanResponse update(Long id, SinhVienLopHocPhanRequest request) {
        validateRequestCoBan(request);

        SinhVienLopHocPhan entity = findEntity(id);

        if (!entity.getSinhVienId().equals(request.getSinhVienId())
                || !entity.getLopHocPhanId().equals(request.getLopHocPhanId())) {
            throw new IllegalArgumentException("Không được đổi sinh viên hoặc lớp học phần tại bản ghi đã tạo. Hãy xóa rồi thêm lại");
        }

        mapper.updateEntity(entity, request);

        if (entity.getNgayDangKy() == null) {
            entity.setNgayDangKy(LocalDateTime.now());
        }

        if (entity.getTrangThai() == null) {
            entity.setTrangThai(TrangThaiSinhVienLopHocPhan.da_dang_ky);
        }

        if (entity.getLaHocLai() == null) {
            entity.setLaHocLai(false);
        }

        SinhVienLopHocPhan saved = repository.save(entity);
        capNhatSiSoLopHocPhan(saved.getLopHocPhanId());

        return mapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        SinhVienLopHocPhan entity = findEntity(id);
        Long lopHocPhanId = entity.getLopHocPhanId();

        repository.delete(entity);
        capNhatSiSoLopHocPhan(lopHocPhanId);
    }

    private SinhVienLopHocPhan findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GiangDayNotFoundException("Sinh viên lớp học phần không tồn tại với id = " + id));
    }

    private void validateRequestCoBan(SinhVienLopHocPhanRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Dữ liệu phân lớp học phần không hợp lệ");
        }

        if (request.getSinhVienId() == null) {
            throw new IllegalArgumentException("Sinh viên không được để trống");
        }

        if (request.getLopHocPhanId() == null) {
            throw new IllegalArgumentException("Lớp học phần không được để trống");
        }
    }
    private Set<Long> layVersionDuocHocLop(LopHocPhan lopHocPhan, Long chuongTrinhVersionId) {
        if (lopHocPhan.getLoaiLopHocPhan() == null) {
            throw new GiangDayException("Lớp học phần chưa có loại lớp");
        }

        if (lopHocPhan.getLoaiLopHocPhan() == LoaiLopHocPhan.CHUYEN_NGANH) {
            if (lopHocPhan.getChuongTrinhMonId() == null) {
                throw new GiangDayException("Lớp chuyên ngành chưa gắn chương trình môn");
            }

            ChuongTrinhMon chuongTrinhMon = chuongTrinhMonRepository.findById(lopHocPhan.getChuongTrinhMonId())
                    .orElseThrow(() -> new GiangDayException("Chương trình môn của lớp học phần không tồn tại"));

            if (chuongTrinhVersionId != null && !chuongTrinhVersionId.equals(chuongTrinhMon.getChuongTrinhVersionId())) {
                throw new GiangDayException("Lớp học phần không thuộc version chương trình đang chọn");
            }

            return java.util.Set.of(chuongTrinhMon.getChuongTrinhVersionId());
        }

        if (lopHocPhan.getLoaiLopHocPhan() == LoaiLopHocPhan.HOC_CHUNG) {
            List<LopHocPhanChuongTrinhMon> danhSachCtm = lopHocPhanChuongTrinhMonRepository
                    .findByLopHocPhanId(lopHocPhan.getId());

            if (danhSachCtm.isEmpty()) {
                throw new GiangDayException("Lớp học chung chưa gắn chương trình môn");
            }

            Set<Long> versionIds = new LinkedHashSet<>();

            for (LopHocPhanChuongTrinhMon item : danhSachCtm) {
                ChuongTrinhMon chuongTrinhMon = chuongTrinhMonRepository.findById(item.getChuongTrinhMonId())
                        .orElseThrow(() -> new GiangDayException("Chương trình môn được gắn với lớp học chung không tồn tại"));

                if (!chuongTrinhMon.getMonHocId().equals(lopHocPhan.getMonHocId())) {
                    throw new GiangDayException("Chương trình môn gắn với lớp học chung không cùng môn học");
                }

                if (chuongTrinhVersionId == null || chuongTrinhVersionId.equals(chuongTrinhMon.getChuongTrinhVersionId())) {
                    versionIds.add(chuongTrinhMon.getChuongTrinhVersionId());
                }
            }

            if (versionIds.isEmpty()) {
                throw new GiangDayException("Không có version chương trình phù hợp với lớp học chung này");
            }

            return versionIds;
        }

        throw new GiangDayException("Loại lớp học phần không hợp lệ");
    }
    private void capNhatSiSoLopHocPhan(Long lopHocPhanId) {
        if (lopHocPhanId == null) {
            return;
        }

        LopHocPhan lopHocPhan = lopHocPhanRepository.findById(lopHocPhanId).orElse(null);

        if (lopHocPhan == null) {
            return;
        }

        long soLuongHienTai = repository.countByLopHocPhanIdAndTrangThaiIn(
                lopHocPhanId,
                validator.trangThaiDangTinhSiSo()
        );

        lopHocPhan.setSoLuongHienTai((int) soLuongHienTai);
        lopHocPhanRepository.save(lopHocPhan);
    }
}