package org.example.trungcapphuongnam.module.giangDay.service;

import org.example.trungcapphuongnam.module.giangDay.dto.request.LichHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LichHocResponse;

import java.util.List;

public interface LichHocService {
    List<LichHocResponse> getAll();
    LichHocResponse getById(Long id);
    LichHocResponse create(LichHocRequest request);
    LichHocResponse update(Long id, LichHocRequest request);
    void delete(Long id);
}
