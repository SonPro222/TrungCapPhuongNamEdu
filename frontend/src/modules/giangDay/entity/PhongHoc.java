package org.example.trungcapphuongnam.module.giangDay.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.trungcapphuongnam.module.giangDay.enums.LoaiPhong;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiPhongHoc;

import java.time.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "phong_hoc")
public class PhongHoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_phong", nullable = false)
    private String maPhong;

    @Column(name = "ten_phong", nullable = false)
    private String tenPhong;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai_phong")
    private LoaiPhong loaiPhong;

    @Column(name = "suc_chua")
    private Integer sucChua;

    @Column(name = "dia_diem", columnDefinition = "TEXT")
    private String diaDiem;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThaiPhongHoc trangThai;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

}
