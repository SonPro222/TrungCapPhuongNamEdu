package org.example.trungcapphuongnam.module.heThong.service;

import org.example.trungcapphuongnam.module.heThong.dto.request.TaiKhoanVaiTroRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.TaiKhoanVaiTroResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaiKhoanVaiTroService {
    List<TaiKhoanVaiTroResponse> getAll();
    Page<TaiKhoanVaiTroResponse> search(Long taiKhoanId, Long vaiTroId, Pageable pageable);
    TaiKhoanVaiTroResponse getById(Long id);
    TaiKhoanVaiTroResponse create(TaiKhoanVaiTroRequest request);
    TaiKhoanVaiTroResponse update(Long id, TaiKhoanVaiTroRequest request);
    void delete(Long id);
}
