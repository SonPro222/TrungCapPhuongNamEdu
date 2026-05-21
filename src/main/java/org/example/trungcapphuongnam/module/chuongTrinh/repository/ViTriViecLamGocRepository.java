package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ViTriViecLamGoc;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface ViTriViecLamGocRepository extends JpaRepository<ViTriViecLamGoc, Long>, JpaSpecificationExecutor<ViTriViecLamGoc> {

    Page<ViTriViecLamGoc> findByMa(String ma, Pageable pageable);

    boolean existsByMa(String ma);

    boolean existsByMaAndIdNot(String ma, Long id);

}
