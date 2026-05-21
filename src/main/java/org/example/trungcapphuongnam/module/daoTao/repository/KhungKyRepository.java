package org.example.trungcapphuongnam.module.daoTao.repository;

import org.example.trungcapphuongnam.module.daoTao.entity.KhungKy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface KhungKyRepository extends JpaRepository<KhungKy, Long> {

    Page<KhungKy> findByChuongTrinhVersionId(
            Long chuongTrinhVersionId,
            Pageable pageable
    );

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