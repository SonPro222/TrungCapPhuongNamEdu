package org.example.trungcapphuongnam.module.heThong.service;

import org.example.trungcapphuongnam.module.heThong.dto.request.VaiTroRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.VaiTroResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VaiTroService {
    List<VaiTroResponse> getAll();
    Page<VaiTroResponse> search(String keyword, Pageable pageable);
    VaiTroResponse getById(Long id);
    VaiTroResponse create(VaiTroRequest request);
    VaiTroResponse update(Long id, VaiTroRequest request);
    void delete(Long id);
}
