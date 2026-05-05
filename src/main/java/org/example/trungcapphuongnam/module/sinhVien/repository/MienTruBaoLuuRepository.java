package org.example.trungcapphuongnam.module.sinhVien.repository;

import org.example.trungcapphuongnam.module.sinhVien.entity.MienTruBaoLuu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MienTruBaoLuuRepository extends JpaRepository<MienTruBaoLuu, Long> {
}
