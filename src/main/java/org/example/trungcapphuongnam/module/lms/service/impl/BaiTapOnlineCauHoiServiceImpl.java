package org.example.trungcapphuongnam.module.lms.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.lms.dto.request.BaiTapOnlineCauHoiRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.BaiTapOnlineCauHoiResponse;
import org.example.trungcapphuongnam.module.lms.entity.BaiTapOnlineCauHoi;
import org.example.trungcapphuongnam.module.lms.mapper.BaiTapOnlineCauHoiMapper;
import org.example.trungcapphuongnam.module.lms.repository.BaiTapOnlineCauHoiRepository;
import org.example.trungcapphuongnam.module.lms.service.BaiTapOnlineCauHoiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BaiTapOnlineCauHoiServiceImpl implements BaiTapOnlineCauHoiService {

    private final BaiTapOnlineCauHoiRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<BaiTapOnlineCauHoiResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(BaiTapOnlineCauHoiMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public BaiTapOnlineCauHoiResponse findById(Long id) {
        return BaiTapOnlineCauHoiMapper.toResponse(getEntity(id));
    }

    @Override
    public BaiTapOnlineCauHoiResponse create(BaiTapOnlineCauHoiRequest request) {
        BaiTapOnlineCauHoi entity = BaiTapOnlineCauHoiMapper.toEntity(request);
        return BaiTapOnlineCauHoiMapper.toResponse(repository.save(entity));
    }

    @Override
    public BaiTapOnlineCauHoiResponse update(Long id, BaiTapOnlineCauHoiRequest request) {
        BaiTapOnlineCauHoi entity = getEntity(id);
        BaiTapOnlineCauHoiMapper.updateEntity(entity, request);
        return BaiTapOnlineCauHoiMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        BaiTapOnlineCauHoi entity = getEntity(id);
        repository.delete(entity);
    }

    private BaiTapOnlineCauHoi getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy BaiTapOnlineCauHoi với id = " + id));
    }
}
