package org.example.trungcapphuongnam.module.lms.repository;

import org.example.trungcapphuongnam.module.lms.entity.CauTraLoiSinhVien;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CauTraLoiSinhVienRepository extends JpaRepository<CauTraLoiSinhVien, Long> {
    List<CauTraLoiSinhVien> findByLanLamBaiId(Long lanLamBaiId);

    Optional<CauTraLoiSinhVien> findByLanLamBaiIdAndLanLamBaiCauHoiId(Long lanLamBaiId, Long lanLamBaiCauHoiId);

    List<CauTraLoiSinhVien> findByLanLamBaiIdAndLanLamBaiCauHoiIdIn(Long lanLamBaiId, List<Long> lanLamBaiCauHoiIds);

    void deleteByLanLamBaiId(Long lanLamBaiId);
}
