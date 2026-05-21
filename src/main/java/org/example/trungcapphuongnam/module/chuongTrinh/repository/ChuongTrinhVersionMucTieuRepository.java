package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersionMucTieu;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface ChuongTrinhVersionMucTieuRepository extends JpaRepository<ChuongTrinhVersionMucTieu, Long>, JpaSpecificationExecutor<ChuongTrinhVersionMucTieu> {

    Page<ChuongTrinhVersionMucTieu> findByChuongTrinhVersionId(Long chuongTrinhVersionId, Pageable pageable);
    Page<ChuongTrinhVersionMucTieu> findByMucTieuGocId(Long mucTieuGocId, Pageable pageable);

    boolean existsByChuongTrinhVersionIdAndMucTieuGocId(Long chuongTrinhVersionId, Long mucTieuGocId);

    boolean existsByChuongTrinhVersionIdAndMucTieuGocIdAndIdNot(Long chuongTrinhVersionId, Long mucTieuGocId, Long id);

}
