package org.example.trungcapphuongnam.module.giangDay.service;

import org.example.trungcapphuongnam.module.giangDay.dto.request.GiaoVienRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.GiaoVienResponse;

import java.util.List;

public interface GiaoVienService {
    List<GiaoVienResponse> getAll();
    GiaoVienResponse getById(Long id);
    GiaoVienResponse create(GiaoVienRequest request);
    GiaoVienResponse update(Long id, GiaoVienRequest request);
    void delete(Long id);
}
