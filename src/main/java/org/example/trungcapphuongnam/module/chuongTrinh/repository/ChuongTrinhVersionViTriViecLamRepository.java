package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersionViTriViecLam;

@Repository
public interface ChuongTrinhVersionViTriViecLamRepository extends JpaRepository<ChuongTrinhVersionViTriViecLam, Long> {

    Page<ChuongTrinhVersionViTriViecLam> findByChuongTrinhVersionId(Long chuongTrinhVersionId, Pageable pageable);
    Page<ChuongTrinhVersionViTriViecLam> findByViTriGocId(Long viTriGocId, Pageable pageable);

    boolean existsByChuongTrinhVersionIdAndViTriGocId(Long chuongTrinhVersionId, Long viTriGocId);

    boolean existsByChuongTrinhVersionIdAndViTriGocIdAndIdNot(Long chuongTrinhVersionId, Long viTriGocId, Long id);

}
