package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SyllabusMonHocRepository extends JpaRepository<SyllabusMonHoc, Long>, JpaSpecificationExecutor<SyllabusMonHoc> {

    Page<SyllabusMonHoc> findByChuongTrinhMonId(Long chuongTrinhMonId, Pageable pageable);

    Optional<SyllabusMonHoc> findFirstByChuongTrinhMonId(Long chuongTrinhMonId);

    Optional<SyllabusMonHoc> findFirstByChuongTrinhMonIdOrderByIdAsc(Long chuongTrinhMonId);

    boolean existsByChuongTrinhMonId(Long chuongTrinhMonId);

    boolean existsByChuongTrinhMonIdAndIdNot(Long chuongTrinhMonId, Long id);

    Page<SyllabusMonHoc> findBySyllabusMonHocGocId(Long syllabusMonHocGocId, Pageable pageable);
}