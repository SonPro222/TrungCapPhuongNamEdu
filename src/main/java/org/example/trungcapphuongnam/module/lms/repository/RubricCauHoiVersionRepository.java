package org.example.trungcapphuongnam.module.lms.repository;

import org.example.trungcapphuongnam.module.lms.entity.RubricCauHoiVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RubricCauHoiVersionRepository extends JpaRepository<RubricCauHoiVersion, Long> {
    List<RubricCauHoiVersion> findByCauHoiVersionIdOrderByThuTuAsc(Long cauHoiVersionId);
    void deleteByCauHoiVersionId(Long cauHoiVersionId);
}
