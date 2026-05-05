package org.example.trungcapphuongnam.module.lms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cau_tra_loi_sinh_vien")
public class CauTraLoiSinhVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lan_lam_bai_id")
    private Long lanLamBaiId;

    @Column(name = "cau_hoi_id")
    private Long cauHoiId;

    @Column(name = "noi_dung_tra_loi")
    private String noiDungTraLoi;

    @Column(name = "diem")
    private BigDecimal diem;

    @Column(name = "la_dung")
    private Boolean laDung;

    @Column(name = "nhan_xet")
    private String nhanXet;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
