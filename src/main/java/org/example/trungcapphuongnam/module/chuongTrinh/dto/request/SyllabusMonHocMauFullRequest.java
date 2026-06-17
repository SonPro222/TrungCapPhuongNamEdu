package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusMonHocMauFullRequest {

    @NotNull(message = "syllabusMonHocMau không được null")
    @Valid
    private SyllabusMonHocMauRequest syllabusMonHocMau;

    private List<SyllabusMonHocMauChuongBaiRequest> chuongBaiList;

    private List<SyllabusMonHocMauDieuKienRequest> dieuKienList;

    private List<SyllabusMonHocMauTaiLieuRequest> taiLieuList;

    private List<CauHinhDanhGiaMauRequest> cauHinhDanhGiaList;

    private List<QuyDoiDiemMauRequest> quyDoiDiemList;
}
