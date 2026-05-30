package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.CauHinhDanhGiaMau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CauHinhDanhGiaMauRepository extends JpaRepository<CauHinhDanhGiaMau, Long>, JpaSpecificationExecutor<CauHinhDanhGiaMau> {

    List<CauHinhDanhGiaMau> findByChuongTrinhMonIdOrderByThuTuAscIdAsc(Long chuongTrinhMonId);

    boolean existsByTenCotDiem(String tenCotDiem);

    boolean existsByTenCotDiemAndIdNot(String tenCotDiem, Long id);

    boolean existsByThuTu(Integer thuTu);

    boolean existsByThuTuAndIdNot(Integer thuTu, Long id);

    boolean existsByChuongTrinhMonIdAndTenCotDiem(Long chuongTrinhMonId, String tenCotDiem);

    boolean existsByChuongTrinhMonIdAndTenCotDiemAndIdNot(Long chuongTrinhMonId, String tenCotDiem, Long id);

    boolean existsByChuongTrinhMonIdAndThuTu(Long chuongTrinhMonId, Integer thuTu);

    boolean existsByChuongTrinhMonIdAndThuTuAndIdNot(Long chuongTrinhMonId, Integer thuTu, Long id);
    List<CauHinhDanhGiaMau> findByChuongTrinhMonIdIsNullOrderByThuTuAscIdAsc();
}