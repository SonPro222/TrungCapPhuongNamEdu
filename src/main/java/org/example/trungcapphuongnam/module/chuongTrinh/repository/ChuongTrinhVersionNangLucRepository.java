package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersionNangLuc;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface ChuongTrinhVersionNangLucRepository extends JpaRepository<ChuongTrinhVersionNangLuc, Long>, JpaSpecificationExecutor<ChuongTrinhVersionNangLuc> {

    Page<ChuongTrinhVersionNangLuc> findByChuongTrinhVersionId(Long chuongTrinhVersionId, Pageable pageable);
    Page<ChuongTrinhVersionNangLuc> findByNangLucMauId(Long nangLucMauId, Pageable pageable);

    boolean existsByChuongTrinhVersionIdAndNangLucMauId(Long chuongTrinhVersionId, Long nangLucMauId);

    boolean existsByChuongTrinhVersionIdAndNangLucMauIdAndIdNot(Long chuongTrinhVersionId, Long nangLucMauId, Long id);


    void deleteByChuongTrinhVersionId(Long chuongTrinhVersionId);

    void deleteByNangLucMauId(Long nangLucMauId);
}
