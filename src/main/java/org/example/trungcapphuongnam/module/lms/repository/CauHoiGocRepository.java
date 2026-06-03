package org.example.trungcapphuongnam.module.lms.repository;

import org.example.trungcapphuongnam.module.lms.entity.CauHoiGoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiCauHoiGoc;

public interface CauHoiGocRepository extends JpaRepository<CauHoiGoc, Long> {
    Page<CauHoiGoc> findByMonHocId(Long monHocId, Pageable pageable);
    Page<CauHoiGoc> findByTrangThai(TrangThaiCauHoiGoc trangThai, Pageable pageable);
}
