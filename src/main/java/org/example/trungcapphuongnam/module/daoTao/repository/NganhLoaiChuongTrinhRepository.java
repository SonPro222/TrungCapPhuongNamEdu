package org.example.trungcapphuongnam.module.daoTao.repository;

import org.example.trungcapphuongnam.module.daoTao.entity.NganhLoaiChuongTrinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NganhLoaiChuongTrinhRepository extends JpaRepository<NganhLoaiChuongTrinh, Long>, JpaSpecificationExecutor<NganhLoaiChuongTrinh> {

    Optional<NganhLoaiChuongTrinh> findByNganhIdAndLoaiChuongTrinhId(Long nganhId, Long loaiChuongTrinhId);

    boolean existsByNganhIdAndLoaiChuongTrinhId(Long nganhId, Long loaiChuongTrinhId);

    boolean existsByNganhIdAndLoaiChuongTrinhIdAndIdNot(Long nganhId, Long loaiChuongTrinhId, Long id);
}
