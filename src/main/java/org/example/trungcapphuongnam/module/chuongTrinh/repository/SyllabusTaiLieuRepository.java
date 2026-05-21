package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusTaiLieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
@Repository
public interface SyllabusTaiLieuRepository extends JpaRepository<SyllabusTaiLieu, Long> {
    Page<SyllabusTaiLieu> findBySyllabusMonId(
            Long syllabusMonId,
            Pageable pageable
    );

    void deleteBySyllabusMonId(Long syllabusMonId);
    boolean existsBySyllabusMonIdAndTen(Long syllabusMonId, String ten);

    boolean existsBySyllabusMonIdAndTenAndIdNot(Long syllabusMonId, String ten, Long id);
}
