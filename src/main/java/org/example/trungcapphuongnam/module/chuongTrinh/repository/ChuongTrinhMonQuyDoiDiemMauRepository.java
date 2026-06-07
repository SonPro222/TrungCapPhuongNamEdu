package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMonQuyDoiDiemMau;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChuongTrinhMonQuyDoiDiemMauRepository extends JpaRepository<ChuongTrinhMonQuyDoiDiemMau, Long>, JpaSpecificationExecutor<ChuongTrinhMonQuyDoiDiemMau> {

    Page<ChuongTrinhMonQuyDoiDiemMau> findByChuongTrinhMonId(Long chuongTrinhMonId, Pageable pageable);

    Page<ChuongTrinhMonQuyDoiDiemMau> findBySyllabusMonHocId(Long syllabusMonHocId, Pageable pageable);

    Page<ChuongTrinhMonQuyDoiDiemMau> findByQuyDoiDiemMauId(Long quyDoiDiemMauId, Pageable pageable);

    List<ChuongTrinhMonQuyDoiDiemMau> findByChuongTrinhMonId(Long chuongTrinhMonId);

    List<ChuongTrinhMonQuyDoiDiemMau> findBySyllabusMonHocId(Long syllabusMonHocId);

    boolean existsByChuongTrinhMonIdAndQuyDoiDiemMauId(Long chuongTrinhMonId, Long quyDoiDiemMauId);

    boolean existsByChuongTrinhMonIdAndQuyDoiDiemMauIdAndIdNot(Long chuongTrinhMonId, Long quyDoiDiemMauId, Long id);

    boolean existsBySyllabusMonHocIdAndQuyDoiDiemMauId(Long syllabusMonHocId, Long quyDoiDiemMauId);

    boolean existsBySyllabusMonHocIdAndQuyDoiDiemMauIdAndIdNot(Long syllabusMonHocId, Long quyDoiDiemMauId, Long id);

    void deleteByChuongTrinhMonId(Long chuongTrinhMonId);

    void deleteBySyllabusMonHocId(Long syllabusMonHocId);

    void deleteByQuyDoiDiemMauId(Long quyDoiDiemMauId);
}