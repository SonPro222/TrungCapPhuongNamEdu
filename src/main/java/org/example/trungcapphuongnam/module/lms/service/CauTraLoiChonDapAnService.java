package org.example.trungcapphuongnam.module.lms.service;

import org.example.trungcapphuongnam.module.lms.dto.request.CauTraLoiChonDapAnRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.CauTraLoiChonDapAnResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CauTraLoiChonDapAnService {
    Page<CauTraLoiChonDapAnResponse> findAll(Pageable pageable);
    CauTraLoiChonDapAnResponse findById(Long id);
    CauTraLoiChonDapAnResponse create(CauTraLoiChonDapAnRequest request);
    CauTraLoiChonDapAnResponse update(Long id, CauTraLoiChonDapAnRequest request);
    void delete(Long id);
}
