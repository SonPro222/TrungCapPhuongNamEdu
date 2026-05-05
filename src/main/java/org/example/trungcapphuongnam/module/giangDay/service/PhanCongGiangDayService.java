package org.example.trungcapphuongnam.module.giangDay.service;

import org.example.trungcapphuongnam.module.giangDay.dto.request.PhanCongGiangDayRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.PhanCongGiangDayResponse;

import java.util.List;

public interface PhanCongGiangDayService {
    List<PhanCongGiangDayResponse> getAll();
    PhanCongGiangDayResponse getById(Long id);
    PhanCongGiangDayResponse create(PhanCongGiangDayRequest request);
    PhanCongGiangDayResponse update(Long id, PhanCongGiangDayRequest request);
    void delete(Long id);
}
