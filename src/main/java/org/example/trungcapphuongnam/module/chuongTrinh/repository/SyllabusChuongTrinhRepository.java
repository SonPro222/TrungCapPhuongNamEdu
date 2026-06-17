package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusChuongTrinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

@Repository
public interface SyllabusChuongTrinhRepository extends JpaRepository<SyllabusChuongTrinh, Long>, JpaSpecificationExecutor<SyllabusChuongTrinh> {

    Page<SyllabusChuongTrinh> findByChuongTrinhVersionId(
            Long chuongTrinhVersionId,
            Pageable pageable
    );

    void deleteByChuongTrinhVersionId(Long chuongTrinhVersionId);

    boolean existsByChuongTrinhVersionId(Long chuongTrinhVersionId);

    boolean existsByChuongTrinhVersionIdAndIdNot(Long chuongTrinhVersionId, Long id);

    boolean existsByChuongTrinhVersionIdAndSyllabusChuongTrinhMauId(
            Long chuongTrinhVersionId,
            Long syllabusChuongTrinhMauId
    );

    boolean existsByChuongTrinhVersionIdAndSyllabusChuongTrinhMauIdAndIdNot(
            Long chuongTrinhVersionId,
            Long syllabusChuongTrinhMauId,
            Long id
    );

    Optional<SyllabusChuongTrinh> findByChuongTrinhVersionIdAndSyllabusChuongTrinhMauId(
            Long chuongTrinhVersionId,
            Long syllabusChuongTrinhMauId
    );

    Optional<SyllabusChuongTrinh> findFirstByChuongTrinhVersionIdOrderByIdDesc(Long chuongTrinhVersionId);

    List<SyllabusChuongTrinh> findByChuongTrinhVersionIdOrderByIdDesc(Long chuongTrinhVersionId);

    boolean existsBySyllabusChuongTrinhMauId(Long syllabusChuongTrinhMauId);
}