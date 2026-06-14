package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.example.trungcapphuongnam.module.chuongTrinh.enums.TrangThaiChuongTrinhVersion;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhVersionResponse {

    private Long id;

    private Long chuongTrinhId;

    private String maVersion;

    private String tenVersion;

    private LocalDate ngayApDung;

    private LocalDate ngayHetHieuLuc;

    private String soQuyetDinh;

    private LocalDate ngayQuyetDinh;

    private String nguoiKy;

    private String coQuanBanHanh;

    private String fileQuyetDinh;

    private BigDecimal tongTinChi;

    private BigDecimal tongSoGio;

    private BigDecimal tongGioLyThuyet;

    private BigDecimal tongGioThucHanh;

    private BigDecimal tongGioKiemTra;

    private Boolean laHienHanh;

    private TrangThaiChuongTrinhVersion trangThai;

    private Boolean conHieuLucTheoNgay;

    private Boolean duocPhepChinhSua;

    private Boolean duocPhepVanHanh;

    private String lyDoTrangThai;

    /** Số môn trong version chưa có syllabus áp dụng; nếu > 0 thì tổng giờ/tín chỉ chưa đầy đủ. */
    private Integer soMonChuaCoSyllabus;

    /** Cảnh báo tổng hợp khi có môn chưa có syllabus. Null nếu không có vấn đề. */
    private String canhBaoTongHop;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
