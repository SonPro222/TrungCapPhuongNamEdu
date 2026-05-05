package org.example.trungcapphuongnam.module.lms.dto.response;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CauTraLoiSinhVienResponse {

    private Long id;

    private Long lanLamBaiId;

    private Long cauHoiId;

    private String noiDungTraLoi;

    private BigDecimal diem;

    private Boolean laDung;

    private String nhanXet;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
