package org.example.trungcapphuongnam.module.lms.repository;

import org.example.trungcapphuongnam.module.lms.entity.LichSuCauHoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.fasterxml.jackson.databind.JsonNode;

public interface LichSuCauHoiRepository extends JpaRepository<LichSuCauHoi, Long> {
    Page<LichSuCauHoi> findByCauHoiGocId(Long cauHoiGocId, Pageable pageable);
    Page<LichSuCauHoi> findByCauHoiVersionId(Long cauHoiVersionId, Pageable pageable);
}
