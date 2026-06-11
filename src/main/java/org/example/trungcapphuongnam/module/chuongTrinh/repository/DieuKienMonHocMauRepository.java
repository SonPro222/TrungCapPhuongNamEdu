package org.example.trungcapphuongnam.module.chuongTrinh.repository;


import org.example.trungcapphuongnam.module.chuongTrinh.entity.DieuKienMonHocMau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DieuKienMonHocGocRepository
        extends JpaRepository<DieuKienMonHocMau, Long>, JpaSpecificationExecutor<DieuKienMonHocMau> {

    boolean existsByMa(String ma);

    boolean existsByMaAndIdNot(String ma, Long id);
}