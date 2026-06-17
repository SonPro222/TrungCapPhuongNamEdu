package org.example.trungcapphuongnam.module.sinhVien.service;

import org.example.trungcapphuongnam.module.sinhVien.dto.request.SinhVienRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.request.TiepNhanSinhVienRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.SinhVienResponse;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.TiepNhanSinhVienResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SinhVienService {
    Page<SinhVienResponse> findAll(Pageable pageable);
    Page<SinhVienResponse> findTheoNganhChuongTrinhVersion(Long nganhId, Long chuongTrinhId, Long chuongTrinhVersionId, Pageable pageable);
    SinhVienResponse findById(Long id);
    SinhVienResponse create(SinhVienRequest request);
    TiepNhanSinhVienResponse tiepNhan(TiepNhanSinhVienRequest request);
    TiepNhanSinhVienResponse tiepNhanCoFile(
            TiepNhanSinhVienRequest request,
            MultipartFile anhChanDung,
            MultipartFile cccdTruoc,
            MultipartFile cccdSau,
            MultipartFile bangCap,
            List<MultipartFile> giayToKhac
    );
    SinhVienResponse update(Long id, SinhVienRequest request);
    void delete(Long id);
}
