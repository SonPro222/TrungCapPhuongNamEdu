package org.example.trungcapphuongnam.module.giangDay.service.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMonQuyDoiDiemMau;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.QuyDoiDiemMau;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhMonQuyDoiDiemMauRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.QuyDoiDiemMauRepository;
import org.example.trungcapphuongnam.module.diem.entity.CauHinhDanhGia;
import org.example.trungcapphuongnam.module.diem.repository.CauHinhDanhGiaRepository;
import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.mapper.CauHinhDanhGiaGiangDayMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.LopHocPhanRepository;
import org.example.trungcapphuongnam.module.giangDay.service.SaoChepCotDiemTuChuongTrinhMonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SaoChepCotDiemTuChuongTrinhMonServiceImpl implements SaoChepCotDiemTuChuongTrinhMonService {

    private final ChuongTrinhMonQuyDoiDiemMauRepository chuongTrinhMonQuyDoiDiemMauRepository;
    private final QuyDoiDiemMauRepository quyDoiDiemMauRepository;
    private final CauHinhDanhGiaRepository cauHinhDanhGiaRepository;
    private final LopHocPhanRepository lopHocPhanRepository;
    private final CauHinhDanhGiaGiangDayMapper cauHinhDanhGiaMapper;
    private final EntityManager entityManager;

    @Override
    public void saoChep(Long lopHocPhanId, Long chuongTrinhMonId) {
        if (lopHocPhanId == null) {
            throw new BadRequestException("Lớp học phần không hợp lệ");
        }

        if (chuongTrinhMonId == null) {
            throw new BadRequestException("Chương trình môn không hợp lệ");
        }

        Long syllabusMonHocId = resolveSyllabusMonHocId(lopHocPhanId, chuongTrinhMonId);

        List<Long> mauIds = chuongTrinhMonQuyDoiDiemMauRepository
                .findByChuongTrinhMonId(chuongTrinhMonId)
                .stream()
                .map(ChuongTrinhMonQuyDoiDiemMau::getQuyDoiDiemMauId)
                .toList();

        List<QuyDoiDiemMau> cotDiemMau = quyDoiDiemMauRepository.findAllById(mauIds)
                .stream()
                .filter(item -> "COT_DIEM".equalsIgnoreCase(item.getLoaiMau()))
                .sorted(Comparator
                        .comparing(QuyDoiDiemMau::getThuTu, Comparator.nullsLast(Integer::compareTo))
                        .thenComparing(QuyDoiDiemMau::getId))
                .toList();

        if (cotDiemMau.isEmpty()) {
            throw new BadRequestException("Chương trình môn chưa gán mẫu cột điểm");
        }

        BigDecimal tongTyLe = cotDiemMau.stream()
                .map(item -> item.getTyLe() == null ? BigDecimal.ZERO : item.getTyLe())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (tongTyLe.compareTo(new BigDecimal("100")) != 0) {
            throw new BadRequestException("Tổng tỷ lệ cột điểm của chương trình môn phải bằng 100%");
        }

        for (QuyDoiDiemMau mau : cotDiemMau) {
            if (tonTaiCauHinhDanhGia(syllabusMonHocId, mau.getTen())) {
                continue;
            }

            CauHinhDanhGia cauHinh = cauHinhDanhGiaMapper.toEntityFromQuyDoiDiemMau(
                    syllabusMonHocId,
                    mau,
                    chuanHoaLoaiDiem(mau.getLoaiMau())
            );

            cauHinhDanhGiaRepository.save(cauHinh);
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
            throw new BadRequestException("Chương trình môn chưa có syllabus môn học");
        }

        return ((Number) result.get(0)).longValue();
    }

    private boolean tonTaiCauHinhDanhGia(Long syllabusMonHocId, String tenCotDiem) {
        Number count = (Number) entityManager.createNativeQuery("""
                        SELECT COUNT(1)
                        FROM cau_hinh_danh_gia chdg
                        WHERE chdg.syllabus_mon_hoc_id = :syllabusMonHocId
                          AND LOWER(TRIM(chdg.ten_cot_diem)) = LOWER(TRIM(:tenCotDiem))
                        """)
                .setParameter("syllabusMonHocId", syllabusMonHocId)
                .setParameter("tenCotDiem", tenCotDiem)
                .getSingleResult();

        return count.longValue() > 0;
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
