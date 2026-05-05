package org.example.trungcapphuongnam.module.daoTao.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.DuplicateResourceException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.util.TextUtil;
import org.example.trungcapphuongnam.module.daoTao.dto.TrinhDoDaoTaoRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.TrinhDoDaoTaoResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.TrinhDoDaoTao;
import org.example.trungcapphuongnam.module.daoTao.mapper.TrinhDoDaoTaoMapper;
import org.example.trungcapphuongnam.module.daoTao.repository.TrinhDoDaoTaoRepository;
import org.example.trungcapphuongnam.module.daoTao.service.TrinhDoDaoTaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TrinhDoDaoTaoServiceImpl implements TrinhDoDaoTaoService {

    private final TrinhDoDaoTaoRepository repository;
    private final TrinhDoDaoTaoMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<TrinhDoDaoTaoResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public TrinhDoDaoTaoResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Override
    public TrinhDoDaoTaoResponse create(TrinhDoDaoTaoRequest request) {
        request.setMaTrinhDo(TextUtil.trimRequired(request.getMaTrinhDo()));
        request.setTenTrinhDo(TextUtil.trimRequired(request.getTenTrinhDo()));
        if (repository.existsByMaTrinhDo(request.getMaTrinhDo())) {
            throw new DuplicateResourceException("Mã trình độ đã tồn tại: " + request.getMaTrinhDo());
        }
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    public TrinhDoDaoTaoResponse update(Long id, TrinhDoDaoTaoRequest request) {
        TrinhDoDaoTao entity = getEntity(id);
        request.setMaTrinhDo(TextUtil.trimRequired(request.getMaTrinhDo()));
        request.setTenTrinhDo(TextUtil.trimRequired(request.getTenTrinhDo()));
        if (repository.existsByMaTrinhDoAndIdNot(request.getMaTrinhDo(), id)) {
            throw new DuplicateResourceException("Mã trình độ đã tồn tại: " + request.getMaTrinhDo());
        }
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Trình độ đào tạo không tồn tại: " + id);
        repository.deleteById(id);
    }

    private TrinhDoDaoTao getEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Trình độ đào tạo không tồn tại: " + id));
    }
}
