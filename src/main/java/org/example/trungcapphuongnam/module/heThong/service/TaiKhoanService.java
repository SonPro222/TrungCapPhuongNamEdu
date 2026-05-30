package org.example.trungcapphuongnam.module.heThong.service;

import org.example.trungcapphuongnam.module.heThong.dto.request.DoiMatKhauTaiKhoanRequest;
import org.example.trungcapphuongnam.module.heThong.dto.request.TaiKhoanRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.TaiKhoanResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaiKhoanService {

    List<TaiKhoanResponse> getAll();

    Page<TaiKhoanResponse> search(String keyword, String loaiTaiKhoan, String trangThai, String vaiTro, Pageable pageable);

    TaiKhoanResponse getById(Long id);

    TaiKhoanResponse getByEmail(String email);

    TaiKhoanResponse create(TaiKhoanRequest request);

    TaiKhoanResponse update(Long id, TaiKhoanRequest request);

    TaiKhoanResponse doiMatKhauQuanTri(Long id, DoiMatKhauTaiKhoanRequest request);

    TaiKhoanResponse doiMatKhauTheoGmail(String email, DoiMatKhauTaiKhoanRequest request);

    void delete(Long id);
}