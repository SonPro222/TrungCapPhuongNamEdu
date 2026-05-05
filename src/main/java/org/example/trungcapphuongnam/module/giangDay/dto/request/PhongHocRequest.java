package org.example.trungcapphuongnam.module.giangDay.dto.request;

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
public class PhongHocRequest {
    private String maPhong;
    private String tenPhong;
    private LoaiPhong loaiPhong;
    private Integer sucChua;
    private String diaDiem;
    private TrangThaiPhongHoc trangThai;
}
