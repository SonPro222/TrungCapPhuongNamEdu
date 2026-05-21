package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersionNangLuc;

@Repository
public interface ChuongTrinhVersionNangLucRepository extends JpaRepository<ChuongTrinhVersionNangLuc, Long> {

    Page<ChuongTrinhVersionNangLuc> findByChuongTrinhVersionId(Long chuongTrinhVersionId, Pageable pageable);
    Page<ChuongTrinhVersionNangLuc> findByNangLucGocId(Long nangLucGocId, Pageable pageable);

    boolean existsByChuongTrinhVersionIdAndNangLucGocId(Long chuongTrinhVersionId, Long nangLucGocId);

    boolean existsByChuongTrinhVersionIdAndNangLucGocIdAndIdNot(Long chuongTrinhVersionId, Long nangLucGocId, Long id);

}
