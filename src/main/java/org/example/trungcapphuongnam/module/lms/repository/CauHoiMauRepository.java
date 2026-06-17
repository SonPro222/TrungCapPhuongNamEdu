package org.example.trungcapphuongnam.module.lms.repository;

import org.example.trungcapphuongnam.module.lms.entity.CauHoiMau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiCauHoiMau;

public interface CauHoiMauRepository extends JpaRepository<CauHoiMau, Long> {
    Page<CauHoiMau> findByMonHocId(Long monHocId, Pageable pageable);
    Page<CauHoiMau> findByTrangThai(TrangThaiCauHoiMau trangThai, Pageable pageable);
}
