package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.DieuKienTotNghiep;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DieuKienTotNghiepRepository extends JpaRepository<DieuKienTotNghiep, Long>, JpaSpecificationExecutor<DieuKienTotNghiep> {

    Page<DieuKienTotNghiep> findBySyllabusChuongTrinhId(Long syllabusChuongTrinhId, Pageable pageable);

    void deleteBySyllabusChuongTrinhId(Long syllabusChuongTrinhId);

    boolean existsBySyllabusChuongTrinhIdAndThuTu(Long syllabusChuongTrinhId, Integer thuTu);

    boolean existsBySyllabusChuongTrinhIdAndThuTuAndIdNot(Long syllabusChuongTrinhId, Integer thuTu, Long id);
}
