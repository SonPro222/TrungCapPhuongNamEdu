package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.NangLucDauRa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface NangLucDauRaRepository extends JpaRepository<NangLucDauRa, Long>, JpaSpecificationExecutor<NangLucDauRa> {

    Page<NangLucDauRa> findByChuongTrinhVersionId(
            Long chuongTrinhVersionId,
            Pageable pageable
    );
    void deleteByChuongTrinhVersionId(Long chuongTrinhVersionId);
    boolean existsByMa(String ma);

    boolean existsByMaAndIdNot(String ma, Long id);
    boolean existsByChuongTrinhVersionIdAndMa(Long chuongTrinhVersionId, String ma);

    boolean existsByChuongTrinhVersionIdAndMaAndIdNot(Long chuongTrinhVersionId, String ma, Long id);

    boolean existsByChuongTrinhVersionIdAndThuTu(Long chuongTrinhVersionId, Integer thuTu);

    boolean existsByChuongTrinhVersionIdAndThuTuAndIdNot(Long chuongTrinhVersionId, Integer thuTu, Long id);
}
