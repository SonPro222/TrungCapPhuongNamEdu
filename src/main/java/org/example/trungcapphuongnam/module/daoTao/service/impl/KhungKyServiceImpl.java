package org.example.trungcapphuongnam.module.daoTao.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.DuplicateResourceException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.util.TextUtil;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhVersionRepository;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.KhungKy;
import org.example.trungcapphuongnam.module.daoTao.mapper.KhungKyMapper;
import org.example.trungcapphuongnam.module.daoTao.repository.KhungKyRepository;
import org.example.trungcapphuongnam.module.daoTao.service.KhungKyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.module.chuongTrinh.service.XoaChuongTrinhCascadeService;
import org.example.trungcapphuongnam.module.daoTao.entity.KhungKyGoc;
import org.example.trungcapphuongnam.module.daoTao.repository.KhungKyGocRepository;
@Service
@RequiredArgsConstructor
@Transactional
public class KhungKyServiceImpl implements KhungKyService {
    private final ChuongTrinhVersionRepository chuongTrinhVersionRepository;
    private final KhungKyRepository repository;
    private final KhungKyMapper mapper;
    private final XoaChuongTrinhCascadeService xoaChuongTrinhCascadeService;
    private final KhungKyGocRepository khungKyGocRepository;
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

        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    public KhungKyResponse update(Long id, KhungKyRequest request) {
        KhungKy entity = getEntity(id);
        validate(request, id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Khung kỳ không tồn tại: " + id);
        }

        xoaChuongTrinhCascadeService.xoaTheoKhungKyId(id);
    }
    private void validate(KhungKyRequest request, Long id) {
        if (request == null) {
            throw new BadRequestException("Dữ liệu khung kỳ không hợp lệ");
        }

        request.setMaKy(TextUtil.trimRequired(request.getMaKy()));
        request.setTenKy(TextUtil.trimRequired(request.getTenKy()));

        if (request.getChuongTrinhVersionId() == null) {
            throw new BadRequestException(
                    "chuongTrinhVersionId không được để trống"
            );
        }

        if (!chuongTrinhVersionRepository.existsById(
                request.getChuongTrinhVersionId()
        )) {
            throw new ResourceNotFoundException(
                    "Version chương trình không tồn tại: "
                            + request.getChuongTrinhVersionId()
            );
        }

        if (request.getKhungKyGocId() != null) {
            KhungKyGoc khungKyGoc = khungKyGocRepository.findById(request.getKhungKyGocId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Khung kỳ gốc không tồn tại: " + request.getKhungKyGocId()
                    ));

            request.setMaKy(khungKyGoc.getMaKy());
            request.setTenKy(khungKyGoc.getTenKy());
            request.setThuTu(khungKyGoc.getThuTu());
        }

        if (request.getThuTu() == null) {
            throw new BadRequestException("thuTu không được để trống");
        }

        if (request.getThuTu() <= 0) {
            throw new BadRequestException("thuTu phải lớn hơn 0");
        }

        if (id == null) {

            if (repository.existsByChuongTrinhVersionIdAndMaKy(
                    request.getChuongTrinhVersionId(),
                    request.getMaKy()
            )) {
                throw new DuplicateResourceException(
                        "Mã kỳ đã tồn tại trong version chương trình"
                );
            }

            if (repository.existsByChuongTrinhVersionIdAndThuTu(
                    request.getChuongTrinhVersionId(),
                    request.getThuTu()
            )) {
                throw new DuplicateResourceException(
                        "Thứ tự kỳ đã tồn tại trong version chương trình"
                );
            }

            if (request.getKhungKyGocId() != null
                    && repository.existsByChuongTrinhVersionIdAndKhungKyGocId(
                    request.getChuongTrinhVersionId(),
                    request.getKhungKyGocId()
            )) {
                throw new DuplicateResourceException(
                        "Khung kỳ gốc đã được gán vào version chương trình này"
                );
            }

        } else {

            if (repository.existsByChuongTrinhVersionIdAndMaKyAndIdNot(
                    request.getChuongTrinhVersionId(),
                    request.getMaKy(),
                    id
            )) {
                throw new DuplicateResourceException(
                        "Mã kỳ đã tồn tại trong version chương trình"
                );
            }

            if (repository.existsByChuongTrinhVersionIdAndThuTuAndIdNot(
                    request.getChuongTrinhVersionId(),
                    request.getThuTu(),
                    id
            )) {
                throw new DuplicateResourceException(
                        "Thứ tự kỳ đã tồn tại trong version chương trình"
                );
            }

            if (request.getKhungKyGocId() != null
                    && repository.existsByChuongTrinhVersionIdAndKhungKyGocIdAndIdNot(
                    request.getChuongTrinhVersionId(),
                    request.getKhungKyGocId(),
                    id
            )) {
                throw new DuplicateResourceException(
                        "Khung kỳ gốc đã được gán vào version chương trình này"
                );
            }
        }
    }

    private KhungKy getEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Khung kỳ không tồn tại: " + id));
    }
    @Override
    public Page<KhungKyResponse> findAllByChuongTrinhVersionId(
            Long chuongTrinhVersionId,
            Pageable pageable
    ) {
        return repository.findByChuongTrinhVersionId(
                        chuongTrinhVersionId,
                        pageable
                )
                .map(mapper::toResponse);
    }
}
