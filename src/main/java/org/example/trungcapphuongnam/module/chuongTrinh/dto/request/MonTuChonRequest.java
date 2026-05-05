package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;
import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonTuChonRequest {

    @NotNull(message = "nhom_id không được để trống")
    private Long nhomId;

    @NotNull(message = "chuong_trinh_mon_id không được để trống")
    private Long chuongTrinhMonId;

}
