package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SyllabusMonHocFileRepository extends JpaRepository<SyllabusMonHocFile, Long> {

    List<SyllabusMonHocFile> findBySyllabusMonHocIdOrderByCreatedAtDesc(Long syllabusMonHocId);

    Optional<SyllabusMonHocFile> findByChecksum(String checksum);

    void deleteBySyllabusMonHocId(Long syllabusMonHocId);
}
