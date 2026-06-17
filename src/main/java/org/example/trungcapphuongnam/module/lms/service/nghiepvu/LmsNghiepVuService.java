package org.example.trungcapphuongnam.module.lms.service.nghiepvu;

import org.example.trungcapphuongnam.module.lms.dto.request.nghiepvu.NopBaiRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.KetQuaBaiTapLopResponse;
import org.example.trungcapphuongnam.module.lms.dto.response.LanLamBaiOnlineResponse;

public interface LmsNghiepVuService {
    LanLamBaiOnlineResponse batDauLamBai(Long baiTapLopId);

    LanLamBaiOnlineResponse nopBai(Long lanLamBaiId, NopBaiRequest request);

    KetQuaBaiTapLopResponse tinhLaiKetQua(Long baiTapLopId, Long sinhVienId);
}
