package org.example.trungcapphuongnam.module.chuongTrinh.dto.response.cauTruc;

import lombok.*;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyResponse;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhungKyCauTrucResponse {

    private KhungKyResponse khungKy;

    private List<ChuongTrinhMonCauTrucResponse> monTrongKy;
}