package org.example.trungcapphuongnam.module.lms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiMaTranDe;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ma_tran_de")
public class MaTranDe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_ma_tran")
    private String tenMaTran;

    @Column(name = "chuong_trinh_mon_id", nullable = false)
    private Long chuongTrinhMonId;

    @Column(name = "tong_diem")
    private BigDecimal tongDiem;

    @Column(name = "thoi_gian_lam_bai")
    private Integer thoiGianLamBai;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThaiMaTranDe trangThai;
}
