package org.example.trungcapphuongnam.module.chuongTrinh.repository;


import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocDieuKien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SyllabusMonHocDieuKienRepository
        extends JpaRepository<SyllabusMonHocDieuKien, Long>, JpaSpecificationExecutor<SyllabusMonHocDieuKien> {

    boolean existsBySyllabusMonIdAndDieuKienMauId(Long syllabusMonId, Long dieuKienMauId);

    boolean existsBySyllabusMonIdAndDieuKienMauIdAndIdNot(Long syllabusMonId, Long dieuKienMauId, Long id);

    boolean existsBySyllabusMonIdAndThuTu(Long syllabusMonId, Integer thuTu);

    boolean existsBySyllabusMonIdAndThuTuAndIdNot(Long syllabusMonId, Integer thuTu, Long id);

    void deleteBySyllabusMonId(Long syllabusMonId);

    void deleteByDieuKienMauId(Long dieuKienMauId);
}