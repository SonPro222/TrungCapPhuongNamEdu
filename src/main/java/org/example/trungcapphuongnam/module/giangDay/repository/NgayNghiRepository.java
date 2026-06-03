package org.example.trungcapphuongnam.module.giangDay.repository;

import org.example.trungcapphuongnam.module.giangDay.entity.NgayNghi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface NgayNghiRepository extends JpaRepository<NgayNghi, Long> {
    boolean existsByNgay(LocalDate ngay);
}
