package org.example.trungcapphuongnam.module.daoTao.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.DuplicateResourceException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.common.util.TextUtil;
import org.example.trungcapphuongnam.module.daoTao.dto.NganhLoaiChuongTrinhRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.NganhLoaiChuongTrinhResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.NganhLoaiChuongTrinh;
import org.example.trungcapphuongnam.module.daoTao.mapper.NganhLoaiChuongTrinhMapper;
import org.example.trungcapphuongnam.module.daoTao.repository.LoaiChuongTrinhRepository;
import org.example.trungcapphuongnam.module.daoTao.repository.NganhLoaiChuongTrinhRepository;
import org.example.trungcapphuongnam.module.daoTao.repository.NganhRepository;
import org.example.trungcapphuongnam.module.daoTao.service.NganhLoaiChuongTrinhService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NganhLoaiChuongTrinhServiceImpl implements NganhLoaiChuongTrinhService {

    private final NganhLoaiChuongTrinhRepository repository;
    private final NganhRepository nganhRepository;
    private final LoaiChuongTrinhRepository loaiChuongTrinhRepository;
    private final NganhLoaiChuongTrinhMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<NganhLoaiChuongTrinhResponse> findAll(Long nganhId, Long loaiChuongTrinhId, String trangThai, Pageable pageable) {
        return repository.findAll(
                LocJpa.<NganhLoaiChuongTrinh>empty()
                        .and(LocJpa.eq("nganhId", nganhId))
                        .and(LocJpa.eq("loaiChuongTrinhId", loaiChuongTrinhId))
                        .and(LocJpa.eq("trangThai", TextUtil.trimToNull(trangThai))),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public NganhLoaiChuongTrinhResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Override
    public NganhLoaiChuongTrinhResponse create(NganhLoaiChuongTrinhRequest request) {
        validate(request, null);
        NganhLoaiChuongTrinh entity = mapper.toEntity(request);
        entity.setTrangThai(chuanHoaTrangThai(entity.getTrangThai()));
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public NganhLoaiChuongTrinhResponse update(Long id, NganhLoaiChuongTrinhRequest request) {
        NganhLoaiChuongTrinh entity = getEntity(id);
        validate(request, id);
        mapper.updateEntity(entity, request);
        entity.setTrangThai(chuanHoaTrangThai(entity.getTrangThai()));
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public NganhLoaiChuongTrinhResponse luu(NganhLoaiChuongTrinhRequest request) {
        validateKhoa(request);
        NganhLoaiChuongTrinh entity = repository.findByNganhIdAndLoaiChuongTrinhId(request.getNganhId(), request.getLoaiChuongTrinhId())
                .orElseGet(() -> NganhLoaiChuongTrinh.builder()
                        .nganhId(request.getNganhId())
                        .loaiChuongTrinhId(request.getLoaiChuongTrinhId())
                        .build());
        entity.setTrangThai("dang_su_dung");
        entity.setGhiChu(TextUtil.trimToNull(request.getGhiChu()));
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Loại chương trình theo ngành không tồn tại: " + id);
        }

        repository.deleteById(id);
    }

    private NganhLoaiChuongTrinh getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loại chương trình đã gán vào ngành không tồn tại: " + id));
    }

    private void validate(NganhLoaiChuongTrinhRequest request, Long id) {
        validateKhoa(request);
        if (id == null && repository.existsByNganhIdAndLoaiChuongTrinhId(request.getNganhId(), request.getLoaiChuongTrinhId())) {
            throw new DuplicateResourceException("Loại chương trình này đã được gán vào ngành");
        }
        if (id != null && repository.existsByNganhIdAndLoaiChuongTrinhIdAndIdNot(request.getNganhId(), request.getLoaiChuongTrinhId(), id)) {
            throw new DuplicateResourceException("Loại chương trình này đã được gán vào ngành");
        }
        chuanHoaTrangThai(request.getTrangThai());
    }

    private void validateKhoa(NganhLoaiChuongTrinhRequest request) {
        if (request == null) throw new BadRequestException("Dữ liệu gán loại chương trình vào ngành không hợp lệ");
        if (request.getNganhId() == null) throw new BadRequestException("Ngành không được để trống");
        if (request.getLoaiChuongTrinhId() == null) throw new BadRequestException("Loại chương trình không được để trống");
        if (!nganhRepository.existsById(request.getNganhId())) throw new ResourceNotFoundException("Ngành không tồn tại: " + request.getNganhId());
        if (!loaiChuongTrinhRepository.existsById(request.getLoaiChuongTrinhId())) throw new ResourceNotFoundException("Loại chương trình không tồn tại: " + request.getLoaiChuongTrinhId());
    }

    private String chuanHoaTrangThai(String trangThai) {
        String value = TextUtil.trimToNull(trangThai);
        if (value == null) return "dang_su_dung";
        if (!value.equals("dang_su_dung") && !value.equals("ngung_su_dung")) {
            throw new BadRequestException("Trạng thái không hợp lệ: " + value);
        }
        return value;
    }
}
