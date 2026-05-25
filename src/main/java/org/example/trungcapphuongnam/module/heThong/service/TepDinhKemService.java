package org.example.trungcapphuongnam.module.heThong.service;

import org.example.trungcapphuongnam.module.heThong.dto.request.TepDinhKemRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.TepDinhKemResponse;
import org.example.trungcapphuongnam.module.heThong.enums.LoaiNguoiGui;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface TepDinhKemService {

    TepDinhKemResponse upload(MultipartFile file, TepDinhKemRequest request);

    Page<TepDinhKemResponse> getAll(Pageable pageable);

    Page<TepDinhKemResponse> getByDoiTuong(String module, String nghiepVu, Long doiTuongId, Pageable pageable);

    Page<TepDinhKemResponse> getByNguoiGui(LoaiNguoiGui nguoiGuiLoai, Long nguoiGuiId, Pageable pageable);

    Page<TepDinhKemResponse> getByModule(String module, String nghiepVu, Pageable pageable);

    TepDinhKemResponse getById(Long id);

    Resource download(Long id);

    TepDinhKemResponse deleteMem(Long id);
}