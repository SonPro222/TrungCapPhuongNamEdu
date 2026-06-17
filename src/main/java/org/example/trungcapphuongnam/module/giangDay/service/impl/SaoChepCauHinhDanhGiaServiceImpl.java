package org.example.trungcapphuongnam.module.giangDay.service.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.QuyDoiDiem;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.QuyDoiDiemRepository;
import org.example.trungcapphuongnam.module.diem.entity.CauHinhDanhGia;
import org.example.trungcapphuongnam.module.diem.repository.CauHinhDanhGiaRepository;
import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.mapper.CauHinhDanhGiaGiangDayMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.LopHocPhanRepository;
import org.example.trungcapphuongnam.module.giangDay.service.SaoChepCauHinhDanhGiaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SaoChepCauHinhDanhGiaServiceImpl implements SaoChepCauHinhDanhGiaService {

    private static final BigDecimal MOT_TRAM = new BigDecimal("100.00");

    private final CauHinhDanhGiaRepository cauHinhDanhGiaRepository;
    private final QuyDoiDiemRepository quyDoiDiemRepository;
    private final LopHocPhanRepository lopHocPhanRepository;
    private final CauHinhDanhGiaGiangDayMapper cauHinhDanhGiaMapper;
    private final EntityManager entityManager;

    @Override
    public void saoChepTuChuongTrinhMonSangLopHocPhan(Long lopHocPhanId, Long chuongTrinhMonId) {
        if (lopHocPhanId == null) {
            throw new BadRequestException("Lớp học phần không hợp lệ");
        }

        if (chuongTrinhMonId == null) {
            throw new BadRequestException("Chương trình môn không hợp lệ");
        }

        Long syllabusMonHocId = resolveSyllabusMonHocId(lopHocPhanId, chuongTrinhMonId);

        List<QuyDoiDiem> danhSachQuyDoiDiem = quyDoiDiemRepository
                .findBySyllabusMonHocIdOrderByThuTuAscIdAsc(syllabusMonHocId);

        if (danhSachQuyDoiDiem.isEmpty()) {
            throw new BadRequestException(
                    "Syllabus môn học áp dụng chưa có Quy đổi điểm. " +
                            "Cần khai báo bảng Quy đổi điểm cho syllabus môn học áp dụng trước khi tạo lớp học phần."
            );
        }

        kiemTraQuyDoiDiemDaLuu(danhSachQuyDoiDiem);

        for (QuyDoiDiem quyDoiDiem : danhSachQuyDoiDiem) {
            String tenCotDiem = layTenCotDiemTuQuyDoiDiem(quyDoiDiem);

            if (tonTaiCauHinhDanhGia(syllabusMonHocId, tenCotDiem)) {
                continue;
            }

            CauHinhDanhGia cauHinhDanhGia = cauHinhDanhGiaMapper.toEntityFromQuyDoiDiem(
                    syllabusMonHocId,
                    quyDoiDiem,
                    tenCotDiem,
                    chuanHoaLoaiDiem(quyDoiDiem.getLoaiMau())
            );

            cauHinhDanhGiaRepository.save(cauHinhDanhGia);
        }
    }

    private Long resolveSyllabusMonHocId(Long lopHocPhanId, Long chuongTrinhMonIdRequest) {
        LopHocPhan lopHocPhan = lopHocPhanRepository.findById(lopHocPhanId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy lớp học phần"));

        Long chuongTrinhMonId = lopHocPhan.getChuongTrinhMonId() == null
                ? chuongTrinhMonIdRequest
                : lopHocPhan.getChuongTrinhMonId();

        if (chuongTrinhMonId == null) {
            throw new BadRequestException("Lớp học phần chưa gắn chương trình môn");
        }

        if (chuongTrinhMonIdRequest != null && !chuongTrinhMonIdRequest.equals(chuongTrinhMonId)) {
            throw new BadRequestException("Chương trình môn truyền lên không khớp với lớp học phần");
        }

        List<?> result = entityManager.createNativeQuery("""
                        SELECT smh.id
                        FROM syllabus_mon_hoc smh
                        WHERE smh.chuong_trinh_mon_id = :chuongTrinhMonId
                        ORDER BY smh.id
                        LIMIT 1
                        """)
                .setParameter("chuongTrinhMonId", chuongTrinhMonId)
                .getResultList();

        if (result.isEmpty()) {
            throw new BadRequestException("Chương trình môn chưa có syllabus môn học áp dụng");
        }

        return ((Number) result.get(0)).longValue();
    }

    private boolean tonTaiCauHinhDanhGia(Long syllabusMonHocId, String tenCotDiem) {
        if (syllabusMonHocId == null || tenCotDiem == null || tenCotDiem.isBlank()) {
            return false;
        }

        return cauHinhDanhGiaRepository.countBySyllabusMonHocIdAndTenCotDiemIgnoreTrim(
                syllabusMonHocId,
                tenCotDiem
        ) > 0;
    }

    private void kiemTraQuyDoiDiemDaLuu(List<QuyDoiDiem> danhSachQuyDoiDiem) {
        BigDecimal tongTyLe = BigDecimal.ZERO;

        for (QuyDoiDiem quyDoiDiem : danhSachQuyDoiDiem) {
            String tenCotDiem = layTenCotDiemTuQuyDoiDiem(quyDoiDiem);
            if (quyDoiDiem.getDiemToiDa() != null) {
                if (quyDoiDiem.getDiemToiDa().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new BadRequestException("Điểm tối đa của Quy đổi điểm '" + tenCotDiem + "' phải lớn hơn 0.");
                }

                if (quyDoiDiem.getDiemToiDa().compareTo(BigDecimal.TEN) > 0) {
                    throw new BadRequestException("Điểm tối đa của Quy đổi điểm '" + tenCotDiem + "' không được lớn hơn 10.");
                }
            }

        }

    }

    private String layTenCotDiemTuQuyDoiDiem(QuyDoiDiem quyDoiDiem) {
        if (quyDoiDiem.getTen() != null && !quyDoiDiem.getTen().isBlank()) {
            return quyDoiDiem.getTen().trim();
        }

        if (quyDoiDiem.getMa() != null && !quyDoiDiem.getMa().isBlank()) {
            return quyDoiDiem.getMa().trim();
        }

        if (quyDoiDiem.getGhiChu() != null && !quyDoiDiem.getGhiChu().isBlank()) {
            return quyDoiDiem.getGhiChu().trim();
        }

        throw new BadRequestException("Quy đổi điểm phải có tên cột điểm.");
    }

    private String chuanHoaLoaiDiem(String loaiDiem) {
        if (loaiDiem == null || loaiDiem.isBlank()) {
            return "khac";
        }

        String value = loaiDiem.trim().toLowerCase();

        return switch (value) {
            case "chuyen_can", "bai_tap", "bai_tap_online", "kiem_tra_online", "kiem_tra_tren_lop",
                 "giua_ky", "cuoi_ky", "thuc_hanh", "do_an", "khac" -> value;
            default -> "khac";
        };
    }
}