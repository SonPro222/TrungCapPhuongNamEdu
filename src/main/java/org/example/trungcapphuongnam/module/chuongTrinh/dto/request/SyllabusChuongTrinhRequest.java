package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;
import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusChuongTrinhRequest {

    @NotNull(message = "chuong_trinh_version_id không được để trống")
    private Long chuongTrinhVersionId;
    private Long syllabusChuongTrinhGocId;
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
