package org.example.trungcapphuongnam.module.daoTao.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NganhHeDaoTaoResponse {

    private Long id;

    private Long nganhId;
    private String maNganh;
    private String tenNganh;

    private Long trinhDoId;
    private String maTrinhDo;
    private String tenTrinhDo;

    private Long loaiChuongTrinhId;
    private String maLoai;
    private String tenLoai;

    private String maHe;
    private String tenHe;
    private Integer soThang;
    private Integer soKy;
    private String trangThai;
    private String ghiChu;

    /** Label cho FE dropdown: "tenNganh - tenTrinhDo - tenLoai - soThang tháng - soKy kỳ" */
    private String label;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
