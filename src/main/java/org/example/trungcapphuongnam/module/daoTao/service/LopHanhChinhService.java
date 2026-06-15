package org.example.trungcapphuongnam.module.daoTao.service;

import org.example.trungcapphuongnam.module.daoTao.dto.LopHanhChinhRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.LopHanhChinhResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LopHanhChinhService {

    Page<LopHanhChinhResponse> findAll(Pageable pageable);

    Page<LopHanhChinhResponse> findAllByChuongTrinhVersionId(
            Long chuongTrinhVersionId,
            Pageable pageable
    );

    Page<LopHanhChinhResponse> findAllByKhoaDaoTaoId(
            Long khoaDaoTaoId,
            Pageable pageable
    );

    Page<LopHanhChinhResponse> findAllByChuongTrinhVersionIdAndKhoaDaoTaoId(
            Long chuongTrinhVersionId,
            Long khoaDaoTaoId,
            Pageable pageable
    );

    LopHanhChinhResponse findById(Long id);

    LopHanhChinhResponse create(LopHanhChinhRequest request);

    LopHanhChinhResponse update(Long id, LopHanhChinhRequest request);

    void delete(Long id);

    LopHanhChinhResponse chotTuyenSinh(Long id);

    /**
     * Hủy chốt tuyển sinh.
     * Chỉ cho phép nếu chưa có SV trong LHC này được phân bổ vào LHP nào.
     */
    LopHanhChinhResponse huyChoTuyenSinh(Long id);
}