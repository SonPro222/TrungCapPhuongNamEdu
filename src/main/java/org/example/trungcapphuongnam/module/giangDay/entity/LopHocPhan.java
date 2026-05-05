package org.example.trungcapphuongnam.module.giangDay.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.trungcapphuongnam.common.enums.TrangThaiLopHocPhan;

import java.time.*;
import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lop_hoc_phan")
public class LopHocPhan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chuong_trinh_mon_id", nullable = false)
    private Long chuongTrinhMonId;

    @Column(name = "ma_lop", nullable = false)
    private String maLop;

    @Column(name = "ten_lop", nullable = false)
    private String tenLop;

    @Column(name = "so_luong_toi_da")
    private Integer soLuongToiDa;

    @Column(name = "so_luong_hien_tai")
    private Integer soLuongHienTai;

    @Column(name = "ngay_bat_dau")
    private LocalDate ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private LocalDate ngayKetThuc;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThaiLopHocPhan trangThai;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

}
