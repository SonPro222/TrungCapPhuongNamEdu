package org.example.trungcapphuongnam.module.giangDay.service;

import org.example.trungcapphuongnam.module.giangDay.dto.request.DiemDanhRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.DiemDanhResponse;

import java.util.List;

public interface DiemDanhService {
    List<DiemDanhResponse> getAll();
    DiemDanhResponse getById(Long id);
    DiemDanhResponse create(DiemDanhRequest request);
    DiemDanhResponse update(Long id, DiemDanhRequest request);
    void delete(Long id);
}
