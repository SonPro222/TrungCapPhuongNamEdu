package org.example.trungcapphuongnam.module.diem.repository;

import org.example.trungcapphuongnam.module.diem.entity.CauHinhDanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CauHinhDanhGiaRepository extends JpaRepository<CauHinhDanhGia, Long> {

    boolean existsBySyllabusMonHocIdAndTenCotDiem(Long syllabusMonHocId, String tenCotDiem);

    boolean existsBySyllabusMonHocIdAndTenCotDiemAndIdNot(Long syllabusMonHocId, String tenCotDiem, Long id);

    boolean existsBySyllabusMonHocIdAndThuTu(Long syllabusMonHocId, Integer thuTu);

    boolean existsBySyllabusMonHocIdAndThuTuAndIdNot(Long syllabusMonHocId, Integer thuTu, Long id);
    List<CauHinhDanhGia> findBySyllabusMonHocIdOrderByThuTuAscIdAsc(Long syllabusMonHocId);
}
