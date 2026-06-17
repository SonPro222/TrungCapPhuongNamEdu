package org.example.trungcapphuongnam.module.daoTao.repository;

import org.example.trungcapphuongnam.module.daoTao.entity.NganhTrinhDoDaoTao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NganhTrinhDoDaoTaoRepository extends JpaRepository<NganhTrinhDoDaoTao, Long>, JpaSpecificationExecutor<NganhTrinhDoDaoTao> {

    Optional<NganhTrinhDoDaoTao> findByNganhIdAndTrinhDoId(Long nganhId, Long trinhDoId);

    boolean existsByNganhIdAndTrinhDoId(Long nganhId, Long trinhDoId);

    boolean existsByNganhIdAndTrinhDoIdAndIdNot(Long nganhId, Long trinhDoId, Long id);
}
