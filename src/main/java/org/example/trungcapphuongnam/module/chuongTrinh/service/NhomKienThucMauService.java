package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NhomKienThucMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NhomKienThucMauResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NhomKienThucMauService {

    Page<NhomKienThucMauResponse> findAll(String ma, String loaiNhom, String keyword, Pageable pageable);

    NhomKienThucMauResponse findById(Long id);

    NhomKienThucMauResponse create(NhomKienThucMauRequest request);

    NhomKienThucMauResponse update(Long id, NhomKienThucMauRequest request);

    void delete(Long id);
}