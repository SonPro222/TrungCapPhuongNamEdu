package org.example.trungcapphuongnam.module.sinhVien.repository;

import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVienChuongTrinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SinhVienChuongTrinhRepository extends JpaRepository<SinhVienChuongTrinh, Long> {

    boolean existsBySinhVienIdAndChuongTrinhVersionId(Long sinhVienId, Long chuongTrinhVersionId);

    List<SinhVienChuongTrinh> findBySinhVienId(Long sinhVienId);

    List<SinhVienChuongTrinh> findByChuongTrinhVersionId(Long chuongTrinhVersionId);

    Optional<SinhVienChuongTrinh> findFirstBySinhVienIdAndChuongTrinhVersionId(
            Long sinhVienId,
            Long chuongTrinhVersionId
    );
    boolean existsBySinhVienIdAndChuongTrinhVersionIdAndIdNot(
            Long sinhVienId,
            Long chuongTrinhVersionId,
            Long id
    );
}