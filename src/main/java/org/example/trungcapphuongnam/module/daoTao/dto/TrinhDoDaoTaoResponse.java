package org.example.trungcapphuongnam.module.daoTao.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrinhDoDaoTaoResponse {
    private Long id;

    private String maTrinhDo;

    private String tenTrinhDo;

    private String moTa;


}
