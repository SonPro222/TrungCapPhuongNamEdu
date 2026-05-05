package org.example.trungcapphuongnam.module.heThong.repository;

import org.example.trungcapphuongnam.module.heThong.entity.NhatKyHeThong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NhatKyHeThongRepository extends JpaRepository<NhatKyHeThong, Long> {
}
