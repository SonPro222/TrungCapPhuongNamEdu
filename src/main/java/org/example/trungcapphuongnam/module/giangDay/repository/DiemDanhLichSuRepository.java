package org.example.trungcapphuongnam.module.giangDay.repository;

import org.example.trungcapphuongnam.module.giangDay.entity.DiemDanhLichSu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiemDanhLichSuRepository extends JpaRepository<DiemDanhLichSu, Long> {

    Page<DiemDanhLichSu> findByDiemDanhIdOrderByCreatedAtDesc(Long diemDanhId, Pageable pageable);

    Page<DiemDanhLichSu> findByDonTuIdOrderByCreatedAtDesc(Long donTuId, Pageable pageable);
}
