package org.example.trungcapphuongnam.module.giangDay.repository;

import org.example.trungcapphuongnam.module.giangDay.entity.NgayNghi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NgayNghiRepository extends JpaRepository<NgayNghi, Long> {

    boolean existsByNgay(LocalDate ngay);

    /**
     * Kiểm tra ngày nghỉ toàn trường (không phân biệt version/kỳ).
     * Giữ lại để tương thích các nơi chưa có context lớp học phần.
     */
    @Query("""
            SELECT CASE WHEN COUNT(n) > 0 THEN true ELSE false END
            FROM NgayNghi n
            WHERE (n.trangThai IS NULL OR n.trangThai = true)
              AND (n.tinhLaNgayKhongHoc IS NULL OR n.tinhLaNgayKhongHoc = true)
              AND :ngay BETWEEN COALESCE(n.ngayBatDau, n.ngay) AND COALESCE(n.ngayKetThuc, n.ngay)
            """)
    boolean existsNgayNghiApDung(@Param("ngay") LocalDate ngay);

    @Query("""
            SELECT n
            FROM NgayNghi n
            WHERE (n.trangThai IS NULL OR n.trangThai = true)
              AND (n.tinhLaNgayKhongHoc IS NULL OR n.tinhLaNgayKhongHoc = true)
              AND COALESCE(n.ngayBatDau, n.ngay) <= :denNgay
              AND COALESCE(n.ngayKetThuc, n.ngay) >= :tuNgay
            ORDER BY COALESCE(n.ngayBatDau, n.ngay) ASC, n.id ASC
            """)
    List<NgayNghi> findNgayNghiApDungTrongKhoang(
            @Param("tuNgay") LocalDate tuNgay,
            @Param("denNgay") LocalDate denNgay
    );

    /**
     * Kiểm tra ngày nghỉ có áp dụng cho lớp học phần thuộc version/kỳ cụ thể không.
     * Phạm vi áp dụng theo thứ tự ưu tiên:
     *   1. Toàn trường: chuong_trinh_version_id IS NULL AND khung_ky_id IS NULL
     *   2. Theo version: chuong_trinh_version_id = :chuongTrinhVersionId AND khung_ky_id IS NULL
     *   3. Theo kỳ: chuong_trinh_version_id = :chuongTrinhVersionId AND khung_ky_id = :khungKyId
     *
     * Nếu chuongTrinhVersionId = null → chỉ kiểm tra nghỉ toàn trường.
     * Nếu khungKyId = null → kiểm tra toàn trường + theo version.
     */
    @Query(value = """
            SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END
            FROM ngay_nghi n
            WHERE COALESCE(n.ngay_bat_dau, n.ngay) <= :ngay
              AND COALESCE(n.ngay_ket_thuc, n.ngay) >= :ngay
              AND COALESCE(n.tinh_la_ngay_khong_hoc, true) = true
              AND (n.trang_thai IS NULL OR n.trang_thai = true)
              AND (
                (n.chuong_trinh_version_id IS NULL AND n.khung_ky_id IS NULL)
                OR (:chuongTrinhVersionId IS NOT NULL
                    AND n.chuong_trinh_version_id = :chuongTrinhVersionId
                    AND n.khung_ky_id IS NULL)
                OR (:chuongTrinhVersionId IS NOT NULL
                    AND :khungKyId IS NOT NULL
                    AND n.chuong_trinh_version_id = :chuongTrinhVersionId
                    AND n.khung_ky_id = :khungKyId)
              )
            """, nativeQuery = true)
    boolean existsNgayNghiApDungTheoPhamVi(
            @Param("ngay") LocalDate ngay,
            @Param("chuongTrinhVersionId") Long chuongTrinhVersionId,
            @Param("khungKyId") Long khungKyId
    );
}
