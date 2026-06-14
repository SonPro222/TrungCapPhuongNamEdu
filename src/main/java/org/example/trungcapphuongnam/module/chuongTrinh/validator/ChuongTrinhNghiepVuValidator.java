package org.example.trungcapphuongnam.module.chuongTrinh.validator;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.DuplicateResourceException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.*;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinh;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMon;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersion;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.NangLucDauRaMau;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.NhomKienThuc;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.NhomTuChon;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ViTriViecLamMau;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.*;
import org.example.trungcapphuongnam.module.daoTao.entity.KhungKy;
import org.example.trungcapphuongnam.module.daoTao.entity.NganhHeDaoTao;
import org.example.trungcapphuongnam.module.daoTao.entity.NganhLoaiChuongTrinh;
import org.example.trungcapphuongnam.module.daoTao.repository.KhungKyRepository;
import org.example.trungcapphuongnam.module.daoTao.repository.NganhHeDaoTaoRepository;
import org.example.trungcapphuongnam.module.daoTao.repository.NganhLoaiChuongTrinhRepository;
import org.example.trungcapphuongnam.module.daoTao.repository.NganhTrinhDoDaoTaoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class ChuongTrinhNghiepVuValidator {

    private final NganhLoaiChuongTrinhRepository nganhLoaiChuongTrinhRepository;
    private final NganhHeDaoTaoRepository nganhHeDaoTaoRepository;
    private final NganhTrinhDoDaoTaoRepository nganhTrinhDoDaoTaoRepository;
    private final SyllabusChuongTrinhMauRepository syllabusChuongTrinhMauRepository;
    private final KhungKyRepository khungKyRepository;

    private final ChuongTrinhRepository chuongTrinhRepository;
    private final ChuongTrinhVersionRepository chuongTrinhVersionRepository;
    private final ChuongTrinhMonRepository chuongTrinhMonRepository;
    private final ChuongTrinhMonQuyDoiDiemMauRepository chuongTrinhMonQuyDoiDiemMauRepository;

    private final MonHocRepository monHocRepository;
    private final NhomKienThucRepository nhomKienThucRepository;
    private final NhomTuChonRepository nhomTuChonRepository;
    private final MonTuChonRepository monTuChonRepository;
    private final MonTienQuyetRepository monTienQuyetRepository;
    private final QuyDoiDiemRepository quyDoiDiemRepository;
    private final QuyDoiDiemMauRepository quyDoiDiemMauRepository;

    private final SyllabusChuongTrinhRepository syllabusChuongTrinhRepository;
    private final SyllabusMonHocRepository syllabusMonHocRepository;
    private final DieuKienMonHocRepository dieuKienMonHocRepository;
    private final SyllabusChuongBaiRepository syllabusChuongBaiRepository;
    private final SyllabusTaiLieuRepository syllabusTaiLieuRepository;

    private final MucTieuChuongTrinhRepository mucTieuChuongTrinhRepository;
    private final NangLucDauRaRepository nangLucDauRaRepository;
    private final ViTriViecLamRepository viTriViecLamRepository;
    private final DieuKienTotNghiepRepository dieuKienTotNghiepRepository;

    private final MucTieuChuongTrinhMauRepository mucTieuChuongTrinhMauRepository;
    private final NangLucDauRaMauRepository nangLucDauRaMauRepository;
    private final ViTriViecLamMauRepository viTriViecLamMauRepository;
    private final DieuKienTotNghiepMauRepository dieuKienTotNghiepMauRepository;

    private final ChuongTrinhVersionMucTieuRepository chuongTrinhVersionMucTieuRepository;
    private final ChuongTrinhVersionNangLucRepository chuongTrinhVersionNangLucRepository;
    private final ChuongTrinhVersionViTriViecLamRepository chuongTrinhVersionViTriViecLamRepository;
    private final ChuongTrinhVersionDieuKienTotNghiepRepository chuongTrinhVersionDieuKienTotNghiepRepository;

    private final DieuKienMonHocMauRepository dieuKienMonHocMauRepository;
    private final TaiLieuMauRepository taiLieuMauRepository;

    private final SyllabusMonHocMauRepository syllabusMonHocMauRepository;
    private final SyllabusMonHocMauChuongBaiRepository syllabusMonHocMauChuongBaiRepository;
    private final SyllabusMonHocMauDieuKienRepository syllabusMonHocMauDieuKienRepository;
    private final SyllabusMonHocMauTaiLieuRepository syllabusMonHocMauTaiLieuRepository;
    private final SyllabusMonHocDieuKienRepository syllabusMonHocDieuKienRepository;
    private final SyllabusMonHocTaiLieuRepository syllabusMonHocTaiLieuRepository;

    private final NhomKienThucMauRepository nhomKienThucMauRepository;
    private final NhomTuChonMauRepository nhomTuChonMauRepository;


    public void validateNhomKienThucMau(NhomKienThucMauRequest request, Long id) {
        notNull(request, "Dữ liệu nhóm kiến thức gốc không hợp lệ");

        String ma = trimRequired(request.getMa(), "Mã nhóm kiến thức gốc");
        trimRequired(request.getTen(), "Tên nhóm kiến thức gốc");
        String loaiNhom = trimRequired(request.getLoaiNhom(), "Loại nhóm kiến thức gốc");

        if (!List.of("chung", "co_so", "chuyen_mon", "tu_chon").contains(loaiNhom)) {
            throw new BadRequestException("Loại nhóm kiến thức gốc không hợp lệ: " + loaiNhom);
        }

        if (id == null && nhomKienThucMauRepository.existsByMa(ma)) {
            throw new DuplicateResourceException("Mã nhóm kiến thức gốc đã tồn tại: " + ma);
        }

        if (id != null && nhomKienThucMauRepository.existsByMaAndIdNot(ma, id)) {
            throw new DuplicateResourceException("Mã nhóm kiến thức gốc đã tồn tại: " + ma);
        }
    }

    public void validateNhomTuChonMau(NhomTuChonMauRequest request, Long id) {
        notNull(request, "Dữ liệu nhóm tự chọn gốc không hợp lệ");

        String ma = trimRequired(request.getMa(), "Mã nhóm tự chọn gốc");
        trimRequired(request.getTen(), "Tên nhóm tự chọn gốc");

        if (id == null && nhomTuChonMauRepository.existsByMa(ma)) {
            throw new DuplicateResourceException("Mã nhóm tự chọn gốc đã tồn tại: " + ma);
        }

        if (id != null && nhomTuChonMauRepository.existsByMaAndIdNot(ma, id)) {
            throw new DuplicateResourceException("Mã nhóm tự chọn gốc đã tồn tại: " + ma);
        }
    }
    public void validateChuongTrinh(ChuongTrinhRequest request, Long id) {
        notNull(request, "Dữ liệu chương trình không hợp lệ");

        String ma = trimRequired(request.getMaChuongTrinh(), "Mã chương trình");
        trimRequired(request.getTenChuongTrinh(), "Tên chương trình");

        Long nganhHeDaoTaoId = request.getNganhHeDaoTaoId();
        if (nganhHeDaoTaoId == null) {
            throw new BadRequestException("nganhHeDaoTaoId không được để trống");
        }

        if (!nganhHeDaoTaoRepository.existsById(nganhHeDaoTaoId)) {
            throw new ResourceNotFoundException("Ngành hệ đào tạo không tồn tại: " + nganhHeDaoTaoId);
        }

        if (id == null && chuongTrinhRepository.existsByMaChuongTrinh(ma)) {
            throw new DuplicateResourceException("Mã chương trình đã tồn tại: " + ma);
        }

        if (id != null && chuongTrinhRepository.existsByMaChuongTrinhAndIdNot(ma, id)) {
            throw new DuplicateResourceException("Mã chương trình đã tồn tại: " + ma);
        }
    }
    public void validateSyllabusChuongTrinhMau(SyllabusChuongTrinhMauRequest request, Long id) {
        notNull(request, "Dữ liệu syllabus chương trình gốc không hợp lệ");

        String ma = trimRequired(request.getMa(), "Mã syllabus chương trình gốc");
        trimRequired(request.getTen(), "Tên syllabus chương trình gốc");

        if (request.getChuongTrinhId() != null) {
            requireExists(chuongTrinhRepository, request.getChuongTrinhId(), "Chương trình đào tạo");
        }

        if (id == null && syllabusChuongTrinhMauRepository.existsByMa(ma)) {
            throw new DuplicateResourceException("Mã syllabus chương trình gốc đã tồn tại: " + ma);
        }

        if (id != null && syllabusChuongTrinhMauRepository.existsByMaAndIdNot(ma, id)) {
            throw new DuplicateResourceException("Mã syllabus chương trình gốc đã tồn tại: " + ma);
        }
    }
    public void validateChuongTrinhVersion(ChuongTrinhVersionRequest request, Long id) {
        notNull(request, "Dữ liệu phiên bản chương trình không hợp lệ");

        Long chuongTrinhId = requireId(request.getChuongTrinhId(), "chuongTrinhId");
        String maVersion = trimRequired(request.getMaVersion(), "Mã phiên bản");
        trimRequired(request.getTenVersion(), "Tên phiên bản");

        requireExists(chuongTrinhRepository, chuongTrinhId, "Chương trình");

        if (request.getNgayApDung() != null
                && request.getNgayHetHieuLuc() != null
                && request.getNgayHetHieuLuc().isBefore(request.getNgayApDung())) {
            throw new BadRequestException("Ngày hết hiệu lực không được nhỏ hơn ngày áp dụng");
        }

        if (request.getNgayQuyetDinh() != null
                && request.getNgayApDung() != null
                && request.getNgayQuyetDinh().isAfter(request.getNgayApDung())) {
            throw new BadRequestException("Ngày quyết định không được sau ngày áp dụng");
        }

        if (request.getNgayApDung() != null && request.getNgayHetHieuLuc() != null) {
            validateThoiHanVersion(chuongTrinhId, request.getNgayApDung(), request.getNgayHetHieuLuc());
        }

        if (id == null && chuongTrinhVersionRepository.existsByChuongTrinhIdAndMaVersion(chuongTrinhId, maVersion)) {
            throw new DuplicateResourceException("Mã phiên bản đã tồn tại trong chương trình: " + maVersion);
        }

        if (id != null && chuongTrinhVersionRepository.existsByChuongTrinhIdAndMaVersionAndIdNot(chuongTrinhId, maVersion, id)) {
            throw new DuplicateResourceException("Mã phiên bản đã tồn tại trong chương trình: " + maVersion);
        }


    }
    public void validateSyllabusMonHocDieuKien(SyllabusMonHocDieuKienRequest request, Long id) {
        notNull(request, "Dữ liệu gán điều kiện môn học vào syllabus không hợp lệ");

        Long syllabusMonId = requireId(request.getSyllabusMonId(), "syllabusMonId");
        Long dieuKienMauId = requireId(request.getDieuKienMauId(), "dieuKienMauId");

        requireExists(syllabusMonHocRepository, syllabusMonId, "Syllabus môn học");
        requireExists(dieuKienMonHocMauRepository, dieuKienMauId, "Điều kiện môn học gốc");

        positiveOrZeroInt(request.getThuTu(), "Thứ tự điều kiện môn học");

        if (id == null && syllabusMonHocDieuKienRepository.existsBySyllabusMonIdAndDieuKienMauId(
                syllabusMonId,
                dieuKienMauId
        )) {
            throw new DuplicateResourceException("Điều kiện môn học gốc đã được gán cho syllabus này");
        }

        if (id != null && syllabusMonHocDieuKienRepository.existsBySyllabusMonIdAndDieuKienMauIdAndIdNot(
                syllabusMonId,
                dieuKienMauId,
                id
        )) {
            throw new DuplicateResourceException("Điều kiện môn học gốc đã được gán cho syllabus này");
        }

        if (request.getThuTu() != null) {
            if (id == null && syllabusMonHocDieuKienRepository.existsBySyllabusMonIdAndThuTu(
                    syllabusMonId,
                    request.getThuTu()
            )) {
                throw new DuplicateResourceException("Thứ tự điều kiện môn học đã tồn tại trong syllabus: " + request.getThuTu());
            }

            if (id != null && syllabusMonHocDieuKienRepository.existsBySyllabusMonIdAndThuTuAndIdNot(
                    syllabusMonId,
                    request.getThuTu(),
                    id
            )) {
                throw new DuplicateResourceException("Thứ tự điều kiện môn học đã tồn tại trong syllabus: " + request.getThuTu());
            }
        }
    }
    public void validateSyllabusMonHocTaiLieu(SyllabusMonHocTaiLieuRequest request, Long id) {
        notNull(request, "Dữ liệu gán tài liệu gốc vào syllabus không hợp lệ");

        Long syllabusMonId = requireId(request.getSyllabusMonId(), "syllabusMonId");
        Long taiLieuMauId = requireId(request.getTaiLieuMauId(), "taiLieuMauId");

        requireExists(syllabusMonHocRepository, syllabusMonId, "Syllabus môn học");
        requireExists(taiLieuMauRepository, taiLieuMauId, "Tài liệu gốc");

        positiveOrZeroInt(request.getThuTu(), "Thứ tự tài liệu syllabus");

        if (id == null && syllabusMonHocTaiLieuRepository.existsBySyllabusMonIdAndTaiLieuMauId(
                syllabusMonId,
                taiLieuMauId
        )) {
            throw new DuplicateResourceException("Tài liệu gốc đã được gán cho syllabus này");
        }

        if (id != null && syllabusMonHocTaiLieuRepository.existsBySyllabusMonIdAndTaiLieuMauIdAndIdNot(
                syllabusMonId,
                taiLieuMauId,
                id
        )) {
            throw new DuplicateResourceException("Tài liệu gốc đã được gán cho syllabus này");
        }

        if (request.getThuTu() != null) {
            if (id == null && syllabusMonHocTaiLieuRepository.existsBySyllabusMonIdAndThuTu(
                    syllabusMonId,
                    request.getThuTu()
            )) {
                throw new DuplicateResourceException("Thứ tự tài liệu đã tồn tại trong syllabus: " + request.getThuTu());
            }

            if (id != null && syllabusMonHocTaiLieuRepository.existsBySyllabusMonIdAndThuTuAndIdNot(
                    syllabusMonId,
                    request.getThuTu(),
                    id
            )) {
                throw new DuplicateResourceException("Thứ tự tài liệu đã tồn tại trong syllabus: " + request.getThuTu());
            }
        }
    }
    public void validateChuongTrinhMon(ChuongTrinhMonRequest request, Long id) {
        notNull(request, "Dữ liệu môn trong chương trình không hợp lệ");

        Long versionId = requireId(request.getChuongTrinhVersionId(), "chuongTrinhVersionId");
        Long monHocId = requireId(request.getMonHocId(), "monHocId");
        // khungKyId cho phép null: copy môn vào version trước, gán kỳ sau.
        Long khungKyId = request.getKhungKyId();

        String maMonTrongCt = trimRequired(request.getMaMonTrongCt(), "Mã môn trong chương trình");
        trimRequired(request.getLoai(), "Loại môn trong chương trình");
        trimRequired(request.getLoaiHocPhan(), "Loại học phần");

        requireExists(chuongTrinhVersionRepository, versionId, "Phiên bản chương trình");
        requireExists(monHocRepository, monHocId, "Môn học");

        if (khungKyId != null) {
            KhungKy khungKy = khungKyRepository.findById(khungKyId)
                    .orElseThrow(() -> new ResourceNotFoundException("Khung kỳ không tồn tại: " + khungKyId));

            if (!versionId.equals(khungKy.getChuongTrinhVersionId())) {
                throw new BadRequestException("Khung kỳ không thuộc đúng phiên bản chương trình đang chọn");
            }
        }

        if (request.getNhomKienThucId() != null) {
            NhomKienThuc nhom = nhomKienThucRepository.findById(request.getNhomKienThucId())
                    .orElseThrow(() -> new ResourceNotFoundException("Nhóm kiến thức không tồn tại: " + request.getNhomKienThucId()));

            if (!versionId.equals(nhom.getChuongTrinhVersionId())) {
                throw new BadRequestException("Nhóm kiến thức không thuộc đúng phiên bản chương trình đang chọn");
            }
        }
        positiveOrZeroInt(request.getThuTu(), "Thứ tự môn trong kỳ");


        if (khungKyId != null) {
            if (id == null && chuongTrinhMonRepository.existsByChuongTrinhVersionIdAndKhungKyIdAndMaMonTrongCt(versionId, khungKyId, maMonTrongCt)) {
                throw new DuplicateResourceException("Mã môn trong chương trình đã tồn tại trong kỳ này: " + maMonTrongCt);
            }

            if (id != null && chuongTrinhMonRepository.existsByChuongTrinhVersionIdAndKhungKyIdAndMaMonTrongCtAndIdNot(versionId, khungKyId, maMonTrongCt, id)) {
                throw new DuplicateResourceException("Mã môn trong chương trình đã tồn tại trong kỳ này: " + maMonTrongCt);
            }
        }

        if (id == null && chuongTrinhMonRepository.existsByChuongTrinhVersionIdAndMonHocId(versionId, monHocId)) {
            throw new DuplicateResourceException("Môn học này đã được thêm vào version hiện tại rồi");
        }

        if (id != null && chuongTrinhMonRepository.existsByChuongTrinhVersionIdAndMonHocIdAndIdNot(versionId, monHocId, id)) {
            throw new DuplicateResourceException("Môn học này đã được thêm vào version hiện tại rồi");
        }

        if (khungKyId != null && request.getThuTu() != null) {
            if (id == null && chuongTrinhMonRepository.existsByChuongTrinhVersionIdAndKhungKyIdAndThuTu(versionId, khungKyId, request.getThuTu())) {
                throw new DuplicateResourceException("Thứ tự môn đã tồn tại trong kỳ đang chọn: " + request.getThuTu());
            }

            if (id != null && chuongTrinhMonRepository.existsByChuongTrinhVersionIdAndKhungKyIdAndThuTuAndIdNot(versionId, khungKyId, request.getThuTu(), id)) {
                throw new DuplicateResourceException("Thứ tự môn đã tồn tại trong kỳ đang chọn: " + request.getThuTu());
            }
        }
    }

    public void validateNhomKienThuc(NhomKienThucRequest request, Long id) {
        notNull(request, "Dữ liệu nhóm kiến thức không hợp lệ");

        Long versionId = requireId(request.getChuongTrinhVersionId(), "chuongTrinhVersionId");
        String ma = trimRequired(request.getMa(), "Mã nhóm kiến thức");
        trimRequired(request.getTen(), "Tên nhóm kiến thức");

        requireExists(chuongTrinhVersionRepository, versionId, "Phiên bản chương trình");

        positiveOrZeroInt(request.getThuTu(), "Thứ tự nhóm kiến thức");

        if (id == null && nhomKienThucRepository.existsByChuongTrinhVersionIdAndMa(versionId, ma)) {
            throw new DuplicateResourceException("Mã nhóm kiến thức đã tồn tại trong version: " + ma);
        }

        if (id != null && nhomKienThucRepository.existsByChuongTrinhVersionIdAndMaAndIdNot(versionId, ma, id)) {
            throw new DuplicateResourceException("Mã nhóm kiến thức đã tồn tại trong version: " + ma);
        }

        if (request.getThuTu() != null) {
            if (id == null && nhomKienThucRepository.existsByChuongTrinhVersionIdAndThuTu(versionId, request.getThuTu())) {
                throw new DuplicateResourceException("Thứ tự nhóm kiến thức đã tồn tại trong version: " + request.getThuTu());
            }

            if (id != null && nhomKienThucRepository.existsByChuongTrinhVersionIdAndThuTuAndIdNot(versionId, request.getThuTu(), id)) {
                throw new DuplicateResourceException("Thứ tự nhóm kiến thức đã tồn tại trong version: " + request.getThuTu());
            }
        }
        if (request.getNhomKienThucMauId() != null) {
            requireExists(
                    nhomKienThucMauRepository,
                    request.getNhomKienThucMauId(),
                    "Nhóm kiến thức gốc"
            );
        }
        if (request.getNhomKienThucMauId() != null) {
            Long MauId = request.getNhomKienThucMauId();

            if (id == null && nhomKienThucRepository.existsByChuongTrinhVersionIdAndNhomKienThucMauId(versionId, MauId)) {
                throw new DuplicateResourceException("Nhóm kiến thức gốc đã được gán vào version này");
            }

            if (id != null && nhomKienThucRepository.existsByChuongTrinhVersionIdAndNhomKienThucMauIdAndIdNot(versionId, MauId, id)) {
                throw new DuplicateResourceException("Nhóm kiến thức gốc đã được gán vào version này");
            }
        }
    }

    public void validateNhomTuChon(NhomTuChonRequest request, Long id) {
        notNull(request, "Dữ liệu nhóm tự chọn không hợp lệ");

        Long versionId = requireId(request.getChuongTrinhVersionId(), "chuongTrinhVersionId");
        String ten = trimRequired(request.getTen(), "Tên nhóm tự chọn");

        requireExists(chuongTrinhVersionRepository, versionId, "Phiên bản chương trình");

        positiveOrZeroInt(request.getSoMonChon(), "Số môn chọn");
        positiveOrZero(request.getSoTinChiCanDat(), "Số tín chỉ cần đạt");

        if (request.getSoMonChon() != null && request.getSoMonChon() == 0) {
            throw new BadRequestException("Số môn chọn phải lớn hơn 0");
        }

        if (request.getSoTinChiCanDat() != null && request.getSoTinChiCanDat().compareTo(BigDecimal.ZERO) == 0) {
            throw new BadRequestException("Số tín chỉ cần đạt phải lớn hơn 0");
        }

        if (id == null && nhomTuChonRepository.existsByChuongTrinhVersionIdAndTen(versionId, ten)) {
            throw new DuplicateResourceException("Tên nhóm tự chọn đã tồn tại trong version: " + ten);
        }

        if (id != null && nhomTuChonRepository.existsByChuongTrinhVersionIdAndTenAndIdNot(versionId, ten, id)) {
            throw new DuplicateResourceException("Tên nhóm tự chọn đã tồn tại trong version: " + ten);
        }
        if (request.getNhomTuChonMauId() != null) {
            requireExists(
                    nhomTuChonMauRepository,
                    request.getNhomTuChonMauId(),
                    "Nhóm tự chọn gốc"
            );
        }
        if (request.getNhomTuChonMauId() != null) {
            Long MauId = request.getNhomTuChonMauId();

            if (id == null && nhomTuChonRepository.existsByChuongTrinhVersionIdAndNhomTuChonMauId(versionId, MauId)) {
                throw new DuplicateResourceException("Nhóm tự chọn gốc đã được gán vào version này");
            }

            if (id != null && nhomTuChonRepository.existsByChuongTrinhVersionIdAndNhomTuChonMauIdAndIdNot(versionId, MauId, id)) {
                throw new DuplicateResourceException("Nhóm tự chọn gốc đã được gán vào version này");
            }
        }
    }

    public void validateMonHoc(MonHocRequest request, Long id) {
        notNull(request, "Dữ liệu môn học không hợp lệ");

        String ma = trimRequired(request.getMaMon(), "Mã môn học");
        trimRequired(request.getTenMon(), "Tên môn học");

        if (id == null && monHocRepository.existsByMaMon(ma)) {
            throw new DuplicateResourceException("Mã môn học đã tồn tại: " + ma);
        }

        if (id != null && monHocRepository.existsByMaMonAndIdNot(ma, id)) {
            throw new DuplicateResourceException("Mã môn học đã tồn tại: " + ma);
        }
    }

    public void validateMonTuChon(MonTuChonRequest request, Long id) {
        notNull(request, "Dữ liệu môn tự chọn không hợp lệ");

        Long nhomId = requireId(request.getNhomId(), "nhomId");
        Long chuongTrinhMonId = requireId(request.getChuongTrinhMonId(), "chuongTrinhMonId");

        NhomTuChon nhom = nhomTuChonRepository.findById(nhomId)
                .orElseThrow(() -> new ResourceNotFoundException("Nhóm tự chọn không tồn tại: " + nhomId));

        ChuongTrinhMon mon = chuongTrinhMonRepository.findById(chuongTrinhMonId)
                .orElseThrow(() -> new ResourceNotFoundException("Môn trong chương trình không tồn tại: " + chuongTrinhMonId));

        if (!nhom.getChuongTrinhVersionId().equals(mon.getChuongTrinhVersionId())) {
            throw new BadRequestException("Môn tự chọn phải thuộc cùng version với nhóm tự chọn");
        }

        if (id == null && monTuChonRepository.existsByNhomIdAndChuongTrinhMonId(nhomId, chuongTrinhMonId)) {
            throw new DuplicateResourceException("Môn này đã có trong nhóm tự chọn");
        }

        if (id != null && monTuChonRepository.existsByNhomIdAndChuongTrinhMonIdAndIdNot(nhomId, chuongTrinhMonId, id)) {
            throw new DuplicateResourceException("Môn này đã có trong nhóm tự chọn");
        }
    }

    public void validateMonTienQuyet(MonTienQuyetRequest request, Long id) {
        notNull(request, "Dữ liệu môn tiên quyết không hợp lệ");

        Long monId = requireId(request.getMonId(), "monId");
        Long monDieuKienId = requireId(request.getMonDieuKienId(), "monDieuKienId");

        trimRequired(request.getLoai(), "Loại điều kiện môn");

        if (monId.equals(monDieuKienId)) {
            throw new BadRequestException("Một môn không được tự làm môn tiên quyết của chính nó");
        }

        ChuongTrinhMon mon = chuongTrinhMonRepository.findById(monId)
                .orElseThrow(() -> new ResourceNotFoundException("Môn trong chương trình không tồn tại: " + monId));

        ChuongTrinhMon dieuKien = chuongTrinhMonRepository.findById(monDieuKienId)
                .orElseThrow(() -> new ResourceNotFoundException("Môn điều kiện không tồn tại: " + monDieuKienId));

        if (!mon.getChuongTrinhVersionId().equals(dieuKien.getChuongTrinhVersionId())) {
            throw new BadRequestException("Môn tiên quyết phải thuộc cùng version chương trình");
        }

        if (id == null && monTienQuyetRepository.existsByMonIdAndMonDieuKienId(monId, monDieuKienId)) {
            throw new DuplicateResourceException("Điều kiện môn tiên quyết đã tồn tại");
        }

        if (id != null && monTienQuyetRepository.existsByMonIdAndMonDieuKienIdAndIdNot(monId, monDieuKienId, id)) {
            throw new DuplicateResourceException("Điều kiện môn tiên quyết đã tồn tại");
        }

        if (monTienQuyetRepository.existsByMonIdAndMonDieuKienId(monDieuKienId, monId)) {
            throw new BadRequestException("Không được tạo điều kiện môn tiên quyết vòng ngược trực tiếp");
        }
    }

    public void validateQuyDoiDiem(QuyDoiDiemRequest request, Long id) {
        notNull(request, "Dữ liệu quy đổi điểm không hợp lệ");

        Long chuongTrinhMonId = request.getChuongTrinhMonId();
        Long syllabusMonHocId = request.getSyllabusMonHocId();

        validateOnlyOneScope(chuongTrinhMonId, syllabusMonHocId);

        if (chuongTrinhMonId == null && syllabusMonHocId == null) {
            throw new BadRequestException("Phải truyền chuongTrinhMonId hoặc syllabusMonHocId");
        }

        if (chuongTrinhMonId != null) {
            requireExists(chuongTrinhMonRepository, chuongTrinhMonId, "Môn trong chương trình");
            validateNguongDiem(request.getNguongTu(), request.getNguongDen(), request.getDiemQuyDoi());
            validateTenCotDiemVaTongTyLeQuyDoiDiem(request, id);

            if (request.getNguongTu() != null
                    && request.getNguongDen() != null
                    && quyDoiDiemRepository.existsOverlap(chuongTrinhMonId, request.getNguongTu(), request.getNguongDen(), id)) {
                throw new DuplicateResourceException("Khoảng ngưỡng quy đổi điểm bị chồng lấn với dòng đã có của môn này");
            }

            return;
        }

        requireExists(syllabusMonHocRepository, syllabusMonHocId, "Syllabus môn học");
        validateNguongDiem(request.getNguongTu(), request.getNguongDen(), request.getDiemQuyDoi());

        if (request.getNguongTu() != null
                && request.getNguongDen() != null
                && quyDoiDiemRepository.existsOverlapBySyllabusMonHocId(
                syllabusMonHocId,
                request.getNguongTu(),
                request.getNguongDen(),
                id
        )) {
            throw new DuplicateResourceException("Khoảng ngưỡng quy đổi điểm bị chồng lấn với dòng đã có của syllabus môn học này");
        }

        validateTenCotDiemVaTongTyLeQuyDoiDiemTheoSyllabus(request, id);
    }
    private void validateTenCotDiemVaTongTyLeQuyDoiDiemTheoSyllabus(QuyDoiDiemRequest request, Long id) {
        Long syllabusMonHocId = request.getSyllabusMonHocId();

        String tenCotDiem = null;
        if (request.getTen() != null && !request.getTen().trim().isEmpty()) {
            tenCotDiem = request.getTen().trim();
        } else if (request.getGhiChu() != null && !request.getGhiChu().trim().isEmpty()) {
            tenCotDiem = request.getGhiChu().trim();
        }

        if (tenCotDiem == null) {
            throw new BadRequestException("Tên cột điểm mẫu không được để trống");
        }

        if (quyDoiDiemRepository.existsTenCotDiemTrongSyllabus(syllabusMonHocId, tenCotDiem, id)) {
            throw new DuplicateResourceException("Tên cột điểm mẫu đã tồn tại trong Quy đổi điểm đã lưu cho syllabus môn học: " + tenCotDiem);
        }

        BigDecimal tyLe = request.getTyLe() == null ? BigDecimal.ZERO : request.getTyLe();

        if (tyLe.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Tỷ lệ % không được âm");
        }

        if (tyLe.compareTo(new BigDecimal("100")) > 0) {
            throw new BadRequestException("Tỷ lệ % không được lớn hơn 100%");
        }

        BigDecimal tongTyLeCu = quyDoiDiemRepository.tongTyLeTrongSyllabusKhongTinhDongHienTai(syllabusMonHocId, id);
        BigDecimal tongTyLeMoi = tongTyLeCu.add(tyLe);

        if (tongTyLeMoi.compareTo(new BigDecimal("100")) > 0) {
            throw new BadRequestException("Tổng tỷ lệ % của Quy đổi điểm đã lưu cho syllabus môn học không được vượt quá 100%. Hiện tại sau khi lưu sẽ là " + tongTyLeMoi.stripTrailingZeros().toPlainString() + "%");
        }
    }
    private void validateTenCotDiemVaTongTyLeQuyDoiDiem(QuyDoiDiemRequest request, Long id) {
        Long chuongTrinhMonId = request.getChuongTrinhMonId();

        String tenCotDiem = null;
        if (request.getTen() != null && !request.getTen().trim().isEmpty()) {
            tenCotDiem = request.getTen().trim();
        } else if (request.getGhiChu() != null && !request.getGhiChu().trim().isEmpty()) {
            tenCotDiem = request.getGhiChu().trim();
        }

        if (tenCotDiem == null) {
            throw new BadRequestException("Tên cột điểm mẫu không được để trống");
        }

        if (quyDoiDiemRepository.existsTenCotDiemTrongMon(chuongTrinhMonId, tenCotDiem, id)) {
            throw new DuplicateResourceException("Tên cột điểm mẫu đã tồn tại trong Quy đổi điểm đã lưu cho môn trong chương trình: " + tenCotDiem);
        }

        BigDecimal tyLe = request.getTyLe() == null ? BigDecimal.ZERO : request.getTyLe();

        if (tyLe.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Tỷ lệ % không được âm");
        }

        if (tyLe.compareTo(new BigDecimal("100")) > 0) {
            throw new BadRequestException("Tỷ lệ % không được lớn hơn 100%");
        }

        BigDecimal tongTyLeCu = quyDoiDiemRepository.tongTyLeTrongMonKhongTinhDongHienTai(chuongTrinhMonId, id);
        BigDecimal tongTyLeMoi = tongTyLeCu.add(tyLe);

        if (tongTyLeMoi.compareTo(new BigDecimal("100")) > 0) {
            throw new BadRequestException("Tổng tỷ lệ % của Quy đổi điểm đã lưu cho môn trong chương trình không được vượt quá 100%. Hiện tại sau khi lưu sẽ là " + tongTyLeMoi.stripTrailingZeros().toPlainString() + "%");
        }
    }
    public void validateSyllabusMonHoc(SyllabusMonHocRequest request, Long id) {
        notNull(request, "Dữ liệu syllabus môn học không hợp lệ");

        Long chuongTrinhMonId = requireId(request.getChuongTrinhMonId(), "chuongTrinhMonId");

        requireExists(chuongTrinhMonRepository, chuongTrinhMonId, "Môn trong chương trình");

        validateThongSoBuoiHoc(request.getSoBuoiHoc(), request.getSoTietMoiBuoi(), request.getSoPhutMotTiet());

        validateDiemTrongKhoang(request.getDiemDatToiThieu(), "Điểm đạt tối thiểu", BigDecimal.ZERO, BigDecimal.TEN);
        validateDiemTrongKhoang(request.getTyLeChuyenCanToiThieu(), "Tỷ lệ chuyên cần tối thiểu", BigDecimal.ZERO, new BigDecimal("100"));

        if (id == null && syllabusMonHocRepository.existsByChuongTrinhMonId(chuongTrinhMonId)) {
            throw new DuplicateResourceException("Môn trong chương trình này đã có syllabus môn học");
        }

        if (id != null && syllabusMonHocRepository.existsByChuongTrinhMonIdAndIdNot(chuongTrinhMonId, id)) {
            throw new DuplicateResourceException("Môn trong chương trình này đã có syllabus môn học");
        }
        if (request.getSyllabusMonHocMauId() != null) {
            requireExists(
                    syllabusMonHocMauRepository,
                    request.getSyllabusMonHocMauId(),
                    "Syllabus môn học gốc"
            );
        }
    }

    public void validateDieuKienMonHocMau(DieuKienMonHocMauRequest request, Long id) {
        notNull(request, "Dữ liệu điều kiện môn học gốc không hợp lệ");

        String ma = trimRequired(request.getMa(), "Mã điều kiện môn học gốc");

        if (request.getLoai() == null) {
            throw new BadRequestException("Loại điều kiện môn học gốc không được để trống");
        }

        trimRequired(request.getNoiDung(), "Nội dung điều kiện môn học gốc");

        if (id == null && dieuKienMonHocMauRepository.existsByMa(ma)) {
            throw new DuplicateResourceException("Mã điều kiện môn học gốc đã tồn tại: " + ma);
        }

        if (id != null && dieuKienMonHocMauRepository.existsByMaAndIdNot(ma, id)) {
            throw new DuplicateResourceException("Mã điều kiện môn học gốc đã tồn tại: " + ma);
        }
    }

    public void validateTaiLieuMau(TaiLieuMauRequest request, Long id) {
        notNull(request, "Dữ liệu tài liệu gốc không hợp lệ");

        String ma = trimRequired(request.getMa(), "Mã tài liệu gốc");
        trimRequired(request.getTen(), "Tên tài liệu gốc");

        if (request.getNamXuatBan() != null) {
            int currentYear = LocalDate.now().getYear();
            if (request.getNamXuatBan() < 1900 || request.getNamXuatBan() > currentYear + 1) {
                throw new BadRequestException("Năm xuất bản tài liệu không hợp lệ");
            }
        }

        if (id == null && taiLieuMauRepository.existsByMa(ma)) {
            throw new DuplicateResourceException("Mã tài liệu gốc đã tồn tại: " + ma);
        }

        if (id != null && taiLieuMauRepository.existsByMaAndIdNot(ma, id)) {
            throw new DuplicateResourceException("Mã tài liệu gốc đã tồn tại: " + ma);
        }
    }
    public void validateSyllabusMonHocMau(SyllabusMonHocMauRequest request, Long id) {
        notNull(request, "Dữ liệu syllabus môn học gốc không hợp lệ");

        Long monHocId = requireId(request.getMonHocId(), "monHocId");
        String ma = trimRequired(request.getMa(), "Mã syllabus môn học gốc");
        String ten = trimRequired(request.getTen(), "Tên syllabus môn học gốc");

        requireExists(monHocRepository, monHocId, "Môn học");

        validateThongSoBuoiHoc(request.getSoBuoiHoc(), request.getSoTietMoiBuoi(), request.getSoPhutMotTiet());

        validateDiemTrongKhoang(
                request.getDiemDatToiThieu(),
                "Điểm đạt tối thiểu",
                BigDecimal.ZERO,
                BigDecimal.TEN
        );

        validateDiemTrongKhoang(
                request.getTyLeChuyenCanToiThieu(),
                "Tỷ lệ chuyên cần tối thiểu",
                BigDecimal.ZERO,
                new BigDecimal("100")
        );

        if (id == null && syllabusMonHocMauRepository.existsByMa(ma)) {
            throw new DuplicateResourceException("Mã syllabus môn học gốc đã tồn tại: " + ma);
        }

        if (id != null && syllabusMonHocMauRepository.existsByMaAndIdNot(ma, id)) {
            throw new DuplicateResourceException("Mã syllabus môn học gốc đã tồn tại: " + ma);
        }

        if (id == null && syllabusMonHocMauRepository.existsByMonHocIdAndTen(monHocId, ten)) {
            throw new DuplicateResourceException("Tên syllabus gốc đã tồn tại trong môn học này: " + ten);
        }

        if (id != null && syllabusMonHocMauRepository.existsByMonHocIdAndTenAndIdNot(monHocId, ten, id)) {
            throw new DuplicateResourceException("Tên syllabus gốc đã tồn tại trong môn học này: " + ten);
        }
    }
    public void validateSyllabusMonHocMauChuongBai(SyllabusMonHocMauChuongBaiRequest request, Long id) {
        notNull(request, "Dữ liệu chương bài syllabus gốc không hợp lệ");

        Long syllabusMauId = requireId(request.getSyllabusMonHocMauId(), "syllabusMonHocMauId");

        trimRequired(request.getTen(), "Tên chương bài syllabus gốc");
        requireExists(syllabusMonHocMauRepository, syllabusMauId, "Syllabus môn học gốc");

        positiveOrZero(request.getTongGio(), "Tổng giờ chương bài gốc");
        positiveOrZero(request.getGioLyThuyet(), "Giờ lý thuyết chương bài gốc");
        positiveOrZero(request.getGioThucHanh(), "Giờ thực hành chương bài gốc");
        positiveOrZero(request.getGioKiemTra(), "Giờ kiểm tra chương bài gốc");
        positiveOrZeroInt(request.getThuTu(), "Thứ tự chương bài gốc");

        checkTotalEqualsParts(
                request.getTongGio(),
                request.getGioLyThuyet(),
                request.getGioThucHanh(),
                request.getGioKiemTra(),
                "Tổng giờ chương bài syllabus gốc"
        );

        if (request.getThuTu() != null) {
            if (id == null && syllabusMonHocMauChuongBaiRepository.existsBySyllabusMonHocMauIdAndThuTu(
                    syllabusMauId,
                    request.getThuTu()
            )) {
                throw new DuplicateResourceException("Thứ tự chương bài gốc đã tồn tại trong syllabus gốc");
            }

            if (id != null && syllabusMonHocMauChuongBaiRepository.existsBySyllabusMonHocMauIdAndThuTuAndIdNot(
                    syllabusMauId,
                    request.getThuTu(),
                    id
            )) {
                throw new DuplicateResourceException("Thứ tự chương bài gốc đã tồn tại trong syllabus gốc");
            }
        }
    }

    public void validateSyllabusMonHocMauDieuKien(SyllabusMonHocMauDieuKienRequest request, Long id) {
        notNull(request, "Dữ liệu gán điều kiện vào syllabus gốc không hợp lệ");

        Long syllabusMauId = requireId(request.getSyllabusMonHocMauId(), "syllabusMonHocMauId");
        Long dieuKienMauId = requireId(request.getDieuKienMauId(), "dieuKienMauId");

        requireExists(syllabusMonHocMauRepository, syllabusMauId, "Syllabus môn học gốc");
        requireExists(dieuKienMonHocMauRepository, dieuKienMauId, "Điều kiện môn học gốc");

        positiveOrZeroInt(request.getThuTu(), "Thứ tự điều kiện trong syllabus gốc");

        if (id == null && syllabusMonHocMauDieuKienRepository.existsBySyllabusMonHocMauIdAndDieuKienMauId(
                syllabusMauId,
                dieuKienMauId
        )) {
            throw new DuplicateResourceException("Điều kiện gốc đã được gán cho syllabus gốc này");
        }

        if (id != null && syllabusMonHocMauDieuKienRepository.existsBySyllabusMonHocMauIdAndDieuKienMauIdAndIdNot(
                syllabusMauId,
                dieuKienMauId,
                id
        )) {
            throw new DuplicateResourceException("Điều kiện gốc đã được gán cho syllabus gốc này");
        }

        if (request.getThuTu() != null) {
            if (id == null && syllabusMonHocMauDieuKienRepository.existsBySyllabusMonHocMauIdAndThuTu(
                    syllabusMauId,
                    request.getThuTu()
            )) {
                throw new DuplicateResourceException("Thứ tự điều kiện đã tồn tại trong syllabus gốc");
            }

            if (id != null && syllabusMonHocMauDieuKienRepository.existsBySyllabusMonHocMauIdAndThuTuAndIdNot(
                    syllabusMauId,
                    request.getThuTu(),
                    id
            )) {
                throw new DuplicateResourceException("Thứ tự điều kiện đã tồn tại trong syllabus gốc");
            }
        }
    }
    public void validateSyllabusMonHocMauTaiLieu(SyllabusMonHocMauTaiLieuRequest request, Long id) {
        notNull(request, "Dữ liệu gán tài liệu vào syllabus gốc không hợp lệ");

        Long syllabusMauId = requireId(request.getSyllabusMonHocMauId(), "syllabusMonHocMauId");
        Long taiLieuMauId = requireId(request.getTaiLieuMauId(), "taiLieuMauId");

        requireExists(syllabusMonHocMauRepository, syllabusMauId, "Syllabus môn học gốc");
        requireExists(taiLieuMauRepository, taiLieuMauId, "Tài liệu gốc");

        positiveOrZeroInt(request.getThuTu(), "Thứ tự tài liệu trong syllabus gốc");

        if (id == null && syllabusMonHocMauTaiLieuRepository.existsBySyllabusMonHocMauIdAndTaiLieuMauId(
                syllabusMauId,
                taiLieuMauId
        )) {
            throw new DuplicateResourceException("Tài liệu gốc đã được gán cho syllabus gốc này");
        }

        if (id != null && syllabusMonHocMauTaiLieuRepository.existsBySyllabusMonHocMauIdAndTaiLieuMauIdAndIdNot(
                syllabusMauId,
                taiLieuMauId,
                id
        )) {
            throw new DuplicateResourceException("Tài liệu gốc đã được gán cho syllabus gốc này");
        }

        if (request.getThuTu() != null) {
            if (id == null && syllabusMonHocMauTaiLieuRepository.existsBySyllabusMonHocMauIdAndThuTu(
                    syllabusMauId,
                    request.getThuTu()
            )) {
                throw new DuplicateResourceException("Thứ tự tài liệu đã tồn tại trong syllabus gốc");
            }

            if (id != null && syllabusMonHocMauTaiLieuRepository.existsBySyllabusMonHocMauIdAndThuTuAndIdNot(
                    syllabusMauId,
                    request.getThuTu(),
                    id
            )) {
                throw new DuplicateResourceException("Thứ tự tài liệu đã tồn tại trong syllabus gốc");
            }
        }
    }

    public void validateSyllabusChuongTrinh(SyllabusChuongTrinhRequest request, Long id) {
        notNull(request, "Dữ liệu syllabus chương trình không hợp lệ");

        Long versionId = requireId(request.getChuongTrinhVersionId(), "chuongTrinhVersionId");

        requireExists(chuongTrinhVersionRepository, versionId, "Phiên bản chương trình");

        if (request.getSyllabusChuongTrinhMauId() != null) {
            requireExists(
                    syllabusChuongTrinhMauRepository,
                    request.getSyllabusChuongTrinhMauId(),
                    "Syllabus chương trình gốc"
            );
        }

        if (id == null && syllabusChuongTrinhRepository.existsByChuongTrinhVersionId(versionId)) {
            throw new DuplicateResourceException("Phiên bản chương trình này đã có syllabus chương trình");
        }

        if (id != null && syllabusChuongTrinhRepository.existsByChuongTrinhVersionIdAndIdNot(versionId, id)) {
            throw new DuplicateResourceException("Phiên bản chương trình này đã có syllabus chương trình");
        }

        if (request.getSyllabusChuongTrinhMauId() != null) {
            Long MauId = request.getSyllabusChuongTrinhMauId();

            if (id == null && syllabusChuongTrinhRepository.existsByChuongTrinhVersionIdAndSyllabusChuongTrinhMauId(versionId, MauId)) {
                throw new DuplicateResourceException("Syllabus chương trình gốc đã được áp dụng cho version này");
            }

            if (id != null && syllabusChuongTrinhRepository.existsByChuongTrinhVersionIdAndSyllabusChuongTrinhMauIdAndIdNot(versionId, MauId, id)) {
                throw new DuplicateResourceException("Syllabus chương trình gốc đã được áp dụng cho version này");
            }
        }
    }

    public void validateSyllabusChuongBai(SyllabusChuongBaiRequest request, Long id) {
        notNull(request, "Dữ liệu chương bài syllabus không hợp lệ");

        Long syllabusMonId = requireId(request.getSyllabusMonId(), "syllabusMonId");

        trimRequired(request.getTen(), "Tên chương bài");
        requireExists(syllabusMonHocRepository, syllabusMonId, "Syllabus môn học");

        positiveOrZero(request.getTongGio(), "Tổng giờ chương bài");
        positiveOrZero(request.getGioLyThuyet(), "Giờ lý thuyết chương bài");
        positiveOrZero(request.getGioThucHanh(), "Giờ thực hành chương bài");
        positiveOrZero(request.getGioKiemTra(), "Giờ kiểm tra chương bài");
        positiveOrZeroInt(request.getThuTu(), "Thứ tự chương bài");

        checkTotalEqualsParts(
                request.getTongGio(),
                request.getGioLyThuyet(),
                request.getGioThucHanh(),
                request.getGioKiemTra(),
                "Tổng giờ chương bài"
        );

        if (request.getThuTu() != null) {
            if (id == null && syllabusChuongBaiRepository.existsBySyllabusMonIdAndThuTu(syllabusMonId, request.getThuTu())) {
                throw new DuplicateResourceException("Thứ tự chương bài đã tồn tại trong syllabus môn");
            }

            if (id != null && syllabusChuongBaiRepository.existsBySyllabusMonIdAndThuTuAndIdNot(syllabusMonId, request.getThuTu(), id)) {
                throw new DuplicateResourceException("Thứ tự chương bài đã tồn tại trong syllabus môn");
            }
        }
    }

    private void validateThongSoBuoiHoc(Integer soBuoiHoc, BigDecimal soTietMoiBuoi, Integer soPhutMotTiet) {
        if (soBuoiHoc == null || soBuoiHoc < 1) {
            throw new BadRequestException("Số buổi học phải lớn hơn 0");
        }

        if (soTietMoiBuoi == null || soTietMoiBuoi.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Số tiết mỗi buổi phải lớn hơn 0");
        }

        if (soPhutMotTiet == null || soPhutMotTiet < 1) {
            throw new BadRequestException("Số phút một tiết phải lớn hơn 0");
        }
    }

    public void validateSyllabusTaiLieu(SyllabusTaiLieuRequest request, Long id) {
        notNull(request, "Dữ liệu tài liệu syllabus không hợp lệ");

        Long syllabusMonId = requireId(request.getSyllabusMonId(), "syllabusMonId");
        String ten = trimRequired(request.getTen(), "Tên tài liệu");

        requireExists(syllabusMonHocRepository, syllabusMonId, "Syllabus môn học");

        if (request.getNamXuatBan() != null) {
            int currentYear = LocalDate.now().getYear();
            if (request.getNamXuatBan() < 1900 || request.getNamXuatBan() > currentYear + 1) {
                throw new BadRequestException("Năm xuất bản tài liệu không hợp lệ");
            }
        }

        if (id == null && syllabusTaiLieuRepository.existsBySyllabusMonIdAndTen(syllabusMonId, ten)) {
            throw new DuplicateResourceException("Tên tài liệu đã tồn tại trong syllabus môn");
        }

        if (id != null && syllabusTaiLieuRepository.existsBySyllabusMonIdAndTenAndIdNot(syllabusMonId, ten, id)) {
            throw new DuplicateResourceException("Tên tài liệu đã tồn tại trong syllabus môn");
        }
    }

    public void validateDieuKienMonHoc(DieuKienMonHocRequest request, Long id) {
        notNull(request, "Dữ liệu điều kiện môn học không hợp lệ");

        Long syllabusMonId = requireId(request.getSyllabusMonId(), "syllabusMonId");

        trimRequired(request.getLoai(), "Loại điều kiện môn học");
        trimRequired(request.getNoiDung(), "Nội dung điều kiện môn học");

        requireExists(syllabusMonHocRepository, syllabusMonId, "Syllabus môn học");

        positiveOrZeroInt(request.getThuTu(), "Thứ tự điều kiện môn học");

        if (request.getThuTu() != null) {
            if (id == null && dieuKienMonHocRepository.existsBySyllabusMonIdAndThuTu(syllabusMonId, request.getThuTu())) {
                throw new DuplicateResourceException("Thứ tự điều kiện môn học đã tồn tại trong syllabus");
            }

            if (id != null && dieuKienMonHocRepository.existsBySyllabusMonIdAndThuTuAndIdNot(syllabusMonId, request.getThuTu(), id)) {
                throw new DuplicateResourceException("Thứ tự điều kiện môn học đã tồn tại trong syllabus");
            }
        }
    }

    public void validateQuyDoiDiemMau(QuyDoiDiemMauRequest request, Long id) {
        notNull(request, "Dữ liệu quy đổi điểm mẫu không hợp lệ");

        requireId(request.getSyllabusMonHocMauId(), "syllabusMonHocMauId");
        trimRequired(request.getMa(), "Mã quy đổi điểm mẫu");
        trimRequired(request.getTen(), "Tên quy đổi điểm mẫu");

        validateNguongDiem(request.getNguongTu(), request.getNguongDen(), request.getDiemQuyDoi());
        positiveOrZeroInt(request.getThuTu(), "Thứ tự quy đổi điểm mẫu");
    }

    public void validateChuongTrinhMonQuyDoiDiemMau(ChuongTrinhMonQuyDoiDiemMauRequest request, Long id) {
        notNull(request, "Dữ liệu gán mẫu quy đổi điểm không hợp lệ");

        Long chuongTrinhMonId = request.getChuongTrinhMonId();
        Long syllabusMonHocId = request.getSyllabusMonHocId();
        Long quyDoiDiemMauId = requireId(request.getQuyDoiDiemMauId(), "quyDoiDiemMauId");

        validateOnlyOneScope(chuongTrinhMonId, syllabusMonHocId);

        if (chuongTrinhMonId == null && syllabusMonHocId == null) {
            throw new BadRequestException("Phải truyền chuongTrinhMonId hoặc syllabusMonHocId");
        }

        requireExists(quyDoiDiemMauRepository, quyDoiDiemMauId, "Quy đổi điểm mẫu");

        if (syllabusMonHocId != null) {
            requireExists(syllabusMonHocRepository, syllabusMonHocId, "Syllabus môn học");

            if (id == null && chuongTrinhMonQuyDoiDiemMauRepository.existsBySyllabusMonHocIdAndQuyDoiDiemMauId(
                    syllabusMonHocId,
                    quyDoiDiemMauId
            )) {
                throw new DuplicateResourceException("Mẫu quy đổi điểm đã được gán cho syllabus môn học này");
            }

            if (id != null && chuongTrinhMonQuyDoiDiemMauRepository.existsBySyllabusMonHocIdAndQuyDoiDiemMauIdAndIdNot(
                    syllabusMonHocId,
                    quyDoiDiemMauId,
                    id
            )) {
                throw new DuplicateResourceException("Mẫu quy đổi điểm đã được gán cho syllabus môn học này");
            }

            return;
        }

        requireExists(chuongTrinhMonRepository, chuongTrinhMonId, "Môn trong chương trình");

        if (id == null && chuongTrinhMonQuyDoiDiemMauRepository.existsByChuongTrinhMonIdAndQuyDoiDiemMauId(
                chuongTrinhMonId,
                quyDoiDiemMauId
        )) {
            throw new DuplicateResourceException("Mẫu quy đổi điểm đã được gán cho môn này");
        }

        if (id != null && chuongTrinhMonQuyDoiDiemMauRepository.existsByChuongTrinhMonIdAndQuyDoiDiemMauIdAndIdNot(
                chuongTrinhMonId,
                quyDoiDiemMauId,
                id
        )) {
            throw new DuplicateResourceException("Mẫu quy đổi điểm đã được gán cho môn này");
        }
    }

    public void validateMucTieuChuongTrinh(MucTieuChuongTrinhRequest request, Long id) {
        notNull(request, "Dữ liệu mục tiêu chương trình không hợp lệ");

        Long syllabusChuongTrinhId = requireId(request.getSyllabusChuongTrinhId(), "syllabusChuongTrinhId");

        trimRequired(request.getLoai(), "Loại mục tiêu");
        trimRequired(request.getNoiDung(), "Nội dung mục tiêu");

        requireExists(syllabusChuongTrinhRepository, syllabusChuongTrinhId, "Syllabus chương trình");
        positiveOrZeroInt(request.getThuTu(), "Thứ tự mục tiêu chương trình");

        if (request.getThuTu() != null) {
            if (id == null && mucTieuChuongTrinhRepository.existsBySyllabusChuongTrinhIdAndThuTu(syllabusChuongTrinhId, request.getThuTu())) {
                throw new DuplicateResourceException("Thứ tự mục tiêu chương trình đã tồn tại trong syllabus chương trình");
            }
            if (id != null && mucTieuChuongTrinhRepository.existsBySyllabusChuongTrinhIdAndThuTuAndIdNot(syllabusChuongTrinhId, request.getThuTu(), id)) {
                throw new DuplicateResourceException("Thứ tự mục tiêu chương trình đã tồn tại trong syllabus chương trình");
            }
        }
    }

    public void validateNangLucDauRa(NangLucDauRaRequest request, Long id) {
        notNull(request, "Dữ liệu năng lực đầu ra không hợp lệ");

        Long syllabusChuongTrinhId = requireId(request.getSyllabusChuongTrinhId(), "syllabusChuongTrinhId");
        String ma = trimRequired(request.getMa(), "Mã năng lực đầu ra");

        trimRequired(request.getLoai(), "Loại năng lực đầu ra");
        trimRequired(request.getNoiDung(), "Nội dung năng lực đầu ra");

        requireExists(syllabusChuongTrinhRepository, syllabusChuongTrinhId, "Syllabus chương trình");

        if (id == null && nangLucDauRaRepository.existsBySyllabusChuongTrinhIdAndMa(syllabusChuongTrinhId, ma)) {
            throw new DuplicateResourceException("Mã năng lực đầu ra đã tồn tại trong syllabus chương trình: " + ma);
        }

        if (id != null && nangLucDauRaRepository.existsBySyllabusChuongTrinhIdAndMaAndIdNot(syllabusChuongTrinhId, ma, id)) {
            throw new DuplicateResourceException("Mã năng lực đầu ra đã tồn tại trong syllabus chương trình: " + ma);
        }

        positiveOrZeroInt(request.getThuTu(), "Thứ tự năng lực đầu ra");

        if (request.getThuTu() != null) {
            if (id == null && nangLucDauRaRepository.existsBySyllabusChuongTrinhIdAndThuTu(syllabusChuongTrinhId, request.getThuTu())) {
                throw new DuplicateResourceException("Thứ tự năng lực đầu ra đã tồn tại trong syllabus chương trình");
            }
            if (id != null && nangLucDauRaRepository.existsBySyllabusChuongTrinhIdAndThuTuAndIdNot(syllabusChuongTrinhId, request.getThuTu(), id)) {
                throw new DuplicateResourceException("Thứ tự năng lực đầu ra đã tồn tại trong syllabus chương trình");
            }
        }
    }

    public void validateViTriViecLam(ViTriViecLamRequest request, Long id) {
        notNull(request, "Dữ liệu vị trí việc làm không hợp lệ");

        Long syllabusChuongTrinhId = requireId(request.getSyllabusChuongTrinhId(), "syllabusChuongTrinhId");

        trimRequired(request.getTen(), "Tên vị trí việc làm");
        requireExists(syllabusChuongTrinhRepository, syllabusChuongTrinhId, "Syllabus chương trình");
        positiveOrZeroInt(request.getThuTu(), "Thứ tự vị trí việc làm");

        if (request.getThuTu() != null) {
            if (id == null && viTriViecLamRepository.existsBySyllabusChuongTrinhIdAndThuTu(syllabusChuongTrinhId, request.getThuTu())) {
                throw new DuplicateResourceException("Thứ tự vị trí việc làm đã tồn tại trong syllabus chương trình");
            }
            if (id != null && viTriViecLamRepository.existsBySyllabusChuongTrinhIdAndThuTuAndIdNot(syllabusChuongTrinhId, request.getThuTu(), id)) {
                throw new DuplicateResourceException("Thứ tự vị trí việc làm đã tồn tại trong syllabus chương trình");
            }
        }
    }

    public void validateDieuKienTotNghiep(DieuKienTotNghiepRequest request, Long id) {
        notNull(request, "Dữ liệu điều kiện tốt nghiệp không hợp lệ");

        Long syllabusChuongTrinhId = requireId(request.getSyllabusChuongTrinhId(), "syllabusChuongTrinhId");

        trimRequired(request.getNoiDung(), "Nội dung điều kiện tốt nghiệp");
        requireExists(syllabusChuongTrinhRepository, syllabusChuongTrinhId, "Syllabus chương trình");
        positiveOrZeroInt(request.getThuTu(), "Thứ tự điều kiện tốt nghiệp");

        if (request.getThuTu() != null) {
            if (id == null && dieuKienTotNghiepRepository.existsBySyllabusChuongTrinhIdAndThuTu(syllabusChuongTrinhId, request.getThuTu())) {
                throw new DuplicateResourceException("Thứ tự điều kiện tốt nghiệp đã tồn tại trong syllabus chương trình");
            }
            if (id != null && dieuKienTotNghiepRepository.existsBySyllabusChuongTrinhIdAndThuTuAndIdNot(syllabusChuongTrinhId, request.getThuTu(), id)) {
                throw new DuplicateResourceException("Thứ tự điều kiện tốt nghiệp đã tồn tại trong syllabus chương trình");
            }
        }
    }

    public void validateMucTieuChuongTrinhMau(MucTieuChuongTrinhMauRequest request, Long id) {
        notNull(request, "Dữ liệu mục tiêu gốc không hợp lệ");

        Long syllabusMauId = requireId(request.getSyllabusChuongTrinhMauId(), "syllabusChuongTrinhMauId");
        requireExists(syllabusChuongTrinhMauRepository, syllabusMauId, "Syllabus chương trình mẫu");
        String ma = trimRequired(request.getMa(), "Mã mục tiêu gốc");

        if (request.getLoai() == null) {
            throw new BadRequestException("Loại mục tiêu gốc không được để trống");
        }

        trimRequired(request.getNoiDung(), "Nội dung mục tiêu gốc");
        if (id == null && mucTieuChuongTrinhMauRepository.existsBySyllabusChuongTrinhMauIdAndMa(syllabusMauId, ma)) {
            throw new DuplicateResourceException("Mã mục tiêu gốc đã tồn tại trong syllabus chương trình mẫu: " + ma);
        }
        if (id != null && mucTieuChuongTrinhMauRepository.existsBySyllabusChuongTrinhMauIdAndMaAndIdNot(syllabusMauId, ma, id)) {
            throw new DuplicateResourceException("Mã mục tiêu gốc đã tồn tại trong syllabus chương trình mẫu: " + ma);
        }
    }

    public void validateNangLucDauRaMau(NangLucDauRaMauRequest request, Long id) {
        notNull(request, "Dữ liệu năng lực gốc không hợp lệ");

        Long syllabusMauId = requireId(request.getSyllabusChuongTrinhMauId(), "syllabusChuongTrinhMauId");
        requireExists(syllabusChuongTrinhMauRepository, syllabusMauId, "Syllabus chương trình mẫu");
        String ma = trimRequired(request.getMa(), "Mã năng lực gốc");

        if (request.getLoai() == null) {
            throw new BadRequestException("Loại năng lực gốc không được để trống");
        }

        trimRequired(request.getNoiDung(), "Nội dung năng lực gốc");

        if (id == null && nangLucDauRaMauRepository.existsBySyllabusChuongTrinhMauIdAndMa(syllabusMauId, ma)) {
            throw new DuplicateResourceException("Mã năng lực gốc đã tồn tại trong syllabus chương trình mẫu: " + ma);
        }

        if (id != null && nangLucDauRaMauRepository.existsBySyllabusChuongTrinhMauIdAndMaAndIdNot(syllabusMauId, ma, id)) {
            throw new DuplicateResourceException("Mã năng lực gốc đã tồn tại trong syllabus chương trình mẫu: " + ma);
        }
    }

    public void validateViTriViecLamMau(ViTriViecLamMauRequest request, Long id) {
        notNull(request, "Dữ liệu vị trí việc làm gốc không hợp lệ");

        Long syllabusMauId = requireId(request.getSyllabusChuongTrinhMauId(), "syllabusChuongTrinhMauId");
        requireExists(syllabusChuongTrinhMauRepository, syllabusMauId, "Syllabus chương trình mẫu");
        String ma = trimRequired(request.getMa(), "Mã vị trí việc làm gốc");

        trimRequired(request.getTen(), "Tên vị trí việc làm gốc");

        if (id == null && viTriViecLamMauRepository.existsBySyllabusChuongTrinhMauIdAndMa(syllabusMauId, ma)) {
            throw new DuplicateResourceException("Mã vị trí việc làm gốc đã tồn tại trong syllabus chương trình mẫu: " + ma);
        }

        if (id != null && viTriViecLamMauRepository.existsBySyllabusChuongTrinhMauIdAndMaAndIdNot(syllabusMauId, ma, id)) {
            throw new DuplicateResourceException("Mã vị trí việc làm gốc đã tồn tại trong syllabus chương trình mẫu: " + ma);
        }
    }

    public void validateDieuKienTotNghiepMau(DieuKienTotNghiepMauRequest request, Long id) {
        notNull(request, "Dữ liệu điều kiện tốt nghiệp gốc không hợp lệ");

        Long syllabusMauId = requireId(request.getSyllabusChuongTrinhMauId(), "syllabusChuongTrinhMauId");
        requireExists(syllabusChuongTrinhMauRepository, syllabusMauId, "Syllabus chương trình mẫu");
        String ma = trimRequired(request.getMa(), "Mã điều kiện tốt nghiệp gốc");

        trimRequired(request.getNoiDung(), "Nội dung điều kiện tốt nghiệp gốc");
        if (id == null && dieuKienTotNghiepMauRepository.existsBySyllabusChuongTrinhMauIdAndMa(syllabusMauId, ma)) {
            throw new DuplicateResourceException("Mã điều kiện tốt nghiệp gốc đã tồn tại trong syllabus chương trình mẫu: " + ma);
        }
        if (id != null && dieuKienTotNghiepMauRepository.existsBySyllabusChuongTrinhMauIdAndMaAndIdNot(syllabusMauId, ma, id)) {
            throw new DuplicateResourceException("Mã điều kiện tốt nghiệp gốc đã tồn tại trong syllabus chương trình mẫu: " + ma);
        }
    }

    public void validateChuongTrinhVersionMucTieu(ChuongTrinhVersionMucTieuRequest request, Long id) {
        Long versionId = requireId(request.getChuongTrinhVersionId(), "chuongTrinhVersionId");
        Long MauId = requireId(request.getMucTieuMauId(), "mucTieuMauId");

        requireExists(chuongTrinhVersionRepository, versionId, "Phiên bản chương trình");
        requireExists(mucTieuChuongTrinhMauRepository, MauId, "Mục tiêu gốc");

        positiveOrZeroInt(request.getThuTu(), "Thứ tự mục tiêu gốc trong version");

        if (id == null && chuongTrinhVersionMucTieuRepository.existsByChuongTrinhVersionIdAndMucTieuMauId(versionId, MauId)) {
            throw new DuplicateResourceException("Mục tiêu gốc đã được gán cho version này");
        }

        if (id != null && chuongTrinhVersionMucTieuRepository.existsByChuongTrinhVersionIdAndMucTieuMauIdAndIdNot(versionId, MauId, id)) {
            throw new DuplicateResourceException("Mục tiêu gốc đã được gán cho version này");
        }
    }

    public void validateChuongTrinhVersionNangLuc(ChuongTrinhVersionNangLucRequest request, Long id) {
        notNull(request, "Dữ liệu gán năng lực gốc vào version không hợp lệ");

        Long versionId = requireId(request.getChuongTrinhVersionId(), "chuongTrinhVersionId");
        Long MauId = requireId(request.getNangLucMauId(), "nangLucMauId");

        requireExists(chuongTrinhVersionRepository, versionId, "Phiên bản chương trình");
        requireExists(nangLucDauRaMauRepository, MauId, "Năng lực gốc");

        positiveOrZeroInt(request.getThuTu(), "Thứ tự năng lực gốc trong version");

        if (id == null && chuongTrinhVersionNangLucRepository.existsByChuongTrinhVersionIdAndNangLucMauId(versionId, MauId)) {
            throw new DuplicateResourceException("Năng lực gốc đã được gán cho version này");
        }

        if (id != null && chuongTrinhVersionNangLucRepository.existsByChuongTrinhVersionIdAndNangLucMauIdAndIdNot(versionId, MauId, id)) {
            throw new DuplicateResourceException("Năng lực gốc đã được gán cho version này");
        }
    }

    public void validateChuongTrinhVersionViTriViecLam(ChuongTrinhVersionViTriViecLamRequest request, Long id) {
        Long versionId = requireId(request.getChuongTrinhVersionId(), "chuongTrinhVersionId");
        Long MauId = requireId(request.getViTriMauId(), "viTriMauId");

        requireExists(chuongTrinhVersionRepository, versionId, "Phiên bản chương trình");
        requireExists(viTriViecLamMauRepository, MauId, "Vị trí việc làm gốc");

        positiveOrZeroInt(request.getThuTu(), "Thứ tự vị trí việc làm gốc trong version");

        if (id == null && chuongTrinhVersionViTriViecLamRepository.existsByChuongTrinhVersionIdAndViTriMauId(versionId, MauId)) {
            throw new DuplicateResourceException("Vị trí việc làm gốc đã được gán cho version này");
        }

        if (id != null && chuongTrinhVersionViTriViecLamRepository.existsByChuongTrinhVersionIdAndViTriMauIdAndIdNot(versionId, MauId, id)) {
            throw new DuplicateResourceException("Vị trí việc làm gốc đã được gán cho version này");
        }
    }

    public void validateChuongTrinhVersionDieuKienTotNghiep(ChuongTrinhVersionDieuKienTotNghiepRequest request, Long id) {
        Long versionId = requireId(request.getChuongTrinhVersionId(), "chuongTrinhVersionId");
        Long MauId = requireId(request.getDieuKienMauId(), "dieuKienMauId");

        requireExists(chuongTrinhVersionRepository, versionId, "Phiên bản chương trình");
        requireExists(dieuKienTotNghiepMauRepository, MauId, "Điều kiện tốt nghiệp gốc");

        positiveOrZeroInt(request.getThuTu(), "Thứ tự điều kiện tốt nghiệp gốc trong version");

        if (id == null && chuongTrinhVersionDieuKienTotNghiepRepository.existsByChuongTrinhVersionIdAndDieuKienMauId(versionId, MauId)) {
            throw new DuplicateResourceException("Điều kiện tốt nghiệp gốc đã được gán cho version này");
        }

        if (id != null && chuongTrinhVersionDieuKienTotNghiepRepository.existsByChuongTrinhVersionIdAndDieuKienMauIdAndIdNot(versionId, MauId, id)) {
            throw new DuplicateResourceException("Điều kiện tốt nghiệp gốc đã được gán cho version này");
        }
    }

    private void validateNguongDiem(BigDecimal nguongTu, BigDecimal nguongDen, BigDecimal diemQuyDoi) {
        positiveOrZero(nguongTu, "Ngưỡng từ");
        positiveOrZero(nguongDen, "Ngưỡng đến");

        validateDiemTrongKhoang(diemQuyDoi, "Điểm quy đổi", BigDecimal.ZERO, BigDecimal.TEN);

        if (nguongTu != null && nguongDen != null && nguongTu.compareTo(nguongDen) > 0) {
            throw new BadRequestException("Ngưỡng từ không được lớn hơn ngưỡng đến");
        }
    }

    private void validateDiemTrongKhoang(BigDecimal value, String name, BigDecimal min, BigDecimal max) {
        if (value == null) return;

        if (value.compareTo(min) < 0 || value.compareTo(max) > 0) {
            throw new BadRequestException(name + " phải nằm trong khoảng " + min + " đến " + max);
        }
    }

    private void checkMaMau(Object repository, String ma, Long id, String label) {
        boolean exists = false;

        if (repository instanceof MucTieuChuongTrinhMauRepository repo) {
            exists = id == null ? repo.existsByMa(ma) : repo.existsByMaAndIdNot(ma, id);
        } else if (repository instanceof DieuKienTotNghiepMauRepository repo) {
            exists = id == null ? repo.existsByMa(ma) : repo.existsByMaAndIdNot(ma, id);
        }

        if (exists) {
            throw new DuplicateResourceException(label + " đã tồn tại: " + ma);
        }
    }

    private void validateOnlyOneScope(Long chuongTrinhMonId, Long syllabusMonHocId) {
        if (chuongTrinhMonId != null && syllabusMonHocId != null) {
            throw new BadRequestException("Chỉ được truyền một trong hai: chuongTrinhMonId hoặc syllabusMonHocId");
        }
    }

    private <T, ID> void requireExists(JpaRepository<T, ID> repository, ID id, String label) {
        if (id == null) {
            throw new BadRequestException(label + " không được để trống");
        }

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(label + " không tồn tại: " + id);
        }
    }

    private Long requireId(Long id, String field) {
        if (id == null) {
            throw new BadRequestException(field + " không được để trống");
        }

        return id;
    }

    private void notNull(Object value, String message) {
        if (value == null) {
            throw new BadRequestException(message);
        }
    }

    private String trimRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new BadRequestException(fieldName + " không được để trống");
        }

        return value.trim();
    }

    private void positiveOrZero(BigDecimal value, String fieldName) {
        if (value != null && value.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException(fieldName + " không được âm");
        }
    }

    private void positiveOrZeroInt(Integer value, String fieldName) {
        if (value != null && value < 0) {
            throw new BadRequestException(fieldName + " không được âm");
        }
    }

    private void validateThoiHanVersion(Long chuongTrinhId, LocalDate ngayApDung, LocalDate ngayHetHieuLuc) {
        ChuongTrinh chuongTrinh = chuongTrinhRepository.findById(chuongTrinhId).orElse(null);
        if (chuongTrinh == null || chuongTrinh.getNganhHeDaoTaoId() == null) return;

        NganhHeDaoTao nganhHe = nganhHeDaoTaoRepository.findById(chuongTrinh.getNganhHeDaoTaoId()).orElse(null);
        if (nganhHe == null || nganhHe.getSoThang() == null || nganhHe.getSoThang() <= 0) return;

        int soThang = nganhHe.getSoThang();

        // Ngày hết hiệu lực kỳ vọng = ngayApDung + soThang tháng - 1 ngày
        LocalDate ngayKyVong = ngayApDung.plusMonths(soThang).minusDays(1);

        // Giới hạn dưới: kỳ vọng - 15 ngày
        LocalDate gioiHanDuoi = ngayKyVong.minusDays(15);

        // Giới hạn trên: kỳ vọng + 1 tháng
        LocalDate gioiHanTren = ngayKyVong.plusMonths(1);

        if (ngayHetHieuLuc.isBefore(gioiHanDuoi)) {
            long soNgayThieu = ChronoUnit.DAYS.between(ngayHetHieuLuc, gioiHanDuoi);
            throw new BadRequestException(
                "Ngày hết hiệu lực quá ngắn so với quy định " + soThang + " tháng của ngành hệ đào tạo. " +
                "Tối thiểu phải là " + gioiHanDuoi + " (hiện thiếu " + soNgayThieu + " ngày)."
            );
        }

        if (ngayHetHieuLuc.isAfter(gioiHanTren)) {
            long soNgayThua = ChronoUnit.DAYS.between(gioiHanTren, ngayHetHieuLuc);
            throw new BadRequestException(
                "Ngày hết hiệu lực vượt quá 1 tháng so với quy định " + soThang + " tháng của ngành hệ đào tạo. " +
                "Tối đa cho phép là " + gioiHanTren + " (hiện thừa " + soNgayThua + " ngày)."
            );
        }
    }

    private void checkTotalEqualsParts(BigDecimal total, BigDecimal lyThuyet, BigDecimal thucHanh, BigDecimal kiemTra, String label) {
        if (total == null || lyThuyet == null || thucHanh == null || kiemTra == null) return;

        BigDecimal sum = lyThuyet.add(thucHanh).add(kiemTra);

        if (total.compareTo(sum) != 0) {
            throw new BadRequestException(label + " phải bằng giờ lý thuyết + giờ thực hành + giờ kiểm tra");
        }
    }
}