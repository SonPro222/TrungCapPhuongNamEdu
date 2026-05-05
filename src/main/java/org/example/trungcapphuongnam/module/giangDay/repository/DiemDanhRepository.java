package org.example.trungcapphuongnam.module.giangDay.repository;

import org.example.trungcapphuongnam.module.giangDay.entity.DiemDanh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiemDanhRepository extends JpaRepository<DiemDanh, Long> {
}
