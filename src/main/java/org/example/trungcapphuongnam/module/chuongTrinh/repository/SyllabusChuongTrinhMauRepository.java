package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusChuongTrinhMau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SyllabusChuongTrinhMauRepository
        extends JpaRepository<SyllabusChuongTrinhMau, Long>, JpaSpecificationExecutor<SyllabusChuongTrinhMau> {

    boolean existsByMa(String ma);

    boolean existsByMaAndIdNot(String ma, Long id);

}