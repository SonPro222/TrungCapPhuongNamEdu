package org.example.trungcapphuongnam.module.heThong.mapper;


import org.example.trungcapphuongnam.module.heThong.enums.LoaiTaiKhoan;
import org.example.trungcapphuongnam.module.heThong.enums.TrangThaiTaiKhoan;
import org.example.trungcapphuongnam.module.heThong.dto.request.TaiKhoanRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.TaiKhoanResponse;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoan;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoanVaiTro;
import org.example.trungcapphuongnam.module.heThong.entity.VaiTroQuyen;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component
public class TaiKhoanMapper {

    public TaiKhoanResponse toResponse(TaiKhoan entity) {
        if (entity == null) {
            return null;
        }

        Set<TaiKhoanVaiTro> taiKhoanVaiTros = entity.getTaiKhoanVaiTros() == null
                ? Collections.emptySet()
                : entity.getTaiKhoanVaiTros();

        List<String> roles = taiKhoanVaiTros.stream()
                .filter(tv -> tv.getVaiTro() != null)
                .map(tv -> tv.getVaiTro().getMaVaiTro())
                .filter(value -> value != null && !value.isBlank())
                .distinct()
                .toList();

        List<String> permissions = taiKhoanVaiTros.stream()
                .filter(tv -> tv.getVaiTro() != null)
                .flatMap(tv -> {
                    Set<VaiTroQuyen> vaiTroQuyens = tv.getVaiTro().getVaiTroQuyens() == null
                            ? Collections.emptySet()
                            : tv.getVaiTro().getVaiTroQuyens();
                    return vaiTroQuyens.stream();
                })
                .filter(vtq -> vtq.getQuyen() != null)
                .map(vtq -> vtq.getQuyen().getMaQuyen())
                .filter(value -> value != null && !value.isBlank())
                .distinct()
                .toList();

        return TaiKhoanResponse.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .loaiTaiKhoan(entity.getLoaiTaiKhoan() == null ? null : entity.getLoaiTaiKhoan().name())
                .trangThai(entity.getTrangThai() == null ? null : entity.getTrangThai().name())
                .roles(roles)
                .permissions(permissions)
                .build();
    }

    public TaiKhoan toEntity(TaiKhoanRequest request) {
        if (request == null) {
            return null;
        }

        TaiKhoan entity = new TaiKhoan();
        entity.setEmail(normalizeEmail(request.getEmail()));
        entity.setLoaiTaiKhoan(parseLoaiTaiKhoan(request.getLoaiTaiKhoan()));
        entity.setTrangThai(parseTrangThai(request.getTrangThai()));
        return entity;
    }

    public void updateEntity(TaiKhoan entity, TaiKhoanRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setEmail(normalizeEmail(request.getEmail()));
        entity.setLoaiTaiKhoan(parseLoaiTaiKhoan(request.getLoaiTaiKhoan()));
        entity.setTrangThai(parseTrangThai(request.getTrangThai()));
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    private LoaiTaiKhoan parseLoaiTaiKhoan(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return LoaiTaiKhoan.valueOf(value.trim().toLowerCase());
    }

    private TrangThaiTaiKhoan parseTrangThai(String value) {
        if (value == null || value.isBlank()) {
            return TrangThaiTaiKhoan.cho_kich_hoat;
        }
        return TrangThaiTaiKhoan.valueOf(value.trim().toLowerCase());
    }
}