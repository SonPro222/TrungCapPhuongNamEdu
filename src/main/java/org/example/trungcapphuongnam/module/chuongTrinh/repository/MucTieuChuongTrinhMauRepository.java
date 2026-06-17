package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.MucTieuChuongTrinhMau;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MucTieuChuongTrinhMauRepository extends JpaRepository<MucTieuChuongTrinhMau, Long>, JpaSpecificationExecutor<MucTieuChuongTrinhMau> {

    Page<MucTieuChuongTrinhMau> findBySyllabusChuongTrinhMauId(Long syllabusChuongTrinhMauId, Pageable pageable);

    Page<MucTieuChuongTrinhMau> findByMa(String ma, Pageable pageable);

    boolean existsBySyllabusChuongTrinhMauIdAndMa(Long syllabusChuongTrinhMauId, String ma);

    boolean existsBySyllabusChuongTrinhMauIdAndMaAndIdNot(Long syllabusChuongTrinhMauId, String ma, Long id);

    boolean existsByMa(String ma);

    boolean existsByMaAndIdNot(String ma, Long id);
}
