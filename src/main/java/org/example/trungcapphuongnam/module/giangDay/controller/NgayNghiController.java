package org.example.trungcapphuongnam.module.giangDay.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.GiangDayPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhVersionRepository;
import org.example.trungcapphuongnam.module.daoTao.repository.KhungKyRepository;
import org.example.trungcapphuongnam.module.giangDay.GiangDayException;
import org.example.trungcapphuongnam.module.giangDay.entity.NgayNghi;
import org.example.trungcapphuongnam.module.giangDay.enums.LoaiNgayNghi;
import org.example.trungcapphuongnam.module.giangDay.repository.NgayNghiRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(GiangDayPath.BASE + "/ngay-nghi")
@RequiredArgsConstructor
public class NgayNghiController {

    private final NgayNghiRepository ngayNghiRepository;
    private final ChuongTrinhVersionRepository chuongTrinhVersionRepository;
    private final KhungKyRepository khungKyRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<NgayNghi>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "ngay") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return ResponseEntity.ok(ApiResponse.ok(
                ngayNghiRepository.findAll(PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 500), Sort.by(direction, sortBy)))
        ));
    }

    @GetMapping("/trong-khoang")
    public ResponseEntity<ApiResponse<List<NgayNghi>>> getTrongKhoang(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tuNgay,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate denNgay
    ) {
        if (tuNgay == null || denNgay == null) {
            throw new GiangDayException("Từ ngày và đến ngày không được để trống");
        }
        if (denNgay.isBefore(tuNgay)) {
            throw new GiangDayException("Đến ngày không được trước từ ngày");
        }
        return ResponseEntity.ok(ApiResponse.ok(ngayNghiRepository.findNgayNghiApDungTrongKhoang(tuNgay, denNgay)));
    }

    @GetMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<NgayNghi>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NgayNghi>> create(@RequestBody NgayNghi request) {
        request.setId(null);
        chuanHoaRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(ngayNghiRepository.save(request)));
    }

    @PutMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<NgayNghi>> update(@PathVariable Long id, @RequestBody NgayNghi request) {
        NgayNghi entity = findById(id);
        entity.setNgay(request.getNgay());
        entity.setNgayBatDau(request.getNgayBatDau());
        entity.setNgayKetThuc(request.getNgayKetThuc());
        entity.setTenNgayNghi(request.getTenNgayNghi());
        entity.setLoaiNgayNghi(request.getLoaiNgayNghi());
        entity.setLapLaiHangNam(request.getLapLaiHangNam());
        entity.setTinhLaNgayKhongHoc(request.getTinhLaNgayKhongHoc());
        entity.setTrangThai(request.getTrangThai());
        entity.setChuongTrinhVersionId(request.getChuongTrinhVersionId());
        entity.setKhungKyId(request.getKhungKyId());
        entity.setGhiChu(request.getGhiChu());
        chuanHoaRequest(entity);
        return ResponseEntity.ok(ApiResponse.ok(ngayNghiRepository.save(entity)));
    }

    @DeleteMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        ngayNghiRepository.delete(findById(id));
        return ResponseEntity.ok(ApiResponse.deleted());
    }

    private NgayNghi findById(Long id) {
        return ngayNghiRepository.findById(id)
                .orElseThrow(() -> new GiangDayException("Ngày nghỉ không tồn tại"));
    }

    private void chuanHoaRequest(NgayNghi request) {
        if (request == null) {
            throw new GiangDayException("Dữ liệu ngày nghỉ không hợp lệ");
        }

        // Validate: khungKyId phải có chuongTrinhVersionId
        if (request.getKhungKyId() != null && request.getChuongTrinhVersionId() == null) {
            throw new GiangDayException("Ngày nghỉ theo kỳ phải chọn chương trình version.");
        }

        // Validate: NGHI_GIUA_KY bắt buộc phải có cả version lẫn kỳ
        if (LoaiNgayNghi.NGHI_GIUA_KY.equals(request.getLoaiNgayNghi())) {
            if (request.getChuongTrinhVersionId() == null || request.getKhungKyId() == null) {
                throw new GiangDayException("Ngày nghỉ giữa kỳ phải chọn chương trình version và khung kỳ.");
            }
        }

        // Validate chuongTrinhVersionId tồn tại
        if (request.getChuongTrinhVersionId() != null
                && !chuongTrinhVersionRepository.existsById(request.getChuongTrinhVersionId())) {
            throw new GiangDayException("Chương trình version không tồn tại.");
        }

        // Validate khungKyId tồn tại và thuộc đúng version
        if (request.getKhungKyId() != null) {
            var khungKy = khungKyRepository.findById(request.getKhungKyId())
                    .orElseThrow(() -> new GiangDayException("Khung kỳ không tồn tại."));
            if (!khungKy.getChuongTrinhVersionId().equals(request.getChuongTrinhVersionId())) {
                throw new GiangDayException("Khung kỳ không thuộc chương trình version đã chọn.");
            }
        }

        request.chuanHoaKhoangNgay();
        if (request.getNgayBatDau() == null || request.getNgayKetThuc() == null) {
            throw new GiangDayException("Ngày bắt đầu và ngày kết thúc nghỉ không được để trống");
        }
    }
}
