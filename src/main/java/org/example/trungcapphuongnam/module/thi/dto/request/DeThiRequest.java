package org.example.trungcapphuongnam.module.thi.dto.request;


import lombok.*;
import org.example.trungcapphuongnam.module.thi.enums.DeThiTrangThai;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeThiRequest {
    private Long lichThiId;
    private Long lopHocPhanId;
    private Long giaoVienId;
    private String maDe;
    private String tieuDe;
    private String noiDungDe;
    private String fileDeThi;
    private DeThiTrangThai trangThai;
}
