package org.example.trungcapphuongnam.module.daoTao.mapper;

import org.example.trungcapphuongnam.module.daoTao.dto.TrinhDoDaoTaoRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.TrinhDoDaoTaoResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.TrinhDoDaoTao;
import org.springframework.stereotype.Component;

@Component
public class TrinhDoDaoTaoMapper {

    public TrinhDoDaoTao toEntity(TrinhDoDaoTaoRequest request) {
        if (request == null) return null;
        return TrinhDoDaoTao.builder()
                .maTrinhDo(request.getMaTrinhDo())
                .tenTrinhDo(request.getTenTrinhDo())
                .moTa(request.getMoTa())
                .build();
    }

    public TrinhDoDaoTaoResponse toResponse(TrinhDoDaoTao entity) {
        if (entity == null) return null;
        return TrinhDoDaoTaoResponse.builder()
                .id(entity.getId())
                .maTrinhDo(entity.getMaTrinhDo())
                .tenTrinhDo(entity.getTenTrinhDo())
                .moTa(entity.getMoTa())
                .build();
    }

    public void updateEntity(TrinhDoDaoTao entity, TrinhDoDaoTaoRequest request) {
        if (entity == null || request == null) return;
        entity.setMaTrinhDo(request.getMaTrinhDo());
        entity.setTenTrinhDo(request.getTenTrinhDo());
        entity.setMoTa(request.getMoTa());
    }
}
