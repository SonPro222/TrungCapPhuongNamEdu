package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.cauTruc.ChuongTrinhCauTrucResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.cauTruc.SyllabusMonHocCauTrucResponse;

public interface ChuongTrinhCauTrucService {

    ChuongTrinhCauTrucResponse findCauTrucByVersionId(Long chuongTrinhVersionId);

    ChuongTrinhCauTrucResponse findCauTrucByVersionId(
            Long chuongTrinhVersionId,
            Long khungKyId,
            boolean includeSyllabusDetail
    );

    SyllabusMonHocCauTrucResponse findSyllabusChiTietById(Long syllabusMonHocId);
}
