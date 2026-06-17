package org.example.trungcapphuongnam.module.chuongTrinh.repository;


import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocMauChuongBai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SyllabusMonHocMauChuongBaiRepository
        extends JpaRepository<SyllabusMonHocMauChuongBai, Long>, JpaSpecificationExecutor<SyllabusMonHocMauChuongBai> {

    boolean existsBySyllabusMonHocMauIdAndThuTu(Long syllabusMonHocMauId, Integer thuTu);

    boolean existsBySyllabusMonHocMauIdAndThuTuAndIdNot(Long syllabusMonHocMauId, Integer thuTu, Long id);

    java.util.List<SyllabusMonHocMauChuongBai> findBySyllabusMonHocMauIdOrderByThuTuAscIdAsc(Long syllabusMonHocMauId);

    void deleteBySyllabusMonHocMauId(@Param("syllabusMonHocMauId") Long syllabusMonHocMauId);

    @Query("select coalesce(sum(cb.tongGio), 0) from SyllabusMonHocMauChuongBai cb where cb.syllabusMonHocMauId = :syllabusMonHocMauId")
    java.math.BigDecimal tongGioTheoSyllabusMonHocMauId(@Param("syllabusMonHocMauId") Long syllabusMonHocMauId);

    @Query("select coalesce(sum(cb.gioLyThuyet), 0) from SyllabusMonHocMauChuongBai cb where cb.syllabusMonHocMauId = :syllabusMonHocMauId")
    java.math.BigDecimal tongGioLyThuyetTheoSyllabusMonHocMauId(@Param("syllabusMonHocMauId") Long syllabusMonHocMauId);

    @Query("select coalesce(sum(cb.gioThucHanh), 0) from SyllabusMonHocMauChuongBai cb where cb.syllabusMonHocMauId = :syllabusMonHocMauId")
    java.math.BigDecimal tongGioThucHanhTheoSyllabusMonHocMauId(@Param("syllabusMonHocMauId") Long syllabusMonHocMauId);

    @Query("select coalesce(sum(cb.gioKiemTra), 0) from SyllabusMonHocMauChuongBai cb where cb.syllabusMonHocMauId = :syllabusMonHocMauId")
    java.math.BigDecimal tongGioKiemTraTheoSyllabusMonHocMauId(@Param("syllabusMonHocMauId") Long syllabusMonHocMauId);
}