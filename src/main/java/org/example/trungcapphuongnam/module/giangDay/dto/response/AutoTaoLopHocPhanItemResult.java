package org.example.trungcapphuongnam.module.giangDay.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class AutoTaoLopHocPhanItemResult {

    /** ID lớp học phần (null nếu dryRun) */
    private Long lopHocPhanId;

    private String maLop;
    private String tenLop;

    /** Số sinh viên dự kiến phân vào lớp này (theo phân phối) */
    private Integer siSoDuKien;

    /** Số sinh viên đã thực sự phân bổ (sau khi trừ SV đã học rồi) */
    private Integer siSoDaPhanBo;

    private Integer siSoToiThieu;
    private Integer siSoToiDa;

    private Long chuongTrinhMonId;
    private Long syllabusMonHocId;

    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;

    /** Trạng thái: "du_kien" hoặc "dang_mo" */
    private String trangThai;
}
