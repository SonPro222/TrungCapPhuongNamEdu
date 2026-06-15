package org.example.trungcapphuongnam.module.diem.repository;

import org.example.trungcapphuongnam.module.diem.entity.DiemChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiemChiTietRepository extends JpaRepository<DiemChiTiet, Long> {

    Optional<DiemChiTiet> findBySinhVienIdAndLopHocPhanIdAndCauHinhDanhGiaId(
            Long sinhVienId,
            Long lopHocPhanId,
            Long cauHinhDanhGiaId
    );

    boolean existsBySinhVienIdAndLopHocPhanIdAndCauHinhDanhGiaId(
            Long sinhVienId,
            Long lopHocPhanId,
            Long cauHinhDanhGiaId
    );

    boolean existsBySinhVienIdAndLopHocPhanIdAndCauHinhDanhGiaIdAndIdNot(
            Long sinhVienId,
            Long lopHocPhanId,
            Long cauHinhDanhGiaId,
            Long id
    );

    Page<DiemChiTiet> findByLopHocPhanId(Long lopHocPhanId, Pageable pageable);

    Page<DiemChiTiet> findBySinhVienId(Long sinhVienId, Pageable pageable);

    Page<DiemChiTiet> findBySinhVienIdAndLopHocPhanId(
            Long sinhVienId,
            Long lopHocPhanId,
            Pageable pageable
    );

    Page<DiemChiTiet> findByBaiKiemTraId(Long baiKiemTraId, Pageable pageable);

    List<DiemChiTiet> findBySinhVienIdAndLopHocPhanId(Long sinhVienId, Long lopHocPhanId);

    boolean existsByLopHocPhanId(Long lopHocPhanId);
}