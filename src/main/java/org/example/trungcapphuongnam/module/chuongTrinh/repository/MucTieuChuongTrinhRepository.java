package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.MucTieuChuongTrinh;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface MucTieuChuongTrinhRepository extends JpaRepository<MucTieuChuongTrinh, Long>, JpaSpecificationExecutor<MucTieuChuongTrinh> {

    Page<MucTieuChuongTrinh> findByChuongTrinhVersionId(
            Long chuongTrinhVersionId,
            Pageable pageable
    );
    void deleteByChuongTrinhVersionId(Long chuongTrinhVersionId);

    boolean existsByChuongTrinhVersionIdAndThuTu(Long chuongTrinhVersionId, Integer thuTu);

    boolean existsByChuongTrinhVersionIdAndThuTuAndIdNot(Long chuongTrinhVersionId, Integer thuTu, Long id);
}
