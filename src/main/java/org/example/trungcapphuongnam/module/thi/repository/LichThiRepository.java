package org.example.trungcapphuongnam.module.thi.repository;

import org.example.trungcapphuongnam.module.thi.entity.LichThi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LichThiRepository extends JpaRepository<LichThi, Long> {
}
