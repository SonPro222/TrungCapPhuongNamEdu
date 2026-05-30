package org.example.trungcapphuongnam.module.lms.repository;

import org.example.trungcapphuongnam.module.lms.entity.DapAnCauHoi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DapAnCauHoiRepository extends JpaRepository<DapAnCauHoi, Long> {
    List<DapAnCauHoi> findByCauHoiIdOrderByThuTuAsc(Long cauHoiId);

    void deleteByCauHoiId(Long cauHoiId);
}
