package org.example.trungcapphuongnam.module.heThong.validator;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhVersionRepository;
import org.example.trungcapphuongnam.module.daoTao.repository.NganhRepository;
import org.example.trungcapphuongnam.module.heThong.HeThongException;
import org.example.trungcapphuongnam.module.heThong.dto.request.CauHinhMaSinhVienRequest;
import org.example.trungcapphuongnam.module.heThong.repository.CauHinhMaSinhVienRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CauHinhMaSinhVienValidator {

    private final CauHinhMaSinhVienRepository repository;
    private final NganhRepository nganhRepository;
    private final ChuongTrinhVersionRepository chuongTrinhVersionRepository;

    public void validateCreate(CauHinhMaSinhVienRequest request) {
        validateCommon(request);

        Long nganhId = request.getNganhId();
        Long chuongTrinhVersionId = request.getChuongTrinhVersionId();
        String tienTo = chuanHoaTienTo(request.getTienTo());
        String maDau = chuanHoaMaDau(request.getMaDau());

        if (repository.existsByNganhIdAndChuongTrinhVersionId(nganhId, chuongTrinhVersionId)) {
            throw new HeThongException("Cấu hình mã sinh viên cho ngành và version này đã tồn tại");
        }

        if (repository.existsByTienToIgnoreCase(tienTo)) {
            throw new HeThongException("Tiền tố này đã được sử dụng cho ngành khác. Mỗi ngành phải có một tiền tố riêng");
        }

        if (repository.existsByMaDauIgnoreCase(maDau)) {
            throw new HeThongException("Mã đầu này đã được sử dụng cho version khác. Mỗi version phải có một mã đầu riêng");
        }
    }

    public void validateUpdate(Long id, CauHinhMaSinhVienRequest request) {
        if (id == null) {
            throw new HeThongException("Cấu hình mã sinh viên cần cập nhật không hợp lệ");
        }

        if (!repository.existsById(id)) {
            throw new HeThongException("Không tìm thấy cấu hình mã sinh viên cần cập nhật");
        }

        validateCommon(request);

        Long nganhId = request.getNganhId();
        Long chuongTrinhVersionId = request.getChuongTrinhVersionId();
        String tienTo = chuanHoaTienTo(request.getTienTo());
        String maDau = chuanHoaMaDau(request.getMaDau());

        if (repository.existsByNganhIdAndChuongTrinhVersionIdAndIdNot(nganhId, chuongTrinhVersionId, id)) {
            throw new HeThongException("Cấu hình mã sinh viên cho ngành và version này đã tồn tại");
        }

        if (repository.existsByTienToIgnoreCaseAndIdNot(tienTo, id)) {
            throw new HeThongException("Tiền tố này đã được sử dụng cho ngành khác. Mỗi ngành phải có một tiền tố riêng");
        }

        if (repository.existsByMaDauIgnoreCaseAndIdNot(maDau, id)) {
            throw new HeThongException("Mã đầu này đã được sử dụng cho version khác. Mỗi version phải có một mã đầu riêng");
        }
    }

    public void validateDelete(Long id) {
        if (id == null) {
            throw new HeThongException("Cấu hình mã sinh viên cần xóa không hợp lệ");
        }

        if (!repository.existsById(id)) {
            throw new HeThongException("Không tìm thấy cấu hình mã sinh viên cần xóa");
        }
    }

    private void validateCommon(CauHinhMaSinhVienRequest request) {
        if (request == null) {
            throw new HeThongException("Dữ liệu cấu hình mã sinh viên không hợp lệ");
        }

        if (request.getNganhId() == null) {
            throw new HeThongException("Ngành không được để trống");
        }

        if (!nganhRepository.existsById(request.getNganhId())) {
            throw new HeThongException("Ngành được chọn không tồn tại");
        }

        if (request.getChuongTrinhVersionId() == null) {
            throw new HeThongException("Version chương trình không được để trống");
        }

        if (!chuongTrinhVersionRepository.existsById(request.getChuongTrinhVersionId())) {
            throw new HeThongException("Version chương trình được chọn không tồn tại");
        }

        String tienTo = chuanHoaTienTo(request.getTienTo());
        String maDau = chuanHoaMaDau(request.getMaDau());
        Integer soHienTai = chuanHoaSoHienTai(request.getSoHienTai());
        Integer doDaiSoThuTu = chuanHoaDoDaiSoThuTu(request.getDoDaiSoThuTu());

        if (tienTo.isBlank()) {
            throw new HeThongException("Tiền tố mã sinh viên không được để trống");
        }

        if (tienTo.length() > 20) {
            throw new HeThongException("Tiền tố mã sinh viên không được vượt quá 20 ký tự");
        }

        if (!tienTo.matches("^[A-Z0-9]+$")) {
            throw new HeThongException("Tiền tố mã sinh viên chỉ được gồm chữ in hoa và số, không dùng dấu hoặc ký tự đặc biệt");
        }

        if (maDau.isBlank()) {
            throw new HeThongException("Mã đầu không được để trống");
        }

        if (maDau.length() > 20) {
            throw new HeThongException("Mã đầu không được vượt quá 20 ký tự");
        }

        if (!maDau.matches("^[A-Z0-9]+$")) {
            throw new HeThongException("Mã đầu chỉ được gồm chữ in hoa và số, không dùng dấu hoặc ký tự đặc biệt");
        }

        if (soHienTai < 0) {
            throw new HeThongException("Số hiện tại không được nhỏ hơn 0");
        }

        if (doDaiSoThuTu <= 0) {
            throw new HeThongException("Độ dài số thứ tự phải lớn hơn 0");
        }

        if (doDaiSoThuTu > 10) {
            throw new HeThongException("Độ dài số thứ tự không được vượt quá 10");
        }

        int soToiDaTheoDoDai = tinhSoToiDaTheoDoDai(doDaiSoThuTu);

        if (soHienTai > soToiDaTheoDoDai) {
            throw new HeThongException("Số hiện tại vượt quá độ dài số thứ tự đã cấu hình");
        }

        String maSinhVienTiepTheo = taoMaSinhVienTiepTheo(tienTo, maDau, soHienTai, doDaiSoThuTu);

        if (maSinhVienTiepTheo.length() > 50) {
            throw new HeThongException("Mã sinh viên sinh ra quá dài. Vui lòng rút ngắn tiền tố hoặc mã đầu");
        }
    }

    public String chuanHoaTienTo(String tienTo) {
        return tienTo == null ? "" : tienTo.trim().toUpperCase();
    }

    public String chuanHoaMaDau(String maDau) {
        return maDau == null ? "" : maDau.trim().toUpperCase();
    }

    public Integer chuanHoaSoHienTai(Integer soHienTai) {
        return soHienTai == null ? 0 : soHienTai;
    }

    public Integer chuanHoaDoDaiSoThuTu(Integer doDaiSoThuTu) {
        return doDaiSoThuTu == null ? 3 : doDaiSoThuTu;
    }

    private int tinhSoToiDaTheoDoDai(Integer doDaiSoThuTu) {
        int soToiDa = 1;

        for (int i = 0; i < doDaiSoThuTu; i++) {
            soToiDa *= 10;
        }

        return soToiDa - 1;
    }

    private String taoMaSinhVienTiepTheo(String tienTo, String maDau, Integer soHienTai, Integer doDaiSoThuTu) {
        int soTiepTheo = soHienTai + 1;
        return tienTo + maDau + String.format("%0" + doDaiSoThuTu + "d", soTiepTheo);
    }
}