package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ChuongTrinhMonRepository extends JpaRepository<ChuongTrinhMon, Long> {
    Page<ChuongTrinhMon> findByChuongTrinhVersionId(Long chuongTrinhVersionId, Pageable pageable);
    Page<ChuongTrinhMon> findByKhungKyId(
            Long khungKyId,
            Pageable pageable
    );

    Page<ChuongTrinhMon> findByChuongTrinhVersionIdAndKhungKyId(
            Long chuongTrinhVersionId,
            Long khungKyId,
            Pageable pageable
    );
    boolean existsByChuongTrinhVersionIdAndMaMonTrongCt(Long chuongTrinhVersionId, String maMonTrongCt);

    boolean existsByChuongTrinhVersionIdAndMaMonTrongCtAndIdNot(Long chuongTrinhVersionId, String maMonTrongCt, Long id);

    boolean existsByChuongTrinhVersionIdAndKhungKyIdAndMonHocId(Long chuongTrinhVersionId, Long khungKyId, Long monHocId);

    boolean existsByChuongTrinhVersionIdAndKhungKyIdAndMonHocIdAndIdNot(Long chuongTrinhVersionId, Long khungKyId, Long monHocId, Long id);

    boolean existsByChuongTrinhVersionIdAndKhungKyIdAndThuTu(Long chuongTrinhVersionId, Long khungKyId, Integer thuTu);

    boolean existsByChuongTrinhVersionIdAndKhungKyIdAndThuTuAndIdNot(Long chuongTrinhVersionId, Long khungKyId, Integer thuTu, Long id);
}
