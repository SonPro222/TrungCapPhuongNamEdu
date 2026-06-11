package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhomTuChonGocResponse {

    private Long id;

    private String ma;
    private String ten;

    private String moTa;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}