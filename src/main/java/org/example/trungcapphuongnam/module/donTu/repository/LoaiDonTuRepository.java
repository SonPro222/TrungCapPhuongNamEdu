package org.example.trungcapphuongnam.module.donTu.repository;

import org.example.trungcapphuongnam.module.donTu.entity.LoaiDonTu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoaiDonTuRepository extends JpaRepository<LoaiDonTu, Long> {

    Optional<LoaiDonTu> findByMa(String ma);

    boolean existsByMa(String ma);
}
