package org.example.trungcapphuongnam.module.daoTao.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhVersionNghiChuyenKyBulkRequest {

    private List<ChuongTrinhVersionNghiChuyenKyRequest> danhSachNghiChuyenKy;
}
