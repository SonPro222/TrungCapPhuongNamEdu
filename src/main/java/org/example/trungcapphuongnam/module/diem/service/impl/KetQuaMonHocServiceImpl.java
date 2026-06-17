package org.example.trungcapphuongnam.module.diem.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.diem.enums.KetQuaHocTap;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.DuplicateResourceException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.diem.dto.request.KetQuaMonHocRequest;
import org.example.trungcapphuongnam.module.diem.dto.response.KetQuaMonHocResponse;
import org.example.trungcapphuongnam.module.diem.entity.KetQuaMonHoc;
import org.example.trungcapphuongnam.module.diem.mapper.KetQuaMonHocMapper;
import org.example.trungcapphuongnam.module.diem.repository.KetQuaMonHocRepository;
import org.example.trungcapphuongnam.module.diem.service.KetQuaMonHocService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
@Transactional
public class KetQuaMonHocServiceImpl implements KetQuaMonHocService {

    private final KetQuaMonHocRepository repository;
    private final KetQuaMonHocMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<KetQuaMonHocResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<KetQuaMonHocResponse> findTheoBoLoc(
            Long sinhVienId,
            Long chuongTrinhMonId,
            Long lopHocPhanId,
            Pageable pageable
    ) {
        if (sinhVienId != null && chuongTrinhMonId != null) {
            return repository.findBySinhVienIdAndChuongTrinhMonId(sinhVienId, chuongTrinhMonId, pageable)
                    .map(mapper::toResponse);
        }

        if (sinhVienId != null) {
            return repository.findBySinhVienId(sinhVienId, pageable)
                    .map(mapper::toResponse);
        }

        if (chuongTrinhMonId != null) {
            return repository.findByChuongTrinhMonId(chuongTrinhMonId, pageable)
                    .map(mapper::toResponse);
        }

        if (lopHocPhanId != null) {
            return repository.findByLopHocPhanId(lopHocPhanId, pageable)
                    .map(mapper::toResponse);
        }

        return findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public KetQuaMonHocResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Override
    public KetQuaMonHocResponse create(KetQuaMonHocRequest request) {
        validate(request);
        if (repository.existsBySinhVienIdAndChuongTrinhMonIdAndSoLanHoc(request.getSinhVienId(), request.getChuongTrinhMonId(), request.getSoLanHoc() == null ? 1 : request.getSoLanHoc())) {
            throw new DuplicateResourceException("Sinh viên đã có kết quả môn học ở lần học này");
        }
        KetQuaMonHoc entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public KetQuaMonHocResponse update(Long id, KetQuaMonHocRequest request) {
        KetQuaMonHoc entity = getEntity(id);
        validate(request);
        if (repository.existsBySinhVienIdAndChuongTrinhMonIdAndSoLanHocAndIdNot(request.getSinhVienId(), request.getChuongTrinhMonId(), request.getSoLanHoc() == null ? 1 : request.getSoLanHoc(), id)) {
            throw new DuplicateResourceException("Sinh viên đã có kết quả môn học ở lần học này");
        }
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("KetQuaMonHoc không tồn tại: " + id);
        }
        repository.deleteById(id);
    }

    private KetQuaMonHoc getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KetQuaMonHoc không tồn tại: " + id));
    }


    private void validate(KetQuaMonHocRequest request) {
        validateDiem(request.getDiemTongKet(), "Điểm tổng kết");
        validateDiem(request.getDiemPhanTram(), "Điểm phần trăm");
        validateDiem(request.getDiemQuyDoi(), "Điểm quy đổi");

        if (request.getSoLanHoc() != null && request.getSoLanHoc() <= 0) {
            throw new BadRequestException("Số lần học phải lớn hơn 0");
        }
        if (request.getKetQua() != null && !request.getKetQua().isBlank()
                && !isValidEnum(KetQuaHocTap.class, request.getKetQua())) {
            throw new BadRequestException("Kết quả môn học không hợp lệ: " + request.getKetQua());
        }
    }

    private void validateDiem(BigDecimal diem, String label) {
        if (diem != null && diem.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException(label + " không được âm");
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
