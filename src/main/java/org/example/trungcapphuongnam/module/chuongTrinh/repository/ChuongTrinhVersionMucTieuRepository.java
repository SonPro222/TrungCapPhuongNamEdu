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
    Page<ChuongTrinhVersionMucTieu> findByMucTieuMauId(Long mucTieuMauId, Pageable pageable);

    boolean existsByChuongTrinhVersionIdAndMucTieuMauId(Long chuongTrinhVersionId, Long mucTieuMauId);

    boolean existsByChuongTrinhVersionIdAndMucTieuMauIdAndIdNot(Long chuongTrinhVersionId, Long mucTieuMauId, Long id);


    void deleteByChuongTrinhVersionId(Long chuongTrinhVersionId);

    void deleteByMucTieuMauId(Long mucTieuMauId);
}
