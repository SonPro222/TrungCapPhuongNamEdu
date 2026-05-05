package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonHocResponse {

    private Long id;

    private String maMon;

    private String tenMon;

    private String moTa;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
