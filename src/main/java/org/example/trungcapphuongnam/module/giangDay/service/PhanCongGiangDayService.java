package org.example.trungcapphuongnam.module.giangDay.service;

import org.example.trungcapphuongnam.module.giangDay.dto.request.PhanCongGiangDayRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.PhanCongGiangDayResponse;
import org.example.trungcapphuongnam.module.giangDay.enums.VaiTroGiangDay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PhanCongGiangDayService {

    Page<PhanCongGiangDayResponse> getAll(
            String keywordLop,
            String keywordGiaoVien,
            VaiTroGiangDay vaiTro,
            Pageable pageable
    );

    PhanCongGiangDayResponse getById(Long id);

    PhanCongGiangDayResponse create(PhanCongGiangDayRequest request);

    PhanCongGiangDayResponse update(Long id, PhanCongGiangDayRequest request);

    void delete(Long id);
}