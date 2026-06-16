package org.example.trungcapphuongnam.module.giangDay.mapper;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhVersionRepository;
import org.example.trungcapphuongnam.module.daoTao.repository.KhungKyRepository;
import org.example.trungcapphuongnam.module.daoTao.repository.NganhRepository;
import org.example.trungcapphuongnam.module.giangDay.dto.request.GiangVienDangKyGiangDayRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.GiangVienDangKyGiangDayResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.GiangVienDangKyGiangDay;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GiangVienDangKyGiangDayMapper {

    private final GiaoVienRepository giaoVienRepository;
    private final KhungKyRepository khungKyRepository;
    private final ChuongTrinhVersionRepository chuongTrinhVersionRepository;
    private final ChuongTrinhRepository chuongTrinhRepository;
    private final NganhRepository nganhRepository;

    public GiangVienDangKyGiangDay toEntity(GiangVienDangKyGiangDayRequest request) {
        if (request == null) return null;
        return GiangVienDangKyGiangDay.builder()
                .giaoVienId(request.getGiaoVienId())
                .khungKyId(request.getKhungKyId())
                .soTietDangKy(request.getSoTietDangKy())
                .soTietDaPhanCong(0)
                .trangThai(request.getTrangThai() != null ? request.getTrangThai() : "dang_hieu_luc")
                .ghiChu(request.getGhiChu())
                .build();
    }

    public GiangVienDangKyGiangDayResponse toResponse(GiangVienDangKyGiangDay entity) {
        if (entity == null) return null;
        int soTietDangKy = entity.getSoTietDangKy() != null ? entity.getSoTietDangKy() : 0;
        int soTietDaPhanCong = entity.getSoTietDaPhanCong() != null ? entity.getSoTietDaPhanCong() : 0;

        GiangVienDangKyGiangDayResponse response = GiangVienDangKyGiangDayResponse.builder()
                .id(entity.getId())
                .giaoVienId(entity.getGiaoVienId())
                .khungKyId(entity.getKhungKyId())
                .soTietDangKy(soTietDangKy)
                .soTietDaPhanCong(soTietDaPhanCong)
                .soTietConLai(soTietDangKy - soTietDaPhanCong)
                .trangThai(entity.getTrangThai())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();

        if (entity.getGiaoVienId() != null) {
            giaoVienRepository.findById(entity.getGiaoVienId()).ifPresent(gv -> {
                response.setMaGiaoVien(gv.getMaGiaoVien());
                response.setHoTenGiaoVien(gv.getHoTen());
            });
        }

        if (entity.getKhungKyId() != null) {
            khungKyRepository.findById(entity.getKhungKyId()).ifPresent(ky -> {
                response.setMaKy(ky.getMaKy());
                response.setTenKy(ky.getTenKy());
                response.setChuongTrinhVersionId(ky.getChuongTrinhVersionId());

                // Enrich: version -> chương trình -> ngành
                if (ky.getChuongTrinhVersionId() != null) {
                    chuongTrinhVersionRepository.findById(ky.getChuongTrinhVersionId()).ifPresent(ver -> {
                        response.setTenVersion(ver.getTenVersion());
                        response.setChuongTrinhId(ver.getChuongTrinhId());

                        if (ver.getChuongTrinhId() != null) {
                            chuongTrinhRepository.findById(ver.getChuongTrinhId()).ifPresent(ct -> {
                                response.setTenChuongTrinh(ct.getTenChuongTrinh());
                                response.setNganhId(ct.getNganhId());

                                if (ct.getNganhId() != null) {
                                    nganhRepository.findById(ct.getNganhId()).ifPresent(nganh ->
                                            response.setTenNganh(nganh.getTenNganh()));
                                }
                            });
                        }
                    });
                }

                // Build label đầy đủ
                response.setLabelKhungKyDayDu(buildLabel(
                        response.getTenNganh(), response.getTenChuongTrinh(),
                        response.getTenVersion(), ky.getTenKy()));
            });
        }

        return response;
    }

    public void updateEntity(GiangVienDangKyGiangDay entity, GiangVienDangKyGiangDayRequest request) {
        if (entity == null || request == null) return;
        entity.setGiaoVienId(request.getGiaoVienId());
        entity.setKhungKyId(request.getKhungKyId());
        entity.setSoTietDangKy(request.getSoTietDangKy());
        if (request.getTrangThai() != null) entity.setTrangThai(request.getTrangThai());
        entity.setGhiChu(request.getGhiChu());
    }

    private String buildLabel(String tenNganh, String tenChuongTrinh, String tenVersion, String tenKy) {
        StringBuilder sb = new StringBuilder();
        if (tenNganh != null && !tenNganh.isBlank()) sb.append(tenNganh);
        if (tenChuongTrinh != null && !tenChuongTrinh.isBlank()) {
            if (sb.length() > 0) sb.append(" / ");
            sb.append(tenChuongTrinh);
        }
        if (tenVersion != null && !tenVersion.isBlank()) {
            if (sb.length() > 0) sb.append(" / ");
            sb.append(tenVersion);
        }
        if (tenKy != null && !tenKy.isBlank()) {
            if (sb.length() > 0) sb.append(" / ");
            sb.append(tenKy);
        }
        return sb.length() > 0 ? sb.toString() : tenKy;
    }
}
