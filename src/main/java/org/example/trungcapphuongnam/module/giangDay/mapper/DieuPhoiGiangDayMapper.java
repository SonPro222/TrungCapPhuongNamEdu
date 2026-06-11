package org.example.trungcapphuongnam.module.giangDay.mapper;

import org.example.trungcapphuongnam.module.giangDay.dto.response.GoiYLichHocResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LichHocPreviewItemResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LichHocResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.response.SinhLichHocPreviewResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.response.XepLichHangLoatItemResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.response.XepLichHangLoatResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhan;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DieuPhoiGiangDayMapper {

    public LichHocPreviewItemResponse toPreviewItem(
            Integer stt,
            LocalDate ngayHoc,
            Long caHocId,
            Long phongHocId,
            Long giaoVienId,
            String noiDungBuoiHoc,
            List<String> canhBao
    ) {
        return LichHocPreviewItemResponse.builder()
                .stt(stt)
                .ngayHoc(ngayHoc)
                .caHocId(caHocId)
                .phongHocId(phongHocId)
                .giaoVienId(giaoVienId)
                .noiDungBuoiHoc(noiDungBuoiHoc)
                .canhBao(canhBao)
                .build();
    }

    public SinhLichHocPreviewResponse toPreviewResponse(
            Boolean hopLe,
            Integer soBuoiCanXep,
            Integer soBuoiDaXep,
            Integer soBuoiConLai,
            Integer soBuoiXepDuoc,
            List<String> canhBao,
            List<LichHocPreviewItemResponse> items
    ) {
        return SinhLichHocPreviewResponse.builder()
                .hopLe(hopLe)
                .soBuoiCanXep(soBuoiCanXep)
                .soBuoiDaXep(soBuoiDaXep)
                .soBuoiConLai(soBuoiConLai)
                .soBuoiXepDuoc(soBuoiXepDuoc)
                .canhBao(canhBao)
                .items(items)
                .build();
    }

    public GoiYLichHocResponse toGoiYResponse(
            SinhLichHocPreviewResponse preview,
            Long phongHocId,
            Long caHocId,
            List<Integer> thuTrongTuan,
            Integer diemUuTien,
            Integer soCanhBao
    ) {
        return GoiYLichHocResponse.builder()
                .hopLe(preview == null ? null : preview.getHopLe())
                .diemUuTien(diemUuTien)
                .phongHocId(phongHocId)
                .caHocId(caHocId)
                .thuTrongTuan(thuTrongTuan)
                .soBuoiCanXep(preview == null ? null : preview.getSoBuoiConLai())
                .soBuoiXepDuoc(preview == null ? null : preview.getSoBuoiXepDuoc())
                .soCanhBao(soCanhBao)
                .canhBao(preview == null ? null : preview.getCanhBao())
                .items(preview == null ? null : preview.getItems())
                .build();
    }

    public XepLichHangLoatItemResponse toXepLichHangLoatNotFound(Long lopHocPhanId) {
        return XepLichHangLoatItemResponse.builder()
                .lopHocPhanId(lopHocPhanId)
                .thanhCong(false)
                .thongBao("Lớp học phần không tồn tại")
                .build();
    }

    public XepLichHangLoatItemResponse toXepLichHangLoatFailure(
            LopHocPhan lop,
            String thongBao
    ) {
        return XepLichHangLoatItemResponse.builder()
                .lopHocPhanId(lop == null ? null : lop.getId())
                .maLop(lop == null ? null : lop.getMaLop())
                .tenLop(lop == null ? null : lop.getTenLop())
                .thanhCong(false)
                .thongBao(thongBao)
                .build();
    }

    public XepLichHangLoatItemResponse toXepLichHangLoatSuccess(
            LopHocPhan lop,
            Boolean chiPreview,
            SinhLichHocPreviewResponse preview,
            List<LichHocResponse> lichDaTao
    ) {
        return XepLichHangLoatItemResponse.builder()
                .lopHocPhanId(lop == null ? null : lop.getId())
                .maLop(lop == null ? null : lop.getMaLop())
                .tenLop(lop == null ? null : lop.getTenLop())
                .thanhCong(true)
                .thongBao(Boolean.TRUE.equals(chiPreview)
                        ? "Preview hợp lệ: có thể tạo " + (preview == null ? 0 : preview.getSoBuoiXepDuoc()) + " buổi"
                        : "Đã tạo " + (lichDaTao == null ? 0 : lichDaTao.size()) + " buổi lịch học")
                .lichDaTao(lichDaTao)
                .build();
    }

    public XepLichHangLoatResponse toXepLichHangLoatResponse(
            Integer tongLop,
            Integer soLopThanhCong,
            Integer soLopLoi,
            Boolean chiPreview,
            List<XepLichHangLoatItemResponse> items
    ) {
        return XepLichHangLoatResponse.builder()
                .tongLop(tongLop)
                .soLopThanhCong(soLopThanhCong)
                .soLopLoi(soLopLoi)
                .chiPreview(chiPreview)
                .items(items)
                .build();
    }
}
