package org.example.trungcapphuongnam.module.chuongTrinh.repository;


import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocGocDieuKien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SyllabusMonHocGocDieuKienRepository
        extends JpaRepository<SyllabusMonHocGocDieuKien, Long>, JpaSpecificationExecutor<SyllabusMonHocGocDieuKien> {

    boolean existsBySyllabusMonHocGocIdAndDieuKienGocId(Long syllabusMonHocGocId, Long dieuKienGocId);

    boolean existsBySyllabusMonHocGocIdAndDieuKienGocIdAndIdNot(Long syllabusMonHocGocId, Long dieuKienGocId, Long id);

    boolean existsBySyllabusMonHocGocIdAndThuTu(Long syllabusMonHocGocId, Integer thuTu);

    boolean existsBySyllabusMonHocGocIdAndThuTuAndIdNot(Long syllabusMonHocGocId, Integer thuTu, Long id);

    void deleteBySyllabusMonHocGocId(Long syllabusMonHocGocId);

    void deleteByDieuKienGocId(Long dieuKienGocId);

}