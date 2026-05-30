package org.example.trungcapphuongnam.module.daoTao.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.DuplicateResourceException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.common.util.TextUtil;
import org.example.trungcapphuongnam.module.daoTao.dto.NganhTrinhDoDaoTaoRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.NganhTrinhDoDaoTaoResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.NganhTrinhDoDaoTao;
import org.example.trungcapphuongnam.module.daoTao.mapper.NganhTrinhDoDaoTaoMapper;
import org.example.trungcapphuongnam.module.daoTao.repository.NganhRepository;
import org.example.trungcapphuongnam.module.daoTao.repository.NganhTrinhDoDaoTaoRepository;
import org.example.trungcapphuongnam.module.daoTao.repository.TrinhDoDaoTaoRepository;
import org.example.trungcapphuongnam.module.daoTao.service.NganhTrinhDoDaoTaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NganhTrinhDoDaoTaoServiceImpl implements NganhTrinhDoDaoTaoService {

    private final NganhTrinhDoDaoTaoRepository repository;
    private final NganhRepository nganhRepository;
    private final TrinhDoDaoTaoRepository trinhDoDaoTaoRepository;
    private final NganhTrinhDoDaoTaoMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<NganhTrinhDoDaoTaoResponse> findAll(Long nganhId, Long trinhDoId, String trangThai, Pageable pageable) {
        return repository.findAll(
                LocJpa.<NganhTrinhDoDaoTao>empty()
                        .and(LocJpa.eq("nganhId", nganhId))
                        .and(LocJpa.eq("trinhDoId", trinhDoId))
                        .and(LocJpa.eq("trangThai", TextUtil.trimToNull(trangThai))),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public NganhTrinhDoDaoTaoResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Override
    public NganhTrinhDoDaoTaoResponse create(NganhTrinhDoDaoTaoRequest request) {
        validate(request, null);
        NganhTrinhDoDaoTao entity = mapper.toEntity(request);
        entity.setTrangThai(chuanHoaTrangThai(entity.getTrangThai()));
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public NganhTrinhDoDaoTaoResponse update(Long id, NganhTrinhDoDaoTaoRequest request) {
        NganhTrinhDoDaoTao entity = getEntity(id);
        validate(request, id);
        mapper.updateEntity(entity, request);
        entity.setTrangThai(chuanHoaTrangThai(entity.getTrangThai()));
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public NganhTrinhDoDaoTaoResponse luu(NganhTrinhDoDaoTaoRequest request) {
        validateKhoa(request);
        NganhTrinhDoDaoTao entity = repository.findByNganhIdAndTrinhDoId(request.getNganhId(), request.getTrinhDoId())
                .orElseGet(() -> NganhTrinhDoDaoTao.builder()
                        .nganhId(request.getNganhId())
                        .trinhDoId(request.getTrinhDoId())
                        .build());
        entity.setTrangThai("dang_su_dung");
        entity.setGhiChu(TextUtil.trimToNull(request.getGhiChu()));
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Trình độ đào tạo theo ngành không tồn tại: " + id);
        }

        repository.deleteById(id);
    }

    private NganhTrinhDoDaoTao getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trình độ đào tạo đã gán vào ngành không tồn tại: " + id));
    }

    private void validate(NganhTrinhDoDaoTaoRequest request, Long id) {
        validateKhoa(request);
        if (id == null && repository.existsByNganhIdAndTrinhDoId(request.getNganhId(), request.getTrinhDoId())) {
            throw new DuplicateResourceException("Trình độ đào tạo này đã được gán vào ngành");
        }
        if (id != null && repository.existsByNganhIdAndTrinhDoIdAndIdNot(request.getNganhId(), request.getTrinhDoId(), id)) {
            throw new DuplicateResourceException("Trình độ đào tạo này đã được gán vào ngành");
        }
        chuanHoaTrangThai(request.getTrangThai());
    }

    private void validateKhoa(NganhTrinhDoDaoTaoRequest request) {
        if (request == null) throw new BadRequestException("Dữ liệu gán trình độ đào tạo vào ngành không hợp lệ");
        if (request.getNganhId() == null) throw new BadRequestException("Ngành không được để trống");
        if (request.getTrinhDoId() == null) throw new BadRequestException("Trình độ đào tạo không được để trống");
        if (!nganhRepository.existsById(request.getNganhId())) throw new ResourceNotFoundException("Ngành không tồn tại: " + request.getNganhId());
        if (!trinhDoDaoTaoRepository.existsById(request.getTrinhDoId())) throw new ResourceNotFoundException("Trình độ đào tạo không tồn tại: " + request.getTrinhDoId());
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
