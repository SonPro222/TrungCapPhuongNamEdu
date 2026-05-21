package org.example.trungcapphuongnam.module.chuongTrinh.dto.response.cauTruc;

import lombok.*;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.MonTuChonResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonTuChonCauTrucResponse {

    private MonTuChonResponse monTuChon;

    private ChuongTrinhMonCauTrucResponse monTrongChuongTrinh;
}