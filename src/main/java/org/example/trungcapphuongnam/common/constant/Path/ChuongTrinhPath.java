package org.example.trungcapphuongnam.common.constant.Path;

import org.example.trungcapphuongnam.common.constant.ApiPath;

public final class ChuongTrinhPath {
    private  ChuongTrinhPath() {}

    public static final String BASE = ApiPath.API + "/chuongTrinh";

    public static final String CHUONG_TRINH = BASE + "/chuong-trinh";
    public static final String CHUONG_TRINH_MON = BASE + "/chuong-trinh-mon";
    public static final String MON = CHUONG_TRINH_MON;
    public static final String CHUONG_TRINH_VERSION = BASE + "/chuong-trinh-version";
    public static final String VERSION = CHUONG_TRINH_VERSION;
    public static final String MON_HOC = BASE + "/mon-hoc";
    public static final String DIEU_KIEN_MON_HOC = BASE + "/dieu-kien-mon-hoc";
    public static final String DIEU_KIEN_TOT_NGHIEP = BASE + "/dieu-kien-tot-nghiep";
    public static final String MON_TIEN_QUYET = BASE + "/mon-tien-quyet";
    public static final String MON_TU_CHON = BASE + "/mon-tu-chon";
    public static final String MUC_TIEU_CHUONG_TRINH = BASE + "/muc-tieu-chuong-trinh";
    public static final String NANG_LUC_DAU_RA = BASE + "/nang-luc-dau-ra";
    public static final String NHOM_KIEN_THUC = BASE + "/nhom-kien-thuc";
    public static final String NHOM_TU_CHON = BASE + "/nhom-tu-chon";
    public static final String QUY_DOI_DIEM = BASE + "/quy-doi-diem";
    public static final String SYLLABUS_CHUONG_BAI = BASE + "/syllabus-chuong-bai";
    public static final String SYLLABUS_CHUONG_TRINH = BASE + "/syllabus-chuong-trinh";
    public static final String SYLLABUS_MON_HOC = BASE + "/syllabus-mon-hoc";
    public static final String SYLLABUS_TAI_LIEU = BASE + "/syllabus-tai-lieu";
    public static final String VI_TRI_VIEC_LAM = BASE + "/vi-tri-viec-lam";

    public static final String ID = "/{id}";
}
