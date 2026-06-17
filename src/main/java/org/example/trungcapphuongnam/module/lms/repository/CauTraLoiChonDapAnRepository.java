package org.example.trungcapphuongnam.module.lms.repository;

import org.example.trungcapphuongnam.module.lms.entity.CauTraLoiChonDapAn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CauTraLoiChonDapAnRepository extends JpaRepository<CauTraLoiChonDapAn, Long> {
    List<CauTraLoiChonDapAn> findByCauTraLoiId(Long cauTraLoiId);

    void deleteByCauTraLoiId(Long cauTraLoiId);

    List<CauTraLoiChonDapAn> findByCauTraLoiIdIn(List<Long> cauTraLoiIds);
}
