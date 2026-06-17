package org.example.trungcapphuongnam.module.lms.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.HanhDongBaiTapLop;
import org.example.trungcapphuongnam.module.lms.enums.VaiTroThucHienLms;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lich_su_bai_tap_lop")
public class LichSuBaiTapLop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bai_tap_lop_id")
    private Long baiTapLopId;

    @Column(name = "nguoi_thuc_hien_id")
    private Long nguoiThucHienId;

    @Enumerated(EnumType.STRING)
    @Column(name = "vai_tro_thuc_hien")
    private VaiTroThucHienLms vaiTroThucHien;

    @Enumerated(EnumType.STRING)
    @Column(name = "hanh_dong")
    private HanhDongBaiTapLop hanhDong;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "du_lieu_cu")
    private JsonNode duLieuCu;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "du_lieu_moi")
    private JsonNode duLieuMoi;

    @Column(name = "ly_do")
    private String lyDo;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}
