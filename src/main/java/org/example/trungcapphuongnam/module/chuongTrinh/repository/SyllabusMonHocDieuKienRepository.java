package org.example.trungcapphuongnam.module.chuongTrinh.repository;


import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocDieuKien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SyllabusMonHocDieuKienRepository
        extends JpaRepository<SyllabusMonHocDieuKien, Long>, JpaSpecificationExecutor<SyllabusMonHocDieuKien> {

    boolean existsBySyllabusMonIdAndDieuKienGocId(Long syllabusMonId, Long dieuKienGocId);

    boolean existsBySyllabusMonIdAndDieuKienGocIdAndIdNot(Long syllabusMonId, Long dieuKienGocId, Long id);

    boolean existsBySyllabusMonIdAndThuTu(Long syllabusMonId, Integer thuTu);

    boolean existsBySyllabusMonIdAndThuTuAndIdNot(Long syllabusMonId, Integer thuTu, Long id);

    void deleteBySyllabusMonId(Long syllabusMonId);

    void deleteByDieuKienGocId(Long dieuKienGocId);
}