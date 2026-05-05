package org.example.trungcapphuongnam.module.thi.repository;

import org.example.trungcapphuongnam.module.thi.entity.BaiThiSinhVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaiThiSinhVienRepository extends JpaRepository<BaiThiSinhVien, Long> {
    boolean existsByDeThiIdAndSinhVienId(Long deThiId, Long sinhVienId);
}
