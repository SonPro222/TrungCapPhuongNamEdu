package org.example.trungcapphuongnam.module.daoTao.dto;

import lombok.*;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NganhResponse {
    private Long id;

    private String maNganh;

    private String tenNganh;

    private String moTa;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
