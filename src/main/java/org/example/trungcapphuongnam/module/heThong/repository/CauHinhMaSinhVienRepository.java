package org.example.trungcapphuongnam.module.heThong.repository;

import jakarta.persistence.LockModeType;
import org.example.trungcapphuongnam.module.heThong.entity.CauHinhMaSinhVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CauHinhMaSinhVienRepository extends JpaRepository<CauHinhMaSinhVien, Long>, JpaSpecificationExecutor<CauHinhMaSinhVien> {

    Optional<CauHinhMaSinhVien> findByNganhIdAndChuongTrinhVersionId(
            Long nganhId,
            Long chuongTrinhVersionId
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<CauHinhMaSinhVien> findWithLockByNganhIdAndChuongTrinhVersionId(
            Long nganhId,
            Long chuongTrinhVersionId
    );

    boolean existsByNganhIdAndChuongTrinhVersionId(
            Long nganhId,
            Long chuongTrinhVersionId
    );

    boolean existsByNganhIdAndChuongTrinhVersionIdAndIdNot(
            Long nganhId,
            Long chuongTrinhVersionId,
            Long id
    );

    boolean existsByTienToIgnoreCase(String tienTo);

    boolean existsByTienToIgnoreCaseAndIdNot(String tienTo, Long id);

    boolean existsByMaDauIgnoreCase(String maDau);

    boolean existsByMaDauIgnoreCaseAndIdNot(String maDau, Long id);
}