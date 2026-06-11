package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.QuyDoiDiemMau;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

@Repository
public interface QuyDoiDiemMauRepository extends JpaRepository<QuyDoiDiemMau, Long>, JpaSpecificationExecutor<QuyDoiDiemMau> {

    Page<QuyDoiDiemMau> findByMa(String ma, Pageable pageable);

    boolean existsByMa(String ma);

    boolean existsByMaAndIdNot(String ma, Long id);

    boolean existsBySyllabusMonHocMauIdAndMa(Long syllabusMonHocMauId, String ma);

    boolean existsBySyllabusMonHocMauIdAndMaAndIdNot(Long syllabusMonHocMauId, String ma, Long id);

    List<QuyDoiDiemMau> findBySyllabusMonHocMauIdOrderByThuTuAscIdAsc(Long syllabusMonHocMauId);
}
