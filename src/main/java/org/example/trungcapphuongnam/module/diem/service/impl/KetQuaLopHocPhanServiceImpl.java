package org.example.trungcapphuongnam.module.diem.service.impl;


import lombok.RequiredArgsConstructor;

import org.example.trungcapphuongnam.common.enums.KetQuaHocTap;
import org.example.trungcapphuongnam.common.enums.TrangThaiKetQuaLop;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.DuplicateResourceException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.diem.dto.request.KetQuaLopHocPhanRequest;
import org.example.trungcapphuongnam.module.diem.dto.response.KetQuaLopHocPhanResponse;
import org.example.trungcapphuongnam.module.diem.entity.KetQuaLopHocPhan;
import org.example.trungcapphuongnam.module.diem.mapper.KetQuaLopHocPhanMapper;
import org.example.trungcapphuongnam.module.diem.repository.KetQuaLopHocPhanRepository;
import org.example.trungcapphuongnam.module.diem.service.KetQuaLopHocPhanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
@Transactional
public class KetQuaLopHocPhanServiceImpl implements KetQuaLopHocPhanService {

    private final KetQuaLopHocPhanRepository repository;
    private final KetQuaLopHocPhanMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<KetQuaLopHocPhanResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public KetQuaLopHocPhanResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Override
    public KetQuaLopHocPhanResponse create(KetQuaLopHocPhanRequest request) {
        validate(request);
        if (repository.existsBySinhVienIdAndLopHocPhanId(request.getSinhVienId(), request.getLopHocPhanId())) {
            throw new DuplicateResourceException("Sinh viên đã có kết quả lớp học phần này");
        }
        KetQuaLopHocPhan entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public KetQuaLopHocPhanResponse update(Long id, KetQuaLopHocPhanRequest request) {
        KetQuaLopHocPhan entity = getEntity(id);
        validate(request);
        if (repository.existsBySinhVienIdAndLopHocPhanIdAndIdNot(request.getSinhVienId(), request.getLopHocPhanId(), id)) {
            throw new DuplicateResourceException("Sinh viên đã có kết quả lớp học phần này");
        }
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("KetQuaLopHocPhan không tồn tại: " + id);
        }
        repository.deleteById(id);
    }

    private KetQuaLopHocPhan getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KetQuaLopHocPhan không tồn tại: " + id));
    }


    private void validate(KetQuaLopHocPhanRequest request) {
        validateDiem(request.getDiemChuyenCan(), "Điểm chuyên cần");
        validateDiem(request.getDiemQuaTrinh(), "Điểm quá trình");
        validateDiem(request.getDiemThi(), "Điểm thi");
        validateDiem(request.getDiemTongKet(), "Điểm tổng kết");
        validateDiem(request.getDiemPhanTram(), "Điểm phần trăm");
        validateDiem(request.getDiemQuyDoi(), "Điểm quy đổi");

        if (request.getKetQua() != null && !request.getKetQua().isBlank()
                && !isValidEnum(KetQuaHocTap.class, request.getKetQua())) {
            throw new BadRequestException("Kết quả không hợp lệ: " + request.getKetQua());
        }
        if (request.getTrangThai() != null && !request.getTrangThai().isBlank()
                && !isValidEnum(TrangThaiKetQuaLop.class, request.getTrangThai())) {
            throw new BadRequestException("Trạng thái kết quả lớp không hợp lệ: " + request.getTrangThai());
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
