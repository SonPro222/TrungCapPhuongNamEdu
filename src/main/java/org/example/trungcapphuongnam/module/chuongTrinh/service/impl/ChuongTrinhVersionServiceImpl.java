package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuyenTrangThaiChuongTrinhVersionRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhVersionRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhVersionResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersion;
import org.example.trungcapphuongnam.module.chuongTrinh.enums.TrangThaiChuongTrinhVersion;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.ChuongTrinhVersionMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhVersionRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhVersionService;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.service.XoaChuongTrinhCascadeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.common.spec.LocJpa;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class ChuongTrinhVersionServiceImpl implements ChuongTrinhVersionService {
    private final XoaChuongTrinhCascadeService xoaChuongTrinhCascadeService;
    private final ChuongTrinhVersionRepository repository;
    private final ChuongTrinhVersionMapper mapper;
    private final ChuongTrinhNghiepVuValidator validator;

    @Override
    @Transactional(readOnly = true)
    public Page<ChuongTrinhVersionResponse> findAll(
            Long chuongTrinhId,
            Boolean laHienHanh,
            TrangThaiChuongTrinhVersion trangThai,
            String keyword,
            Pageable pageable
    ) {
        return repository.findAll(
                LocJpa.<ChuongTrinhVersion>empty()
                        .and(LocJpa.eq("chuongTrinhId", chuongTrinhId))
                        .and(LocJpa.eq("laHienHanh", laHienHanh))
                        .and(LocJpa.eq("trangThai", trangThai))
                        .and(LocJpa.keyword(keyword, "maVersion", "tenVersion", "soQuyetDinh", "nguoiKy", "coQuanBanHanh", "fileQuyetDinh")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ChuongTrinhVersionResponse findById(Long id) {
        ChuongTrinhVersion entity = getEntity(id);
        return mapper.toResponse(entity);
    }

    @Override
    public ChuongTrinhVersionResponse create(ChuongTrinhVersionRequest request) {
        validator.validateChuongTrinhVersion(request, null);

        ChuongTrinhVersion entity = mapper.toEntity(request);
        dongBoTrangThaiKhiLuu(entity);
        validateTrangThaiHienHanh(entity);

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public ChuongTrinhVersionResponse update(Long id, ChuongTrinhVersionRequest request) {
        validator.validateChuongTrinhVersion(request, id);

        ChuongTrinhVersion entity = getEntity(id);
        kiemTraDuocPhepChinhSua(entity);

        mapper.updateEntity(entity, request);
        dongBoTrangThaiKhiLuu(entity);
        validateTrangThaiHienHanh(entity);

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public ChuongTrinhVersionResponse chuyenTrangThai(Long id, ChuyenTrangThaiChuongTrinhVersionRequest request) {
        ChuongTrinhVersion entity = getEntity(id);
        TrangThaiChuongTrinhVersion trangThaiMoi = request.getTrangThai();

        if (trangThaiMoi == null) {
            throw new BadRequestException("Trạng thái version không được để trống.");
        }

        if (trangThaiMoi == TrangThaiChuongTrinhVersion.HIEN_HANH) {
            validateTrangThaiHienHanh(entity);
        }

        if (entity.getTrangThai() == TrangThaiChuongTrinhVersion.HUY
                && trangThaiMoi != TrangThaiChuongTrinhVersion.DANG_SOAN) {
            throw new BadRequestException("Version đã hủy. Chỉ có thể đưa về trạng thái DANG_SOAN nếu cần cấu hình lại.");
        }

        entity.setTrangThai(trangThaiMoi);
        entity.setLaHienHanh(trangThaiMoi == TrangThaiChuongTrinhVersion.HIEN_HANH);

        // Không tắt các version hiện hành khác cùng chương trình.
        // Một chương trình có thể có nhiều version hiện hành song song cho nhiều khóa/lớp khác nhau.

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        ChuongTrinhVersion entity = getEntity(id);
        if (entity.getTrangThai() == TrangThaiChuongTrinhVersion.HIEN_HANH) {
            throw new BadRequestException("Không được xóa version đang hiện hành. Hãy chuyển sang HET_HIEU_LUC, KHOA hoặc HUY nếu không dùng nữa.");
        }
        xoaChuongTrinhCascadeService.xoaTheoChuongTrinhVersionId(id);
    }

    public void kiemTraDuocPhepChinhSua(Long chuongTrinhVersionId) {
        kiemTraDuocPhepChinhSua(getEntity(chuongTrinhVersionId));
    }

    public void kiemTraDuocPhepVanHanh(Long chuongTrinhVersionId) {
        kiemTraDuocPhepVanHanh(getEntity(chuongTrinhVersionId));
    }

    private ChuongTrinhVersion getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ChuongTrinhVersion không tồn tại: " + id));
    }

    private void dongBoTrangThaiKhiLuu(ChuongTrinhVersion entity) {
        if (entity.getTrangThai() == null) {
            entity.setTrangThai(Boolean.TRUE.equals(entity.getLaHienHanh())
                    ? TrangThaiChuongTrinhVersion.HIEN_HANH
                    : TrangThaiChuongTrinhVersion.DANG_SOAN);
        }
        entity.setLaHienHanh(entity.getTrangThai() == TrangThaiChuongTrinhVersion.HIEN_HANH);
    }

    private void validateTrangThaiHienHanh(ChuongTrinhVersion entity) {
        if (entity.getTrangThai() != TrangThaiChuongTrinhVersion.HIEN_HANH
                && !Boolean.TRUE.equals(entity.getLaHienHanh())) {
            return;
        }

        if (entity.getNgayApDung() == null || entity.getNgayHetHieuLuc() == null) {
            throw new BadRequestException("Muốn chuyển version sang HIEN_HANH thì phải có ngày áp dụng và ngày hết hiệu lực.");
        }

        LocalDate today = LocalDate.now();
        if (today.isBefore(entity.getNgayApDung())) {
            throw new BadRequestException("Version chưa tới ngày áp dụng. Hãy để trạng thái CHO_AP_DUNG.");
        }

        if (today.isAfter(entity.getNgayHetHieuLuc())) {
            throw new BadRequestException("Version đã qua ngày hết hiệu lực. Không thể chuyển sang HIEN_HANH.");
        }
    }

    private void kiemTraDuocPhepChinhSua(ChuongTrinhVersion entity) {
        TrangThaiChuongTrinhVersion trangThai = entity.getTrangThai();
        if (trangThai == null) {
            trangThai = Boolean.TRUE.equals(entity.getLaHienHanh())
                    ? TrangThaiChuongTrinhVersion.HIEN_HANH
                    : TrangThaiChuongTrinhVersion.DANG_SOAN;
        }

        if (trangThai != TrangThaiChuongTrinhVersion.DANG_SOAN
                && trangThai != TrangThaiChuongTrinhVersion.CHO_AP_DUNG) {
            throw new BadRequestException("Chỉ version DANG_SOAN hoặc CHO_AP_DUNG mới được sửa cấu trúc.");
        }
    }

    private void kiemTraDuocPhepVanHanh(ChuongTrinhVersion entity) {
        if (entity.getTrangThai() != TrangThaiChuongTrinhVersion.HIEN_HANH) {
            throw new BadRequestException("Version chương trình không ở trạng thái HIEN_HANH nên không được vận hành chính thức.");
        }

        if (entity.getNgayApDung() == null || entity.getNgayHetHieuLuc() == null) {
            throw new BadRequestException("Version chương trình chưa có đủ ngày áp dụng/ngày hết hiệu lực.");
        }

        LocalDate today = LocalDate.now();
        if (today.isBefore(entity.getNgayApDung())) {
            throw new BadRequestException("Version chương trình chưa tới ngày áp dụng.");
        }

        if (today.isAfter(entity.getNgayHetHieuLuc())) {
            throw new BadRequestException("Version chương trình đã hết hiệu lực, không được vận hành mới.");
        }
    }
}
