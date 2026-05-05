package org.example.trungcapphuongnam.module.heThong.repository;

import org.example.trungcapphuongnam.module.heThong.entity.VaiTro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VaiTroRepository extends JpaRepository<VaiTro, Long>, JpaSpecificationExecutor<VaiTro> {
    Optional<VaiTro> findByMaVaiTro(String maVaiTro);
}
