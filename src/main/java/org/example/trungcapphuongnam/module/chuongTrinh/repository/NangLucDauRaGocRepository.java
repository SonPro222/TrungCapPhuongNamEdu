package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.NangLucDauRaGoc;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface NangLucDauRaGocRepository extends JpaRepository<NangLucDauRaGoc, Long>, JpaSpecificationExecutor<NangLucDauRaGoc> {

    Page<NangLucDauRaGoc> findByMa(String ma, Pageable pageable);

    boolean existsByChuongTrinhIdAndMa(Long chuongTrinhId, String ma);

    boolean existsByChuongTrinhIdAndMaAndIdNot(Long chuongTrinhId, String ma, Long id);

}
