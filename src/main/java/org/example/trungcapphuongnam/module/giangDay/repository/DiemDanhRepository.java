package org.example.trungcapphuongnam.module.giangDay.repository;

import org.example.trungcapphuongnam.module.giangDay.entity.DiemDanh;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiDiemDanh;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DiemDanhRepository extends JpaRepository<DiemDanh, Long>, JpaSpecificationExecutor<DiemDanh> {

    Page<DiemDanh> findByLichHocId(Long lichHocId, Pageable pageable);

    Page<DiemDanh> findBySinhVienId(Long sinhVienId, Pageable pageable);

    Page<DiemDanh> findByTrangThai(TrangThaiDiemDanh trangThai, Pageable pageable);

    Page<DiemDanh> findByLichHocIdAndSinhVienId(Long lichHocId, Long sinhVienId, Pageable pageable);

    Page<DiemDanh> findByLichHocIdAndTrangThai(Long lichHocId, TrangThaiDiemDanh trangThai, Pageable pageable);

    Page<DiemDanh> findBySinhVienIdAndTrangThai(Long sinhVienId, TrangThaiDiemDanh trangThai, Pageable pageable);

    Page<DiemDanh> findByLichHocIdAndSinhVienIdAndTrangThai(
            Long lichHocId,
            Long sinhVienId,
            TrangThaiDiemDanh trangThai,
            Pageable pageable
    );

    boolean existsByLichHocIdAndSinhVienId(Long lichHocId, Long sinhVienId);

    boolean existsByLichHocIdAndSinhVienIdAndIdNot(Long lichHocId, Long sinhVienId, Long id);
}