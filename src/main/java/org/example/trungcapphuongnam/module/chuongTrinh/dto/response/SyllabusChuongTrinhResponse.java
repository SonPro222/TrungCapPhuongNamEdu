package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;
import lombok.*;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusChuongTrinhResponse {

    private Long id;

    private Long chuongTrinhVersionId;
    private Long syllabusChuongTrinhMauId;
    private String duongDan;
    private String moTaTongQuan;

    private String mucDich;

    private String yeuCauDaoTao;

    private String phuongPhapDaoTao;

    private String ghiChu;

    private String ma;

    private String ten;

    private String mucTieu;

    private String doiTuongTuyenSinh;

    private String thoiGianDaoTao;

    private String khoiLuongKienThuc;

    private String dieuKienTotNghiep;

    private String phuongPhapDanhGia;

    private String huongDanThucHien;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
