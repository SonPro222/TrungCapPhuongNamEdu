package org.example.trungcapphuongnam.module.lms.repository;

import org.example.trungcapphuongnam.module.lms.entity.MaTranDe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiMaTranDe;

public interface MaTranDeRepository extends JpaRepository<MaTranDe, Long> {
    Page<MaTranDe> findByChuongTrinhMonId(Long chuongTrinhMonId, Pageable pageable);
    Page<MaTranDe> findByTrangThai(TrangThaiMaTranDe trangThai, Pageable pageable);
}
