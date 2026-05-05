package org.example.trungcapphuongnam.module.sinhVien.repository;

import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVienChuongTrinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SinhVienChuongTrinhRepository extends JpaRepository<SinhVienChuongTrinh, Long> {
    boolean existsBySinhVienIdAndChuongTrinhVersionId(Long sinhVienId, Long chuongTrinhVersionId);
}
