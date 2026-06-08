package org.example.trungcapphuongnam.module.giangDay.service;

import org.example.trungcapphuongnam.module.giangDay.dto.request.SinhLichHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LichHocResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.response.SinhLichHocPreviewResponse;

import java.util.List;

public interface DieuPhoiGiangDayService {
    SinhLichHocPreviewResponse previewSinhLich(Long lopHocPhanId, SinhLichHocRequest request);

    List<LichHocResponse> taoLichTuDong(Long lopHocPhanId, SinhLichHocRequest request);
}
