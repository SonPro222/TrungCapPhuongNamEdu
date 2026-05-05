package org.example.trungcapphuongnam.module.giangDay.service;

import org.example.trungcapphuongnam.module.giangDay.dto.request.YeuCauDoiLichRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.YeuCauDoiLichResponse;

import java.util.List;

public interface YeuCauDoiLichService {
    List<YeuCauDoiLichResponse> getAll();
    YeuCauDoiLichResponse getById(Long id);
    YeuCauDoiLichResponse create(YeuCauDoiLichRequest request);
    YeuCauDoiLichResponse update(Long id, YeuCauDoiLichRequest request);
    void delete(Long id);
}
