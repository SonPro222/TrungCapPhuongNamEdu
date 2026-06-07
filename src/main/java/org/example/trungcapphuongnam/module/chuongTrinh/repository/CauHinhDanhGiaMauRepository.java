package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.CauHinhDanhGiaMau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CauHinhDanhGiaMauRepository extends JpaRepository<CauHinhDanhGiaMau, Long>, JpaSpecificationExecutor<CauHinhDanhGiaMau> {

    List<CauHinhDanhGiaMau> findBySyllabusMonHocGocIdOrderByThuTuAscIdAsc(Long syllabusMonHocGocId);

    boolean existsBySyllabusMonHocGocIdAndTenCotDiem(Long syllabusMonHocGocId, String tenCotDiem);

    boolean existsBySyllabusMonHocGocIdAndTenCotDiemAndIdNot(Long syllabusMonHocGocId, String tenCotDiem, Long id);

    boolean existsBySyllabusMonHocGocIdAndThuTu(Long syllabusMonHocGocId, Integer thuTu);

    boolean existsBySyllabusMonHocGocIdAndThuTuAndIdNot(Long syllabusMonHocGocId, Integer thuTu, Long id);
}
