package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface ChuongTrinhRepository extends JpaRepository<ChuongTrinh, Long>, JpaSpecificationExecutor<ChuongTrinh> {
    Page<ChuongTrinh> findByNganhId(Long nganhId, Pageable pageable);
    boolean existsByMaChuongTrinh(String maChuongTrinh);

    boolean existsByMaChuongTrinhAndIdNot(String maChuongTrinh, Long id);
}
