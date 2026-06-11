package org.example.trungcapphuongnam.module.chuongTrinh.repository;


import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocTaiLieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SyllabusMonHocTaiLieuRepository
        extends JpaRepository<SyllabusMonHocTaiLieu, Long>, JpaSpecificationExecutor<SyllabusMonHocTaiLieu> {

    boolean existsBySyllabusMonIdAndTaiLieuMauId(Long syllabusMonId, Long taiLieuMauId);

    boolean existsBySyllabusMonIdAndTaiLieuMauIdAndIdNot(Long syllabusMonId, Long taiLieuMauId, Long id);

    boolean existsBySyllabusMonIdAndThuTu(Long syllabusMonId, Integer thuTu);

    boolean existsBySyllabusMonIdAndThuTuAndIdNot(Long syllabusMonId, Integer thuTu, Long id);

    void deleteBySyllabusMonId(Long syllabusMonId);

    void deleteByTaiLieuMauId(Long taiLieuMauId);
}