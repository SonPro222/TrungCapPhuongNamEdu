package org.example.trungcapphuongnam.module.daoTao.repository;

import org.example.trungcapphuongnam.module.daoTao.entity.ChuongTrinhVersionNghiChuyenKy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChuongTrinhVersionNghiChuyenKyRepository extends JpaRepository<ChuongTrinhVersionNghiChuyenKy, Long> {

    List<ChuongTrinhVersionNghiChuyenKy> findByChuongTrinhVersionIdOrderByTuKyThuAsc(Long chuongTrinhVersionId);

    Optional<ChuongTrinhVersionNghiChuyenKy> findByChuongTrinhVersionIdAndTuKyThuAndDenKyThu(
            Long chuongTrinhVersionId,
            Integer tuKyThu,
            Integer denKyThu
    );

    void deleteByChuongTrinhVersionId(Long chuongTrinhVersionId);
}
