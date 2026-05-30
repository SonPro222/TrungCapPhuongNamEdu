package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocGoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SyllabusMonHocGocRepository
        extends JpaRepository<SyllabusMonHocGoc, Long>, JpaSpecificationExecutor<SyllabusMonHocGoc> {

    boolean existsByMa(String ma);

    boolean existsByMaAndIdNot(String ma, Long id);

    boolean existsByMonHocIdAndTen(Long monHocId, String ten);

    boolean existsByMonHocIdAndTenAndIdNot(Long monHocId, String ten, Long id);
}