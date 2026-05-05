package org.example.trungcapphuongnam.module.lms.service;

import org.example.trungcapphuongnam.module.lms.dto.request.CauTraLoiSinhVienRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.CauTraLoiSinhVienResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CauTraLoiSinhVienService {
    Page<CauTraLoiSinhVienResponse> findAll(Pageable pageable);
    CauTraLoiSinhVienResponse findById(Long id);
    CauTraLoiSinhVienResponse create(CauTraLoiSinhVienRequest request);
    CauTraLoiSinhVienResponse update(Long id, CauTraLoiSinhVienRequest request);
    void delete(Long id);
}
