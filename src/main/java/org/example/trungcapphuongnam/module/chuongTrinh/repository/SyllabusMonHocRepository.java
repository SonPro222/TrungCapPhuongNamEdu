package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface SyllabusMonHocRepository extends JpaRepository<SyllabusMonHoc, Long> {
    Page<SyllabusMonHoc> findByChuongTrinhMonId(Long chuongTrinhMonId, Pageable pageable);
}
