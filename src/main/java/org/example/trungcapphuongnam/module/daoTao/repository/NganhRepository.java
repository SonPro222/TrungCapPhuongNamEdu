package org.example.trungcapphuongnam.module.daoTao.repository;

import org.example.trungcapphuongnam.module.daoTao.entity.Nganh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NganhRepository extends JpaRepository<Nganh, Long> {
    boolean existsByMaNganh(String maNganh);
    boolean existsByMaNganhAndIdNot(String maNganh, Long id);
    Optional<Nganh> findByMaNganh(String maNganh);
}
