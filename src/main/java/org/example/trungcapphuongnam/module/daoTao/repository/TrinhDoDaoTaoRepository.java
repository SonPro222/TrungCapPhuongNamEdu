package org.example.trungcapphuongnam.module.daoTao.repository;

import org.example.trungcapphuongnam.module.daoTao.entity.TrinhDoDaoTao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrinhDoDaoTaoRepository extends JpaRepository<TrinhDoDaoTao, Long> {
    boolean existsByMaTrinhDo(String maTrinhDo);
    boolean existsByMaTrinhDoAndIdNot(String maTrinhDo, Long id);
}
