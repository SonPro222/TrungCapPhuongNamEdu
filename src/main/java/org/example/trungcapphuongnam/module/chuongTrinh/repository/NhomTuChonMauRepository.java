package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.NhomTuChonMau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NhomTuChonGocRepository
        extends JpaRepository<NhomTuChonMau, Long>, JpaSpecificationExecutor<NhomTuChonMau> {

    boolean existsByMa(String ma);

    boolean existsByMaAndIdNot(String ma, Long id);

}