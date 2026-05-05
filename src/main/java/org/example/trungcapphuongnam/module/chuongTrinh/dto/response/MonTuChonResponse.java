package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonTuChonResponse {

    private Long id;

    private Long nhomId;

    private Long chuongTrinhMonId;

}
