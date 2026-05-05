package org.example.trungcapphuongnam.module.diem.repository;

import org.example.trungcapphuongnam.module.diem.entity.DiemChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiemChiTietRepository extends JpaRepository<DiemChiTiet, Long> {
    Optional<DiemChiTiet> findBySinhVienIdAndLopHocPhanIdAndCauHinhDanhGiaId(
            Long sinhVienId,
            Long lopHocPhanId,
            Long cauHinhDanhGiaId
    );
    boolean existsBySinhVienIdAndLopHocPhanIdAndCauHinhDanhGiaId(Long sinhVienId, Long lopHocPhanId, Long cauHinhDanhGiaId);
    boolean existsBySinhVienIdAndLopHocPhanIdAndCauHinhDanhGiaIdAndIdNot(Long sinhVienId, Long lopHocPhanId, Long cauHinhDanhGiaId, Long id);
}
