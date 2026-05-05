package org.example.trungcapphuongnam.module.chuongTrinh.mapper;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.DieuKienMonHocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.DieuKienMonHocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.DieuKienMonHoc;
import org.springframework.stereotype.Component;

@Component
public class DieuKienMonHocMapper {

    public DieuKienMonHoc toEntity(DieuKienMonHocRequest request) {
        if (request == null) return null;
        return DieuKienMonHoc.builder()
                .syllabusMonId(request.getSyllabusMonId())
                .loai(request.getLoai())
                .noiDung(request.getNoiDung())
                .thuTu(request.getThuTu())
                .build();
    }

    public DieuKienMonHocResponse toResponse(DieuKienMonHoc entity) {
        if (entity == null) return null;
        return DieuKienMonHocResponse.builder()
                .id(entity.getId())
                .syllabusMonId(entity.getSyllabusMonId())
                .loai(entity.getLoai())
                .noiDung(entity.getNoiDung())
                .thuTu(entity.getThuTu())
                .build();
    }

    public void updateEntity(DieuKienMonHoc entity, DieuKienMonHocRequest request) {
        if (entity == null || request == null) return;
        entity.setSyllabusMonId(request.getSyllabusMonId());
        entity.setLoai(request.getLoai());
        entity.setNoiDung(request.getNoiDung());
        entity.setThuTu(request.getThuTu());
    }
}
