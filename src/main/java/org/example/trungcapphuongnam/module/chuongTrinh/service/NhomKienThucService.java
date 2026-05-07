package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NhomKienThucRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NhomKienThucResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NhomKienThucService {
    Page<NhomKienThucResponse> findAll(Pageable pageable);
    Page<NhomKienThucResponse> findAllByChuongTrinhVersionId(Long chuongTrinhVersionId, Pageable pageable);
    NhomKienThucResponse findById(Long id);
    NhomKienThucResponse create(NhomKienThucRequest request);
    NhomKienThucResponse update(Long id, NhomKienThucRequest request);
    void delete(Long id);
}
