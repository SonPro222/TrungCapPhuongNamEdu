package org.example.trungcapphuongnam.module.lms.repository;

import org.example.trungcapphuongnam.module.lms.entity.CauTraLoiSinhVien;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CauTraLoiSinhVienRepository extends JpaRepository<CauTraLoiSinhVien, Long> {

    List<CauTraLoiSinhVien> findByLanLamBaiId(Long lanLamBaiId);

    List<CauTraLoiSinhVien> findByLanLamBaiIdAndCauHoiId(Long lanLamBaiId, Long cauHoiId);

    void deleteByLanLamBaiId(Long lanLamBaiId);
}