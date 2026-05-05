package org.example.trungcapphuongnam.module.heThong.repository;

import org.example.trungcapphuongnam.module.heThong.entity.NhatKyDangNhap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NhatKyDangNhapRepository extends JpaRepository<NhatKyDangNhap, Long> {
}
