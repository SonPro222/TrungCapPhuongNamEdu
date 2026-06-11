package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.NhomKienThucMau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NhomKienThucGocRepository
        extends JpaRepository<NhomKienThucMau, Long>, JpaSpecificationExecutor<NhomKienThucMau> {

    boolean existsByMa(String ma);

    boolean existsByMaAndIdNot(String ma, Long id);
}