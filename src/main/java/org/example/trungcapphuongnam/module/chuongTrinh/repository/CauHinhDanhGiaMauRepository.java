package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.CauHinhDanhGiaMau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CauHinhDanhGiaMauRepository extends JpaRepository<CauHinhDanhGiaMau, Long>, JpaSpecificationExecutor<CauHinhDanhGiaMau> {

    List<CauHinhDanhGiaMau> findBySyllabusMonHocMauIdOrderByThuTuAscIdAsc(Long syllabusMonHocMauId);

    boolean existsBySyllabusMonHocMauIdAndTenCotDiem(Long syllabusMonHocMauId, String tenCotDiem);

    boolean existsBySyllabusMonHocMauIdAndTenCotDiemAndIdNot(Long syllabusMonHocMauId, String tenCotDiem, Long id);

    boolean existsBySyllabusMonHocMauIdAndThuTu(Long syllabusMonHocMauId, Integer thuTu);

    boolean existsBySyllabusMonHocMauIdAndThuTuAndIdNot(Long syllabusMonHocMauId, Integer thuTu, Long id);
}
