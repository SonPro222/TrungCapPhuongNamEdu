package org.example.trungcapphuongnam.module.chuongTrinh.dto.response.cauTruc;

import lombok.*;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusMonHocCauTrucResponse {

    private SyllabusMonHocResponse syllabusMonHoc;

    private List<DieuKienMonHocResponse> dieuKienMonHoc;

    private List<SyllabusChuongBaiResponse> chuongBai;

    private List<SyllabusTaiLieuResponse> taiLieu;
}