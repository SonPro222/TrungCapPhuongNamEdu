package org.example.trungcapphuongnam.module.giangDay.repository;

import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LopHocPhanRepository extends JpaRepository<LopHocPhan, Long>, JpaSpecificationExecutor<LopHocPhan> {

    void deleteByChuongTrinhMonId(Long chuongTrinhMonId);

    List<LopHocPhan> findByChuongTrinhMonIdOrderByMaLopAsc(Long chuongTrinhMonId);

    List<LopHocPhan> findByMonHocIdOrderByMaLopAsc(Long monHocId);

    boolean existsByMaLopIgnoreCase(String maLop);

    boolean existsByMaLopIgnoreCaseAndIdNot(String maLop, Long id);

    boolean existsByChuongTrinhMonId(Long chuongTrinhMonId);

    boolean existsByChuongTrinhMonIdAndIdNot(Long chuongTrinhMonId, Long id);
}