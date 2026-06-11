package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NhomTuChonMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NhomTuChonMauResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NhomTuChonMauService {

    Page<NhomTuChonMauResponse> findAll(String ma, String keyword, Pageable pageable);

    NhomTuChonMauResponse findById(Long id);

    NhomTuChonMauResponse create(NhomTuChonMauRequest request);

    NhomTuChonMauResponse update(Long id, NhomTuChonMauRequest request);

    void delete(Long id);
}