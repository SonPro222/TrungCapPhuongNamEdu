package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.QuyDoiDiemMau;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface QuyDoiDiemMauRepository extends JpaRepository<QuyDoiDiemMau, Long>, JpaSpecificationExecutor<QuyDoiDiemMau> {

    Page<QuyDoiDiemMau> findByMa(String ma, Pageable pageable);

    boolean existsByMa(String ma);

    boolean existsByMaAndIdNot(String ma, Long id);
}
