package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;

import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhMonRequest {

    @NotNull(message = "chuong_trinh_version_id không được để trống")
    private Long chuongTrinhVersionId;

    @NotNull(message = "mon_hoc_id không được để trống")
    private Long monHocId;

    @NotBlank(message = "ma_mon_trong_ct không được để trống")
    private String maMonTrongCt;

    // Cho phép null: copy môn gốc vào version trước, gán kỳ sau (nghiệp vụ tái sử dụng).
    private Long khungKyId;

    private Long nhomKienThucId;

    @NotBlank(message = "loai không được để trống")
    private String loai;

    @NotBlank(message = "loai_hoc_phan không được để trống")
    private String loaiHocPhan;
    private String loaiPhamVi;
    private Boolean batBuoc;

    private Boolean laMonDieuKien;

    private Integer thuTu;

//    private BigDecimal soTinChi;
//
//    private BigDecimal tongGio;
//
//    private BigDecimal gioLyThuyet;
//
//    private BigDecimal gioThucHanh;
//
//    private BigDecimal gioKiemTra;

    private String ghiChu;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
