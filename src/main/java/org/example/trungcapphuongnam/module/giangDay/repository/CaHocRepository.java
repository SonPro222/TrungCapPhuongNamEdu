package org.example.trungcapphuongnam.module.giangDay.repository;

import org.example.trungcapphuongnam.module.giangDay.entity.CaHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaHocRepository extends JpaRepository<CaHoc, Long>, JpaSpecificationExecutor<CaHoc> {

    boolean existsByMaCaIgnoreCase(String maCa);

    boolean existsByMaCaIgnoreCaseAndIdNot(String maCa, Long id);

    List<CaHoc> findAllByOrderByGioBatDauAsc();

    @Query("""
            SELECT c.id
            FROM CaHoc c
            ORDER BY c.gioBatDau ASC
            """)
    List<Long> findAllIdsOrderByGioBatDauAsc();
}
