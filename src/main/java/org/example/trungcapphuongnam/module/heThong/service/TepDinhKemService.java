package org.example.trungcapphuongnam.module.heThong.service;


import org.example.trungcapphuongnam.module.heThong.constant.LoaiNguoiGui;
import org.example.trungcapphuongnam.module.heThong.dto.request.TepDinhKemRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.TepDinhKemResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TepDinhKemService {

    TepDinhKemResponse upload(MultipartFile file, TepDinhKemRequest request);

    List<TepDinhKemResponse> getAll();

    List<TepDinhKemResponse> getByDoiTuong(String module, String nghiepVu, Long doiTuongId);

    List<TepDinhKemResponse> getByNguoiGui(LoaiNguoiGui nguoiGuiLoai, Long nguoiGuiId);

    TepDinhKemResponse getById(Long id);

    Resource download(Long id);

    TepDinhKemResponse deleteMem(Long id);
}