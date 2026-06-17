package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.QuyDoiDiem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface QuyDoiDiemRepository extends JpaRepository<QuyDoiDiem, Long>, JpaSpecificationExecutor<QuyDoiDiem> {

    Page<QuyDoiDiem> findBySyllabusMonHocId(
            Long syllabusMonHocId,
            Pageable pageable
    );

    List<QuyDoiDiem> findBySyllabusMonHocIdOrderByThuTuAscIdAsc(Long syllabusMonHocId);

    void deleteBySyllabusMonHocId(Long syllabusMonHocId);

    @Query("""
        select count(q) > 0
        from QuyDoiDiem q
        where q.syllabusMonHocId = :syllabusMonHocId
          and (:id is null or q.id <> :id)
          and q.nguongTu <= :nguongDen
          and q.nguongDen >= :nguongTu
        """)
    boolean existsOverlapBySyllabusMonHocId(
            @Param("syllabusMonHocId") Long syllabusMonHocId,
            @Param("nguongTu") BigDecimal nguongTu,
            @Param("nguongDen") BigDecimal nguongDen,
            @Param("id") Long id
    );

    @Query("""
        select count(q) > 0
        from QuyDoiDiem q
        where q.syllabusMonHocId = :syllabusMonHocId
          and (:id is null or q.id <> :id)
          and lower(trim(coalesce(q.ten, q.ghiChu, ''))) = lower(trim(:tenCotDiem))
        """)
    boolean existsTenCotDiemTrongSyllabus(
            @Param("syllabusMonHocId") Long syllabusMonHocId,
            @Param("tenCotDiem") String tenCotDiem,
            @Param("id") Long id
    );


}
