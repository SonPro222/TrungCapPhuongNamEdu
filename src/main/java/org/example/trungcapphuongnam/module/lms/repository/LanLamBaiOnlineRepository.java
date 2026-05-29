package org.example.trungcapphuongnam.module.lms.repository;

import org.example.trungcapphuongnam.module.lms.entity.LanLamBaiOnline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LanLamBaiOnlineRepository extends JpaRepository<LanLamBaiOnline, Long> {
    List<LanLamBaiOnline> findByBaiTapLopIdAndSinhVienIdOrderByLanThuDesc(
            Long baiTapLopId,
            Long sinhVienId
    );

    Optional<LanLamBaiOnline> findFirstByBaiTapLopIdAndSinhVienIdOrderByLanThuDesc(
            Long baiTapLopId,
            Long sinhVienId
    );
}
