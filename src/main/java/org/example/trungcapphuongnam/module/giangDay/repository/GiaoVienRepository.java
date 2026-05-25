package org.example.trungcapphuongnam.module.giangDay.repository;

import org.example.trungcapphuongnam.module.giangDay.entity.GiaoVien;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiGiaoVien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GiaoVienRepository extends JpaRepository<GiaoVien, Long> {

    Optional<GiaoVien> findByTaiKhoanId(Long taiKhoanId);

    boolean existsByMaGiaoVienIgnoreCase(String maGiaoVien);

    boolean existsByMaGiaoVienIgnoreCaseAndIdNot(String maGiaoVien, Long id);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    boolean existsByTaiKhoanId(Long taiKhoanId);

    boolean existsByTaiKhoanIdAndIdNot(Long taiKhoanId, Long id);

    Page<GiaoVien> findByTrangThai(TrangThaiGiaoVien trangThai, Pageable pageable);

    Page<GiaoVien> findByMaGiaoVienContainingIgnoreCaseOrHoTenContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String maGiaoVien,
            String hoTen,
            String email,
            Pageable pageable
    );

    Page<GiaoVien> findByTrangThaiAndMaGiaoVienContainingIgnoreCaseOrTrangThaiAndHoTenContainingIgnoreCaseOrTrangThaiAndEmailContainingIgnoreCase(
            TrangThaiGiaoVien trangThai1,
            String maGiaoVien,
            TrangThaiGiaoVien trangThai2,
            String hoTen,
            TrangThaiGiaoVien trangThai3,
            String email,
            Pageable pageable
    );
}