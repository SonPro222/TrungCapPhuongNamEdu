package org.example.trungcapphuongnam.module.donTu.repository;

import org.example.trungcapphuongnam.module.donTu.entity.DonTu;
import org.example.trungcapphuongnam.module.donTu.enums.DonTuTrangThai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;

public interface DonTuRepository extends JpaRepository<DonTu, Long>, JpaSpecificationExecutor<DonTu> {

    Page<DonTu> findBySinhVienIdOrderByNgayGuiDesc(Long sinhVienId, Pageable pageable);

    Page<DonTu> findByTrangThaiOrderByNgayGuiDesc(DonTuTrangThai trangThai, Pageable pageable);

    boolean existsBySinhVienIdAndLoaiDonTuMaAndTrangThaiIn(
            Long sinhVienId,
            String ma,
            Collection<DonTuTrangThai> trangThai
    );
}
