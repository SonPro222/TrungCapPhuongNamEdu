package org.example.trungcapphuongnam.module.donTu.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonTuXuLyRequest {

    private Long nguoiThucHienId;

    private String ghiChu;

    private String yKienXuLy;

    private String lyDoTuChoi;
}
