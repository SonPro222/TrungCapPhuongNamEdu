package org.example.trungcapphuongnam.module.chuongTrinh.repository;


import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocGocTaiLieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SyllabusMonHocGocTaiLieuRepository
        extends JpaRepository<SyllabusMonHocGocTaiLieu, Long>, JpaSpecificationExecutor<SyllabusMonHocGocTaiLieu> {

    boolean existsBySyllabusMonHocGocIdAndTaiLieuGocId(Long syllabusMonHocGocId, Long taiLieuGocId);

    boolean existsBySyllabusMonHocGocIdAndTaiLieuGocIdAndIdNot(Long syllabusMonHocGocId, Long taiLieuGocId, Long id);

    boolean existsBySyllabusMonHocGocIdAndThuTu(Long syllabusMonHocGocId, Integer thuTu);

    boolean existsBySyllabusMonHocGocIdAndThuTuAndIdNot(Long syllabusMonHocGocId, Integer thuTu, Long id);

    void deleteBySyllabusMonHocGocId(Long syllabusMonHocGocId);

    void deleteByTaiLieuGocId(Long taiLieuGocId);
}