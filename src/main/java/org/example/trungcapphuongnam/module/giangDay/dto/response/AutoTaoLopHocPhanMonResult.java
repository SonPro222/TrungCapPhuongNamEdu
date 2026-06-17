package org.example.trungcapphuongnam.module.giangDay.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AutoTaoLopHocPhanMonResult {

    private Long chuongTrinhMonId;
    private Long monHocId;
    private String maMon;
    private String tenMon;

    private Long syllabusMonHocId;
    private Integer soBuoiHoc;

    /** Tổng sinh viên cần học môn này trong kỳ */
    private Integer tongSinhVien;

    /** Số lớp học phần cần tạo: k = ceil(tongSinhVien / siSoToiDa) */
    private Integer soLopCanTao;

    private List<AutoTaoLopHocPhanItemResult> lopHocPhanResults;

    /** Cảnh báo riêng cho môn này (nếu có) */
    private String canhBao;

    /** Lỗi riêng cho môn này (nếu có, môn này sẽ bị bỏ qua) */
    private String loi;
}
