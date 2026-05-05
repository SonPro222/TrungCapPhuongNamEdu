package org.example.trungcapphuongnam.module.giangDay.repository;

import org.example.trungcapphuongnam.module.giangDay.entity.PhanCongGiangDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhanCongGiangDayRepository extends JpaRepository<PhanCongGiangDay, Long> {
}
