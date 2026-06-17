package org.example.trungcapphuongnam.module.daoTao.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NganhLoaiChuongTrinhResponse {

    private Long id;

    private Long nganhId;

    private Long loaiChuongTrinhId;

    private String trangThai;

    private String ghiChu;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
