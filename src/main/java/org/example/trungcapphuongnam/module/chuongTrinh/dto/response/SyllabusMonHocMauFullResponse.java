package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusMonHocMauFullResponse {

    private SyllabusMonHocMauResponse syllabusMonHocMau;
    private List<SyllabusMonHocMauChuongBaiResponse> chuongBaiList;
    private List<SyllabusMonHocMauDieuKienResponse> dieuKienList;
    private List<SyllabusMonHocMauTaiLieuResponse> taiLieuList;
    private List<CauHinhDanhGiaMauResponse> cauHinhDanhGiaList;
    private List<QuyDoiDiemMauResponse> quyDoiDiemList;
    private List<SyllabusMonHocMauFileResponse> fileList;
    /** Danh sách cảnh báo từ quá trình parse/import file (null nếu không có) */
    private List<String> canhBao;
}
