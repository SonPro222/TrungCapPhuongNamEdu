package org.example.trungcapphuongnam.module.giangDay.dto.response;

import lombok.*;
import org.example.trungcapphuongnam.common.enums.LoaiPhong;
import org.example.trungcapphuongnam.common.enums.TrangThaiPhongHoc;

import java.time.*;
import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhongHocResponse {
    private Long id;
    private String maPhong;
    private String tenPhong;
    private LoaiPhong loaiPhong;
    private Integer sucChua;
    private String diaDiem;
    private TrangThaiPhongHoc trangThai;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
