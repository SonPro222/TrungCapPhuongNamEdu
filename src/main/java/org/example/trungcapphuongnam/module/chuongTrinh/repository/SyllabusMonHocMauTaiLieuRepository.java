package org.example.trungcapphuongnam.module.chuongTrinh.repository;


import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocMauTaiLieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SyllabusMonHocMauTaiLieuRepository
        extends JpaRepository<SyllabusMonHocMauTaiLieu, Long>, JpaSpecificationExecutor<SyllabusMonHocMauTaiLieu> {

    boolean existsBySyllabusMonHocMauIdAndTaiLieuMauId(Long syllabusMonHocMauId, Long taiLieuMauId);

    boolean existsBySyllabusMonHocMauIdAndTaiLieuMauIdAndIdNot(Long syllabusMonHocMauId, Long taiLieuMauId, Long id);

    boolean existsBySyllabusMonHocMauIdAndThuTu(Long syllabusMonHocMauId, Integer thuTu);

    boolean existsBySyllabusMonHocMauIdAndThuTuAndIdNot(Long syllabusMonHocMauId, Integer thuTu, Long id);

    java.util.List<SyllabusMonHocMauTaiLieu> findBySyllabusMonHocMauIdOrderByThuTuAscIdAsc(Long syllabusMonHocMauId);

    void deleteBySyllabusMonHocMauId(Long syllabusMonHocMauId);

    void deleteByTaiLieuMauId(Long taiLieuMauId);
}