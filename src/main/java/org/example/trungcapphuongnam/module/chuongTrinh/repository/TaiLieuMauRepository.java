package org.example.trungcapphuongnam.module.chuongTrinh.repository;


import org.example.trungcapphuongnam.module.chuongTrinh.entity.TaiLieuMau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TaiLieuGocRepository
        extends JpaRepository<TaiLieuMau, Long>, JpaSpecificationExecutor<TaiLieuMau> {

    boolean existsByMa(String ma);

    boolean existsByMaAndIdNot(String ma, Long id);
}