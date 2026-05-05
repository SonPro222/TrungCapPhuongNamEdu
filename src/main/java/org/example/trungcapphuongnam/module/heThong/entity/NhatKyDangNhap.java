package org.example.trungcapphuongnam.module.heThong.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "nhat_ky_dang_nhap")
public class NhatKyDangNhap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tai_khoan_id")
    private TaiKhoan taiKhoan;

    @Column(name = "thoi_gian", insertable = false, updatable = false)
    private LocalDateTime thoiGian;

    @Column(name = "ip_address", length = 100)
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "thanh_cong")
    private Boolean thanhCong;

    @Column(name = "ly_do_that_bai", columnDefinition = "TEXT")
    private String lyDoThatBai;
}