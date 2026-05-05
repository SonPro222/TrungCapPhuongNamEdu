package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;
import lombok.*;


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

}
