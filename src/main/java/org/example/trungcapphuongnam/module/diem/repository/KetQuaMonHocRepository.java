package org.example.trungcapphuongnam.module.diem.repository;

import org.example.trungcapphuongnam.module.diem.entity.KetQuaMonHoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KetQuaMonHocRepository extends JpaRepository<KetQuaMonHoc, Long> {

    List<KetQuaMonHoc> findBySinhVienIdAndKetQua(Long sinhVienId, String ketQua);

    boolean existsBySinhVienIdAndChuongTrinhMonIdAndSoLanHoc(
            Long sinhVienId,
            Long chuongTrinhMonId,
            Integer soLanHoc
    );

    boolean existsBySinhVienIdAndChuongTrinhMonIdAndSoLanHocAndIdNot(
            Long sinhVienId,
            Long chuongTrinhMonId,
            Integer soLanHoc,
            Long id
    );

    Page<KetQuaMonHoc> findBySinhVienId(Long sinhVienId, Pageable pageable);

    Page<KetQuaMonHoc> findByChuongTrinhMonId(Long chuongTrinhMonId, Pageable pageable);

    Page<KetQuaMonHoc> findByLopHocPhanId(Long lopHocPhanId, Pageable pageable);

    Page<KetQuaMonHoc> findBySinhVienIdAndChuongTrinhMonId(
            Long sinhVienId,
            Long chuongTrinhMonId,
            Pageable pageable
    );
}