package org.example.trungcapphuongnam.module.giangDay.validator;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMon;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhMonRepository;
import org.example.trungcapphuongnam.module.giangDay.GiangDayException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.LopHocPhanChuongTrinhMonRequest;
import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.enums.LoaiLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.repository.LopHocPhanChuongTrinhMonRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.LopHocPhanRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LopHocPhanChuongTrinhMonValidator {

    private final LopHocPhanRepository lopHocPhanRepository;
    private final ChuongTrinhMonRepository chuongTrinhMonRepository;
    private final LopHocPhanChuongTrinhMonRepository repository;

    public void validateCreate(LopHocPhanChuongTrinhMonRequest request) {
        validateCommon(request);

        if (repository.existsByLopHocPhanIdAndChuongTrinhMonId(
                request.getLopHocPhanId(),
                request.getChuongTrinhMonId()
        )) {
            throw new GiangDayException("Chương trình môn này đã được gắn vào lớp học phần");
        }

        if (repository.existsByChuongTrinhMonId(request.getChuongTrinhMonId())) {
            throw new GiangDayException("Môn này đã có lớp học phần học chung, không được gắn thêm vào lớp học phần khác");
        }

        if (lopHocPhanRepository.existsByChuongTrinhMonId(request.getChuongTrinhMonId())) {
            throw new GiangDayException("Môn này đã có lớp học phần chuyên ngành, không được gắn thêm vào lớp học chung");
        }
    }

    public void validateUpdate(Long id, LopHocPhanChuongTrinhMonRequest request) {
        if (id == null) {
            throw new GiangDayException("Bản ghi gắn chương trình môn không hợp lệ");
        }

        if (!repository.existsById(id)) {
            throw new GiangDayException("Không tìm thấy bản ghi gắn chương trình môn cần cập nhật");
        }

        validateCommon(request);

        if (repository.existsByLopHocPhanIdAndChuongTrinhMonIdAndIdNot(
                request.getLopHocPhanId(),
                request.getChuongTrinhMonId(),
                id
        )) {
            throw new GiangDayException("Chương trình môn này đã được gắn vào lớp học phần");
        }
    }

    private void validateCommon(LopHocPhanChuongTrinhMonRequest request) {
        if (request == null) {
            throw new GiangDayException("Dữ liệu gắn chương trình môn vào lớp học phần không hợp lệ");
        }

        if (request.getLopHocPhanId() == null) {
            throw new GiangDayException("Lớp học phần không được để trống");
        }

        if (request.getChuongTrinhMonId() == null) {
            throw new GiangDayException("Chương trình môn không được để trống");
        }

        LopHocPhan lopHocPhan = lopHocPhanRepository.findById(request.getLopHocPhanId())
                .orElseThrow(() -> new GiangDayException("Lớp học phần không tồn tại"));

        ChuongTrinhMon chuongTrinhMon = chuongTrinhMonRepository.findById(request.getChuongTrinhMonId())
                .orElseThrow(() -> new GiangDayException("Chương trình môn không tồn tại"));

        if (lopHocPhan.getLoaiLopHocPhan() == null) {
            throw new GiangDayException("Lớp học phần chưa có loại lớp");
        }

        if (lopHocPhan.getLoaiLopHocPhan() != LoaiLopHocPhan.HOC_CHUNG) {
            throw new GiangDayException("Chỉ lớp học chung mới được gắn nhiều chương trình môn");
        }

        if (lopHocPhan.getTrangThai() == TrangThaiLopHocPhan.huy) {
            throw new GiangDayException("Lớp học phần đã hủy, không được gắn chương trình môn");
        }

        if (lopHocPhan.getTrangThai() == TrangThaiLopHocPhan.da_ket_thuc) {
            throw new GiangDayException("Lớp học phần đã kết thúc, không được gắn chương trình môn");
        }

        if (lopHocPhan.getMonHocId() == null) {
            throw new GiangDayException("Lớp học chung chưa chọn môn học");
        }

        if (!lopHocPhan.getMonHocId().equals(chuongTrinhMon.getMonHocId())) {
            throw new GiangDayException("Chương trình môn gắn vào lớp học chung phải cùng môn học với lớp");
        }
    }
}