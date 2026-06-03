package org.example.trungcapphuongnam.module.lms.repository;

import org.example.trungcapphuongnam.module.lms.entity.BaiNopLms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiBaiNopLms;

public interface BaiNopLmsRepository extends JpaRepository<BaiNopLms, Long> {
    Page<BaiNopLms> findByBaiTapLopId(Long baiTapLopId, Pageable pageable);
    Page<BaiNopLms> findBySinhVienId(Long sinhVienId, Pageable pageable);
}
