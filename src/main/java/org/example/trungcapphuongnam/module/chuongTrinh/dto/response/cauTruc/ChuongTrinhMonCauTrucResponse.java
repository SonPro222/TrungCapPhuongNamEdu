package org.example.trungcapphuongnam.module.chuongTrinh.dto.response.cauTruc;

import lombok.*;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhMonCauTrucResponse {

    private ChuongTrinhMonResponse chuongTrinhMon;

    private MonHocResponse monHoc;

    private NhomKienThucResponse nhomKienThuc;

    private List<MonTienQuyetResponse> monTienQuyet;

    private List<QuyDoiDiemResponse> quyDoiDiem;

    private List<SyllabusMonHocCauTrucResponse> syllabusMonHoc;
}