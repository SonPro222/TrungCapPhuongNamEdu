package org.example.trungcapphuongnam.module.daoTao.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.DuplicateResourceException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.common.util.TextUtil;
import org.example.trungcapphuongnam.module.daoTao.dto.NganhHeDaoTaoRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.NganhHeDaoTaoResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.LoaiChuongTrinh;
import org.example.trungcapphuongnam.module.daoTao.entity.Nganh;
import org.example.trungcapphuongnam.module.daoTao.entity.NganhHeDaoTao;
import org.example.trungcapphuongnam.module.daoTao.entity.TrinhDoDaoTao;
import org.example.trungcapphuongnam.module.daoTao.mapper.NganhHeDaoTaoMapper;
import org.example.trungcapphuongnam.module.daoTao.repository.LoaiChuongTrinhRepository;
import org.example.trungcapphuongnam.module.daoTao.repository.NganhHeDaoTaoRepository;
import org.example.trungcapphuongnam.module.daoTao.repository.NganhRepository;
import org.example.trungcapphuongnam.module.daoTao.repository.TrinhDoDaoTaoRepository;
import org.example.trungcapphuongnam.module.daoTao.service.NganhHeDaoTaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NganhHeDaoTaoServiceImpl implements NganhHeDaoTaoService {

    private final NganhHeDaoTaoRepository repository;
    private final NganhRepository nganhRepository;
    private final TrinhDoDaoTaoRepository trinhDoDaoTaoRepository;
    private final LoaiChuongTrinhRepository loaiChuongTrinhRepository;
    private final NganhHeDaoTaoMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<NganhHeDaoTaoResponse> findAll(Long nganhId, Long trinhDoId, Long loaiChuongTrinhId, String trangThai, Pageable pageable) {
        return repository.findAll(
                LocJpa.<NganhHeDaoTao>empty()
                        .and(LocJpa.eq("nganhId", nganhId))
                        .and(LocJpa.eq("trinhDoId", trinhDoId))
                        .and(LocJpa.eq("loaiChuongTrinhId", loaiChuongTrinhId))
                        .and(LocJpa.eq("trangThai", TextUtil.trimToNull(trangThai))),
                pageable
        ).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NganhHeDaoTaoResponse> findOptions(Long nganhId) {
        return repository.findAll(
                LocJpa.<NganhHeDaoTao>empty()
                        .and(LocJpa.eq("nganhId", nganhId))
                        .and(LocJpa.eq("trangThai", "dang_su_dung"))
        ).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public NganhHeDaoTaoResponse findById(Long id) {
        return toResponse(getEntity(id));
    }

    @Override
    public NganhHeDaoTaoResponse create(NganhHeDaoTaoRequest request) {
        validate(request, null);
        // Auto-generate maHe / tenHe nếu để trống
        if (TextUtil.trimToNull(request.getMaHe()) == null || TextUtil.trimToNull(request.getTenHe()) == null) {
            Nganh nganh = nganhRepository.findById(request.getNganhId()).orElseThrow(
                    () -> new ResourceNotFoundException("Ngành không tồn tại: " + request.getNganhId()));
            TrinhDoDaoTao td = trinhDoDaoTaoRepository.findById(request.getTrinhDoId()).orElseThrow(
                    () -> new ResourceNotFoundException("Trình độ không tồn tại: " + request.getTrinhDoId()));
            LoaiChuongTrinh loai = loaiChuongTrinhRepository.findById(request.getLoaiChuongTrinhId()).orElseThrow(
                    () -> new ResourceNotFoundException("Loại chương trình không tồn tại: " + request.getLoaiChuongTrinhId()));
            if (TextUtil.trimToNull(request.getMaHe()) == null)
                request.setMaHe(nganh.getMaNganh() + "_" + td.getMaTrinhDo() + "_" + loai.getMaLoai());
            if (TextUtil.trimToNull(request.getTenHe()) == null)
                request.setTenHe(nganh.getTenNganh() + " - " + td.getTenTrinhDo() + " - " + loai.getTenLoai());
        }
        NganhHeDaoTao entity = mapper.toEntity(request);
        entity.setTrangThai(chuanHoaTrangThai(entity.getTrangThai()));
        return toResponse(repository.save(entity));
    }

    @Override
    public NganhHeDaoTaoResponse update(Long id, NganhHeDaoTaoRequest request) {
        NganhHeDaoTao entity = getEntity(id);
        validate(request, id);
        mapper.updateEntity(entity, request);
        entity.setTrangThai(chuanHoaTrangThai(entity.getTrangThai()));
        return toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Ngành hệ đào tạo không tồn tại: " + id);
        }
        repository.deleteById(id);
    }

    // ---- helpers ----

    private NganhHeDaoTao getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ngành hệ đào tạo không tồn tại: " + id));
    }

    private NganhHeDaoTaoResponse toResponse(NganhHeDaoTao entity) {
        Nganh nganh = nganhRepository.findById(entity.getNganhId()).orElse(null);
        TrinhDoDaoTao trinhDo = trinhDoDaoTaoRepository.findById(entity.getTrinhDoId()).orElse(null);
        LoaiChuongTrinh loai = loaiChuongTrinhRepository.findById(entity.getLoaiChuongTrinhId()).orElse(null);
        return mapper.toResponse(entity, nganh, trinhDo, loai);
    }

    private void validate(NganhHeDaoTaoRequest request, Long id) {
        if (request == null) throw new BadRequestException("Dữ liệu ngành hệ đào tạo không hợp lệ");
        if (request.getNganhId() == null) throw new BadRequestException("Ngành không được để trống");
        if (request.getTrinhDoId() == null) throw new BadRequestException("Trình độ không được để trống");
        if (request.getLoaiChuongTrinhId() == null) throw new BadRequestException("Loại chương trình không được để trống");

        if (!nganhRepository.existsById(request.getNganhId()))
            throw new ResourceNotFoundException("Ngành không tồn tại: " + request.getNganhId());
        if (!trinhDoDaoTaoRepository.existsById(request.getTrinhDoId()))
            throw new ResourceNotFoundException("Trình độ đào tạo không tồn tại: " + request.getTrinhDoId());
        if (!loaiChuongTrinhRepository.existsById(request.getLoaiChuongTrinhId()))
            throw new ResourceNotFoundException("Loại chương trình không tồn tại: " + request.getLoaiChuongTrinhId());

        boolean duplicate = (id == null)
                ? repository.existsByNganhIdAndTrinhDoIdAndLoaiChuongTrinhId(request.getNganhId(), request.getTrinhDoId(), request.getLoaiChuongTrinhId())
                : repository.existsByNganhIdAndTrinhDoIdAndLoaiChuongTrinhIdAndIdNot(request.getNganhId(), request.getTrinhDoId(), request.getLoaiChuongTrinhId(), id);
        if (duplicate) throw new DuplicateResourceException("Tổ hợp ngành - trình độ - loại chương trình này đã tồn tại");
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
