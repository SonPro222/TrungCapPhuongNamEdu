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
}
