package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusChuongTrinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

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

    boolean existsByChuongTrinhVersionIdAndSyllabusChuongTrinhGocId(
            Long chuongTrinhVersionId,
            Long syllabusChuongTrinhGocId
    );

    boolean existsByChuongTrinhVersionIdAndSyllabusChuongTrinhGocIdAndIdNot(
            Long chuongTrinhVersionId,
            Long syllabusChuongTrinhGocId,
            Long id
    );

    Optional<SyllabusChuongTrinh> findByChuongTrinhVersionIdAndSyllabusChuongTrinhGocId(
            Long chuongTrinhVersionId,
            Long syllabusChuongTrinhGocId
    );
}