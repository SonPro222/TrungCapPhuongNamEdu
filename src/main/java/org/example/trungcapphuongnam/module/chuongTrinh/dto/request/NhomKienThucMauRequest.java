package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhomKienThucMauRequest {

    private String ma;

    private String ten;

    private String loaiNhom;

    private String moTa;
}