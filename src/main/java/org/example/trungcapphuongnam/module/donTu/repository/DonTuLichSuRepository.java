package org.example.trungcapphuongnam.module.donTu.repository;

import org.example.trungcapphuongnam.module.donTu.entity.DonTuLichSu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonTuLichSuRepository extends JpaRepository<DonTuLichSu, Long> {

    Page<DonTuLichSu> findByDonTuIdOrderByCreatedAtAsc(Long donTuId, Pageable pageable);

    List<DonTuLichSu> findByDonTuIdOrderByCreatedAtAsc(Long donTuId);
}
