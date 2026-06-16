package org.example.trungcapphuongnam.module.donTu.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoaiDonTuResponse {

    private Long id;
    private String ma;
    private String ten;
    private String moTa;
    private Boolean yeuCauFile;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
