package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.ViTriViecLamMau;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ViTriViecLamMauRepository extends JpaRepository<ViTriViecLamMau, Long>, JpaSpecificationExecutor<ViTriViecLamMau> {

    Page<ViTriViecLamMau> findBySyllabusChuongTrinhMauId(Long syllabusChuongTrinhMauId, Pageable pageable);

    Page<ViTriViecLamMau> findByMa(String ma, Pageable pageable);

    boolean existsBySyllabusChuongTrinhMauIdAndMa(Long syllabusChuongTrinhMauId, String ma);

    boolean existsBySyllabusChuongTrinhMauIdAndMaAndIdNot(Long syllabusChuongTrinhMauId, String ma, Long id);

    boolean existsByMa(String ma);

    boolean existsByMaAndIdNot(String ma, Long id);
}
