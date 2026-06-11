package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SyllabusMonHocRepository extends JpaRepository<SyllabusMonHoc, Long>, JpaSpecificationExecutor<SyllabusMonHoc> {

    Page<SyllabusMonHoc> findByChuongTrinhMonId(Long chuongTrinhMonId, Pageable pageable);

    @Query(value = """
            select smh.id
            from syllabus_mon_hoc smh
            where smh.chuong_trinh_mon_id = :chuongTrinhMonId
            order by smh.id asc
            limit 1
            """, nativeQuery = true)
    Optional<Long> findFirstIdByChuongTrinhMonId(@Param("chuongTrinhMonId") Long chuongTrinhMonId);

    Optional<SyllabusMonHoc> findFirstByChuongTrinhMonIdOrderByIdAsc(Long chuongTrinhMonId);

    boolean existsByChuongTrinhMonId(Long chuongTrinhMonId);

    boolean existsByChuongTrinhMonIdAndIdNot(Long chuongTrinhMonId, Long id);

    Page<SyllabusMonHoc> findBySyllabusMonHocMauId(Long syllabusMonHocMauId, Pageable pageable);
}