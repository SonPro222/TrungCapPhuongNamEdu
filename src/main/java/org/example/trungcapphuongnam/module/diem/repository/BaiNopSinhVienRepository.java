package org.example.trungcapphuongnam.module.diem.repository;

import org.example.trungcapphuongnam.module.diem.entity.BaiNopSinhVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaiNopSinhVienRepository extends JpaRepository<BaiNopSinhVien, Long> {

    boolean existsByBaiKiemTraIdAndSinhVienId(Long baiKiemTraId, Long sinhVienId);
    boolean existsByBaiKiemTraIdAndSinhVienIdAndIdNot(Long baiKiemTraId, Long sinhVienId, Long id);
}
