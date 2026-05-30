package org.example.trungcapphuongnam.module.diem.repository;

import org.example.trungcapphuongnam.module.diem.entity.KetQuaLopHocPhan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KetQuaLopHocPhanRepository extends JpaRepository<KetQuaLopHocPhan, Long> {

    boolean existsBySinhVienIdAndLopHocPhanId(Long sinhVienId, Long lopHocPhanId);

    boolean existsBySinhVienIdAndLopHocPhanIdAndIdNot(Long sinhVienId, Long lopHocPhanId, Long id);

    Page<KetQuaLopHocPhan> findByLopHocPhanId(Long lopHocPhanId, Pageable pageable);

    Page<KetQuaLopHocPhan> findBySinhVienId(Long sinhVienId, Pageable pageable);

    Page<KetQuaLopHocPhan> findBySinhVienIdAndLopHocPhanId(
            Long sinhVienId,
            Long lopHocPhanId,
            Pageable pageable
    );
}