package org.example.trungcapphuongnam.module.lms.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@IdClass(MigrateCauHoiLegacyMapId.class)
@Table(name = "migrate_cau_hoi_legacy_map")
public class MigrateCauHoiLegacyMap {
    @Id
    @Column(name = "ngan_hang_cau_hoi_id", nullable = false)
    private Long nganHangCauHoiId;

    @Column(name = "cau_hoi_mau_id", nullable = false)
    private Long cauHoiMauId;

    @Column(name = "cau_hoi_version_id", nullable = false)
    private Long cauHoiVersionId;
}
