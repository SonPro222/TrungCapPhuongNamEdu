package org.example.trungcapphuongnam.module.sinhVien.repository;

import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SinhVienRepository extends JpaRepository<SinhVien, Long> {
    boolean existsByMaSinhVien(String maSinhVien);
    boolean existsByEmail(String email);
    boolean existsByTaiKhoanId(Long taiKhoanId);
}
