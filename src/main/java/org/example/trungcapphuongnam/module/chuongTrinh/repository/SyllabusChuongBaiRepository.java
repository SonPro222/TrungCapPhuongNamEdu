package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusChuongBai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface SyllabusChuongBaiRepository extends JpaRepository<SyllabusChuongBai, Long>, JpaSpecificationExecutor<SyllabusChuongBai> {
    Page<SyllabusChuongBai> findBySyllabusMonId(Long syllabusMonId, Pageable pageable);

    List<SyllabusChuongBai> findBySyllabusMonIdOrderByThuTuAsc(@Param("syllabusMonId") Long syllabusMonId);


    void deleteBySyllabusMonId(@Param("syllabusMonId") Long syllabusMonId);
    boolean existsBySyllabusMonIdAndThuTu(Long syllabusMonId, Integer thuTu);

    boolean existsBySyllabusMonIdAndThuTuAndIdNot(Long syllabusMonId, Integer thuTu, Long id);

    @Query("select coalesce(sum(cb.tongGio), 0) from SyllabusChuongBai cb where cb.syllabusMonId = :syllabusMonId")
    java.math.BigDecimal tongGioTheoSyllabusMonId(@Param("syllabusMonId") Long syllabusMonId);

    @Query("select coalesce(sum(cb.gioLyThuyet), 0) from SyllabusChuongBai cb where cb.syllabusMonId = :syllabusMonId")
    java.math.BigDecimal tongGioLyThuyetTheoSyllabusMonId(@Param("syllabusMonId") Long syllabusMonId);

    @Query("select coalesce(sum(cb.gioThucHanh), 0) from SyllabusChuongBai cb where cb.syllabusMonId = :syllabusMonId")
    java.math.BigDecimal tongGioThucHanhTheoSyllabusMonId(@Param("syllabusMonId") Long syllabusMonId);

    @Query("select coalesce(sum(cb.gioKiemTra), 0) from SyllabusChuongBai cb where cb.syllabusMonId = :syllabusMonId")
    java.math.BigDecimal tongGioKiemTraTheoSyllabusMonId(@Param("syllabusMonId") Long syllabusMonId);
}
