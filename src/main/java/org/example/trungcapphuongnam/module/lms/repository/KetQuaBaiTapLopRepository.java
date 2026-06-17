package org.example.trungcapphuongnam.module.lms.repository;

import org.example.trungcapphuongnam.module.lms.entity.KetQuaBaiTapLop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KetQuaBaiTapLopRepository extends JpaRepository<KetQuaBaiTapLop, Long> {
    Optional<KetQuaBaiTapLop> findByBaiTapLopIdAndSinhVienId(Long baiTapLopId, Long sinhVienId);

    Page<KetQuaBaiTapLop> findByBaiTapLopId(Long baiTapLopId, Pageable pageable);

    Page<KetQuaBaiTapLop> findBySinhVienId(Long sinhVienId, Pageable pageable);


    List<KetQuaBaiTapLop> findAllByBaiTapLopId(Long baiTapLopId);
}
