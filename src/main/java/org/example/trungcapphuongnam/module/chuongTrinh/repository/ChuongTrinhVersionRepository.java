package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ChuongTrinhVersionRepository extends JpaRepository<ChuongTrinhVersion, Long> {
    Page<ChuongTrinhVersion> findByChuongTrinhId(Long chuongTrinhId, Pageable pageable);
}
