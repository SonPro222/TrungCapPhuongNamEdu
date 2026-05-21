package org.example.trungcapphuongnam.module.heThong.dto.response;


import lombok.*;
import org.example.trungcapphuongnam.module.heThong.constant.LoaiNguoiGui;
import org.example.trungcapphuongnam.module.heThong.constant.LoaiNoiDungTep;
import org.example.trungcapphuongnam.module.heThong.constant.TrangThaiTepDinhKem;


import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TepDinhKemResponse {

    private Long id;

    private String tenGoc;

    private String tenLuu;

    private String duongDanTuongDoi;

    private String duongDanTaiVe;

    private LoaiNoiDungTep loaiNoiDung;

    private String contentType;

    private String extension;

    private Long dungLuong;

    private String module;

    private String nghiepVu;

    private Long doiTuongId;

    private LoaiNguoiGui nguoiGuiLoai;

    private Long nguoiGuiId;

    private String nguoiGuiTen;

    private String moTa;

    private TrangThaiTepDinhKem trangThai;

    private LocalDateTime ngayTao;

    private LocalDateTime ngayCapNhat;
}