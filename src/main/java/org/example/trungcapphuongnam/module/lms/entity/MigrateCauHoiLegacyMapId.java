package org.example.trungcapphuongnam.module.lms.entity;

import java.io.Serializable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MigrateCauHoiLegacyMapId implements Serializable {
    private Long nganHangCauHoiId;
}
