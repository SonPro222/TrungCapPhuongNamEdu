package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.NangLucDauRa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NangLucDauRaRepository extends JpaRepository<NangLucDauRa, Long>, JpaSpecificationExecutor<NangLucDauRa> {

    Page<NangLucDauRa> findBySyllabusChuongTrinhId(Long syllabusChuongTrinhId, Pageable pageable);

    void deleteBySyllabusChuongTrinhId(Long syllabusChuongTrinhId);

    boolean existsBySyllabusChuongTrinhIdAndMa(Long syllabusChuongTrinhId, String ma);

    boolean existsBySyllabusChuongTrinhIdAndMaAndIdNot(Long syllabusChuongTrinhId, String ma, Long id);

    boolean existsBySyllabusChuongTrinhIdAndThuTu(Long syllabusChuongTrinhId, Integer thuTu);

    boolean existsBySyllabusChuongTrinhIdAndThuTuAndIdNot(Long syllabusChuongTrinhId, Integer thuTu, Long id);
}
