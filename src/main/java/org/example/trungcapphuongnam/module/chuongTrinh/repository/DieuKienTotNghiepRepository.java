package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.DieuKienTotNghiep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DieuKienTotNghiepRepository extends JpaRepository<DieuKienTotNghiep, Long> {
}
