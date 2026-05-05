package org.example.trungcapphuongnam.module.hocPhi.repository;

import org.example.trungcapphuongnam.module.hocPhi.entity.GiaoDichHocPhi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GiaoDichHocPhiRepository extends JpaRepository<GiaoDichHocPhi, Long> {
    List<GiaoDichHocPhi> findByHocPhiId(Long hocPhiId);
    boolean existsByMaGiaoDich(String maGiaoDich);
}
