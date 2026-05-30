package org.example.trungcapphuongnam.module.chuongTrinh.dto.response.cauTruc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NhomTuChonResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhomTuChonCauTrucResponse {

    private NhomTuChonResponse nhomTuChon;

    private List<MonTuChonCauTrucResponse> monTuChon;
}