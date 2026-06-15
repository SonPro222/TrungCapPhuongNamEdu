package org.example.trungcapphuongnam.module.lms.repository;

import org.example.trungcapphuongnam.module.lms.entity.BaiTapLop;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiBaiTapLop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaiTapLopRepository extends JpaRepository<BaiTapLop, Long> {
    Page<BaiTapLop> findByLopHocPhanId(Long lopHocPhanId, Pageable pageable);

    Page<BaiTapLop> findByBaiTapOnlineId(Long baiTapOnlineId, Pageable pageable);

    Page<BaiTapLop> findByTrangThai(TrangThaiBaiTapLop trangThai, Pageable pageable);

    boolean existsByLopHocPhanId(Long lopHocPhanId);
}
