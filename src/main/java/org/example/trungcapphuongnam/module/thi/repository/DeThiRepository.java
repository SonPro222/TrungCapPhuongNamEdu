package org.example.trungcapphuongnam.module.thi.repository;

import org.example.trungcapphuongnam.module.thi.entity.DeThi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeThiRepository extends JpaRepository<DeThi, Long> {
    boolean existsByMaDe(String maDe);
}
