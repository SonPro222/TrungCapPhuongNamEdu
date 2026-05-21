package org.example.trungcapphuongnam.module.daoTao.dto.cauTruc;

import lombok.*;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhVersionResponse;
import org.example.trungcapphuongnam.module.daoTao.dto.LoaiChuongTrinhResponse;
import org.example.trungcapphuongnam.module.daoTao.dto.TrinhDoDaoTaoResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhTheoNganhResponse {

    private ChuongTrinhResponse chuongTrinh;

    private LoaiChuongTrinhResponse loaiChuongTrinh;

    private TrinhDoDaoTaoResponse trinhDoDaoTao;

    private List<ChuongTrinhVersionResponse> versions;
}