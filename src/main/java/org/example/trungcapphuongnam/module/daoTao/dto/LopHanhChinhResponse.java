package org.example.trungcapphuongnam.module.daoTao.dto;

import lombok.*;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LopHanhChinhResponse {
    private Long id;

    private String maLop;

    private String tenLop;

    private Long chuongTrinhVersionId;

    private Long khoaDaoTaoId;

    private Integer siSo;

    private String trangThai;

    private String ghiChu;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
