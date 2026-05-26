package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;
import lombok.*;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DieuKienMonHocResponse {

    private Long id;

    private Long syllabusMonId;

    private String loai;

    private String noiDung;

    private Integer thuTu;

    private String ma;

    private String ghiChu;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
