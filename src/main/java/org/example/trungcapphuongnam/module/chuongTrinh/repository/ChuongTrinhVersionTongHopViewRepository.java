package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.view.ChuongTrinhVersionTongHopView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChuongTrinhVersionTongHopViewRepository
        extends JpaRepository<ChuongTrinhVersionTongHopView, Long> {

    /**
     * Đếm số môn ĐÃ GÁN KỲ (khung_ky_id trỏ tới khung_ky của cùng version)
     * nhưng chưa có syllabus áp dụng trong v_syllabus_mon_hoc_tong_hop.
     * Môn chưa gán kỳ (khung_ky_id = NULL) không được đếm vì không đóng góp vào tổng.
     * Luồng đúng: Version -> Kỳ -> Môn trong kỳ -> Syllabus áp dụng.
     */
    @Query(value = """
            SELECT COUNT(*)
            FROM chuong_trinh_mon ctm
            JOIN khung_ky kk
              ON kk.id = ctm.khung_ky_id
             AND kk.chuong_trinh_version_id = ctm.chuong_trinh_version_id
            WHERE ctm.chuong_trinh_version_id = :versionId
              AND NOT EXISTS (
                  SELECT 1
                  FROM v_syllabus_mon_hoc_tong_hop v
                  WHERE v.chuong_trinh_mon_id = ctm.id
              )
            """, nativeQuery = true)
    long countMonChuaCoSyllabus(@Param("versionId") Long versionId);
}