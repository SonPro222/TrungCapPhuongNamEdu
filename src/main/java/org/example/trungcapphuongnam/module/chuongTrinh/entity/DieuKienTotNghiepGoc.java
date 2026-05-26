package org.example.trungcapphuongnam.module.chuongTrinh.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "dieu_kien_tot_nghiep_goc")
public class DieuKienTotNghiepGoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "ma", nullable = true, length = 50)
    private String ma;
    @Column(name = "noi_dung", nullable = false, columnDefinition = "TEXT")
    private String noiDung;
    @Column(name = "ghi_chu", nullable = true, columnDefinition = "TEXT")
    private String ghiChu;
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

}
