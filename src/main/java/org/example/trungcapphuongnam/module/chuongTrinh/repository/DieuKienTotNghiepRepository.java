package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.DieuKienTotNghiep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
@Repository
public interface DieuKienTotNghiepRepository extends JpaRepository<DieuKienTotNghiep, Long>, JpaSpecificationExecutor<DieuKienTotNghiep> {
    Page<DieuKienTotNghiep> findByChuongTrinhVersionId(
            Long chuongTrinhVersionId,
            Pageable pageable
    );

    void deleteByChuongTrinhVersionId(Long chuongTrinhVersionId);

    boolean existsByChuongTrinhVersionIdAndThuTu(Long chuongTrinhVersionId, Integer thuTu);

    boolean existsByChuongTrinhVersionIdAndThuTuAndIdNot(Long chuongTrinhVersionId, Integer thuTu, Long id);

}
