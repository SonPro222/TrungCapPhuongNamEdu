package org.example.trungcapphuongnam.module.diem.repository;

import org.example.trungcapphuongnam.module.diem.entity.KetQuaLopHocPhan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KetQuaLopHocPhanRepository extends JpaRepository<KetQuaLopHocPhan, Long> {

    boolean existsBySinhVienIdAndLopHocPhanId(Long sinhVienId, Long lopHocPhanId);
    boolean existsBySinhVienIdAndLopHocPhanIdAndIdNot(Long sinhVienId, Long lopHocPhanId, Long id);
}
