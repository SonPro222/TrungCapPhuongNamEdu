package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusMonHocParsePreviewResponse {

    /** Thông tin chung syllabus môn học trong chương trình parse ra */
    private SyllabusMonHocResponse syllabusMonHoc;

    private List<SyllabusChuongBaiResponse> chuongBaiList;

    private List<SyllabusMonHocDieuKienResponse> dieuKienList;

    private List<SyllabusMonHocTaiLieuResponse> taiLieuList;

    /** Nội dung text thô bóc từ file */
    private String noiDungText;

    /** Danh sách cảnh báo parse */
    private List<String> canhBao;
}
