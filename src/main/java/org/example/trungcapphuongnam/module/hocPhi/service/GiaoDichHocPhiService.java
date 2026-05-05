package org.example.trungcapphuongnam.module.hocPhi.service;

import org.example.trungcapphuongnam.module.hocPhi.dto.request.GiaoDichHocPhiRequest;
import org.example.trungcapphuongnam.module.hocPhi.dto.response.GiaoDichHocPhiResponse;

import java.util.List;

public interface GiaoDichHocPhiService {
    List<GiaoDichHocPhiResponse> getAll();
    GiaoDichHocPhiResponse getById(Long id);
    List<GiaoDichHocPhiResponse> getByHocPhiId(Long hocPhiId);
    GiaoDichHocPhiResponse create(GiaoDichHocPhiRequest request);
    GiaoDichHocPhiResponse update(Long id, GiaoDichHocPhiRequest request);
    void delete(Long id);
}
