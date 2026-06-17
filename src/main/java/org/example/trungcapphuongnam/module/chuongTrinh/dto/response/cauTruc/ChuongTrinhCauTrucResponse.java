package org.example.trungcapphuongnam.module.chuongTrinh.dto.response.cauTruc;

import lombok.*;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhCauTrucResponse {

    private ChuongTrinhResponse chuongTrinh;

    private ChuongTrinhVersionResponse version;

    private List<MucTieuChuongTrinhResponse> mucTieuChuongTrinh;

    private List<NangLucDauRaResponse> nangLucDauRa;

    private List<ViTriViecLamResponse> viTriViecLam;

    private List<DieuKienTotNghiepResponse> dieuKienTotNghiep;

    private List<SyllabusChuongTrinhResponse> syllabusChuongTrinh;

    private List<NhomKienThucResponse> nhomKienThuc;

    private List<NhomTuChonCauTrucResponse> nhomTuChon;

    private List<KhungKyCauTrucResponse> khungKy;

    private List<ChuongTrinhMonCauTrucResponse> monTrongChuongTrinh;
}