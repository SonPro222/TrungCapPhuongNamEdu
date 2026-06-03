package org.example.trungcapphuongnam.module.lms.dto.request.nghiepvu;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NopBaiRequest {

    @Valid
    @Builder.Default
    private List<CauTraLoiRequest> cauTraLois = new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CauTraLoiRequest {
        /**
         * ID snapshot câu hỏi trong lần làm bài: lan_lam_bai_cau_hoi.id.
         * Đây là field chuẩn theo DB mới.
         */
        private Long lanLamBaiCauHoiId;

        /**
         * Cho phép FE cũ gửi cauHoiVersionId để BE tự map sang snapshot nếu cần.
         */
        private Long cauHoiVersionId;

        private String noiDungTraLoi;

        /**
         * Danh sách ID snapshot đáp án: lan_lam_bai_dap_an.id.
         */
        @Builder.Default
        private List<Long> lanLamBaiDapAnIds = new ArrayList<>();
    }
}
