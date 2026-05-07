package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ChuongTrinhMonRepository extends JpaRepository<ChuongTrinhMon, Long> {
    Page<ChuongTrinhMon> findByChuongTrinhVersionId(Long chuongTrinhVersionId, Pageable pageable);
}
