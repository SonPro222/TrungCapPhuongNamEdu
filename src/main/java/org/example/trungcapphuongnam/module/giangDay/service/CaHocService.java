package org.example.trungcapphuongnam.module.giangDay.service;

import org.example.trungcapphuongnam.module.giangDay.dto.request.CaHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.CaHocResponse;

import java.util.List;

public interface CaHocService {
    List<CaHocResponse> getAll();
    CaHocResponse getById(Long id);
    CaHocResponse create(CaHocRequest request);
    CaHocResponse update(Long id, CaHocRequest request);
    void delete(Long id);
}
