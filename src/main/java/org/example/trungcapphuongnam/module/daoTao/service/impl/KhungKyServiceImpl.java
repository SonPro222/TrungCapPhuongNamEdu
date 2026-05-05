package org.example.trungcapphuongnam.module.daoTao.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.DuplicateResourceException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.util.TextUtil;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.KhungKy;
import org.example.trungcapphuongnam.module.daoTao.mapper.KhungKyMapper;
import org.example.trungcapphuongnam.module.daoTao.repository.KhungKyRepository;
import org.example.trungcapphuongnam.module.daoTao.repository.LoaiChuongTrinhRepository;
import org.example.trungcapphuongnam.module.daoTao.service.KhungKyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class KhungKyServiceImpl implements KhungKyService {

    private final KhungKyRepository repository;
    private final LoaiChuongTrinhRepository loaiChuongTrinhRepository;
    private final KhungKyMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<KhungKyResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public KhungKyResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Override
    public KhungKyResponse create(KhungKyRequest request) {
        validate(request, null);
        if (repository.existsByLoaiChuongTrinhIdAndMaKy(request.getLoaiChuongTrinhId(), request.getMaKy())) {
            throw new DuplicateResourceException("Mã kỳ đã tồn tại trong loại chương trình này");
        }
        if (repository.existsByLoaiChuongTrinhIdAndThuTu(request.getLoaiChuongTrinhId(), request.getThuTu())) {
            throw new DuplicateResourceException("Thứ tự kỳ đã tồn tại trong loại chương trình này");
        }
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    public KhungKyResponse update(Long id, KhungKyRequest request) {
        KhungKy entity = getEntity(id);
        validate(request, id);
        if (repository.existsByLoaiChuongTrinhIdAndMaKyAndIdNot(request.getLoaiChuongTrinhId(), request.getMaKy(), id)) {
            throw new DuplicateResourceException("Mã kỳ đã tồn tại trong loại chương trình này");
        }
        if (repository.existsByLoaiChuongTrinhIdAndThuTuAndIdNot(request.getLoaiChuongTrinhId(), request.getThuTu(), id)) {
            throw new DuplicateResourceException("Thứ tự kỳ đã tồn tại trong loại chương trình này");
        }
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Khung kỳ không tồn tại: " + id);
        repository.deleteById(id);
    }

    private void validate(KhungKyRequest request, Long id) {
        request.setMaKy(TextUtil.trimRequired(request.getMaKy()));
        request.setTenKy(TextUtil.trimRequired(request.getTenKy()));
        if (request.getLoaiChuongTrinhId() == null) throw new BadRequestException("loaiChuongTrinhId không được để trống");
        if (!loaiChuongTrinhRepository.existsById(request.getLoaiChuongTrinhId())) {
            throw new ResourceNotFoundException("Loại chương trình không tồn tại: " + request.getLoaiChuongTrinhId());
        }
        if (request.getThuTu() == null || request.getThuTu() <= 0) throw new BadRequestException("Thứ tự kỳ phải lớn hơn 0");
    }

    private KhungKy getEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Khung kỳ không tồn tại: " + id));
    }
}
