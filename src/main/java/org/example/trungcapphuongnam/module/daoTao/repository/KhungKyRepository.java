package org.example.trungcapphuongnam.module.daoTao.repository;

import org.example.trungcapphuongnam.module.daoTao.entity.KhungKy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KhungKyRepository extends JpaRepository<KhungKy, Long> {

    Page<KhungKy> findByChuongTrinhVersionId(
            Long chuongTrinhVersionId,
            Pageable pageable
    );

    List<KhungKy> findByChuongTrinhVersionIdOrderByThuTuAsc(Long chuongTrinhVersionId);

    Optional<KhungKy> findByChuongTrinhVersionIdAndThuTu(
            Long chuongTrinhVersionId,
            Integer thuTu
    );

    long countByChuongTrinhVersionId(Long chuongTrinhVersionId);

    boolean existsByChuongTrinhVersionIdAndMaKy(
            Long chuongTrinhVersionId,
            String maKy
    );

    boolean existsByChuongTrinhVersionIdAndMaKyAndIdNot(
            Long chuongTrinhVersionId,
            String maKy,
            Long id
    );

    boolean existsByChuongTrinhVersionIdAndThuTu(
            Long chuongTrinhVersionId,
            Integer thuTu
    );

    boolean existsByChuongTrinhVersionIdAndThuTuAndIdNot(
            Long chuongTrinhVersionId,
            Integer thuTu,
            Long id
    );
}