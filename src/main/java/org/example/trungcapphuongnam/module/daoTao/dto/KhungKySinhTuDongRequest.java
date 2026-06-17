package org.example.trungcapphuongnam.module.daoTao.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhungKySinhTuDongRequest {

    /**
     * true: cập nhật lại cả các kỳ đã tồn tại theo cấu hình nghỉ chuyển kỳ mới.
     * false/null: chỉ tạo các kỳ còn thiếu, giữ nguyên kỳ đã có.
     */
    private Boolean ghiDeKyDaCo;

    /**
     * Cấu hình chi tiết: kỳ 1 -> kỳ 2 nghỉ bao nhiêu ngày, kỳ 2 -> kỳ 3 nghỉ bao nhiêu ngày...
     */
    private List<ChuongTrinhVersionNghiChuyenKyRequest> danhSachNghiChuyenKy;

    /**
     * Dạng rút gọn cho FE: hệ 3 kỳ gửi [7, 7], hệ 4 kỳ gửi [7, 5, 10].
     */
    private List<Integer> soNgayNghiChuyenKySau;
}
