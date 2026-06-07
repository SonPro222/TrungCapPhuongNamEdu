package org.example.trungcapphuongnam.module.chuongTrinh.repository;


import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocGocChuongBai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SyllabusMonHocGocChuongBaiRepository
        extends JpaRepository<SyllabusMonHocGocChuongBai, Long>, JpaSpecificationExecutor<SyllabusMonHocGocChuongBai> {

    boolean existsBySyllabusMonHocGocIdAndThuTu(Long syllabusMonHocGocId, Integer thuTu);

    boolean existsBySyllabusMonHocGocIdAndThuTuAndIdNot(Long syllabusMonHocGocId, Integer thuTu, Long id);

    void deleteBySyllabusMonHocGocId(@Param("syllabusMonHocGocId") Long syllabusMonHocGocId);

    @Query("select coalesce(sum(cb.tongGio), 0) from SyllabusMonHocGocChuongBai cb where cb.syllabusMonHocGocId = :syllabusMonHocGocId")
    java.math.BigDecimal tongGioTheoSyllabusMonHocGocId(@Param("syllabusMonHocGocId") Long syllabusMonHocGocId);

    @Query("select coalesce(sum(cb.gioLyThuyet), 0) from SyllabusMonHocGocChuongBai cb where cb.syllabusMonHocGocId = :syllabusMonHocGocId")
    java.math.BigDecimal tongGioLyThuyetTheoSyllabusMonHocGocId(@Param("syllabusMonHocGocId") Long syllabusMonHocGocId);

    @Query("select coalesce(sum(cb.gioThucHanh), 0) from SyllabusMonHocGocChuongBai cb where cb.syllabusMonHocGocId = :syllabusMonHocGocId")
    java.math.BigDecimal tongGioThucHanhTheoSyllabusMonHocGocId(@Param("syllabusMonHocGocId") Long syllabusMonHocGocId);

    @Query("select coalesce(sum(cb.gioKiemTra), 0) from SyllabusMonHocGocChuongBai cb where cb.syllabusMonHocGocId = :syllabusMonHocGocId")
    java.math.BigDecimal tongGioKiemTraTheoSyllabusMonHocGocId(@Param("syllabusMonHocGocId") Long syllabusMonHocGocId);
}