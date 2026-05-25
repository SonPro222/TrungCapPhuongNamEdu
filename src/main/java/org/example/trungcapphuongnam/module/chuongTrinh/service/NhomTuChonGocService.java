package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NhomTuChonGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NhomTuChonGocResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NhomTuChonGocService {

    Page<NhomTuChonGocResponse> findAll(String ma, String keyword, Pageable pageable);

    NhomTuChonGocResponse findById(Long id);

    NhomTuChonGocResponse create(NhomTuChonGocRequest request);

    NhomTuChonGocResponse update(Long id, NhomTuChonGocRequest request);

    void delete(Long id);
}