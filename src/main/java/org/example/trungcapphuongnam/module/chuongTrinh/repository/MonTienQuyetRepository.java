package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.MonTienQuyet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonTienQuyetRepository extends JpaRepository<MonTienQuyet, Long> {
}
