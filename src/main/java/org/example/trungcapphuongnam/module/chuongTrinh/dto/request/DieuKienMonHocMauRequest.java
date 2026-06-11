package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;


import lombok.*;
        import org.example.trungcapphuongnam.module.chuongTrinh.enums.LoaiDieuKienMonHoc;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DieuKienMonHocGocRequest {

    private String ma;

    private LoaiDieuKienMonHoc loai;

    private String noiDung;

    private String ghiChu;
}