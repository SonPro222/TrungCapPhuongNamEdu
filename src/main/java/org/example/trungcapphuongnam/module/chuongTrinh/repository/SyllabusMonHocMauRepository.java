package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocMau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SyllabusMonHocMauRepository
        extends JpaRepository<SyllabusMonHocMau, Long>, JpaSpecificationExecutor<SyllabusMonHocMau> {

    boolean existsByMa(String ma);

    boolean existsByMaAndIdNot(String ma, Long id);

    boolean existsByMonHocIdAndTen(Long monHocId, String ten);

    boolean existsByMonHocIdAndTenAndIdNot(Long monHocId, String ten, Long id);

    Optional<SyllabusMonHocMau> findFirstByMonHocIdOrderByIdAsc(Long monHocId);

    boolean existsByMonHocId(Long monHocId);
}