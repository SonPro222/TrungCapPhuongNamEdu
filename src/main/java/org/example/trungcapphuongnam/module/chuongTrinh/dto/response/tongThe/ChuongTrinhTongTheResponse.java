package org.example.trungcapphuongnam.module.chuongTrinh.dto.response.tongThe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhVersionResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.cauTruc.ChuongTrinhCauTrucResponse;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhTongTheResponse {

    private ChuongTrinhResponse chuongTrinh;

    private ChuongTrinhVersionResponse versionDangXem;

    private ChuongTrinhCauTrucResponse cauTruc;

    private List<ChuongTrinhVersionResponse> danhSachVersion;

    private Boolean coVersion;

    private Long chuongTrinhId;

    private Long versionDangXemId;

    private Long syllabusChuongTrinhId;

    private Long syllabusChuongTrinhGocId;

    private Integer tongSoKy;

    private Integer tongSoMon;

    private BigDecimal tongSoTinChi;

    private BigDecimal tongSoGio;

    private String lyDoChonVersion;

    private String message;
}