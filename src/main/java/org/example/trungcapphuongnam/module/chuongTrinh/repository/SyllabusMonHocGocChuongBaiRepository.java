package org.example.trungcapphuongnam.module.chuongTrinh.repository;


import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocGocChuongBai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SyllabusMonHocGocChuongBaiRepository
        extends JpaRepository<SyllabusMonHocGocChuongBai, Long>, JpaSpecificationExecutor<SyllabusMonHocGocChuongBai> {

    boolean existsBySyllabusMonHocGocIdAndThuTu(Long syllabusMonHocGocId, Integer thuTu);

    boolean existsBySyllabusMonHocGocIdAndThuTuAndIdNot(Long syllabusMonHocGocId, Integer thuTu, Long id);

    void deleteBySyllabusMonHocGocId(Long syllabusMonHocGocId);
}