package org.example.trungcapphuongnam.module.daoTao.dto;


import lombok.*;

        import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhungKyMauResponse {

    private Long id;

    private String maKy;

    private String tenKy;

    private Integer thuTu;

    private String moTa;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}