package org.example.trungcapphuongnam.module.lms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.VaiTroThucHienLms;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lich_su_chinh_diem_bai_tap")
public class LichSuChinhDiemBaiTap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ket_qua_bai_tap_lop_id")
    private Long ketQuaBaiTapLopId;

    @Column(name = "nguoi_chinh_id")
    private Long nguoiChinhId;

    @Enumerated(EnumType.STRING)
    @Column(name = "vai_tro_chinh")
    private VaiTroThucHienLms vaiTroChinh;

    @Column(name = "diem_cu")
    private BigDecimal diemCu;

    @Column(name = "diem_moi")
    private BigDecimal diemMoi;

    @Column(name = "ly_do")
    private String lyDo;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}
