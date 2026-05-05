package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.NhomTuChon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NhomTuChonRepository extends JpaRepository<NhomTuChon, Long> {
}
