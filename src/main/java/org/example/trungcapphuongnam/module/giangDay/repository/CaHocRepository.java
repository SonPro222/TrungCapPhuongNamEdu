package org.example.trungcapphuongnam.module.giangDay.repository;

import org.example.trungcapphuongnam.module.giangDay.entity.CaHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaHocRepository extends JpaRepository<CaHoc, Long> {
}
