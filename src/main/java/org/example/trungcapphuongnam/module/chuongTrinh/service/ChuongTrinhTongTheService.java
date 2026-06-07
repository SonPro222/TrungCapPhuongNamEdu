package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.tongThe.ChuongTrinhTongTheResponse;

public interface ChuongTrinhTongTheService {

    ChuongTrinhTongTheResponse findTongTheByChuongTrinhId(Long chuongTrinhId, Long versionId);

    ChuongTrinhTongTheResponse findTongTheByChuongTrinhId(
            Long chuongTrinhId,
            Long versionId,
            Long khungKyId,
            Boolean includeSyllabusDetail
    );
}
