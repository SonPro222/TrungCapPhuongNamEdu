package org.example.trungcapphuongnam.module.lms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.HanhDongDayDiemLms;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lich_su_day_diem_lms")
public class LichSuDayDiemLms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ket_qua_bai_tap_lop_id")
    private Long ketQuaBaiTapLopId;

    @Column(name = "diem_chi_tiet_id")
    private Long diemChiTietId;

    @Column(name = "nguoi_thuc_hien_id")
    private Long nguoiThucHienId;

    @Column(name = "diem_cu")
    private BigDecimal diemCu;

    @Column(name = "diem_moi")
    private BigDecimal diemMoi;

    @Enumerated(EnumType.STRING)
    @Column(name = "hanh_dong")
    private HanhDongDayDiemLms hanhDong;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}
