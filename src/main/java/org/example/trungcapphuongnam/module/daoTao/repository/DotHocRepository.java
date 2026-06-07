package org.example.trungcapphuongnam.module.daoTao.repository;

import org.example.trungcapphuongnam.module.daoTao.entity.DotHoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DotHocRepository extends JpaRepository<DotHoc, Long>, JpaSpecificationExecutor<DotHoc> {
    boolean existsByMaDotIgnoreCase(String maDot);
    boolean existsByMaDotIgnoreCaseAndIdNot(String maDot, Long id);
    Page<DotHoc> findByKhoaDaoTaoId(Long khoaDaoTaoId, Pageable pageable);
    Page<DotHoc> findByLopHanhChinhId(Long lopHanhChinhId, Pageable pageable);
}
