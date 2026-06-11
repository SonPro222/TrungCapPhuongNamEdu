package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersionViTriViecLam;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface ChuongTrinhVersionViTriViecLamRepository extends JpaRepository<ChuongTrinhVersionViTriViecLam, Long>, JpaSpecificationExecutor<ChuongTrinhVersionViTriViecLam> {

    Page<ChuongTrinhVersionViTriViecLam> findByChuongTrinhVersionId(Long chuongTrinhVersionId, Pageable pageable);
    Page<ChuongTrinhVersionViTriViecLam> findByViTriMauId(Long viTriMauId, Pageable pageable);

    boolean existsByChuongTrinhVersionIdAndViTriMauId(Long chuongTrinhVersionId, Long viTriMauId);

    boolean existsByChuongTrinhVersionIdAndViTriMauIdAndIdNot(Long chuongTrinhVersionId, Long viTriMauId, Long id);


    void deleteByChuongTrinhVersionId(Long chuongTrinhVersionId);

    void deleteByViTriMauId(Long viTriMauId);
}
