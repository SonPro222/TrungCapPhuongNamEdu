package org.example.trungcapphuongnam.module.lms.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lich_su_cau_hoi")
public class LichSuCauHoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cau_hoi_mau_id")
    private Long cauHoiMauId;

    @Column(name = "cau_hoi_version_id")
    private Long cauHoiVersionId;

    @Column(name = "hanh_dong")
    private String hanhDong;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "noi_dung_cu")
    private JsonNode noiDungCu;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "noi_dung_moi")
    private JsonNode noiDungMoi;

    @Column(name = "nguoi_thuc_hien_id")
    private Long nguoiThucHienId;

    @CreationTimestamp
    @Column(name = "thoi_gian", updatable = false)
    private LocalDateTime thoiGian;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;
}
