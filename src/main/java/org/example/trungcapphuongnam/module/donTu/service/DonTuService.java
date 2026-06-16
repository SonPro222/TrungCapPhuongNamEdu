package org.example.trungcapphuongnam.module.donTu.service;

import org.example.trungcapphuongnam.module.donTu.dto.request.DonTuCreateRequest;
import org.example.trungcapphuongnam.module.donTu.dto.request.DonTuXuLyRequest;
import org.example.trungcapphuongnam.module.donTu.dto.response.DonTuDetailResponse;
import org.example.trungcapphuongnam.module.donTu.dto.response.DonTuResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DonTuService {

    Page<DonTuResponse> findCuaSinhVien(Long sinhVienId, Pageable pageable);

    DonTuDetailResponse findById(Long id);

    DonTuResponse create(DonTuCreateRequest request);

    DonTuResponse huy(Long id, DonTuXuLyRequest request);
}
