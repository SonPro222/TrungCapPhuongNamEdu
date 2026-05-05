package org.example.trungcapphuongnam.module.giangDay.service;

import org.example.trungcapphuongnam.module.giangDay.dto.request.PhongHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.PhongHocResponse;

import java.util.List;

public interface PhongHocService {
    List<PhongHocResponse> getAll();
    PhongHocResponse getById(Long id);
    PhongHocResponse create(PhongHocRequest request);
    PhongHocResponse update(Long id, PhongHocRequest request);
    void delete(Long id);
}
