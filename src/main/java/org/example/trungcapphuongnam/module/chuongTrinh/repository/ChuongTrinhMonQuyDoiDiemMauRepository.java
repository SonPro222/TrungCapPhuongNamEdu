package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMonQuyDoiDiemMau;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
@Repository
public interface ChuongTrinhMonQuyDoiDiemMauRepository extends JpaRepository<ChuongTrinhMonQuyDoiDiemMau, Long>, JpaSpecificationExecutor<ChuongTrinhMonQuyDoiDiemMau> {

    Page<ChuongTrinhMonQuyDoiDiemMau> findByChuongTrinhMonId(Long chuongTrinhMonId, Pageable pageable);
    Page<ChuongTrinhMonQuyDoiDiemMau> findByQuyDoiDiemMauId(Long quyDoiDiemMauId, Pageable pageable);

    boolean existsByChuongTrinhMonIdAndQuyDoiDiemMauId(Long chuongTrinhMonId, Long quyDoiDiemMauId);

    boolean existsByChuongTrinhMonIdAndQuyDoiDiemMauIdAndIdNot(Long chuongTrinhMonId, Long quyDoiDiemMauId, Long id);

    void deleteByChuongTrinhMonId(Long chuongTrinhMonId);

    void deleteByQuyDoiDiemMauId(Long quyDoiDiemMauId);
    List<ChuongTrinhMonQuyDoiDiemMau> findByChuongTrinhMonId(Long chuongTrinhMonId);
}
