package org.example.trungcapphuongnam.module.lms.repository;

import org.example.trungcapphuongnam.module.lms.entity.LanLamBaiCauHoi;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import org.example.trungcapphuongnam.module.lms.enums.LoaiCauHoi;

public interface LanLamBaiCauHoiRepository extends JpaRepository<LanLamBaiCauHoi, Long> {
    List<LanLamBaiCauHoi> findByLanLamBaiIdOrderByThuTuAsc(Long lanLamBaiId);
    Optional<LanLamBaiCauHoi> findByLanLamBaiIdAndCauHoiVersionId(Long lanLamBaiId, Long cauHoiVersionId);
    void deleteByLanLamBaiId(Long lanLamBaiId);
}
