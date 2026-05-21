package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.cauTruc.ChuongTrinhCauTrucResponse;

public interface ChuongTrinhCauTrucService {

    ChuongTrinhCauTrucResponse findCauTrucByVersionId(Long chuongTrinhVersionId);
}