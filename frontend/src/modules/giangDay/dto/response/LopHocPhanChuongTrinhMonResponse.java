package org.example.trungcapphuongnam.module.giangDay.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LopHocPhanChuongTrinhMonResponse {
    private Long id;
    private Long lopHocPhanId;
    private Long chuongTrinhMonId;
    private String ghiChu;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}