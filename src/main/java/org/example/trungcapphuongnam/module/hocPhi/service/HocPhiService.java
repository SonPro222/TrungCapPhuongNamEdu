package org.example.trungcapphuongnam.module.hocPhi.service;


import org.example.trungcapphuongnam.module.hocPhi.enums.TrangThaiHocPhi;
import org.example.trungcapphuongnam.module.hocPhi.dto.request.HocPhiRequest;
import org.example.trungcapphuongnam.module.hocPhi.dto.response.HocPhiResponse;

import java.util.List;

public interface HocPhiService {
    List<HocPhiResponse> getAll();
    HocPhiResponse getById(Long id);
    List<HocPhiResponse> getBySinhVienChuongTrinhId(Long sinhVienChuongTrinhId);
    List<HocPhiResponse> getByTrangThai(TrangThaiHocPhi trangThai);
    HocPhiResponse create(HocPhiRequest request);
    HocPhiResponse update(Long id, HocPhiRequest request);
    void delete(Long id);
}
