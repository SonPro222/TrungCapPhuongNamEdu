package org.example.trungcapphuongnam.module.giangDay.repository;

import org.example.trungcapphuongnam.module.giangDay.entity.GiaoVienKhaDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GiaoVienKhaDungRepository extends JpaRepository<GiaoVienKhaDung, Long> {

    /**
     * Trả về true nếu giáo viên KHÔNG khả dụng cho slot đó.
     * Không khả dụng khi:
     * - loaiDangKy = 'ban' (mới), HOẶC
     * - loaiDangKy IS NULL và (khaDung IS NULL OR khaDung = false) (logic cũ)
     */
    @Query("""
        SELECT COUNT(gvkd) > 0
        FROM GiaoVienKhaDung gvkd
        WHERE gvkd.giaoVienId = :giaoVienId
          AND (gvkd.thuTrongTuan IS NULL OR gvkd.thuTrongTuan = :thuTrongTuan)
          AND (gvkd.caHocId IS NULL OR gvkd.caHocId = :caHocId)
          AND (gvkd.tuNgay IS NULL OR gvkd.tuNgay <= :ngayHoc)
          AND (gvkd.denNgay IS NULL OR gvkd.denNgay >= :ngayHoc)
          AND (
            (gvkd.loaiDangKy IS NOT NULL
                AND gvkd.loaiDangKy = org.example.trungcapphuongnam.module.giangDay.enums.LoaiDangKyGiangVien.ban)
            OR
            (gvkd.loaiDangKy IS NULL
                AND (gvkd.khaDung IS NULL OR gvkd.khaDung = false))
          )
    """)
    boolean existsGiaoVienKhongKhaDung(
            @Param("giaoVienId") Long giaoVienId,
            @Param("thuTrongTuan") Integer thuTrongTuan,
            @Param("caHocId") Long caHocId,
            @Param("ngayHoc") LocalDate ngayHoc
    );

    /**
     * Trả về true nếu giáo viên có đăng ký BÁN (loaiDangKy = ban) cho slot đó.
     * Dùng trong LichHocValidator để block xếp lịch thủ công.
     */
    @Query("""
        SELECT COUNT(gvkd) > 0
        FROM GiaoVienKhaDung gvkd
        WHERE gvkd.giaoVienId = :giaoVienId
          AND gvkd.loaiDangKy = org.example.trungcapphuongnam.module.giangDay.enums.LoaiDangKyGiangVien.ban
          AND (gvkd.thuTrongTuan IS NULL OR gvkd.thuTrongTuan = :thuTrongTuan)
          AND (gvkd.caHocId IS NULL OR gvkd.caHocId = :caHocId)
          AND (gvkd.tuNgay IS NULL OR gvkd.tuNgay <= :ngayHoc)
          AND (gvkd.denNgay IS NULL OR gvkd.denNgay >= :ngayHoc)
    """)
    boolean existsGiaoVienBan(
            @Param("giaoVienId") Long giaoVienId,
            @Param("thuTrongTuan") Integer thuTrongTuan,
            @Param("caHocId") Long caHocId,
            @Param("ngayHoc") LocalDate ngayHoc
    );

    /**
     * Tìm entry có loaiDangKy = uu_tien hoặc kha_dung cho giáo viên + ca học, dùng để tính điểm ưu tiên.
     * Sắp xếp theo loaiDangKy (uu_tien trước) và mucDoUuTien giảm dần.
     */
    @Query("""
        SELECT gvkd
        FROM GiaoVienKhaDung gvkd
        WHERE gvkd.giaoVienId = :giaoVienId
          AND gvkd.loaiDangKy IS NOT NULL
          AND gvkd.loaiDangKy != org.example.trungcapphuongnam.module.giangDay.enums.LoaiDangKyGiangVien.ban
          AND (gvkd.caHocId IS NULL OR gvkd.caHocId = :caHocId)
        ORDER BY gvkd.loaiDangKy ASC, gvkd.mucDoUuTien DESC NULLS LAST
    """)
    List<GiaoVienKhaDung> findBestUuTienEntry(
            @Param("giaoVienId") Long giaoVienId,
            @Param("caHocId") Long caHocId
    );
}
