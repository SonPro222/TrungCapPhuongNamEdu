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
@Table(name = "bai_tap_online_cau_hoi")
public class BaiTapOnlineCauHoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bai_tap_online_id")
    private Long baiTapOnlineId;

    @Column(name = "cau_hoi_id")
    private Long cauHoiId;

    @Column(name = "diem")
    private BigDecimal diem;

    @Column(name = "thu_tu")
    private Integer thuTu;

    @Column(name = "bat_buoc")
    private Boolean batBuoc;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}
