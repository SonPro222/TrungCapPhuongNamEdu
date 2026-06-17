package org.example.trungcapphuongnam.module.lms.repository;

import org.example.trungcapphuongnam.module.lms.entity.NhatKyLamBaiOnline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.fasterxml.jackson.databind.JsonNode;

public interface NhatKyLamBaiOnlineRepository extends JpaRepository<NhatKyLamBaiOnline, Long> {
    Page<NhatKyLamBaiOnline> findByLanLamBaiId(Long lanLamBaiId, Pageable pageable);
}
