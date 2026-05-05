package org.example.trungcapphuongnam.module.daoTao.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.DuplicateResourceException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.util.TextUtil;
import org.example.trungcapphuongnam.module.daoTao.dto.LoaiChuongTrinhRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.LoaiChuongTrinhResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.LoaiChuongTrinh;
import org.example.trungcapphuongnam.module.daoTao.mapper.LoaiChuongTrinhMapper;
import org.example.trungcapphuongnam.module.daoTao.repository.LoaiChuongTrinhRepository;
import org.example.trungcapphuongnam.module.daoTao.service.LoaiChuongTrinhService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LoaiChuongTrinhServiceImpl implements LoaiChuongTrinhService {

    private final LoaiChuongTrinhRepository repository;
    private final LoaiChuongTrinhMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<LoaiChuongTrinhResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public LoaiChuongTrinhResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Override
    public LoaiChuongTrinhResponse create(LoaiChuongTrinhRequest request) {
        validate(request, null);
        if (repository.existsByMaLoai(request.getMaLoai())) {
            throw new DuplicateResourceException("Mã loại chương trình đã tồn tại: " + request.getMaLoai());
        }
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    public LoaiChuongTrinhResponse update(Long id, LoaiChuongTrinhRequest request) {
        LoaiChuongTrinh entity = getEntity(id);
        validate(request, id);
        if (repository.existsByMaLoaiAndIdNot(request.getMaLoai(), id)) {
            throw new DuplicateResourceException("Mã loại chương trình đã tồn tại: " + request.getMaLoai());
        }
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Loại chương trình không tồn tại: " + id);
        repository.deleteById(id);
    }

    private void validate(LoaiChuongTrinhRequest request, Long id) {
        request.setMaLoai(TextUtil.trimRequired(request.getMaLoai()));
        request.setTenLoai(TextUtil.trimRequired(request.getTenLoai()));
        if (request.getSoThang() == null || request.getSoThang() <= 0) throw new BadRequestException("Số tháng phải lớn hơn 0");
        if (request.getSoKy() == null || request.getSoKy() <= 0) throw new BadRequestException("Số kỳ phải lớn hơn 0");
    }

    private LoaiChuongTrinh getEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Loại chương trình không tồn tại: " + id));
    }
}
