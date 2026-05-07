package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ChuongTrinhRepository extends JpaRepository<ChuongTrinh, Long> {
    Page<ChuongTrinh> findByNganhId(Long nganhId, Pageable pageable);
}
