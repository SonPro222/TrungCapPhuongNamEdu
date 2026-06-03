package org.example.trungcapphuongnam.module.lms.repository;

import org.example.trungcapphuongnam.module.lms.entity.MigrateCauHoiLegacyMap;
import org.example.trungcapphuongnam.module.lms.entity.MigrateCauHoiLegacyMapId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MigrateCauHoiLegacyMapRepository extends JpaRepository<MigrateCauHoiLegacyMap, MigrateCauHoiLegacyMapId> {
    Optional<MigrateCauHoiLegacyMap> findByNganHangCauHoiId(Long nganHangCauHoiId);
}
