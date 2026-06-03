package org.example.trungcapphuongnam.module.giangDay.service;

import org.example.trungcapphuongnam.module.giangDay.dto.request.SinhLichHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.request.XepLichHangLoatRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.GoiYLichHocResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LichHocResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.response.SinhLichHocPreviewResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.response.XepLichHangLoatResponse;

import java.util.List;

public interface DieuPhoiGiangDayService {
    SinhLichHocPreviewResponse previewSinhLich(Long lopHocPhanId, SinhLichHocRequest request);

    List<LichHocResponse> taoLichTuDong(Long lopHocPhanId, SinhLichHocRequest request);

    List<GoiYLichHocResponse> goiYLichHoc(Long lopHocPhanId, SinhLichHocRequest request);

    XepLichHangLoatResponse xepLichHangLoat(XepLichHangLoatRequest request);
}
