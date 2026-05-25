package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NhomKienThucGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NhomKienThucGocResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NhomKienThucGocService {

    Page<NhomKienThucGocResponse> findAll(String ma, String loaiNhom, String keyword, Pageable pageable);

    NhomKienThucGocResponse findById(Long id);

    NhomKienThucGocResponse create(NhomKienThucGocRequest request);

    NhomKienThucGocResponse update(Long id, NhomKienThucGocRequest request);

    void delete(Long id);
}