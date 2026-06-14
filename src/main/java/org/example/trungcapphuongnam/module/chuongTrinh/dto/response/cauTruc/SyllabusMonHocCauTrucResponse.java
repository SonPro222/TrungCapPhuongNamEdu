package org.example.trungcapphuongnam.module.chuongTrinh.dto.response.cauTruc;

import lombok.*;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.*;
import org.example.trungcapphuongnam.module.diem.dto.response.CauHinhDanhGiaResponse;
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

    /** Cột điểm thật của syllabus_mon_hoc áp dụng, không phải cấu hình đánh giá mẫu của syllabus gốc. */
    private List<CauHinhDanhGiaResponse> cauHinhDanhGia;

    /** Quy đổi điểm thật gắn trực tiếp với syllabus_mon_hoc áp dụng. */
    private List<QuyDoiDiemResponse> quyDoiDiem;

    /** Các mẫu quy đổi kết quả đã được gán cho syllabus_mon_hoc áp dụng trong chương trình. */
    private List<ChuongTrinhMonQuyDoiDiemMauResponse> quyDoiDiemTheoChuongTrinh;

    /** File gốc đã được gán vào syllabus môn học áp dụng. */
    private List<SyllabusMonHocFileResponse> fileSyllabus;
}
