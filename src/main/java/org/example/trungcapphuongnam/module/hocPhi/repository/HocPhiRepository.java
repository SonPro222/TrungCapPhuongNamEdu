package org.example.trungcapphuongnam.module.hocPhi.repository;


import org.example.trungcapphuongnam.module.hocPhi.enums.TrangThaiHocPhi;
import org.example.trungcapphuongnam.module.hocPhi.entity.HocPhi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HocPhiRepository extends JpaRepository<HocPhi, Long> {
    List<HocPhi> findBySinhVienChuongTrinhId(Long sinhVienChuongTrinhId);
    List<HocPhi> findByTrangThai(TrangThaiHocPhi trangThai);
}
