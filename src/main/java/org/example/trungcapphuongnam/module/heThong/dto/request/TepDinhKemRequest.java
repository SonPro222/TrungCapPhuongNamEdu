package org.example.trungcapphuongnam.module.heThong.dto.request;


import lombok.*;
import org.example.trungcapphuongnam.module.heThong.constant.LoaiNguoiGui;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TepDinhKemRequest {

    private String module;

    private String nghiepVu;

    private Long doiTuongId;

    private LoaiNguoiGui nguoiGuiLoai;

    private Long nguoiGuiId;

    private String nguoiGuiTen;

    private String moTa;
}