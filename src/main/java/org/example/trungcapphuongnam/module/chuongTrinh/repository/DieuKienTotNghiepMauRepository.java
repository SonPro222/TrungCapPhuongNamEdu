package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.DieuKienTotNghiepMau;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DieuKienTotNghiepMauRepository extends JpaRepository<DieuKienTotNghiepMau, Long>, JpaSpecificationExecutor<DieuKienTotNghiepMau> {

    Page<DieuKienTotNghiepMau> findBySyllabusChuongTrinhMauId(Long syllabusChuongTrinhMauId, Pageable pageable);

    Page<DieuKienTotNghiepMau> findByMa(String ma, Pageable pageable);

    boolean existsBySyllabusChuongTrinhMauIdAndMa(Long syllabusChuongTrinhMauId, String ma);

    boolean existsBySyllabusChuongTrinhMauIdAndMaAndIdNot(Long syllabusChuongTrinhMauId, String ma, Long id);

    boolean existsByMa(String ma);

    boolean existsByMaAndIdNot(String ma, Long id);
}
