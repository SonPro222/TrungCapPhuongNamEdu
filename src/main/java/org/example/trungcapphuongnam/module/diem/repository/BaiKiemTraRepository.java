package org.example.trungcapphuongnam.module.diem.repository;

import org.example.trungcapphuongnam.module.diem.entity.BaiKiemTra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaiKiemTraRepository extends JpaRepository<BaiKiemTra, Long> {
}
