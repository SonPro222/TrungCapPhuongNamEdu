package org.example.trungcapphuongnam.module.giangDay.dto.request;

import lombok.*;
import org.example.trungcapphuongnam.module.giangDay.enums.LoaiPhong;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiPhongHoc;


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
