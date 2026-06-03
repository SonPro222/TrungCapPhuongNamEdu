package org.example.trungcapphuongnam.module.giangDay.repository;

import org.example.trungcapphuongnam.module.giangDay.entity.GiaoVienKhaDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface GiaoVienKhaDungRepository extends JpaRepository<GiaoVienKhaDung, Long> {

    @Query("""
        SELECT COUNT(gvkd) > 0
        FROM GiaoVienKhaDung gvkd
        WHERE gvkd.giaoVienId = :giaoVienId
          AND (gvkd.khaDung IS NULL OR gvkd.khaDung = false)
          AND (gvkd.thuTrongTuan IS NULL OR gvkd.thuTrongTuan = :thuTrongTuan)
          AND (gvkd.caHocId IS NULL OR gvkd.caHocId = :caHocId)
          AND (gvkd.tuNgay IS NULL OR gvkd.tuNgay <= :ngayHoc)
          AND (gvkd.denNgay IS NULL OR gvkd.denNgay >= :ngayHoc)
    """)
    boolean existsGiaoVienKhongKhaDung(
            @Param("giaoVienId") Long giaoVienId,
            @Param("thuTrongTuan") Integer thuTrongTuan,
            @Param("caHocId") Long caHocId,
            @Param("ngayHoc") LocalDate ngayHoc
    );
}
