package org.example.trungcapphuongnam.module.diem.repository;

import org.example.trungcapphuongnam.module.diem.entity.CauHinhDanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CauHinhDanhGiaRepository extends JpaRepository<CauHinhDanhGia, Long> {

    boolean existsBySyllabusMonHocIdAndTenCotDiem(Long syllabusMonHocId, String tenCotDiem);

    boolean existsBySyllabusMonHocIdAndTenCotDiemAndIdNot(Long syllabusMonHocId, String tenCotDiem, Long id);

    boolean existsBySyllabusMonHocIdAndThuTu(Long syllabusMonHocId, Integer thuTu);

    boolean existsBySyllabusMonHocIdAndThuTuAndIdNot(Long syllabusMonHocId, Integer thuTu, Long id);

    List<CauHinhDanhGia> findBySyllabusMonHocIdOrderByThuTuAscIdAsc(Long syllabusMonHocId);

    @Query("""
            SELECT COUNT(c) FROM CauHinhDanhGia c
            WHERE c.syllabusMonHocId = :syllabusMonHocId
              AND LOWER(TRIM(c.tenCotDiem)) = LOWER(TRIM(:tenCotDiem))
            """)
    long countBySyllabusMonHocIdAndTenCotDiemIgnoreTrim(
            @Param("syllabusMonHocId") Long syllabusMonHocId,
            @Param("tenCotDiem") String tenCotDiem
    );


    @Query("""
            SELECT COALESCE(SUM(c.tyLe), 0)
            FROM CauHinhDanhGia c
            WHERE c.syllabusMonHocId = :syllabusMonHocId
              AND (:excludeId IS NULL OR c.id <> :excludeId)
            """)
    java.math.BigDecimal sumTyLeBySyllabusMonHocIdExcludeId(
            @Param("syllabusMonHocId") Long syllabusMonHocId,
            @Param("excludeId") Long excludeId
    );

    void deleteBySyllabusMonHocId(Long syllabusMonHocId);
}
