package org.example.trungcapphuongnam.module.thi.dto.response;

import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.common.enums.Thi.DeThiTrangThai;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeThiResponse {
    private Long id;
    private Long lichThiId;
    private Long lopHocPhanId;
    private Long giaoVienId;
    private String maDe;
    private String tieuDe;
    private String noiDungDe;
    private String fileDeThi;
    private DeThiTrangThai trangThai;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
