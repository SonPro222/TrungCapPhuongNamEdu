package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusChuongTrinhDongBoTuMauRequest {

    @NotNull(message = "chuongTrinhVersionId không được để trống")
    private Long chuongTrinhVersionId;

    @JsonAlias({"syllabusChuongTrinhMauId"})
    @NotNull(message = "syllabusChuongTrinhMauId không được để trống")
    private Long syllabusChuongTrinhMauId;
}
