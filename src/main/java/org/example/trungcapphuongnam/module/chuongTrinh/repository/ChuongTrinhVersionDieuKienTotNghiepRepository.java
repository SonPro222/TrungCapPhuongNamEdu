package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersionDieuKienTotNghiep;

@Repository
public interface ChuongTrinhVersionDieuKienTotNghiepRepository extends JpaRepository<ChuongTrinhVersionDieuKienTotNghiep, Long> {

    Page<ChuongTrinhVersionDieuKienTotNghiep> findByChuongTrinhVersionId(Long chuongTrinhVersionId, Pageable pageable);
    Page<ChuongTrinhVersionDieuKienTotNghiep> findByDieuKienGocId(Long dieuKienGocId, Pageable pageable);

    boolean existsByChuongTrinhVersionIdAndDieuKienGocId(Long chuongTrinhVersionId, Long dieuKienGocId);

    boolean existsByChuongTrinhVersionIdAndDieuKienGocIdAndIdNot(Long chuongTrinhVersionId, Long dieuKienGocId, Long id);

}
