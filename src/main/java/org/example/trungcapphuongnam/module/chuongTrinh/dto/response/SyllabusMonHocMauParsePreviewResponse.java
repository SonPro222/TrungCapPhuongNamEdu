package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusMonHocMauParsePreviewResponse {

    /** Thông tin chung của syllabus được parse ra (có thể null nếu chưa parse được) */
    private SyllabusMonHocMauResponse syllabusMonHocMau;

    private List<SyllabusMonHocMauChuongBaiResponse> chuongBaiList;

    private List<SyllabusMonHocMauDieuKienResponse> dieuKienList;

    private List<SyllabusMonHocMauTaiLieuResponse> taiLieuList;

    private List<CauHinhDanhGiaMauResponse> cauHinhDanhGiaList;

    private List<QuyDoiDiemMauResponse> quyDoiDiemList;

    /** Nội dung text thô bóc từ file - để FE hiển thị hoặc người dùng tra cứu */
    private String noiDungText;

    /** Danh sách cảnh báo parse: trường không đọc được, dữ liệu thiếu, v.v. */
    private List<String> canhBao;
}
