package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.ViTriViecLam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
@Repository
public interface ViTriViecLamRepository extends JpaRepository<ViTriViecLam, Long> {
    Page<ViTriViecLam> findByChuongTrinhVersionId(
            Long chuongTrinhVersionId,
            Pageable pageable
    );
    void deleteByChuongTrinhVersionId(Long chuongTrinhVersionId);
    boolean existsByChuongTrinhVersionIdAndThuTu(Long chuongTrinhVersionId, Integer thuTu);

    boolean existsByChuongTrinhVersionIdAndThuTuAndIdNot(Long chuongTrinhVersionId, Integer thuTu, Long id);


}
