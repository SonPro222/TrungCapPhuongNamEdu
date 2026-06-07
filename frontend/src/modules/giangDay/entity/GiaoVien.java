package org.example.trungcapphuongnam.module.giangDay.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiGiaoVien;

import java.time.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "giao_vien")
public class GiaoVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_giao_vien", nullable = false)
    private String maGiaoVien;

    @Column(name = "ho_ten", nullable = false)
    private String hoTen;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @Column(name = "chuyen_mon", columnDefinition = "TEXT")
    private String chuyenMon;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThaiGiaoVien trangThai;

    @Column(name = "tai_khoan_id")
    private Long taiKhoanId;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

}
