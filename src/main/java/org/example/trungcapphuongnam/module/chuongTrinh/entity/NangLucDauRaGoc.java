package org.example.trungcapphuongnam.module.chuongTrinh.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import org.example.trungcapphuongnam.module.chuongTrinh.constant.LoaiNangLucDauRa;
import org.example.trungcapphuongnam.module.chuongTrinh.constant.LoaiNangLucDauRaConverter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "nang_luc_dau_ra_goc")
public class NangLucDauRaGoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "ma", nullable = true, length = 50)
    private String ma;
    @Convert(converter = LoaiNangLucDauRaConverter.class)
    @Column(name = "loai", nullable = false, length = 50)
    private LoaiNangLucDauRa loai;
    @Column(name = "noi_dung", nullable = false, columnDefinition = "TEXT")
    private String noiDung;
    @Column(name = "ghi_chu", nullable = true, columnDefinition = "TEXT")
    private String ghiChu;
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
