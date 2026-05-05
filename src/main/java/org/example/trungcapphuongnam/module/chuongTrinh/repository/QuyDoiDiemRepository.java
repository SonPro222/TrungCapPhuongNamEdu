package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.QuyDoiDiem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuyDoiDiemRepository extends JpaRepository<QuyDoiDiem, Long> {
}
