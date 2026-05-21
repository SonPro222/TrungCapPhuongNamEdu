package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.DieuKienMonHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
@Repository
public interface DieuKienMonHocRepository extends JpaRepository<DieuKienMonHoc, Long>, JpaSpecificationExecutor<DieuKienMonHoc> {
    Page<DieuKienMonHoc> findBySyllabusMonId(
            Long syllabusMonId,
            Pageable pageable
    );
    void deleteBySyllabusMonId(Long syllabusMonId);
    boolean existsBySyllabusMonIdAndThuTu(Long syllabusMonId, Integer thuTu);

    boolean existsBySyllabusMonIdAndThuTuAndIdNot(Long syllabusMonId, Integer thuTu, Long id);
}
