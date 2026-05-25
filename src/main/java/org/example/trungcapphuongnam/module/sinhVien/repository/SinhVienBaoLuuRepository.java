package org.example.trungcapphuongnam.module.sinhVien.repository;

import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVienBaoLuu;
import org.example.trungcapphuongnam.module.sinhVien.enums.SinhVienBaoLuuTrangThai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SinhVienBaoLuuRepository extends JpaRepository<SinhVienBaoLuu, Long> {

    boolean existsBySinhVienIdAndTrangThai(Long sinhVienId, SinhVienBaoLuuTrangThai trangThai);

    Page<SinhVienBaoLuu> findBySinhVienIdOrderByCreatedAtDesc(Long sinhVienId, Pageable pageable);

    Page<SinhVienBaoLuu> findByTrangThaiOrderByCreatedAtDesc(SinhVienBaoLuuTrangThai trangThai, Pageable pageable);

    Page<SinhVienBaoLuu> findBySinhVienIdAndTrangThaiOrderByCreatedAtDesc(
            Long sinhVienId,
            SinhVienBaoLuuTrangThai trangThai,
            Pageable pageable
    );
}