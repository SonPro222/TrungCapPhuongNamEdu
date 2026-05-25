package org.example.trungcapphuongnam.module.diem.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.diem.enums.HinhThucBaiKiemTra;
import org.example.trungcapphuongnam.module.diem.enums.LoaiBaiKiemTra;
import org.example.trungcapphuongnam.module.diem.enums.TrangThaiBaiKiemTra;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.DuplicateResourceException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.util.TextUtil;
import org.example.trungcapphuongnam.module.diem.dto.request.BaiKiemTraRequest;
import org.example.trungcapphuongnam.module.diem.dto.response.BaiKiemTraResponse;
import org.example.trungcapphuongnam.module.diem.entity.BaiKiemTra;
import org.example.trungcapphuongnam.module.diem.mapper.BaiKiemTraMapper;
import org.example.trungcapphuongnam.module.diem.repository.BaiKiemTraRepository;
import org.example.trungcapphuongnam.module.diem.service.BaiKiemTraService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class BaiKiemTraServiceImpl implements BaiKiemTraService {

    private final BaiKiemTraRepository repository;
    private final BaiKiemTraMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<BaiKiemTraResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public BaiKiemTraResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Override
    public BaiKiemTraResponse create(BaiKiemTraRequest request) {
        validate(request);
        if (false) {
            throw new DuplicateResourceException("");
        }
        BaiKiemTra entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public BaiKiemTraResponse update(Long id, BaiKiemTraRequest request) {
        BaiKiemTra entity = getEntity(id);
        validate(request);
        if (false) {
            throw new DuplicateResourceException("");
        }
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("BaiKiemTra không tồn tại: " + id);
        }
        repository.deleteById(id);
    }

    private BaiKiemTra getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BaiKiemTra không tồn tại: " + id));
    }


    private void validate(BaiKiemTraRequest request) {
        request.setTieuDe(TextUtil.trimRequired(request.getTieuDe()));
        request.setLoaiBai(TextUtil.trimRequired(request.getLoaiBai()));

        if (!isValidEnum(LoaiBaiKiemTra.class, request.getLoaiBai())) {
            throw new BadRequestException("Loại bài không hợp lệ: " + request.getLoaiBai());
        }
        if (request.getHinhThuc() != null && !request.getHinhThuc().isBlank()
                && !isValidEnum(HinhThucBaiKiemTra.class, request.getHinhThuc())) {
            throw new BadRequestException("Hình thức bài không hợp lệ: " + request.getHinhThuc());
        }
        if (request.getTrangThai() != null && !request.getTrangThai().isBlank()
                && !isValidEnum(TrangThaiBaiKiemTra.class, request.getTrangThai())) {
            throw new BadRequestException("Trạng thái bài kiểm tra không hợp lệ: " + request.getTrangThai());
        }
        if (request.getThoiGianBatDau() != null && request.getThoiGianKetThuc() != null
                && request.getThoiGianKetThuc().isBefore(request.getThoiGianBatDau())) {
            throw new BadRequestException("Thời gian kết thúc phải lớn hơn hoặc bằng thời gian bắt đầu");
        }
    }


    private <E extends Enum<E>> boolean isValidEnum(Class<E> enumClass, String value) {
        if (value == null) return false;
        for (E e : enumClass.getEnumConstants()) {
            if (e.name().equals(value)) return true;
        }
        return false;
    }
}
