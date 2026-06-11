package org.example.trungcapphuongnam.module.daoTao.repository;

import org.example.trungcapphuongnam.module.daoTao.entity.NganhHeDaoTao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NganhHeDaoTaoRepository extends JpaRepository<NganhHeDaoTao, Long>, JpaSpecificationExecutor<NganhHeDaoTao> {

    Optional<NganhHeDaoTao> findByNganhIdAndTrinhDoIdAndLoaiChuongTrinhId(Long nganhId, Long trinhDoId, Long loaiChuongTrinhId);

    boolean existsByNganhIdAndTrinhDoIdAndLoaiChuongTrinhId(Long nganhId, Long trinhDoId, Long loaiChuongTrinhId);

    boolean existsByNganhIdAndTrinhDoIdAndLoaiChuongTrinhIdAndIdNot(Long nganhId, Long trinhDoId, Long loaiChuongTrinhId, Long id);
}
