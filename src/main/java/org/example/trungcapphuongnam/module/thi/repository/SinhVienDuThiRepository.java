package org.example.trungcapphuongnam.module.thi.repository;

import org.example.trungcapphuongnam.module.thi.entity.SinhVienDuThi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SinhVienDuThiRepository extends JpaRepository<SinhVienDuThi, Long> {
    boolean existsByLichThiIdAndSinhVienId(Long lichThiId, Long sinhVienId);
}
