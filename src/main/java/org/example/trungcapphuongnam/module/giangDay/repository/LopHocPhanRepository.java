package org.example.trungcapphuongnam.module.giangDay.repository;

import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LopHocPhanRepository extends JpaRepository<LopHocPhan, Long> {
}
