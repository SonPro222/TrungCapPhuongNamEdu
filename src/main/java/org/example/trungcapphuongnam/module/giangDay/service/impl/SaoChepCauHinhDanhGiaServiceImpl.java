package org.example.trungcapphuongnam.module.giangDay.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocRepository;
import org.example.trungcapphuongnam.module.giangDay.service.SaoChepCauHinhDanhGiaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SaoChepCauHinhDanhGiaServiceImpl implements SaoChepCauHinhDanhGiaService {

    private final SyllabusMonHocRepository syllabusMonHocRepository;

    /**
     * Flow mới: không copy cấu hình đánh giá xuống lớp học phần nữa.
     * Lớp học phần chỉ giữ chuong_trinh_mon_id, sau đó BE resolve sang syllabus_mon_hoc
     * để lấy cột điểm trong bảng cau_hinh_danh_gia.
     */
    @Override
    public void saoChepTuChuongTrinhMonSangLopHocPhan(Long lopHocPhanId, Long chuongTrinhMonId) {
        if (lopHocPhanId == null) {
            throw new BadRequestException("Lớp học phần không hợp lệ");
        }

        if (chuongTrinhMonId == null) {
            throw new BadRequestException("Chương trình môn không hợp lệ");
        }

        if (!syllabusMonHocRepository.existsByChuongTrinhMonId(chuongTrinhMonId)) {
            throw new BadRequestException("Môn trong chương trình chưa có syllabus áp dụng nên lớp học phần chưa thể tham chiếu cột điểm.");
        }
    }
}
