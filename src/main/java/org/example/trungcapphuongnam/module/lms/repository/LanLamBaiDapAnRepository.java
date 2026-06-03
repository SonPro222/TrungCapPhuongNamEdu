package org.example.trungcapphuongnam.module.lms.repository;

import org.example.trungcapphuongnam.module.lms.entity.LanLamBaiDapAn;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LanLamBaiDapAnRepository extends JpaRepository<LanLamBaiDapAn, Long> {
    List<LanLamBaiDapAn> findByLanLamBaiCauHoiIdOrderByThuTuAsc(Long lanLamBaiCauHoiId);
    List<LanLamBaiDapAn> findByLanLamBaiCauHoiIdIn(List<Long> lanLamBaiCauHoiIds);
    void deleteByLanLamBaiCauHoiId(Long lanLamBaiCauHoiId);
}
