package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusChuongTrinhGoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SyllabusChuongTrinhGocRepository
        extends JpaRepository<SyllabusChuongTrinhGoc, Long>, JpaSpecificationExecutor<SyllabusChuongTrinhGoc> {

    boolean existsByMa(String ma);

    boolean existsByMaAndIdNot(String ma, Long id);

}