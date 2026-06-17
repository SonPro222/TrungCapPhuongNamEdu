package org.example.trungcapphuongnam.module.lms.repository;

import org.example.trungcapphuongnam.module.lms.entity.DapAnCauHoiVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DapAnCauHoiVersionRepository extends JpaRepository<DapAnCauHoiVersion, Long> {
    List<DapAnCauHoiVersion> findByCauHoiVersionIdOrderByThuTuAsc(Long cauHoiVersionId);
    void deleteByCauHoiVersionId(Long cauHoiVersionId);
}
