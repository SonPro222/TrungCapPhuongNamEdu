package org.example.trungcapphuongnam.module.chuongTrinh.repository;


import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocMauDieuKien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SyllabusMonHocMauDieuKienRepository
        extends JpaRepository<SyllabusMonHocMauDieuKien, Long>, JpaSpecificationExecutor<SyllabusMonHocMauDieuKien> {

    boolean existsBySyllabusMonHocMauIdAndDieuKienMauId(Long syllabusMonHocMauId, Long dieuKienMauId);

    boolean existsBySyllabusMonHocMauIdAndDieuKienMauIdAndIdNot(Long syllabusMonHocMauId, Long dieuKienMauId, Long id);

    boolean existsBySyllabusMonHocMauIdAndThuTu(Long syllabusMonHocMauId, Integer thuTu);

    boolean existsBySyllabusMonHocMauIdAndThuTuAndIdNot(Long syllabusMonHocMauId, Integer thuTu, Long id);

    java.util.List<SyllabusMonHocMauDieuKien> findBySyllabusMonHocMauIdOrderByThuTuAscIdAsc(Long syllabusMonHocMauId);

    void deleteBySyllabusMonHocMauId(Long syllabusMonHocMauId);

    void deleteByDieuKienMauId(Long dieuKienMauId);

}