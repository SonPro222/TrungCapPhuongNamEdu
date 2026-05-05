package org.example.trungcapphuongnam.module.heThong.service;

import org.example.trungcapphuongnam.module.heThong.dto.request.VaiTroQuyenRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.VaiTroQuyenResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VaiTroQuyenService {
    List<VaiTroQuyenResponse> getAll();
    Page<VaiTroQuyenResponse> search(Long vaiTroId, Long quyenId, Pageable pageable);
    VaiTroQuyenResponse getById(Long id);
    VaiTroQuyenResponse create(VaiTroQuyenRequest request);
    VaiTroQuyenResponse update(Long id, VaiTroQuyenRequest request);
    void delete(Long id);
}
