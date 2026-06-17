package org.example.trungcapphuongnam.module.lms.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.KetQuaDat;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiKetQuaBaiTap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KetQuaBaiTapLopRequest {

    @NotNull

    private Long baiTapLopId;

    @NotNull

    private Long sinhVienId;


    private Long lanLamDuocChonId;

    @DecimalMin(value = "0")

    private BigDecimal diemHeThong;

    @DecimalMin(value = "0")

    private BigDecimal diemGiaoVienChinhSua;

    @DecimalMin(value = "0")

    private BigDecimal diemCuoiCung;

    private KetQuaDat ketQua;

    private TrangThaiKetQuaBaiTap trangThai;


    private Long diemChiTietId;

    private String nhanXet;

    private LocalDateTime ngayTinhDiem;

    private LocalDateTime ngayChot;


    private Long nguoiChotId;

    private LocalDateTime ngayDaySangDiem;

    private Long nguoiDaySangDiemId;

}
