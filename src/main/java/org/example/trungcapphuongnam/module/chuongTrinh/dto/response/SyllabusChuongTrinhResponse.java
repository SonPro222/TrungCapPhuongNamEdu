package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusChuongTrinhResponse {

    private Long id;

    private Long chuongTrinhVersionId;
    private Long syllabusChuongTrinhGocId;

    private String moTaTongQuan;

    private String mucDich;

    private String yeuCauDaoTao;

    private String phuongPhapDaoTao;

    private String ghiChu;

}
