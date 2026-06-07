package org.example.trungcapphuongnam.module.giangDay.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocRepository;
import org.example.trungcapphuongnam.module.giangDay.service.SaoChepCotDiemTuChuongTrinhMonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SaoChepCotDiemTuChuongTrinhMonServiceImpl implements SaoChepCotDiemTuChuongTrinhMonService {

    private final SyllabusMonHocRepository syllabusMonHocRepository;

    /**
     * Flow mới: không copy cột điểm xuống lớp học phần.
     * Cột điểm thật nằm ở cau_hinh_danh_gia theo syllabus_mon_hoc_id.
     */
    @Override
    public void saoChep(Long lopHocPhanId, Long chuongTrinhMonId) {
        if (lopHocPhanId == null) {
            throw new BadRequestException("Lớp học phần không hợp lệ");
        }

        if (chuongTrinhMonId == null) {
            throw new BadRequestException("Chương trình môn không hợp lệ");
        }

        if (!syllabusMonHocRepository.existsByChuongTrinhMonId(chuongTrinhMonId)) {
            throw new BadRequestException("Môn trong chương trình chưa có syllabus áp dụng nên chưa có cột điểm để lớp học phần tham chiếu.");
        }
    }
}
