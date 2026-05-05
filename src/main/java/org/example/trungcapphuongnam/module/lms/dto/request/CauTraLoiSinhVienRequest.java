package org.example.trungcapphuongnam.module.lms.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CauTraLoiSinhVienRequest {

    @NotNull

    private Long lanLamBaiId;

    @NotNull

    private Long cauHoiId;

    private String noiDungTraLoi;

    @DecimalMin(value = "0")

    private BigDecimal diem;

    private Boolean laDung;

    private String nhanXet;

}
