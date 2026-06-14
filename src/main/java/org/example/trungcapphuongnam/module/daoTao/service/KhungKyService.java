package org.example.trungcapphuongnam.module.daoTao.service;

import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyCanhBaoTaiHocResponse;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyGoiYResponse;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface KhungKyService {
    Page<KhungKyResponse> findAllByChuongTrinhVersionId(
            Long chuongTrinhVersionId,
            Pageable pageable
    );

    KhungKyGoiYResponse goiYTheoVersion(Long chuongTrinhVersionId);

    KhungKyCanhBaoTaiHocResponse canhBaoTaiHocTheoVersion(Long chuongTrinhVersionId);

    List<KhungKyResponse> taoCacKyConThieuTheoVersion(Long chuongTrinhVersionId);

    Page<KhungKyResponse> findAll(Pageable pageable);

    KhungKyResponse findById(Long id);

    KhungKyResponse create(KhungKyRequest request);

    KhungKyResponse update(Long id, KhungKyRequest request);

    void delete(Long id);
}
