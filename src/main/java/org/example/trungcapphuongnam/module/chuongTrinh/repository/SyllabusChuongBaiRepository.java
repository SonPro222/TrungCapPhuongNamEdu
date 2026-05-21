package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusChuongBai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface SyllabusChuongBaiRepository extends JpaRepository<SyllabusChuongBai, Long> {
    Page<SyllabusChuongBai> findBySyllabusMonId(Long syllabusMonId, Pageable pageable);


    void deleteBySyllabusMonId(Long syllabusMonId);
    boolean existsBySyllabusMonIdAndThuTu(Long syllabusMonId, Integer thuTu);

    boolean existsBySyllabusMonIdAndThuTuAndIdNot(Long syllabusMonId, Integer thuTu, Long id);
}
