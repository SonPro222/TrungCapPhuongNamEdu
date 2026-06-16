package org.example.trungcapphuongnam.module.donTu.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonTuDetailResponse {

    private DonTuResponse donTu;
    private List<DonTuLichSuResponse> lichSu;
}
