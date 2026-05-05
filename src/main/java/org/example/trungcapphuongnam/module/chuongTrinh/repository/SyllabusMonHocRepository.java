package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyllabusMonHocRepository extends JpaRepository<SyllabusMonHoc, Long> {
}
